<?php
  require_once dirname(__FILE__) . "/config.php";
  require_once dirname(__FILE__) . "/uc_client/client.php";

  // ������ʾ��HTML(��ɫ)
  function info($msg) {
    return "<font size=\"2\" color=\"blue\">" . htmlentities($msg, ENT_COMPAT, "GB2312") . "</font>";
  }

  // ���ؾ����HTML(��ɫ)
  function warn($msg) {
    return "<font size=\"2\" color=\"red\">" . htmlentities($msg, ENT_COMPAT, "GB2312") . "</font>";
  }

  // ��JavaScript���HTML
  function jsWrite($html) {
    echo "document.write(\"{$html}\");\r\n";
  }

  // ��ÿͻ���IP��ַ
  function getRemoteAddr() {
    if (isset($_SERVER["HTTP_CLIENT_IP"])) {
      return $_SERVER["HTTP_CLIENT_IP"];
    }
    if (isset($_SERVER["HTTP_X_FORWARDED_FOR"])) {
      return $_SERVER["HTTP_X_FORWARDED_FOR"];
    }
    return $_SERVER["REMOTE_ADDR"];
  }

  // ���salt
  function getSalt() {
    return substr(md5(mt_rand()), 0, 6);
  }

  class MysqlLink {
    var $link;

    function MysqlLink() {
      global $mysql_host, $mysql_username, $mysql_password, $mysql_database;
      $this->link = mysql_connect($mysql_host, $mysql_username, $mysql_password);
      mysql_select_db($mysql_database, $this->link);
    }

    function query($sql) {
      return mysql_query($sql, $this->link);
    }

    function affected_rows() {
      return mysql_affected_rows($this->link);
    }

    function insert_id() {
      return mysql_insert_id($this->link);
    }

    function escape($str) {
      return mysql_real_escape_string($str, $this->link);
    }

    function close() {
      mysql_close($this->link);
    }
  }

  // �û�����
  define("USERTYPE_ADMIN", 128);

  class UserData {
    var $uid, $username, $email, $usertype, $score, $points, $charged, $info;

    function UserData($uid, $username, $email, $line = null) {
      $this->uid = $uid;
      $this->username = $username;
      $this->email = $email;
      if ($line) {
        $this->usertype = $line["usertype"];
        $this->score = $line["score"];
        $this->points = $line["points"];
        $this->charged = $line["charged"];
      } else {
        $this->usertype = $this->score = $this->points = $this->charged = 0;
      }
    }

    function isAdmin() {
      return $this->usertype >= USERTYPE_ADMIN;
    }
  }

  // ��¼
  function login($username, $password) {
    global $mysql_tablepre, $mysql_link;

    // ���ȼ��"retry"�����Ƿ����������Լ�¼
    $sql = sprintf("SELECT retrycount, retrytime FROM {$mysql_tablepre}retry " .
        "WHERE username = '%s'", $mysql_link->escape($username));
    $result = $mysql_link->query($sql);
    $retry = mysql_fetch_assoc($result);
    if ($retry) {
      // ���δ�ﵽ��������ʱ�䣬���ֹ����
      if (time() < $retry["retrytime"]) {
        return "noretry";
      }
    }

    // ��¼
    list($uid, $dummy, $password, $email) = uc_user_login($username, $password);

    // ���û�в�ѯ���û����򷵻ء���¼ʧ�ܡ�
    if ($uid == -1) {
      return "error";
    }

    // ������벻�ԣ������û��Ƿ��ڡ������ƽ⡱
    if ($uid == -2) {
      // ���"retry"����û���������Լ�¼�������Ӹü�¼
      if (!$retry) {
        $sql = sprintf("INSERT INTO {$mysql_tablepre}retry (username, retrycount, retrytime) " .
            "VALUES ('%s', 1, 0)", $mysql_link->escape($username));
        $mysql_link->query($sql);
        return "error";
      }
      // ���������������5�Σ������Դ�����1����������
      if ($retry["retrycount"] < 5) {
        $sql = sprintf("UPDATE {$mysql_tablepre}retry SET retrycount = retrycount + 1 " .
            "WHERE username = '%s'", $mysql_link->escape($username));
        $mysql_link->query($sql);
        return "error";
      }
      // ���Դ����ﵽ5�Σ���������ʱ�䣬��ֹ����
      $sql = sprintf("UPDATE {$mysql_tablepre}retry SET retrycount = 0, retrytime = %d " .
          "WHERE username = '%s'", time() + 300, $mysql_link->escape($username));
      $mysql_link->query($sql);
      return "noretry";
    }

    // �����������
    if ($uid <= 0) {
      return "error";
    }

    // ��¼�ɹ���ɾ���������Լ�¼
    if ($retry) {
      $sql = sprintf("DELETE FROM {$mysql_tablepre}retry " .
          "WHERE username = '%s'", $mysql_link->escape($username));
      $mysql_link->query($sql);
    }

    $sql = sprintf("SELECT * FROM {$mysql_tablepre}user WHERE uid = %d", $uid);
    $result = $mysql_link->query($sql);
    $line = mysql_fetch_assoc($result);
    // ���"user"����û�м�¼��������¼
    if (!$line) {
      $sql = sprintf("INSERT INTO {$mysql_tablepre}user (uid, lastip, lasttime) " .
          "VALUES (%d, '%s', %d)", $uid, getRemoteAddr(), time());
      $mysql_link->query($sql);
      return new UserData($uid, $username, $email);
    }
    // ����"user"��
    $sql = sprintf("UPDATE {$mysql_tablepre}user SET lastip = '%s', lasttime = %d " .
        "WHERE uid = %d", getRemoteAddr(), time(), $uid);
    $mysql_link->query($sql);
    return new UserData($uid, $username, $email, $line);
  }

  // �¼�����
  define("EVENT_REGISTER", 101);
  define("EVENT_LOGIN", 102);
  define("EVENT_CHARGE", 105);
  define("EVENT_EMAIL", 106);
  define("EVENT_PASSWORD", 107);
  define("EVENT_GETPASSWORD", 109);
  define("EVENT_SAVE", 111);
  define("EVENT_RETRACT", 121);
  define("EVENT_HINT", 122);
  define("EVENT_CHARGECODE", 150);
  define("EVENT_ADMIN_CHARGE", 201);
  define("EVENT_ADMIN_PASSWORD", 202);
  define("EVENT_ADMIN_DELETE", 299);

  // �û�����
  define("USER_PLATINUM", 2800);
  define("USER_DIAMOND", 8800);

  // ��¼��־
  function insertLog($uid, $eventtype, $detail = 0) {
    global $mysql_tablepre, $mysql_link;
    $sql = sprintf("INSERT INTO {$mysql_tablepre}log (uid, eventip, eventtime, eventtype, detail) " .
        "VALUES (%d, '%s', %d, %d, %d)", $uid, getRemoteAddr(), time(), $eventtype, $detail);
    $mysql_link->query($sql);
  }

  // ����PHP����
  function runPhpTask($path) {
    $fp = fsockopen("127.0.0.1", 80);
    fwrite($fp, "GET $path HTTP/1.1\r\n" .
      "Host: users.elephantbase.net\r\n" .
      "Connection: Close\r\n\r\n");
    fclose($fp);
  }

  // ��ȡ������е�ʱ��
  function getTaskTime() {
    global $mysql_tablepre, $mysql_link;
    $result = $mysql_link->query("SELECT nexttime, lasttime FROM {$mysql_tablepre}task WHERE taskname = 'dailytask'");
    return mysql_fetch_assoc($result);
  }

  // ��һʱ��
  function nextDailyTime($currTime, $timeOffset) {
    $nextTime = floor($currTime / 86400) * 86400 + $timeOffset;
    if ($timeOffset < 0) {
      $nextTime += 86400;
    }
    if ($nextTime < $currTime) {
      $nextTime += 86400;
    }
    return $nextTime;
  }

  // ����Ƿ������ÿ������
  function checkDailyTask() {  
    global $mysql_tablepre, $mysql_link, $mysql_password;
    $currTime = time();
    // ��һ�μ��
    $taskTime = getTaskTime();
    if ($taskTime["nexttime"] < $currTime) {
      // ����
      $mysql_link->query("UPDATE {$mysql_tablepre}task SET tasklock = 1 WHERE taskname = 'dailytask'", $mysql_link);
      if ($mysql_link->affected_rows() > 0) {
        // �ڶ��μ�飬��ֹ�ڵ�һ�μ��ͼ���֮�䣬���ݱ�����̸߳ĵ���
        $taskTime = getTaskTime();
        if ($taskTime["nexttime"] < $currTime) {
          // ��һʱ����GMT-4:00
          $lastTime2 = $taskTime["lasttime"];
          $nextTime = nextDailyTime($currTime, -14400);
          $lastTime = $nextTime - 86400;
          // ���¼�¼��ͬʱ��������ֹrunPhpTaskʱ���������Ҳ��֤��֮��������̵߳õ��µ�����
          $sql = sprintf("UPDATE {$mysql_tablepre}task SET lasttime = %d, nexttime = %d, tasklock = 0 " .
              "WHERE taskname = 'dailytask'", $lastTime, $nextTime);
          $mysql_link->query($sql);
          // ��������
          sleep(1);
          runPhpTask("/task/backup.php?password=" . $mysql_password .
              "&timestamp=" . $lastTime . "&timestamp2=" . $lastTime2);
          // ˢ������
          sleep(1);
          runPhpTask("/task/updaterank.php?password=" . $mysql_password .
              "&timestamp=" . $lastTime);
        } else {
          // ����
          $mysql_link->query("UPDATE {$mysql_tablepre}task SET tasklock = 0 WHERE taskname = 'dailytask'");
        }
      }
    }
  }

  // ��������
  $score_catagory = array("ȫ��", "ȫ��", "����", "�о�", "�о�", "�ž�", "����", "����", "�̲�");
?>
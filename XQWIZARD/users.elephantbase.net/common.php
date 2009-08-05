<?php
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

  // ��¼
  function login($username, $password) {
    global $mysql_tablepre;
    $list($uid, $dummy, $password, $email) = uc_user_login($username, $password);

    // ���û�в�ѯ���û����򷵻ء���¼ʧ�ܡ�
    if ($uid == -1) {
      return "error";
    }
    // ������벻�ԣ������û��Ƿ��ڡ������ƽ⡱
    if ($uid == -2) {
      $sql = sprintf("SELECT retrycount, retrytime FROM {$mysql_tablepre}retry WHERE username = %s",
          mysql_real_escape_string($username));
      // 
      if (time() < $line["retrytime"]) {
        return "noretry";
      }
      if ($line["retrycount"] < 5) {
        $sql = sprintf("UPDATE {$mysql_tablepre}user SET retrycount = retrycount + 1 " .
            "WHERE username = '%s'", mysql_real_escape_string($username));
        mysql_query($sql);
        return "error";
      }
      // ���ء���ֹ���ԡ�
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET retrycount = 0, retrytime = %d " .
          "WHERE username = '%s'", time() + 300, mysql_real_escape_string($username));
      mysql_query($sql);
      return "noretry";
    }
    // �����������
    if ($uid < 0) {
      return "error";
    }

    return array("usertype"=>$line["usertype"], "email"=>$line["email"],
        "score"=>$line["score"], "points"=>$line["points"], "charged"=>$line["charged"]);
  }

  // �¼�����
  define("EVENT_REGISTER", 101);
  define("EVENT_LOGIN", 102);
  define("EVENT_CHARGE", 105);
  define("EVENT_EMAIL", 106);
  define("EVENT_PASSWORD", 107);
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
  function insertLog($username, $eventtype, $detail = 0) {
    global $mysql_tablepre;
    $sql = sprintf("INSERT INTO {$mysql_tablepre}log (username, eventip, eventtime, eventtype, detail) " .
        "VALUES ('%s', '%s', %d, %d, %d)",
        mysql_real_escape_string($username), getRemoteAddr(), time(), $eventtype, $detail);
    mysql_query($sql);
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
    global $mysql_tablepre;
    $result = mysql_query("SELECT nexttime, lasttime FROM {$mysql_tablepre}task WHERE taskname = 'dailytask'");
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
    global $mysql_tablepre, $mysql_password;
    $currTime = time();
    // ��һ�μ��
    $taskTime = getTaskTime();
    if ($taskTime["nexttime"] < $currTime) {
      // ����
      mysql_query("UPDATE {$mysql_tablepre}task SET tasklock = 1 WHERE taskname = 'dailytask'");
      if (mysql_affected_rows() > 0) {
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
          mysql_query($sql);
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
          mysql_query("UPDATE {$mysql_tablepre}task SET tasklock = 0 WHERE taskname = 'dailytask'");
        }
      }
    }
  }
?>
<?php
  // ������ʾ��HTML(��ɫ)
  function info($msg) {
    return "<font size=\"2\" color=\"blue\">" . htmlentities($msg, ENT_COMPAT, "GB2312") . "</font>";
  }

  // ���ؾ����HTML(��ɫ)
  function warn($msg) {
    return "<font size=\"2\" color=\"red\">" . htmlentities($msg, ENT_COMPAT, "GB2312") . "</font>";
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

  // ��¼
  function login($username, $password) {
    global $mysql_tablepre;
    $sql = sprintf("SELECT * FROM {$mysql_tablepre}user WHERE username = '%s'",
        mysql_real_escape_string($username));
    $result = mysql_query($sql);
    $line = mysql_fetch_assoc($result);
    // ���û�в�ѯ���û����򷵻ء���¼ʧ�ܡ�
    if (!$line) {
      return "error";
    }
    // �����ǰʱ��û�дﵽ����ʱ�䣬�򷵻ء���ֹ���ԡ�
    if (time() < $line["retrytime"]) {
      return "noretry";
    }
    // ����û���������ƥ�䣬�򷵻����͡�Email����������������Ϣ
    if (md5($username . $password) == $line["password"]) {
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET lastip = '%s', lasttime = %d, retrycount = 0 " .
          "WHERE username = '%s'", getRemoteAddr(), time(), mysql_real_escape_string($username));
      mysql_query($sql);
      return array("usertype"=>$line["usertype"], "email"=>$line["email"],
          "scores"=>$line["scores"], "points"=>$line["points"]);
    }
    // ������Դ���С��5�Σ��򷵻ء���¼ʧ�ܡ�
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

  // �¼�����
  define("EVENT_REGISTER", 101);
  define("EVENT_LOGIN", 101);
  define("EVENT_EMAIL", 102);
  define("EVENT_PASSWORD", 103);
  define("EVENT_SAVE", 111);
  define("EVENT_RETRACT", 121);
  define("EVENT_HINT", 122);
  define("EVENT_CHARGE", 201);
  define("EVENT_RESET", 202);
  define("EVENT_DELETE", 299);

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
    $result = mysql_query("SELECT tasktime FROM {$mysql_tablepre}task WHERE taskname = 'dailytask'");
    $line = mysql_fetch_assoc($result);
    return $line["tasktime"];
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
    $currTime = time();
    // ��һ�μ��
    if (getTaskTime() < $currTime) {
      // ����
      mysql_query("LOCK TABKE {$mysql_tablepre}task WRITE");
      // �ڶ��μ�飬��ֹ�ڵ�һ�μ��ͼ���֮�䣬���ݱ��ĵ���
      if (getTaskTime() < $currTime) {
        // ��һʱ����GMT-4:00
        $nextTime = nextDailyTime($currTime, -14400);
        $sql = sprintf("UPDATE {$mysql_tablepre}task SET tasktime = %d " .
            "WHERE taskname = 'dailytask'", $nextTime);
        mysql_query($sql);
        $lastTime = $nextTime - 86400;
        // ������־
        runPhpTask("/task/backuplog.php?password=" . $mysql_password . "&timestamp=" . $lastTime);
        // ˢ������
        runPhpTask("/task/updaterank.php?password=" . $mysql_password . "&timestamp=" . $lastTime);
      }
      mysql_query("UNLOCK TABLE");
    }
  }
?>
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
    $sql = sprintf("SELECT * FROM {$mysql_tablepre}user WHERE username = '%s'", mysql_real_escape_string($username));
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
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET lastip = '%s', lasttime = %d, retrycount = 0 WHERE username = '%s'", getRemoteAddr(), time(), mysql_real_escape_string($username));
      mysql_query($sql);
      return array("usertype"=>$line["usertype"], "email"=>$line["email"], "scores"=>$line["scores"], "points"=>$line["points"]);
    }
    // ������Դ���С��5�Σ��򷵻ء���¼ʧ�ܡ�
    if ($line["retrycount"] < 5) {
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET retrycount = retrycount + 1 WHERE username = '%s'", mysql_real_escape_string($username));
      mysql_query($sql);
      return "error";
    }
    // ���ء���ֹ���ԡ�
    $sql = sprintf("UPDATE {$mysql_tablepre}user SET retrycount = 0, retrytime = %d WHERE username = '%s'", time() + 300, mysql_real_escape_string($username));
    mysql_query($sql);
    return "noretry";
  }
?>
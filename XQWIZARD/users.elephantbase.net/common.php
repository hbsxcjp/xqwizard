<?php
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
    $sql = sprintf("SELECT password, email, retrycount, retrytime, scores, points FROM tb_user WHERE username = '%s'", mysql_real_escape_string($username));
    $result = mysql_query($sql);
    $line = mysql_fetch_array($result, MYSQL_ASSOC);
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
      $sql = sprintf("UPDATE tb_user SET lastip = '%s', lasttime = %d, retrycount = 0 WHERE username = '%s'", getRemoteAddr(), time(), mysql_real_escape_string($username));
      mysql_query($sql);
      return array("usertype"=>$line["usertype"], "email"=>$line["email"], "scores"=>$line["scores"], "points"=>$line["points"]);
    }
    // ������Դ���С��5�Σ��򷵻ء���¼ʧ�ܡ�
    if ($line["retrycount"] < 5) {
      $sql = sprintf("UPDATE tb_user SET retrycount = retrycount + 1 WHERE username = '%s'", mysql_real_escape_string($username));
      mysql_query($sql);
      return "error";
    }
    // ���ء���ֹ���ԡ�
    $sql = sprintf("UPDATE tb_user SET retrycount = 0, retrytime = %d WHERE username = '%s'", time() + 300, mysql_real_escape_string($username));
    mysql_query($sql);
    return "noretry";
  }
?>
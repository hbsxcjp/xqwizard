<?php
  require_once "./mysql_conf.php";
  require_once "./common.php";
  require_once "./user.php";

  $password0 = $_POST["password0"];
  $password = $_POST["password"];
  $password2 = $_POST["password2"];
  $email = $_POST["email"];

  mysql_connect($mysql_host, $mysql_username, $mysql_password);
  mysql_select_db($mysql_database);
  if (strlen($password0) < 6) {
    // ������Email
    $sql = sprintf("UPDATE {$mysql_tablepre}user SET email = '%s' WHERE username = '%s'",
        mysql_real_escape_string($email), mysql_real_escape_string($username));
    mysql_query($sql);
    insertLog($username, EVENT_EMAIL);
    $info = info("Email���³ɹ�");
    $_SESSION["userdata"]["info"] = $info;
    $_SESSION["userdata"]["email"] = $email;
    header("Location: info.php");
  } else {
    // ����Email������
    $sql = sprintf("SELECT password, salt FROM {$mysql_tablepre}user WHERE username = '%s'",
        mysql_real_escape_string($username));
    $result = mysql_query($sql);
    $line = mysql_fetch_assoc($result);
    if ($line) {
      if ($line["password"] == md5($username . $password0) || $line["password"] == md5(md5($password0) . $line["salt"])) {
        if (strlen($password) < 6) {
          $info = warn("���벻������6���ַ�");
        } else if ($password == $password2) {
          $info = info("�����Email���³ɹ�");
          $salt = getSalt();
          $sql = sprintf("UPDATE {$mysql_tablepre}user SET password = '%s', salt = '%s', email = '%s' WHERE username = '%s'",
              md5(md5($password) . $salt), $salt, mysql_real_escape_string($email),
              mysql_real_escape_string($username));
          mysql_query($sql);
          insertLog($username, EVENT_PASSWORD);
          $_SESSION["userdata"]["email"] = $email;
        } else {
          $info = warn("�������벻һ��");
        }
      } else {
        $info = warn("ԭ�������");
      }
      $_SESSION["userdata"]["info"] = $info;
      header("Location: info.php");
    } else {
      header("Location: login.htm#timeout");
    }
  }
  mysql_close();
?>
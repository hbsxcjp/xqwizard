<?php
  require_once "./mysql_conf.php";

  $password0 = $_POST["password0"];
  $password = $_POST["password"];
  $password2 = $_POST["password2"];
  $email = $_POST["email"];

  session_start();
  session_register("userdata");
  if (!isset($_SESSION["userdata"])) {
    header("Location: login.htm#timeout");
    exit();
  }
  $username = $_SESSION["userdata"]["username"];

  mysql_connect($mysql_host, $mysql_username, $mysql_password);
  mysql_select_db($mysql_database);
  if (strlen($password0) < 6) {
    $sql = sprintf("UPDATE {$mysql_tablepre}user SET email = '%s' WHERE username = '%s'",
        mysql_real_escape_string($email), mysql_real_escape_string($username));
    mysql_query($sql);
    $info = info("Email���³ɹ�");
    $_SESSION["userdata"]["info"] = $info;
    header("Location: info.php");
  } else {
    $sql = sprintf("SELECT password FROM {$mysql_tablepre}user WHERE username = '%s'",
        mysql_real_escape_string($username));
    $result = mysql_query($sql);
    $line = mysql_fetch_assoc($result);
    if ($line) {
      if ($line["password"] == md5($username . $password0)) {
        if (strlen($password) < 6) {
          $info = warn("���벻������6���ַ�");
        } else if ($password == $password2) {
          $info = info("�����Email���³ɹ�");
          $sql = sprintf("UPDATE {$mysql_tablepre}user SET password = '%s', email = '%s' WHERE username = '%s'",
              md5($username . $password), mysql_real_escape_string($email),
              mysql_real_escape_string($username));
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
<?php
  require_once "./common.php";
  require_once "./user.php";

  $password0 = $_POST["password0"];
  $password = $_POST["password"];
  $password2 = $_POST["password2"];
  $email = $_POST["email"];

  mysql_connect($mysql_host, $mysql_username, $mysql_password);
  mysql_select_db($mysql_database);
  $timeout = false;
  if (strlen($password0) < 6 || strlen($password) < 6) {
    // ������Email
    $ucresult = uc_user_edit($username, "", "", $email);
    if ($ucresult < 0) {
      $info = warn("����Email���󣬴����룺" . $ucresult);
    } else {
      $info = info("Email���³ɹ�");
      insertLog($uid, EVENT_EMAIL);
      $_SESSION["userdata"]["email"] = $email;
    }
  } else if ($password != $password2) {
    $info = warn("�������벻һ��");
  } else {
    // ����Email������
    $ucresult = uc_user_edit($username, $password0, $password, $email);
    if ($ucresult >= 0) {
      $info = info("�����Email���³ɹ�");
      insertLog($uid, EVENT_PASSWORD);
    } else if ($ucresult == -1) {
      $info = warn("ԭ�������");
    } else if ($ucresult == -8) {
      $timeout = true;
    } else if {
      $info = warn("����Email��������󣬴����룺" . $ucresult);
    }
  }
  if ($timeout) {
    header("Location: login.htm#timeout");
  } else {
    $_SESSION["userdata"]["info"] = $info;
    header("Location: info.php");
  }
  mysql_close();
?>
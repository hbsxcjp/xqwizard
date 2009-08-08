<?php
  require_once "./common.php";
  require_once "./user.php";

  $password0 = $_POST["password0"];
  $password = $_POST["password"];
  $password2 = $_POST["password2"];
  $email = $_POST["email"];

  $mysql_link = new MysqlLink;
  $timeout = false;
  if (strlen($password0) < 6 || strlen($password) < 6) {
    // ������Email
    $result = uc_user_edit($userdata->username, "", "", $email);
    if ($result >= 0) {
      $info = info("Email���³ɹ�");
      insertLog($userdata->uid, EVENT_EMAIL);
      $userdata->email = $email;
    } else if ($result == -4 || $result == -5) {
      $info = warn("Email�����Ϲ��");
    } else {
      $info = warn("Email����ʧ��");
    }
  } else if ($password != $password2) {
    $info = warn("�������벻һ��");
  } else {
    // ����Email������
    $result = uc_user_edit($userdata->username, $password0, $password, $email);
    if ($result >= 0) {
      $info = info("�����Email���³ɹ�");
      insertLog($userdata->uid, EVENT_PASSWORD);
    } else if ($result == -1) {
      $info = warn("ԭ�������");
    } else if ($result == -4 || $result == -5) {
      $info = warn("Email�����Ϲ��");
    } else {
      $info = warn("�����Email����ʧ��");
    }
  }
  if ($timeout) {
    header("Location: login.htm#timeout");
  } else {
    $userdata->info = $info;
    header("Location: info.php");
  }
  $mysql_link->close();
?>
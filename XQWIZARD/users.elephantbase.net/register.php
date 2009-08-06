<?php
  require_once "./common.php";
  require_once "./ejewimage.php";

  $username = $_POST["username"];
  $password = $_POST["password"];
  $password2 = $_POST["password2"];
  $email = $_POST["email"];
  $ejew = $_POST["ejew"];

  // ����û��������롢��֤���Ƿ���Ϲ��
  if (strlen($username) < 6 || strlen($password) < 6) {
    header("Location: register.htm#short");
  } else if ($password != $password2) {
    header("Location: register.htm#password");
  } else if ($ejew != $_SESSION["ejew_value"]) {
    header("Location: register.htm#ejew");
  } else {
    // ��ʼע��
    $uid = uc_user_register($username, $password, $email);
    if ($uid > 0) {
      header("Location: login.htm#register");      
    } else if ($uid == -1 || $uid == -2) {
      header("Location: register.htm#username");
    } else if ($uid == -3) {
      header("Location: register.htm#exist");
    } else if ($uid == -4 || $uid == -5) {
      header("Location: register.htm#email");
    } else {
      header("Location: register.htm#error" . uid);
    }
  }
?>
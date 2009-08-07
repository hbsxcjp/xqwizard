<?php
  require_once "./common.php";

  $username = $_POST["username"];
  $password = $_POST["password"];

  if (strlen($username) < 6 || strlen($password) < 6) {
    header("Location: login.htm#error");
  } else {
    $mysql_link = new MysqlLink;
    $result = login($username, $password);
    if ($result == "error") {
      header("Location: login.htm#error");
    } else if ($result == "noretry") {
      header("Location: login.htm#noretry");
    } else {
      insertLog($username, EVENT_LOGIN);
      session_start();
      session_register("userdata");
      $result["username"] = $username;
      $points = $result["points"];
      $charged = $result["charged"];
      $result["info"] = "���Ѿ������� " . $result["score"] . " ��" .
          ($points == 0 ? "" : "<br>������ " . $points . " �����") .
          ($charged < USER_PLATINUM ? "" :
          ($charged < USER_DIAMOND ? "<br>�������ǣ��׽��Ա�û�" : "<br>�������ǣ���ʯ��Ա�û�"));
      $_SESSION["userdata"] = $result;
      header("Location: info.php");
    }
    $mysql_link->close();
  }
?>
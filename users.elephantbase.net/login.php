<?php
  require_once "./common.php";

  $username = $_POST["username"];
  $password = $_POST["password"];
  $location = $_POST["location"];

  $search = (strlen($location) == 0 ? "" : "?location=" . urlencode($location));
  if (strlen($username) < 6 || strlen($password) < 6) {
    header("Location: login.htm" . $search . "#error");
  } else {
    $mysql_link = new MysqlLink;
    $result = login($username, $password);
    if ($result == "error") {
      header("Location: login.htm" . $search . "#error");
    } else if ($result == "noretry") {
      header("Location: login.htm" . $search . "#noretry");
    } else {
      insertLog($result->uid, EVENT_LOGIN);
      session_start();
      session_register("userdata");
      $_SESSION["userdata"] = $result;
      $userdata = &$_SESSION["userdata"];
      $userdata->info = "���Ѿ������� " . $userdata->score . " ��";
      if ($userdata->points > 0) {
          $userdata->info .= "<br>������ " . $userdata->points . " �����";
      }
      if ($userdata->charged >= USER_DIAMOND) {
          $userdata->info .= "<br>�������ǣ���ʯ��Ա�û�";
      } else if ($userdata->charged >= USER_PLATINUM) {
          $userdata->info .= "<br>�������ǣ��׽��Ա�û�";
      }
      if (strlen($location) > 0) {
        header("Location: " . $location);
      } else if ($userdata->isAdmin()) {
        header("Location: admin/admin.htm");
      } else {
        header("Location: info.php");
      }
    }
    $mysql_link->close();
  }
?>
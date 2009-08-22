<?php
  require_once "../common.php";

  session_start();
  session_register("userdata");
  if (!isset($_SESSION["userdata"])) {
    header("Location: ../login.htm#timeout");
    exit;
  }
  $userdata = &$_SESSION["userdata"];

  if (!$userdata->isAdmin()) {
    $userdata->info = warn("�����ǹ���Ա���޷���ѯ�û���Ϣ");
    header("Location: ../info.php");
    exit;
  }
?>
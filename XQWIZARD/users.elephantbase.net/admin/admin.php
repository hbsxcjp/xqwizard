<?php
  require_once "./user.php";

  if ($userdata->usertype != 128) {
    $userdata->info = "<font size=\"2\" color=\"red\">�����ǹ���Ա���޷���ѯ�û���Ϣ</font>";
    header("Location: info.php");
    exit;
  }
?>
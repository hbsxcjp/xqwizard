<?php
  session_start();
  session_register("userdata");
  if (!isset($_SESSION["userdata"])) {
    header("Location: login.htm#timeout");
    exit();
  }
  if ($_SESSION["userdata"]["usertype"] != 128) {
    $_SESSION["userdata"]["info"] = "<font size=\"2\" color=\"red\">�����ǹ���Ա���޷���ѯ�û���Ϣ</font>";
    header("Location: info.php");
    exit();
  }
?>
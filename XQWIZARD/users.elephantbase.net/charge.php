<?php
  require_once "./mysql_conf.php";
  require_once "./common.php";
  require_once "./user.php";

  $chargecode = $_POST["chargecode"];

  mysql_connect($mysql_host, $mysql_username, $mysql_password);
  mysql_select_db($mysql_database);
  $sql = sprintf("SELECT points FROM {$mysql_tablepre}chargecode WHERE chargecode = '%s'", $chargecode);
  $result = mysql_query($sql);
  $line = mysql_fetch_assoc($result);
  if ($line) {
    $points = $line["points"];
    $sql = sprintf("DELETE FROM {$mysql_tablepre}chargecode WHERE chargecode = '%s'", $chargecode);
    mysql_query($sql);
    $sql = sprintf("UPDATE {$mysql_tablepre}user SET points = points + %d WHERE username = '%s'",
        $points, mysql_real_escape_string($username));
    mysql_query($sql);
    $_SESSION["userdata"]["points"] += $points;
    $info = info("���ղŲ����� " . $points . " �㣬���ڹ��� " . $_SESSION["userdata"]["points"] . " �����");
  } else {
    $info = warn("�㿨�������");
  }

  $_SESSION["userdata"]["info"] = $info;    
  header("Location: info.php");
  mysql_close();
?>
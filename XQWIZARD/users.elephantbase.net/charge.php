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
  $info = warn("�㿨�������");
  if ($line) {
    $points = $line["points"];
    $sql = sprintf("DELETE FROM {$mysql_tablepre}chargecode WHERE chargecode = '%s'", $chargecode);
    mysql_query($sql);
    // ��ȡ�����󣬱���߳�Ҳ���ܻ�Ѽ�¼ɾ��������Ҫ����Ƿ�ȷʵɾ����
    if (mysql_affected_rows() > 0) {
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET points = points + %d, charged = charged + %d WHERE username = '%s'",
          $points, $points, mysql_real_escape_string($username));
      mysql_query($sql);
      insertLog($username, EVENT_CHARGE, $points);
      $_SESSION["userdata"]["points"] += $points;
      $_SESSION["userdata"]["charged"] += $points;
      $charged = $_SESSION["userdata"]["charged"];
      $info = info("���ղŲ����� " . $points . " �㣬���ڹ��� " . $_SESSION["userdata"]["points"] . " �����") .
          ($charged < USER_PLATINUM ? "" : "<br>" .
          info($charged < USER_DIAMOND ? "���Ѿ�����Ϊ���׽��Ա�û�" : "���Ѿ�����Ϊ����ʯ��Ա�û�"));
    }
  }

  $_SESSION["userdata"]["info"] = $info;    
  header("Location: info.php");
  mysql_close();
?>
<?php
  require_once "./common.php";
  require_once "./user.php";

  $chargecode = $_POST["chargecode"];

  $mysql_link = new MysqlLink;
  $sql = sprintf("SELECT points FROM {$mysql_tablepre}chargecode WHERE chargecode = '%s'", $chargecode);
  $result = $mysql_link->query($sql);
  $line = mysql_fetch_assoc($result);
  $info = warn("�㿨�������");
  if ($line) {
    $points = $line["points"];
    $sql = sprintf("DELETE FROM {$mysql_tablepre}chargecode WHERE chargecode = '%s'", $chargecode);
    $mysql_link->query($sql);
    // ��ȡ�����󣬱���߳�Ҳ���ܻ�Ѽ�¼ɾ��������Ҫ����Ƿ�ȷʵɾ����
    if (mysql_affected_rows() > 0) {
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET points = points + %d, charged = charged + %d " .
          "WHERE uid = %d", $points, $points, $uid);
      $mysql_link->query($sql);
      insertLog($uid, EVENT_CHARGE, $points);
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
  $mysql_link->close();
?>
<?php
  require_once "./user.php";

  $chargecode = $_POST["chargecode"];

  $mysql_link = new MysqlLink;
  $sql = sprintf("SELECT points FROM " . MYSQL_TABLEPRE . "chargecode WHERE chargecode = '%s'",
      $mysql_link->escape($chargecode));
  $result = $mysql_link->query($sql);
  $line = mysql_fetch_assoc($result);
  $info = warn("�㿨�������");
  if ($line) {
    $points = $line["points"];
    $sql = sprintf("DELETE FROM " . MYSQL_TABLEPRE . "chargecode WHERE chargecode = '%s'",
        $mysql_link->escape($chargecode));
    $mysql_link->query($sql);
    // ��ȡ�����󣬱���߳�Ҳ���ܻ�Ѽ�¼ɾ��������Ҫ����Ƿ�ȷʵɾ����
    if (mysql_affected_rows() > 0) {
      $sql = sprintf("UPDATE " . MYSQL_TABLEPRE . "user SET points = points + %d, charged = charged + %d " .
          "WHERE uid = %d", $points, $points, $userdata->uid);
      $mysql_link->query($sql);
      insertLog($userdata->uid, EVENT_CHARGE, $points);
      $userdata->points += $points;
      $userdata->charged += $points;
      $info = info("���ղŲ����� " . $points . " �㣬���ڹ��� " . $userdata->points . " �����");
      if ($userdata->charged >= USER_DIAMOND) {
         $info .= "<br>" . info("���Ѿ�����Ϊ����ʯ��Ա�û�");
      } else if ($userdata->charged >= USER_PLATINUM) {
         $info .= "<br>" . info("���Ѿ�����Ϊ���׽��Ա�û�");
      }
    }
  }

  $userdata->info = $info;    
  header("Location: info.php");
  $mysql_link->close();
?>
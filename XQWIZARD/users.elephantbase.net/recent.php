<?php
  require_once "./common.php";

  mysql_connect($mysql_host, $mysql_username, $mysql_password);
  mysql_select_db($mysql_database);

  $result = mysql_query("SELECT username, savetime, score FROM {$mysql_tablepre}recent " .
      "LEFT JOIN {UC_DBTABLEPRE}members USING (uid) ORDER BY savetime DESC LIMIT 10");
  while ($line = mysql_fetch_assoc($result)) {
    $score = $line["score"];
    jsWrite(date("H:i:s ", $line["savetime"]) .
        htmlentities($line["username"], ENT_COMPAT, "GB2312") .
        " �Ѵ�����" . ($score > 900 ? "����900" : $score) . "��<br>");
  }

  mysql_close();
?>
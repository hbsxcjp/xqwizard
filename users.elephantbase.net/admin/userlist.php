<html>

<head>
<meta http-equiv="Content-Type"
content="text/html; charset=gb_2312-80">
<meta name="GENERATOR" content="Microsoft FrontPage Express 2.0">
<title>�û��б� - ������ʦ�û�����</title>
</head>

<body bgcolor="#3869B6" topmargin="0" leftmargin="0"
bottommargin="0" rightmargin="0">

<table border="0" cellpadding="8" cellspacing="0" width="100%">
    <tr>
        <td>��</td>
        <td width="750" bgcolor="#FFFFFF"><table border="0"
        cellspacing="0" width="100%">
            <tr>
                <td background="../images/topbg.gif"><table
                border="0" width="100%">
                    <tr>
                        <td valign="bottom" nowrap><table
                        border="0">
                            <tr>
                                <td nowrap><img
                                src="../images/wizard.jpg"
                                width="64" height="64"><!--webbot
                                bot="HTMLMarkup" startspan -->&nbsp;<!--webbot
                                bot="HTMLMarkup" endspan --><font
                                color="#FFFFFF" size="6"
                                face="����">������ʦ�û�����</font></td>
                            </tr>
                        </table>
                        </td>
                        <td align="right" valign="bottom"><table
                        border="0">
                            <tr>
                                <td><p align="right"><font
                                size="5">����</font></p>
                                </td>
                            </tr>
                            <tr>
                                <td><p align="right"><img
                                src="../images/elephantbase.gif"
                                width="88" height="31"></p>
                                </td>
                            </tr>
                            <tr>
                                <td><p align="right"><font
                                color="#FFFFFF" size="2"
                                face="Arial"><strong>www.elephantbase.net</strong></font></p>
                                </td>
                            </tr>
                        </table>
                        </td>
                    </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td>��</td>
            </tr>
        </table>
        <table border="0" cellpadding="4" cellspacing="0"
        width="100%" bgcolor="#F0F0F0">
            <tr>
                <td background="../images/headerbg.gif"><font
                size="3"><strong>�û��б�</strong></font></td>
            </tr>
            <tr>
                <td><p align="center"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  require_once "./admin.php";

  $username = $_POST["username"];
  $email = $_POST["email"];
  $orderby = $_POST["orderby"];
  $limit = intval($_POST["limit"]);

  $mysql_link = new MysqlLink;
  $sqlSelect = "SELECT username, email, score, points, charged " .
        "FROM " . UC_DBTABLEPRE . "members LEFT JOIN {$mysql_tablepre}user USING (uid)";
  if ($username != "") {
    $sql = sprintf($sqlSelect . " WHERE username like '%%%s%%' AND score IS NOT NULL " .
        "ORDER BY %s DESC LIMIT %d", $mysql_link->escape($username), $orderby, $limit);
  } else if ($email != "") {
    $sql = sprintf($sqlSelect . " WHERE email like '%%%s%%' AND score IS NOT NULL " .
        "ORDER BY %s DESC LIMIT %d", $mysql_link->escape($email), $orderby, $limit);
  } else {
    $sql = sprintf($sqlSelect . " WHERE score IS NOT NULL " .
        "ORDER BY %s DESC LIMIT %d", $orderby, $limit);
  }
  $result = $mysql_link->query($sql);
  $line = mysql_fetch_assoc($result);
  if ($line) {
    echo "<table border=\"1\">";
    $th0 = "<th><font size=\"2\">";
    $th1 = "</font></th>";
    $th10 = $th1 . $th0;
    echo "<tr>{$th0}�û���{$th10}Email{$th10}�ɼ�{$th10}����{$th10}��ֵ{$th10}&nbsp;{$th1}</tr>";
    $td0 = "<td align=\"center\"><font size=\"2\">&nbsp;";
    $td1 = "&nbsp;</font></td>";
    $td10 = $td1 . $td0;
    while ($line) {
      echo sprintf("<tr>{$td0}%s{$td10}%s{$td10}%d{$td10}%d{$td10}%d{$td10}" .
          "<a href=\"edituser.php?username=%s\" target=\"_blank\">�༭</a>{$td1}</tr>",
          htmlentities($line["username"], ENT_COMPAT, "GB2312"),
          htmlentities($line["email"], ENT_COMPAT, "GB2312"),
          $line["score"], $line["points"], $line["charged"], urlencode($line["username"]));
      $line = mysql_fetch_assoc($result);
    }
    echo "</table>";
  } else {
    echo "<font size=\"2\" color=\"red\">û���ҵ��û�</font>";
  }
  $mysql_link->close();
?><!--webbot
                bot="HTMLMarkup" endspan --></p>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E0E0E0"><p align="right"><a
                href="http://www.elephantbase.net/"
                target="_blank"><font color="#000060" size="2">��Ȩ����</font><font
                color="#000060">&copy;</font><font
                color="#000060" size="2" face="Times New Roman">2004-2009
                </font><font color="#000060" size="2">����ٿ�ȫ��</font></a><font
                color="#000060" size="2"> </font><a
                href="http://www.miibeian.gov.cn/"
                target="_blank"><font color="#000060" size="2">��</font><font
                color="#000060" size="2" face="Times New Roman">ICP</font><font
                color="#000060" size="2">��</font><font
                color="#000060" size="2" face="Times New Roman">05047724</font></a></p>
                </td>
            </tr>
        </table>
        </td>
        <td>��</td>
    </tr>
</table>
</body>
</html>

<html>

<head>
<meta http-equiv="Content-Type"
content="text/html; charset=gb_2312-80">
<meta name="GENERATOR" content="Microsoft FrontPage Express 2.0">
<title>�����б� - �ֻ����׽���ƽ̨</title>
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
                                face="����">�ֻ����׽���ƽ̨</font></td>
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
                <td background="../images/headerbg.gif"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  require_once "../user.php";

  $fid = $_GET["fid"];
  $uid = $userdata->$uid;

  $mysql_link = new MysqlLink;

  // ��ȷ��������û�б�����
  $sql = sprintf("SELECT * FROM " . MYSQL_TABLEPRE . "download WHERE fid = %d AND uid = %d", $fid, $uid);
  $result = $mysql_link->query($sql);
  $line = mysql_fetch_assoc($result);
  $payed = false;
  if ($line) {
    $payed = true;
  }

  // ��ȡ������Ϣ
  $sql = sprintf("SELECT u.uid, username, title, catagory, size, price, eventtime, download, positive, negative " .
      "FROM " . MYSQL_TABLEPRE . "upload u LEFT JOIN " . UC_DBTABLEPRE . "members USING (uid) " .
      "WHERE fid = %d AND state = 0", $fid);
  $result = $mysql_link->query($sql);
  $line = mysql_fetch_assoc($result);
  if (!$line) {
    $line = array("username"=>"-", "title"=>"-", "catagory"=>0, "size"=>0, "price"=>0, "eventtime"=>time(),
        "download"=>0, "positive"=>0, "negative"=>0);
  }

  echo "<font size=\"3\"><b>" . . "</b></font>"
?><!--webbot
                bot="HTMLMarkup" endspan --></td>
            </tr>
            <tr>
                <td align="center"><table border="0">
                    <tr>
                        <td align="right"><font size="2">�ϴ�ʱ�䣺</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo htmlentities($username, ENT_COMPAT, "GB2312"); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">���ͣ�</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo htmlentities($email, ENT_COMPAT, "GB2312"); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">���⣺</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["regip"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">�ṩ�ߣ�</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo date("Y-m-d H:i:s", $line["regdate"]); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">��С��</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["lastip"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">��Ҫ������</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo date("Y-m-d H:i:s", $line["lasttime"]); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">���ش�����</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["score"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">������</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["points"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --> <!--webbot
                        bot="HTMLMarkup" startspan --><a id="positive" href="#"><!--webbot
                        bot="HTMLMarkup" endspan --><img
                        src="positive.gif" width="17" height="16">�������<!--webbot
                        bot="HTMLMarkup" startspan --></a><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">������</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["charged"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --> <!--webbot
                        bot="HTMLMarkup" startspan --><a id="negative" href="#"><!--webbot
                        bot="HTMLMarkup" endspan --><img
                        src="negative.gif" width="17" height="16">�������<!--webbot
                        bot="HTMLMarkup" startspan --></a><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td>��</td>
                        <td>��</td>
                        <td>��</td>
                    </tr>
                    <tr>
                        <td>��</td>
                        <td>��</td>
                        <td><!--webbot bot="HTMLMarkup"
                        startspan --><a id="downloadJar" href="#"><!--webbot
                        bot="HTMLMarkup" endspan --><font
                        size="2"><img src="download_jar.gif"
                        width="16" height="16">����Jar�ļ�<!--webbot
                        bot="HTMLMarkup" startspan --></a><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td>��</td>
                        <td>��</td>
                        <td><!--webbot bot="HTMLMarkup"
                        startspan --><a id="downloadJad" href="#"><!--webbot
                        bot="HTMLMarkup" endspan --><font
                        size="2"><img src="download_jad.gif"
                        width="16" height="16">����Jad�ļ�<!--webbot
                        bot="HTMLMarkup" startspan --></a><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td background="../images/headerbg.gif"><font
                size="3"><strong>����</strong></font></td>
            </tr>
            <tr>
                <td align="center"><table border="0"
                cellpadding="4" cellspacing="1" bgcolor="#000000">
                    <tr>
                        <td bgcolor="#FFFFFF"><!--webbot
                        bot="HTMLMarkup" startspan --><?php
  $th0 = "<td align=\"center\" background=\"../images/headerbg.gif\" nowrap><font size=\"2\">";
  $th1 = "</font></td>";
  $th10 = $th1 . $th0;

  $cond = "";
  if ($title) {
    $cond .= sprintf("title LIKE '%%%s%%' AND ", $mysql_link->escape($title));
  }
  if ($catagory) {
    $cond .= sprintf("catagory = %d AND ", $catagory);
  }
  $orderColumn = ($order == SCORE_ORDER_DOWNLOAD ? "download" :
      $order == SCORE_ORDER_POSITIVE ? "positive" : "eventtime");

  $sql = "SELECT fid, username, comments, eventtime FROM " . MYSQL_TABLEPRE . "download_comments u " .
      "LEFT JOIN " . UC_DBTABLEPRE . "members USING (uid) WHERE fid = %d ORDER BY eventtime DESC";
  $result = $mysql_link->query($sql);
  $gray = false;
  $line = mysql_fetch_assoc($result);
  if ($line) {
    echo "<table border=\"0\">";
    echo "<tr>{$th0}�ϴ�ʱ��{$th10}����{$th10}����{$th10}�ṩ��{$th10}" .
        "��С{$th10}����{$th10}����{$th10}��{$th10}��{$th1}</tr>";
    while ($line) {
      $gray = !$gray;
      $td0 = sprintf("<td align=\"center\" bgcolor=\"%s\" nowrap><font size=\"2\">",
          $gray ? "#F0F0F0" : "#E0E0E0");
      $td1 = "</font></td>";
      $td10 = $td1 . $td0;

      $uid = $line[MYSQL_TABLEPRE . "upload.uid"];
      $cat = $line["catagory"];
      echo sprintf("<tr>{$td0}%s{$td10}" .
          "<a href=\"search.php?catagory=%d&order=%d&title=\" target=\"_blank\">%s</a>{$td10}" .
          "<a href=\"download.php?fid=%d\" target=\"_blank\"><b>%s</b></a>{$td10}" .
          "<a href=\"uploaduser.php?uid=%d\" target=\"_blank\">%s</a>{$td10}" .
          "%dK{$td10}%d{$td10}%d{$td10}%d{$td10}%d{$td1}</tr>",
          lapseTime($line["eventtime"]), $cat, $order, $score_catagory[$cat],
          $line["fid"], htmlentities($line["title"], ENT_COMPAT, "GB2312"),
          $uid, htmlentities($line["username"], ENT_COMPAT, "GB2312"),
          $line["size"], $line["price"], $line["download"], $line["positive"], $line["negative"]);
      $line = mysql_fetch_assoc($result);
    }
    echo "</table>";
  } else {
    echo warn("û������");
  }
?><!--webbot
                        bot="HTMLMarkup" endspan --></td>
                    </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td background="../images/headerbg.gif"><font
                size="3"><strong>��������</strong> </font><font
                size="2">(��Ҫ����120��)</font></td>
            </tr>
            <tr>
                <td align="center"><form action="comments.php"
                method="POST">
                    <p><textarea name="comments" rows="5"
                    cols="50"></textarea></p>
                    <p><input type="submit" value="�ύ"></p>
                </form>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E0E0E0"><p align="right"><script
                language="JavaScript"><!--
// --></script><a
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

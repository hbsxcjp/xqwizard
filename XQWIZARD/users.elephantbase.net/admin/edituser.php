<html>

<head>
<meta http-equiv="Content-Type"
content="text/html; charset=gb_2312-80">
<meta name="GENERATOR" content="Microsoft FrontPage Express 2.0">
<title>�༭�û� - ������ʦ�û�����</title>
</head>

<body bgcolor="#3869B6" topmargin="0" leftmargin="0"
bottommargin="0" rightmargin="0">

<table border="0" cellpadding="8" cellspacing="0" width="100%">
    <tr>
        <td>��</td>
        <td width="750" bgcolor="#FFFFFF"><table border="0"
        cellspacing="0" width="100%">
            <tr>
                <td colspan="3" background="../images/topbg.gif"><table
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
                <td colspan="3">��</td>
            </tr>
        </table>
        <table border="0" cellpadding="4" cellspacing="0"
        width="100%" bgcolor="#F0F0F0">
            <tr>
                <td background="../images/headerbg.gif"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  require_once "../mysql_conf.php";
  require_once "../common.php";
  require_once "./admin.php";

  $username = $_GET["username"];

  mysql_connect($mysql_host, $mysql_username, $mysql_password);
  mysql_select_db($mysql_database);
  $sql = sprintf("SELECT * FROM {$mysql_tablepre}user WHERE username = '%s'",
      mysql_real_escape_string($username));
  $result = mysql_query($sql);
  $line = mysql_fetch_assoc($result);
  if (!$line) {
    header("Location: close.htm#" . "�û�[" . $username . "]������");
    mysql_close();
    exit;
  }

  $act = $_GET["act"];
  if (false) {
    //
  } else if ($act == "charge") {
    // �������
    $charge = intval($_POST["charge"]);
    if ($charge > 0) {
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET points = points + %d WHERE username = '%s'",
          $charge, mysql_real_escape_string($username));
      mysql_query($sql);
      insertLog($username, EVENT_ADMIN_CHARGE, $charge);
      $info = info(sprintf("�û� %s �ѳ�ֵ %d ��", $username, $charge));
      $line["points"] += $charge;
    } else {
      $info = warn("��ֵ�����������0");
    }
  } else if ($act == "reset") {
    // ��������
    $password = $_POST["password"];
    if (strlen($password) <6) {
      $info = warn("���벻������6���ַ�");
    } else {
      $sql = sprintf("UPDATE {$mysql_tablepre}user SET password = '%s' WHERE username = '%s'",
          md5($username . $_POST["password"]), mysql_real_escape_string($username));
      mysql_query($sql);
      insertLog($username, EVENT_ADMIN_PASSWORD);
      $info = info("�����Ѹ���");
    }
  } else if ($act == "delete") {
    // ɾ���ʺ�
    $sql = sprintf("SELECT password FROM {$mysql_tablepre}user WHERE username = '%s'",
        mysql_real_escape_string($username));
    $result = mysql_query($sql);
    $line2 = mysql_fetch_assoc($result);
    if ($line2 && $line2["password"] == md5($username . $_POST["password2"])) {
      $sql = sprintf("DELETE FROM {$mysql_tablepre}user WHERE username = '%s'",
          mysql_real_escape_string($username));
      mysql_query($sql);
      insertLog($username, EVENT_ADMIN_DELETE);
      header("Location: close.htm#" . "�û�[" . $username . "]�ѱ�ɾ��");
      mysql_close();
      exit;
    } else {
      $info = warn("�������ɾ���û�ʧ��");
    }
  } else {
    $info = "";
  }
?><!--webbot
                bot="HTMLMarkup" endspan --><strong>��ϸ��Ϣ</strong></td>
            </tr>
            <tr>
                <td align="center"><table border="0">
                    <tr>
                        <td align="right"><font size="2">�û�����</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo htmlentities($username, ENT_COMPAT, "GB2312"); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">Email��</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo htmlentities($line["email"], ENT_COMPAT, "GB2312"); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">ע��IP��</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["regip"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">ע��ʱ�䣺</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo date("Y-m-d H:i:s", $line["regtime"]); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">�ϴε�¼IP��</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["lastip"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">�ϴε�¼ʱ�䣺</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo date("Y-m-d H:i:s", $line["lasttime"]); ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                    <tr>
                        <td align="right"><font size="2">�ɼ���</font></td>
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
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td><p align="center"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  echo $info;
  mysql_close();
?><!--webbot
                bot="HTMLMarkup" endspan --></p>
                </td>
            </tr>
            <tr>
                <td background="../images/headerbg.gif"><p
                align="left"><strong>�������</strong></p>
                </td>
            </tr>
            <tr>
                <td align="center"><form method="POST"
                id="frmCharge">
                    <p><font size="2">���������<input
                    type="text" size="20" name="charge"></font></p>
                    <p><input type="submit" value="�ύ"></p>
                </form>
                </td>
            </tr>
            <tr>
                <td background="../images/headerbg.gif"><p
                align="left"><strong>��������<script
                language="JavaScript"><!--
function sendmail() {
  var username = "<?php echo $username; ?>";
  var email = "<?php echo $email; ?>";
  var arrBody = [];
  arrBody.push(username + "�����ã�");
  arrBody.push("");
  arrBody.push("�������������ѱ�����Ϊ��" + frmReset.password.value);
  arrBody.push("�������ô������¼��������ʦ�û����ģ�");
  arrBody.push("��������http://users.elephantbase.net/login.htm");
  arrBody.push("������¼�ɹ��������ϰ�����ĵ���");
  arrBody.push("");
  arrBody.push("������л��ʹ��������ʦ��");
  arrBody.push("");
  arrBody.push("������ʦ�û�����");
  location.href = "mailto:webmaster@elephantbase.net?subject=�������� - ����������ʦ�û�����&body=" + arrBody.join("%0D%0A");
}
// --></script></strong></p>
                </td>
            </tr>
            <tr>
                <td align="center"><form method="POST"
                id="frmReset">
                    <table border="0">
                        <tr>
                            <td><font size="2">�������룺</font></td>
                            <td><font size="2"><input type="text"
                            size="20" name="password"
                            id="password"></font></td>
                        </tr>
                        <tr>
                            <td>��</td>
                            <td><a href="#" onclick="sendmail()"><font
                            size="2">���û�����Email</font></a></td>
                        </tr>
                    </table>
                    <p><input type="submit" value="�ύ"></p>
                </form>
                </td>
            </tr>
            <tr>
                <td background="../images/headerbg.gif"><strong>ɾ���ʺ�</strong></td>
            </tr>
            <tr>
                <td align="center"><form method="POST"
                id="frmDelete">
                    <p><font size="2">ȷ�����룺<input
                    type="password" size="20" name="password2"></font></p>
                    <p><input type="submit" value="�ύ"></p>
                </form>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E0E0E0"><p align="right"><script
                language="JavaScript"><!--
var action = "edituser.php?username=<?php echo urlencode($username); ?>&act=";
frmCharge.action = action + "charge";
frmReset.action = action + "reset";
frmDelete.action = action + "delete";
// --></script> <a
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

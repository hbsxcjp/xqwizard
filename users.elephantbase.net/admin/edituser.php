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
                <td background="../images/headerbg.gif"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  require_once "./admin.php";

  $username = $_GET["username"];

  $mysql_link = new MysqlLink;
  $sql = sprintf("SELECT u.uid, username, email, regip, regdate, lastip, lasttime, score, points, charged " .
        "FROM " . UC_DBTABLEPRE . "members u LEFT JOIN " . MYSQL_TABLEPRE . "user USING (uid) " .
        "WHERE username = '%s' AND score IS NOT NULL", $mysql_link->escape($username));
  $result = $mysql_link->query($sql);
  $line = mysql_fetch_assoc($result);
  if (!$line) {
    header("Location: close.htm#�û�[" . $username . "]������");
    $mysql_link->close();
    exit;
  }

  $uid = $line["uid"];
  $email = $line["email"];
  $act = $_GET["act"];
  if (false) {
    //
  } else if ($act == "charge") {
    // �������
    $charge = intval($_POST["charge"]);
    if ($charge > 0) {
      $sql = sprintf("UPDATE " . MYSQL_TABLEPRE . "user SET points = points + %d, charged = charged + %d " .
          "WHERE uid = '%s'", $charge, $charge, $uid);
      $mysql_link->query($sql);
      insertLog($uid, EVENT_ADMIN_CHARGE, $charge);
      $line["points"] += $charge;
      $line["charged"] += $charge;
      $info = info(sprintf("�û� %s �ѳ�ֵ %d �㣬��������ı����͸��û���", $username, $charge)) .
          "<font size=\"2\"><p align=\"left\">" . sprintf("������Ϊ����������ʦ�ʺ�[%s]��ֵ%d��",
          htmlentities($username, ENT_COMPAT, "GB2312"), $charge) .
          ($line["charged"] < USER_PLATINUM ? "" : "��������Ϊ�׽��Ա(��ʾ�ͻ��岻�۵�)") . "��<br>" .
          sprintf("Ŀǰ�����ʺŹ���%d����ã�", $line["points"]) .
          "����������ʦħ��ѧУ���û�����/��ѯ���������ܲ��ա�<br>" .
          "���κ����⡢����ͽ����뼰ʱ��������ϵ����л����������ʦ��֧�֡�</p></font>";
    } else {
      $info = warn("��ֵ�����������0");
    }
  } else if ($act == "reset") {
    // ��������
    $password = $_POST["password"];
    if (strlen($password) <6) {
      $info = warn("���벻������6���ַ�");
    } else {
      uc_user_edit($username , "", $password, $email, true);
      insertLog($uid, EVENT_ADMIN_PASSWORD);
      $info = info("�����Ѹ���");
    }
  } else if ($act == "delete") {
    // ɾ���ʺ�
    $password = $_POST["password2"];
    list($uid) = uc_user_login($username, $password);
    if ($uid > 0) {
      uc_user_delete($uid);
      $sql = sprintf("DELETE FROM " . MYSQL_TABLEPRE . "user WHERE uid = %d", $uid);
      $mysql_link->query($sql);
      insertLog($uid, EVENT_ADMIN_DELETE);
      header("Location: close.htm#�û�[" . $username . "]�ѱ�ɾ��");
      $mysql_link->close();
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
                        bot="HTMLMarkup" startspan --><?php echo htmlentities($email, ENT_COMPAT, "GB2312"); ?><!--webbot
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
                        bot="HTMLMarkup" startspan --><?php echo date("Y-m-d H:i:s", $line["regdate"]); ?><!--webbot
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
                    <tr>
                        <td align="right"><font size="2">��ֵ��</font></td>
                        <td align="right">��</td>
                        <td><font size="2"><!--webbot
                        bot="HTMLMarkup" startspan --><?php echo $line["charged"]; ?><!--webbot
                        bot="HTMLMarkup" endspan --></font></td>
                    </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td><p align="center"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  echo $info;
  $mysql_link->close();
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
                align="left"><strong>��������</strong></p>
                </td>
            </tr>
            <tr>
                <td align="center"><form method="POST"
                id="frmReset">
                    <p><font size="2">�������룺<input
                    type="text" size="20" name="password"
                    id="password"></font></p>
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

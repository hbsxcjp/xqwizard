<html>

<head>
<meta http-equiv="Content-Type"
content="text/html; charset=gb_2312-80">
<meta name="GENERATOR" content="Microsoft FrontPage Express 2.0">
<title>�û���Ϣ - ������ʦ�û�����</title>
</head>

<body bgcolor="#3869B6" topmargin="0" leftmargin="0"
bottommargin="0" rightmargin="0">

<table border="0" cellpadding="8" cellspacing="0" width="100%">
    <tr>
        <td>��</td>
        <td width="750" bgcolor="#FFFFFF"><table border="0"
        cellspacing="0" width="100%">
            <tr>
                <td background="images/topbg.gif"><table
                border="0" width="100%">
                    <tr>
                        <td valign="bottom" nowrap><table
                        border="0">
                            <tr>
                                <td nowrap><img
                                src="images/wizard.jpg"
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
                                src="images/elephantbase.gif"
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
                <td background="images/headerbg.gif"><table
                border="0" cellpadding="0" cellspacing="0"
                width="100%">
                    <tr>
                        <td><strong><!--webbot bot="HTMLMarkup"
                        startspan --><?php
  require_once "./user.php";

  echo htmlentities($userdata->username, ENT_COMPAT, "GB2312");
?><!--webbot
                        bot="HTMLMarkup" endspan -->�����ã�</strong></td>
                        <td><p align="right"><a href="logout.php"><font
                        size="2">��ע����</font></a></p>
                        </td>
                    </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td bgcolor="#F0F0F0"><p align="center"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  echo $userdata->info;
?><!--webbot
                bot="HTMLMarkup" endspan --></p>
                </td>
            </tr>
            <tr>
                <td bgcolor="#F0F0F0" id="admin"><p
                align="center"><font size="2">������ǹ���Ա�������</font><a
                href="admin/admin.htm"><font
                size="2">������</font></a><font size="2">ҳ��</font></p>
                </td>
            </tr>
            <tr>
                <td align="center" bgcolor="#F0F0F0"><font
                size="2">��ʾ��������ħ��ѧУ��ʹ�á��ύ�ɼ������ܣ����ĳɼ��Ż����</font></td>
            </tr>
            <tr>
                <td background="images/headerbg.gif"
                id="chargeTitle" style="display:none"><font
                size="3"><strong>�������</strong></font></td>
            </tr>
            <tr>
                <td align="center" bgcolor="#F0F0F0" id="charge"
                style="display:none"><form action="charge.php"
                method="POST">
                    <table border="0">
                        <tr>
                            <td align="right"><font size="2">�㿨���룺</font></td>
                            <td>��</td>
                            <td><input type="text" size="20"
                            name="chargecode"></td>
                        </tr>
                        <tr>
                            <td align="right">��</td>
                            <td>��</td>
                            <td><a href="charge.htm"
                            target="_blank"><font size="2">��β��������</font></a></td>
                        </tr>
                    </table>
                    <p><input type="submit" value="�ύ"></p>
                </form>
                </td>
            </tr>
            <tr>
                <td background="images/headerbg.gif"><font
                size="3"><strong>�����û���Ϣ</strong></font></td>
            </tr>
            <tr>
                <td align="center" bgcolor="#F0F0F0"><form
                action="updateinfo.php" method="POST" id="frm">
                    <table border="0">
                        <tr>
                            <td align="right"><font size="2">ԭ���룺</font></td>
                            <td align="right">��</td>
                            <td><input type="password" size="20"
                            name="password0"></td>
                        </tr>
                        <tr>
                            <td>��</td>
                            <td>��</td>
                            <td><font size="2">����������룬����������ԭ����</font></td>
                        </tr>
                        <tr>
                            <td align="right"><font size="2">�����룺</font></td>
                            <td align="right">��</td>
                            <td><input type="password" size="20"
                            name="password"></td>
                        </tr>
                        <tr>
                            <td align="right"><font size="2">ȷ�������룺</font></td>
                            <td align="right">��</td>
                            <td><input type="password" size="20"
                            name="password2"></td>
                        </tr>
                        <tr>
                            <td align="right">��</td>
                            <td align="right">��</td>
                            <td><font size="2">����6���ַ�����������ĸ�����ֺͷ��ŵ����</font></td>
                        </tr>
                        <tr>
                            <td align="right"><font size="2">Email��</font></td>
                            <td align="right">��</td>
                            <td><input type="text" size="20"
                            name="email" id="email"></td>
                        </tr>
                        <tr>
                            <td>��</td>
                            <td align="right">��</td>
                            <td><font size="2">�����������һ��������Ҫ;����������д</font></td>
                        </tr>
                    </table>
                    <p><input type="submit" value="�ύ"></p>
                </form>
                </td>
            </tr>
            <tr>
                <td id="chargeLink"><p align="center"><a href="#"
                onclick="showCharge()"><font size="2">�����������</font></a></p>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E0E0E0"><p align="right"><script
                language="JavaScript"><!--
function showCharge() {
  chargeTitle.style.display = "block";
  charge.style.display = "block";
  chargeLink.style.display = "none";
}

var userType = <?php echo $userdata->usertype; ?>;
admin.style.display = (userType == 128 ? "block" : "none");
if (userType > 0) {
  showCharge();
}
frm.email.value = "<?php echo $userdata->email; ?>";
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

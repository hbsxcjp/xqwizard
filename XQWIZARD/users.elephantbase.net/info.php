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
        width="100%">
            <tr>
                <td colspan="3" background="topbg.gif"><table
                border="0" width="100%">
                    <tr>
                        <td valign="bottom" nowrap><table
                        border="0">
                            <tr>
                                <td nowrap><img src="wizard.jpg"
                                width="64" height="64"><font
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
                                <td><p align="right"><a
                                target="_blank"><img
                                src="elephantbase.gif" width="88"
                                height="31"></a></p>
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
        <table border="0" cellpadding="4" width="100%">
            <tr>
                <td width="50%" background="headerbg.gif"><table
                border="0" cellpadding="0" cellspacing="0"
                width="100%">
                    <tr>
                        <td><strong><!--webbot bot="HTMLMarkup"
                        startspan --><?php
  session_start();
  session_register("userdata");
  if (!isset($_SESSION["userdata"])) {
    header("Location: login.htm#timeout");
    exit;
  }
  echo htmlentities($userdata["username"]);
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
                <td><p align="center"><!--webbot
                bot="HTMLMarkup" startspan --><?php
  echo $userdata["info"];
?><!--webbot
                bot="HTMLMarkup" endspan --></p>
                </td>
            </tr>
            <tr>
                <td id="admin"><p align="center"><font size="2">������ǹ���Ա�������</font><a
                href="admin.htm" target="_blank"><font size="2">������</font></a><font
                size="2">ҳ��</font></p>
                </td>
            </tr>
            <tr>
                <td width="50%" background="headerbg.gif"><font
                size="3"><strong>�����û���Ϣ</strong></font></td>
            </tr>
            <tr>
                <td align="center"><form action="updateinfo.php"
                method="POST" id="frm">
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
                    <p><input type="submit" name="B1"
                    value="�ύ"><script language="JavaScript"><!--
frm.email.value = "<?php echo $userdata['email']; ?>";
admin.style.display = "<?php echo $userdata['usertype'] == 128 ? 'block' : 'none' ; ?>";
// --></script></p>
                </form>
                </td>
            </tr>
            <tr>
                <td><p align="right"><a
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

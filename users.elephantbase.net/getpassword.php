<?php
  require_once "./common.php";
  require_once "./ejewimage.php";

  $username = $_POST["username"];
  $email = $_POST["email"];
  $ejew = $_POST["ejew"];

  if ($ejew == $_SESSION["ejew_value"]) {
    $mysql_link = new MysqlLink;
    if ($data = uc_get_user($username)) {
      list($uid, $username, $email2) = $data;
      if ($email == $email2) {
        $password = substr(md5(mt_rand()), 0, 6);
        uc_user_edit($username, "", $password, "", true);
        insertLog($uid, EVENT_GETPASSWORD);
        $succ = sendMail($email, $username . "�������ѱ�����",
            $username . "�����ã�\r\n\r\n" .
            "�������������ѱ�����Ϊ��" . $password . "\r\n" .
            "�������ô������¼��������ʦ�û����ģ�\r\n" .
            "��������http://users.elephantbase.net/login.htm\r\n" .
            "������¼�ɹ��������ϰ�����ĵ���\r\n\r\n" .
            "������л��ʹ��������ʦ��\r\n\r\n" .
            "������ʦ�û�����");
        if ($succ) {
          header("Location: getpassword2.htm#info");
        } else {
          header("Location: getpassword2.htm#fail");
        }
      } else {
        header("Location: getpassword2.htm#error");
      }
    } else {
      header("Location: getpassword2.htm#error");
    }
    $mysql_link->close();
  } else {
    header("Location: getpassword2.htm#ejew");
  }
?>
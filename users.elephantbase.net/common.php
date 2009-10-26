<?php
  require_once dirname(__FILE__) . "/config.php";
  require_once dirname(__FILE__) . "/uc_client/client.php";
  require_once dirname(__FILE__) . "/lib/class.phpmailer.php";

  // ������ʾ��HTML(��ɫ)
  function info($msg) {
    return "<font size=\"2\" color=\"blue\">" . htmlentities($msg, ENT_COMPAT, "GB2312") . "</font>";
  }

  // ���ؾ����HTML(��ɫ)
  function warn($msg) {
    return "<font size=\"2\" color=\"red\">" . htmlentities($msg, ENT_COMPAT, "GB2312") . "</font>";
  }

  // ��JavaScript���HTML
  function jsWrite($html) {
    echo "document.write(\"{$html}\");\r\n";
  }

  // ��ÿͻ���IP��ַ
  function getRemoteAddr() {
    if (isset($_SERVER["HTTP_CLIENT_IP"])) {
      return $_SERVER["HTTP_CLIENT_IP"];
    }
    if (isset($_SERVER["HTTP_X_FORWARDED_FOR"])) {
      return $_SERVER["HTTP_X_FORWARDED_FOR"];
    }
    return $_SERVER["REMOTE_ADDR"];
  }

  // ��õ�ǰURL
  function requestUrl() {
    return $_SERVER["PHP_SELF"] . "?" . $_SERVER["QUERY_STRING"];
  }

  $arrPeriod = array("��", "����", "Сʱ", "��", "����", "����", "��");
  $arrSeconds = array(60, 3600, 86400, 604800, 2592000, 31536000);

  // ������/����/Сʱ/��/��/��/��ǰ
  function lapseTime($lastTime) {
    global $arrPeriod, $arrSeconds;

    $lapse = time() - $lastTime;
    $dir = "ǰ";
    if ($lapse < 0) {
      $lapse = -$lapse;
      $dir = "��";
    }
    $seconds = 1;
    for ($i = 0; $i < count($arrSeconds); $i ++) {
      if ($lapse < $arrSeconds[$i]) {
        break;
      }
      $seconds = $arrSeconds[$i];
    }
    return floor($lapse / $seconds) . $arrPeriod[$i] . $dir;
  }

  class MysqlLink {
    var $link;

    function MysqlLink() {
      $this->link = mysql_connect(MYSQL_HOST, MYSQL_USERNAME, MYSQL_PASSWORD);
      mysql_select_db(MYSQL_DATABASE, $this->link);
    }

    function query($sql) {
      return mysql_query($sql, $this->link);
    }

    function affected_rows() {
      return mysql_affected_rows($this->link);
    }

    function insert_id() {
      return mysql_insert_id($this->link);
    }

    function escape($str) {
      return mysql_real_escape_string($str, $this->link);
    }

    function close() {
      mysql_close($this->link);
    }
  }

  // �û�����
  define("USERTYPE_ADMIN", 128);

  class UserData {
    var $uid, $username, $email, $usertype, $score, $points, $charged, $info;

    function UserData($uid, $username, $email, $line = null) {
      $this->uid = $uid;
      $this->username = $username;
      $this->email = $email;
      if ($line) {
        $this->usertype = $line["usertype"];
        $this->score = $line["score"];
        $this->points = $line["points"];
        $this->charged = $line["charged"];
      } else {
        $this->usertype = $this->score = $this->points = $this->charged = 0;
      }
    }

    function isAdmin() {
      return $this->usertype >= USERTYPE_ADMIN;
    }
  }

  // ��¼
  function login($username, $password) {
    global $mysql_link;

    // ���ȼ��"retry"�����Ƿ����������Լ�¼
    $sql = sprintf("SELECT retrycount, retrytime FROM " . MYSQL_TABLEPRE . "retry " .
        "WHERE username = '%s'", $mysql_link->escape($username));
    $result = $mysql_link->query($sql);
    $retry = mysql_fetch_assoc($result);
    if ($retry) {
      // ���δ�ﵽ��������ʱ�䣬���ֹ����
      if (time() < $retry["retrytime"]) {
        return "noretry";
      }
    }

    // ��¼
    list($uid, $dummy, $password, $email) = uc_user_login($username, $password);

    // ���û�в�ѯ���û����򷵻ء���¼ʧ�ܡ�
    if ($uid == -1) {
      return "error";
    }

    // ������벻�ԣ������û��Ƿ��ڡ������ƽ⡱
    if ($uid == -2) {
      // ���"retry"����û���������Լ�¼�������Ӹü�¼
      if (!$retry) {
        $sql = sprintf("INSERT INTO " . MYSQL_TABLEPRE . "retry (username, retrycount, retrytime) " .
            "VALUES ('%s', 1, 0)", $mysql_link->escape($username));
        $mysql_link->query($sql);
        return "error";
      }
      // ���������������5�Σ������Դ�����1����������
      if ($retry["retrycount"] < 5) {
        $sql = sprintf("UPDATE " . MYSQL_TABLEPRE . "retry SET retrycount = retrycount + 1 " .
            "WHERE username = '%s'", $mysql_link->escape($username));
        $mysql_link->query($sql);
        return "error";
      }
      // ���Դ����ﵽ5�Σ���������ʱ�䣬��ֹ����
      $sql = sprintf("UPDATE " . MYSQL_TABLEPRE . "retry SET retrycount = 0, retrytime = %d " .
          "WHERE username = '%s'", time() + 300, $mysql_link->escape($username));
      $mysql_link->query($sql);
      return "noretry";
    }

    // �����������
    if ($uid <= 0) {
      return "error";
    }

    // ��¼�ɹ���ɾ���������Լ�¼
    if ($retry) {
      $sql = sprintf("DELETE FROM " . MYSQL_TABLEPRE . "retry " .
          "WHERE username = '%s'", $mysql_link->escape($username));
      $mysql_link->query($sql);
    }

    $sql = sprintf("SELECT * FROM " . MYSQL_TABLEPRE . "user WHERE uid = %d", $uid);
    $result = $mysql_link->query($sql);
    $line = mysql_fetch_assoc($result);
    // ���"user"����û�м�¼��������¼
    if (!$line) {
      $sql = sprintf("INSERT INTO " . MYSQL_TABLEPRE . "user (uid, lastip, lasttime) " .
          "VALUES (%d, '%s', %d)", $uid, getRemoteAddr(), time());
      $mysql_link->query($sql);
      return new UserData($uid, $username, $email);
    }
    // ����"user"��
    $sql = sprintf("UPDATE " . MYSQL_TABLEPRE . "user SET lastip = '%s', lasttime = %d " .
        "WHERE uid = %d", getRemoteAddr(), time(), $uid);
    $mysql_link->query($sql);
    return new UserData($uid, $username, $email, $line);
  }

  // �¼�����
  define("EVENT_REGISTER", 101);
  define("EVENT_LOGIN", 102);
  define("EVENT_CHARGE", 105);
  define("EVENT_EMAIL", 106);
  define("EVENT_PASSWORD", 107);
  define("EVENT_GETPASSWORD", 109);
  define("EVENT_SAVE", 111);
  define("EVENT_RETRACT", 121);
  define("EVENT_HINT", 122);
  define("EVENT_CHARGECODE", 150);
  define("EVENT_ADMIN_CHARGE", 201);
  define("EVENT_ADMIN_PASSWORD", 202);
  define("EVENT_ADMIN_DELETE", 299);

  // �û�����
  define("USER_PLATINUM", 2800);
  define("USER_DIAMOND", 8800);

  // ��¼��־
  function insertLog($uid, $eventtype, $detail = 0) {
    global $mysql_link;
    $sql = sprintf("INSERT INTO " . MYSQL_TABLEPRE . "log (uid, eventip, eventtime, eventtype, detail) " .
        "VALUES (%d, '%s', %d, %d, %d)", $uid, getRemoteAddr(), time(), $eventtype, $detail);
    $mysql_link->query($sql);
  }

  // �����ʼ�
  function sendMail($to, $subject, $body) {
    $mail = new PHPMailer;
    $mail->Mailer = "smtp";
    $mail->SMTPAuth = true;
    $mail->Host = SMTP_HOST;
    $mail->Username = SMTP_USERNAME;
    $mail->Password = SMTP_PASSWORD;
    $mail->CharSet = "gbk";
    $mail->From = SMTP_FROM;
    $mail->FromName = SMTP_FROMNAME;
    $mail->AddAddress($to);
    $mail->Subject = $subject;
    $mail->Body = $body;
    return $mail->Send();
  }

  // ��������
  $score_catagory = array("ȫ��", "ȫ��", "����", "�о�", "�о�", "�ž�", "����", "����", "�̲�");

  // ��������
  define("SCORE_ORDER_DOWNLOAD", 1);
  define("SCORE_ORDER_POSITIVE", 2);
  define("SCORE_ORDER_EVENTTIME", 3);
?>
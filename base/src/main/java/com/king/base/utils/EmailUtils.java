package com.king.base.utils;

//import com.xiang.mail.Mail;
//import com.xiang.mail.MailSender;

public
class EmailUtils {

    public static void send(String subject,String content){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //    implementation 'com.github.zongxiaomi:AndroidMail:1.07'
//                Mail mail = new Mail();
//                mail.setMailServerHost("smtp.qq.com");
//                mail.setMailServerPort("25");
//                mail.setUserName("2149962127");
//                mail.setPassword("ijtwskrvumskdjdb");
//                mail.setFromAddress("2149962127@qq.com");
//                mail.setToAddress(new String[]{"784065153@qq.com"});
//                mail.setSubject(subject);
//                mail.setContent(content);
//                MailSender mailSender = new MailSender();
//                mailSender.sendTextMail(mail);
            }
        }).start();
    }
}

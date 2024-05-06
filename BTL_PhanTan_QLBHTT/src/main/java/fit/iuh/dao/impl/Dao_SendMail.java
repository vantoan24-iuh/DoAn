/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;

import fit.iuh.dao.ISendMail;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Properties;


/**
 *
 * @author phant
 */
public class Dao_SendMail extends UnicastRemoteObject implements ISendMail {
    static final String fromEmail = "phansinh0606@gmail.com";
    static final String password = "elihvpvqateibfbe";

    public Dao_SendMail() throws RemoteException {
    }

    @Override
    public void sendEmailMa(String toEmail, String tieuDe, String noiDung) throws RemoteException{
        //Khai báo các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");// thông tin đầu là gửi từ đâu -> gửi từ gmail,
        props.put("mail.smtp.port", "587"); //tls 587 ssl 465
        props.put("mail.smtp.auth", "true"); //Dùng để đăng nhập vào gmail
        props.put("mail.smtp.starttls.enable", "true"); //khơi tạo giao thức
        
        //Tạo Authenticator:tạo ra tài khoản để đăng nhập 
        Authenticator auth = new Authenticator() {
            //Nhập mật khẩu và tài khoản -> để đăng nhập vào gmail
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        
        //Cấu hình phiên làm việc liên kết với auth thông qua tài khoản đăng nhập để khi quản lý gmail
        Session session = Session.getInstance(props, auth);
        
        //Tạo hộp thoại tin nhắn gửi
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Conten-type", "text;charset=UTF-8"); // Kiểu nội dung
            
            //Người gửi
            msg.setFrom(fromEmail);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));//gửi đến người nhận
            
            //Tiêu đề mail
            msg.setSubject(tieuDe);
            
            //Quy định ngày gửi
            msg.setSentDate(new Date());
            
            //Nội dung gửi
            msg.setText(noiDung,"UTF-8");
            System.out.println(noiDung.toString());
            //Gui mail thông qua giao thức
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Thực hiện gửi hóa đơn qua email
     * @param toEmail email khách hàng  
     * @param tieuDe   tiêu đề gửi
     * @param noiDung    nội dung hóa đơn
     */
    @Override
    public void guiHoaDonVeEmail(String toEmail, String tieuDe, String noiDung) throws RemoteException{
        //Khai báo các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");// thông tin đầu là gửi từ đâu -> gửi từ gmail,
        props.put("mail.smtp.port", "587"); //tls 587 ssl 465
        props.put("mail.smtp.auth", "true"); //Dùng để đăng nhập vào gmail
        props.put("mail.smtp.starttls.enable", "true"); //khơi tạo giao thức
        
        //Tạo Authenticator:tạo ra tài khoản để đăng nhập 
        Authenticator auth = new Authenticator() {
            //Nhập mật khẩu và tài khoản -> để đăng nhập vào gmail
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }

        };
        
        //Cấu hình phiên làm việc liên kết với auth thông qua tài khoản đăng nhập để khi quản lý gmail
        Session session = Session.getInstance(props, auth);
        
        //Tạo hộp thoại tin nhắn gửi
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Conten-type", "text/HTML;charset=UTF-8"); // Kiểu nội dung
            
            //Người gửi
            msg.setFrom(fromEmail);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));//gửi đến người nhận
            
            //Tiêu đề mail
            msg.setSubject(tieuDe);
            
            //Quy định ngày gửi
            msg.setSentDate(new Date());
            
            //Nội dung gửi
            msg.setContent(noiDung,"text/html;charset=UTF-8");
            
            //Gui mail thông qua giao thức
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            
}

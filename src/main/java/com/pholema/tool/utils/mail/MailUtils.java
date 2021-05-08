package com.pholema.tool.utils.mail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

public class MailUtils {

	static Logger logger = Logger.getLogger(MailUtils.class);

	private static String To;
    private static String Cc;
    private static String Bcc;
    //private static String From;
    //private static String Subject;
    //private static String Content;
    private static String SmtpHost;
    private static Message MyMessage ;
    
    
    public static String ADMIN_MAIL = "a@b.c";
    public static String SYSTEM_MAIL = "a@b.c";

    static {
    	SmtpHost = System.getProperty("mail.smtp.host");
    }
    
    // throws MessagingException
    public static void sendMessage(
            String to,
			String cc,
			String bcc,
			String from,
			String subject,
			String content) {
    	To = to;
    	Cc = cc;
    	Bcc = bcc;
    	//From = from;
    	//Subject = subject;
    	//Content = content;
    	MyMessage = createMessage();
    	
        try {
        	MyMessage.setFrom(new InternetAddress(from));
        	setToCcBccRecipients();
        	MyMessage.setSentDate(new Date());
        	MyMessage.setSubject(subject);
        	MyMessage.setText(content);
			Transport.send(MyMessage);
		} catch (MessagingException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		}
    }

    public static Message createMessage(){
    	Properties properties = new Properties();
        properties.put("mail.smtp.host", SmtpHost);
        Session session = Session.getDefaultInstance(properties, null);
        return new MimeMessage(session);
    }

    // throws AddressException, MessagingException
    public static void setToCcBccRecipients() throws MessagingException {
        setMessageRecipients(To, Message.RecipientType.TO);
        if (Cc != null) {
             setMessageRecipients(Cc, Message.RecipientType.CC);
        }
        if (Bcc != null) {
             setMessageRecipients(Bcc, Message.RecipientType.BCC);
        }
    }

    // throws AddressException, MessagingException
    public static void setMessageRecipients( String recipient,Message.RecipientType recipientType) throws MessagingException {
    	InternetAddress[] addressArray = buildInternetAddressArray(recipient);
        if ((addressArray != null) && (addressArray.length > 0))
        	MyMessage.setRecipients(recipientType, addressArray);
    }

    // throws AddressException
    public static InternetAddress[] buildInternetAddressArray(String address) throws AddressException{
        return InternetAddress.parse(address);
    }

    public static void main(String[] args){
    	MailUtils.sendMessage(MailUtils.ADMIN_MAIL, null, null, MailUtils.SYSTEM_MAIL, "快下班了~", "gogo");
    }
}

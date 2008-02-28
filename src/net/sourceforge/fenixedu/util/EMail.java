package net.sourceforge.fenixedu.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EMail extends FenixUtil {
    private String Servidor;

    private String Origem;

    public static String W3C_EMAIL_SINTAX_VALIDATOR = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$";

    /**
     * Metodo Construtor, recebe um Servidor e uma Origem por omissao
     */
    public EMail(String Servidor, String Origem) {
	this.Servidor = Servidor;
	this.Origem = Origem;
    }

    /**
     * Envia um mail utilizando o Servidor e Origem por Omissao
     */
    public boolean send(String Destino, String Assunto, String Texto) {
	return _send(this.Servidor, this.Origem, Destino, Assunto, Texto);
    }

    /**
     * Envia um mail utilizando o Servidor por Omissao
     */
    public boolean send(String Origem, String Destino, String Assunto, String Texto) {
	return _send(this.Servidor, Origem, Destino, Assunto, Texto);
    }

    /**
     * Envia um mail
     */
    public static boolean send(String Servidor, String Origem, String Destino, String Assunto, String Texto) {
	return _send(Servidor, Origem, Destino, Assunto, Texto);
    }

    /**
     * Envia um mail com anexo
     */
    public static boolean send(String Servidor, String Origem, String Destino, String Assunto, String Texto, String fileName,
	    String attachment) {
	return _send(Servidor, Origem, Destino, Assunto, Texto, fileName, attachment);
    }

    public static List send(String server, String fromName, String fromAddress, String subject, List tos, List ccs, List bccs,
	    String body) {
	List<String> unsentMails = new LinkedList<String>();
	/* Configura as propriedades do sistema */
	Properties props = new Properties();
	props.put("mail.smtp.host", server);
	/* Obtem a sessao */
	Session sessao = Session.getDefaultInstance(props, null);
	/* Cria a mensagem */
	MimeMessage mensagem = new MimeMessage(sessao);
	try {
	    String composedFrom = fromAddress;
	    if (fromName != null && !fromName.equals(""))
		composedFrom = "\"" + fromName + "\"" + " <" + fromAddress + ">";
	    /* Define os parametros da mensagem */
	    mensagem.setFrom(new InternetAddress(composedFrom));
	    addRecipients(mensagem, Message.RecipientType.TO, tos, unsentMails);
	    addRecipients(mensagem, Message.RecipientType.CC, ccs, unsentMails);
	    addRecipients(mensagem, Message.RecipientType.BCC, bccs, unsentMails);
	    mensagem.setSubject(subject);
	    mensagem.setText(body);
	    Transport.send(mensagem);
	} catch (SendFailedException e) {
	    if (e.getValidUnsentAddresses() != null) {
		for (int i = 0; i < e.getValidUnsentAddresses().length; i++) {
		    unsentMails.add(e.getValidUnsentAddresses()[i].toString());
		}
	    } else {
		if (e.getValidSentAddresses() == null || e.getValidSentAddresses().length == 0) {
		    unsentMails.addAll(tos);
		    unsentMails.addAll(ccs);
		    unsentMails.addAll(bccs);
		}
	    }
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
	return unsentMails;
    }

    private static void addRecipients(MimeMessage mensagem, RecipientType recipientType, List<String> emailAddresses,
	    List<String> unsentMails) throws MessagingException {
	for (final String emailAddress : emailAddresses) {
	    try {
		if (emailAddressFormatIsValid(emailAddress)) {
		    mensagem.addRecipient(recipientType, new InternetAddress(emailAddress));
		}
	    } catch (AddressException e) {
		unsentMails.add(emailAddress);
	    }
	}
    }

    private static boolean _send(String Servidor, String Origem, String Destino, String Assunto, String Texto) {
	/* Configura as propriedades do sistema */
	Properties props = new Properties();
	props.put("mail.smtp.host", Servidor);
	/* Obtem a sessao */
	Session sessao = Session.getDefaultInstance(props, null);
	/* Cria a mensagem */
	MimeMessage message = new MimeMessage(sessao);

	try {
	    /* Define os parametros da mensagem */
	    message.setFrom(new InternetAddress(Origem));
	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(Destino));
	    message.setSubject(Assunto);
	    message.setText(Texto);
	    Transport.send(message);
	} catch (AddressException e) {
	    return false;
	} catch (MessagingException e) {
	    return false;
	}
	return true;
    }

    private static boolean _send(String Servidor, String Origem, String Destino, String Assunto, String Texto, String fileName,
	    String Attachment) {
	// Configura as propriedades do sistema
	Properties props = new Properties();
	props.put("mail.smtp.host", Servidor);

	// Obtem a sessao
	Session sessao = Session.getDefaultInstance(props, null);

	// Cria a mensagem
	MimeMessage mensagem = new MimeMessage(sessao);

	try {
	    // Define os parametros da mensagem
	    mensagem.setFrom(new InternetAddress(Origem));
	    mensagem.addRecipient(Message.RecipientType.TO, new InternetAddress(Destino));
	    mensagem.setSubject(Assunto);

	    // Create the message part
	    BodyPart messageBodyPart = new MimeBodyPart();

	    // Fill the message
	    messageBodyPart.setText(Texto);

	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(messageBodyPart);

	    // Part two is attachment
	    messageBodyPart = new MimeBodyPart();
	    DataSource source = new FileDataSource(Attachment);
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(fileName);
	    multipart.addBodyPart(messageBodyPart);

	    mensagem.setContent(multipart);

	    Transport.send(mensagem);
	} catch (AddressException e) {
	    return false;
	} catch (MessagingException e) {
	    return false;
	}
	return true;
    }

    public static boolean emailAddressFormatIsValid(String emailAddress) {
	if ((emailAddress == null) || (emailAddress.length() == 0))
	    return false;

	return emailAddress.matches(W3C_EMAIL_SINTAX_VALIDATOR);
    }

}

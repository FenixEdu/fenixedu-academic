package Util;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EMail implements Serializable
{
	private String Servidor;
	private String Origem;
	/**
	 * Metodo Construtor, recebe um Servidor e uma Origem por omissao
	 */
	public EMail(String Servidor, String Origem)
	{
		this.Servidor = Servidor;
		this.Origem = Origem;
	}
	/**
	 * Envia um mail utilizando o Servidor e Origem por Omissao
	 */
	public boolean send(String Destino, String Assunto, String Texto)
	{
		return _send(this.Servidor, this.Origem, Destino, Assunto, Texto);
	}
	/**
	 * Envia um mail utilizando o Servidor por Omissao
	 */
	public boolean send(String Origem, String Destino, String Assunto, String Texto)
	{
		return _send(this.Servidor, Origem, Destino, Assunto, Texto);
	}
	/**
	 * Envia um mail
	 */
	public static boolean send(String Servidor, String Origem, String Destino, String Assunto, String Texto)
	{
		return _send(Servidor, Origem, Destino, Assunto, Texto);
	}
	public static List send(String server, String fromName, String fromAddress, String subject, List tos, List ccs, List bccs, String body)
	{
		List unsentMails = new LinkedList();
		/* Configura as propriedades do sistema */
		Properties props = new Properties();
		props.put("mail.smtp.host", server);
		/* Obtem a sessao */
		Session sessao = Session.getDefaultInstance(props, null);
		/* Cria a mensagem */
		MimeMessage mensagem = new MimeMessage(sessao);
		try
		{
			String composedFrom = fromAddress;
			if (fromName != null && !fromName.equals(""))
				composedFrom = "\"" + fromName + "\"" + " <" + fromAddress + ">";
			/* Define os parametros da mensagem */
			mensagem.setFrom(new InternetAddress(composedFrom));
			for (Iterator iter = bccs.iterator(); iter.hasNext();)
			{
				String bcc = (String) iter.next();
				try
				{
					if (bcc != null) {
						mensagem.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
					}
					
				} catch (AddressException e)
				{
					unsentMails.add(bcc);
				}
			}
			for (Iterator iter = tos.iterator(); iter.hasNext();)
			{
				String to = (String) iter.next();
				try
				{
					if (to != null) {
						mensagem.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					}
					
				} catch (AddressException e)
				{
					unsentMails.add(to);
				}
			}
			for (Iterator iter = ccs.iterator(); iter.hasNext();)
			{
				String cc = (String) iter.next();
				try
				{
					if (cc != null) {
						mensagem.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
					}
					
				} catch (AddressException e)
				{
					unsentMails.add(cc);
				}
			}
			mensagem.setSubject(subject);
			mensagem.setText(body);
			Transport.send(mensagem);
		} catch (SendFailedException e)
		{
			System.out.println("EMail: Nao foi possivel enviar o email");
			for (int i = 0; i < e.getValidUnsentAddresses().length; i++)
				unsentMails.add(e.getValidUnsentAddresses()[i]);
			e.printStackTrace(System.out);
		} catch (MessagingException e)
		{
			System.out.println("EMail: Nao foi possivel enviar o email");
			e.printStackTrace(System.out);
		}
		return unsentMails;
	}
	private static boolean _send(String Servidor, String Origem, String Destino, String Assunto, String Texto)
	{
		/* Configura as propriedades do sistema */
		Properties props = new Properties();
		props.put("mail.smtp.host", Servidor);
		/* Obtem a sessao */
		Session sessao = Session.getDefaultInstance(props, null);
		/* Cria a mensagem */
		MimeMessage message = new MimeMessage(sessao);

		try
		{
			/* Define os parametros da mensagem */
			message.setFrom(new InternetAddress(Origem));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(Destino));
			message.setSubject(Assunto);
			message.setText(Texto);
			Transport.send(message);
		} catch (AddressException e)
		{
			System.out.println("EMail: Nao foi possivel enviar o email");
			e.printStackTrace(System.out);
			return false;
		} catch (MessagingException e)
		{
			System.out.println("EMail: Nao foi possivel enviar o email");
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}
}
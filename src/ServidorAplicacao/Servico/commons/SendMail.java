/*
 * Created on 18/Set/2003, 19:16:29
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.commons;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import ServidorAplicacao.IServico;
import Util.EMail;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 18/Set/2003, 19:16:29
 * 
 */
public class SendMail implements IServico
{
	private static SendMail service= new SendMail();
	private static String bundleFile= new String("SMTPConfiguration");
	private static ResourceBundle bundle= null;
	private static String mailServer= null;
	/**
	 * The singleton access method of this class.
	 **/
	public static SendMail getService()
	{
		return service;
	}
	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome()
	{
		return "commons.SendMail";
	}
	//returns the list of the e-mail addresses to which it wasn't possible to deliver the mail
	public List run(List toList, String fromName, String from, String subject, String text)
	{
		if (bundle == null)
		{
			try
			{
				SendMail.bundle= ResourceBundle.getBundle(SendMail.bundleFile);
				SendMail.mailServer= SendMail.bundle.getString("server.url");
			}
			catch (Exception e)
			{
                // the default server
				SendMail.mailServer = "mail.adm";
			}
		}
		List failedMails= new LinkedList();
		for (Iterator iter= toList.iterator(); iter.hasNext();)
		{
			String composedFrom= new String();
			String to= (String) iter.next();
			if (!fromName.equals(""))
				composedFrom= "\"" + fromName + "\"" + " <" + from + ">";
			EMail email= new EMail(SendMail.mailServer, composedFrom);
			if (!email.send(to, subject, text))
				failedMails.add(to);
		}
		return failedMails;
	}
}

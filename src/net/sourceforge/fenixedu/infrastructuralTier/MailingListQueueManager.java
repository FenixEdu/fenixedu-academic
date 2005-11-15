/**
 * 
 */


package net.sourceforge.fenixedu.infrastructuralTier;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 10:24:51,20/Out/2005
 * @version $Id$
 */
public class MailingListQueueManager implements Runnable
{
	public static long SENDING_MAIL_TIMEOUT = 1000*60*5; 
	
	private class MailSenderEntry
	{		
		private long timestamp;
		private Integer mailingListIdInternal;
		public MailSenderEntry(Integer mailingListIdInternal, long timestamp)
		{
			this.timestamp = timestamp;
			this.mailingListIdInternal = mailingListIdInternal;
		}
		public Integer getMailingListIdInternal()
		{
			return mailingListIdInternal;
		}
		public long getTimestamp()
		{
			return timestamp;
		}		
	}
	
	private Map<Integer,MailSenderEntry> jobs;
	private String username;
	private String password;
	private IUserView userView;

	public MailingListQueueManager(String username, String password) throws FenixFilterException,
			FenixServiceException
	{
		this.jobs = new HashMap<Integer,MailSenderEntry>();
		this.username = username;
		this.password = password;
	}

	public void run()
	{
		System.out.println("$$$$$$$$$$Eu senhor todo poderoso vou ver se e preciso comprar escravos");
		try
		{
			if (this.userView == null)
			{
				Object[] args =
				{ this.username, this.password, null, "http://localhost.e.troca.o.passo" }; // TODO:
				// isto
				// e
				// ridiculo
				// !!!!!!!!!!!!!!!
				// O
				// servico
				// n
				// pode
				// assumir
				// que
				// existe
				// um
				// URL
				// !!!!!!!!!!!!
				this.userView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", args);
			}
			Collection<IMailingList> mailingLists = (Collection<IMailingList>) ServiceManagerServiceFactory.executeService(userView, "ReadAllMailingLists", new Object[] {});
			for (IMailingList mailingList : mailingLists)
			{
				if (mailingList.getQueue().getMessagesCount() > 0)
				{
					synchronized (this.jobs)
					{
						if (this.jobs.containsKey(mailingList.getIdInternal()))
						{
							long howLong = System.currentTimeMillis() - this.jobs.get(mailingList.getIdInternal()).getTimestamp();
							if (howLong > SENDING_MAIL_TIMEOUT)
							{
								System.out.println("$$$$$$$$$$$Por acaso ja alguem esta a processar a ML HA TEMPO DEMAIS !!!!!!!!!! " + mailingList.getIdInternal());	
							}
							System.out.println("$$$$$$$$$$$Por acaso ja alguem esta a processar a ML " + mailingList.getIdInternal());
						}
						else
						{
							this.jobs.put(mailingList.getIdInternal(),new MailSenderEntry(mailingList.getIdInternal(),System.currentTimeMillis()));
							System.out.println("$$$$$$$$$$Vou criar um escravo !! para a ml:  "
									+ mailingList.getName() + "(" + mailingList.getIdInternal() + ")");
							MailSender sender = new MailSender(this, mailingList);
							new Thread(sender).start();
							System.out.println("$$$$$$$$$$Ja seguiu viagem o escravo para a ml:  "
									+ mailingList.getName() + "(" + mailingList.getIdInternal() + ")");							
						}
					}
				}
			}
			System.out.println("$$$$$$$$$$Ja fiz o que tinha a fazer, ate breve !");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param idInternal
	 */
	public void signalFinishedProcessing(Integer mailingListIdInternal)
	{
		System.out.println("$$$$$$$$$$recebi um aviso de um escravo. Parece que terminou o processamento da lista :  "
				+ mailingListIdInternal);
		synchronized (this.jobs)
		{
			System.out.println("$$$$$$$$$$Vou então remover o gajo da lista:  " + mailingListIdInternal);
			this.jobs.remove(mailingListIdInternal);
			System.out.println("$$$$$$$$$$Done " + mailingListIdInternal);
		}
	}
}

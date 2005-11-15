/**
 * 
 */
package net.sourceforge.fenixedu.infrastructuralTier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import relations.MailingListQueueOutgoingMails;

import net.sourceforge.fenixedu.domain.cms.infrastructure.CmsConfiguration;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.stm.Transaction;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:34:01,20/Out/2005
 * @version $Id$
 */
public class MailSender implements Runnable
{
	private MailingListQueueManager manager;
	private IMailingList mailingList;
	
	public MailSender (MailingListQueueManager manager, IMailingList mailingList)
	{	
		this.manager = manager;
		this.mailingList = mailingList;
	}

	public void run()
	{
		Transaction.begin();
		System.out.println("#######Vou começar a tratar as cenas (ml:" +this.mailingList.getName() +")!!!!!!!!!!, sou o gajo que de facto envia");
		Random r = new Random();
		if (r.nextInt(2) < 1);
		{
			System.out.println("#######Este vai demorar....... (" + this.manager);
//				Thread.sleep(14000);
			System.out.println("#######Acordei (" + this.manager + ") que acabei!!");
		}
		Collection<IMailMessage> localQueue = new ArrayList<IMailMessage>();
		localQueue.addAll(mailingList.getQueue().getMessages());
		for (IMailMessage message : localQueue)
		{
			// enviar !!!!!!!!!! n esquecer do REPLY TO LIST se for o caso
			MailingListQueueOutgoingMails.remove(mailingList.getQueue(),message);
			String hello = mailingList.getOwner().getCms().getConfiguration().getSmtpServerAddressToUse();
		}
		System.out.println("#######Vou avisar o meu manager (" + this.manager + ") que acabei!!");
		manager.signalFinishedProcessing(mailingList.getIdInternal());
		System.out.println("#######Adeus mundo cruel");		
		Transaction.commit();
		System.out.println("######## E COMMITEI !!!!!!!!!!!!!! :D :D: D:D");
	}

}

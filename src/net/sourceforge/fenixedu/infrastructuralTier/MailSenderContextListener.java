/**
 * 
 */


package net.sourceforge.fenixedu.infrastructuralTier;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 10:06:47,20/Out/2005
 * @version $Id$
 */

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.cms.infrastructure.CmsConfiguration;
import net.sourceforge.fenixedu.presentationTier.Action.cms.MailingListManagement;

public class MailSenderContextListener implements ServletContextListener
{
	final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

	public void contextInitialized(ServletContextEvent arg0)
	{
		System.out.println("Entrei no context initializer com o scheduler a " + scheduler);
		// TODO: maybe a configuration parameter
		try
		{
			MailingListQueueManager manager = new MailingListQueueManager("mailer", "mailer");
			scheduler.scheduleAtFixedRate(manager, 0, 5, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Vou sair do context initializer com o scheduler a " + scheduler);
	}

	public void contextDestroyed(ServletContextEvent arg0)
	{
		System.out.println("Entrei no context destroyed com o scheduler a " + scheduler);
		scheduler.shutdownNow();
		System.out.println("Vou sair do  context destroyed com o scheduler a " + scheduler);
	}
}

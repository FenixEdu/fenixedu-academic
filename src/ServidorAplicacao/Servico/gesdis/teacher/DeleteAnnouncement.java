/*
 * ApagarAnuncio.java
 *
 * Created on January 6, 2003, 11:01 PM
 */

package ServidorAplicacao.Servico.gesdis.teacher;

/**
 *
 * @author  EP15
 * @author jmota
 */
import org.apache.commons.beanutils.BeanUtils;

import DataBeans.gesdis.AnnouncementView;
import DataBeans.gesdis.SiteView;
import Dominio.IAnnouncement;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteAnnouncement implements IServico {

	private static DeleteAnnouncement service = new DeleteAnnouncement();

	public static DeleteAnnouncement getService() {

		return service;

	}

	private DeleteAnnouncement() {
	}

	public final String getNome() {

		return "gesdis.teacher.ApagarAnuncio";

	}

	public void run(SiteView siteView, AnnouncementView announcementView)
		throws Exception {

		String announcementTitle = announcementView.getTitle();

		ISuportePersistente sp = SuportePersistenteOJB.getInstance();

		ISite site = null;
		BeanUtils.copyProperties(site, siteView);
		
		IPersistentAnnouncement persistentAnnouncement =
			sp.getIPersistentAnnouncement();

		 
		IAnnouncement announcement =
			persistentAnnouncement.readAnnouncementByTitleAndDateAndSite(
				announcementTitle,
				announcementView.getDate(),
				site);

		if (announcement != null)
			persistentAnnouncement.delete(announcement);

	}

}

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
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import Dominio.IAnnouncement;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
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

		return "gesdis.teacher.DeleteAnnouncement";

	}

	public void run(InfoSite siteView, InfoAnnouncement announcementView)
		throws FenixServiceException {

		try {
			String announcementTitle = announcementView.getTitle();
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			ISite site = null;
			BeanUtils.copyProperties(site, siteView);
			
			IPersistentAnnouncement persistentAnnouncement =
				sp.getIPersistentAnnouncement();
			
			
			IAnnouncement announcement =
				persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(
					announcementTitle,
					announcementView.getCreationDate(),
					site);
			
			if (announcement != null)
				persistentAnnouncement.delete(announcement);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} catch (IllegalAccessException e) {
			throw new FenixServiceException(e);
		} catch (InvocationTargetException e) {
			throw new FenixServiceException(e);
		}

	}

}

/*
 * DeleteAnnouncement.java
 *
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
		return "DeleteAnnouncement";
	}

	public void run(InfoSite infoSite, InfoAnnouncement infoAnnouncement)
		throws FenixServiceException {

		try {
			String announcementTitle = infoAnnouncement.getTitle();
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			ISite site = null;
			BeanUtils.copyProperties(site, infoSite);
			
			IPersistentAnnouncement persistentAnnouncement = sp.getIPersistentAnnouncement();
			
			
			IAnnouncement announcement =
				persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(
					announcementTitle,
					infoAnnouncement.getCreationDate(),
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

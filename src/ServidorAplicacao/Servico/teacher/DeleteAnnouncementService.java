package ServidorAplicacao.Servico.teacher;

import Dominio.Announcement;
import Dominio.IAnnouncement;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteAnnouncementService implements IServico {

	private static DeleteAnnouncementService service = new DeleteAnnouncementService();

	public static DeleteAnnouncementService getService() {
		return service;
	}

	private DeleteAnnouncementService() {
	}

	public final String getNome() {
		return "DeleteAnnouncementService";
	}

	public boolean run(
		Integer infoExecutionCourseCode,
		Integer announcementCode)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentAnnouncement persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

			Announcement announcement = new Announcement(announcementCode);
			IAnnouncement iAnnouncement = (IAnnouncement) persistentAnnouncement.readByOId(announcement, false);

			if (iAnnouncement != null)
				persistentAnnouncement.delete(announcement);

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}
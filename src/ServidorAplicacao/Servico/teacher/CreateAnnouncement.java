package ServidorAplicacao.Servico.teacher;

import java.sql.Timestamp;
import java.util.Calendar;

import Dominio.Announcement;
import Dominio.DisciplinaExecucao;
import Dominio.IAnnouncement;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Fernanda Quitério
 *  
 */
public class CreateAnnouncement implements IServico {

	private ISuportePersistente persistentSupport = null;
	private IPersistentAnnouncement persistentAnnouncement = null;

	private static CreateAnnouncement service = new CreateAnnouncement();
	/**
	 * The singleton access method of this class.
	 */
	public static CreateAnnouncement getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private CreateAnnouncement() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "CreateAnnouncement";
	}

	private void checkIfAnnouncementExists(String announcementTitle, ISite announcementSite, Timestamp currentDate)
		throws FenixServiceException {
		IAnnouncement announcement = null;
		persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

		try {
			announcement =
				persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(announcementTitle, currentDate, announcementSite);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		if (announcement != null)
			throw new ExistingServiceException();
	}
	/**
	 * Executes the service.
	 */
	public boolean run(
		Integer infoExecutionCourseCode,
		String newAnnouncementTitle,
		String newAnnouncementInformation)
		throws FenixServiceException {
		ISite site = null;

		//retrieve current date
		Calendar calendar = Calendar.getInstance();

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = persistentSupport.getIPersistentSite();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(infoExecutionCourseCode), false);
			site = persistentSite.readByExecutionCourse(executionCourse);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		checkIfAnnouncementExists(newAnnouncementTitle, site, new Timestamp(calendar.getTime().getTime()));

		try {
			IAnnouncement newAnnouncement =
				new Announcement(
					newAnnouncementTitle,
					new Timestamp(calendar.getTime().getTime()),
					new Timestamp(calendar.getTime().getTime()),
					newAnnouncementInformation,
					site);
			persistentAnnouncement.lockWrite(newAnnouncement);
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return true;
	}
}
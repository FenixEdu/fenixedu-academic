package ServidorAplicacao.Servico.teacher;

import java.sql.Timestamp;
import java.util.Date;

import DataBeans.InfoAnnouncement;
import DataBeans.util.Cloner;
import Dominio.Announcement;
import Dominio.DisciplinaExecucao;
import Dominio.IAnnouncement;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Fernanda Quitério
 * 
 */

public class EditAnnouncementService implements IServico {
	ISuportePersistente persistentSupport = null;
	private IPersistentSite persistentSite = null;
	private IPersistentAnnouncement persistentAnnouncement = null;
	private IPersistentExecutionCourse persistentExecutionCourse = null;
	private static EditAnnouncementService service = new EditAnnouncementService();
	/**
	 * The singleton access method of this class.
	 */
	public static EditAnnouncementService getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditAnnouncementService() {
	}
	/**
	 * Devolve o nome do servico
	 */
	public final String getNome() {
		return "EditAnnouncementService";
	}
	private void checkIfAnnouncementExists(
		String oldAnnouncementTitle,
		Timestamp date,
		String announcementTitle,
		ISite announcementSite)
		throws FenixServiceException {
		IAnnouncement announcement = null;
		if (oldAnnouncementTitle.equals(announcementTitle) == false) {
			try {
				announcement =
					persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(announcementTitle, date, announcementSite);
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
				throw new FenixServiceException(excepcaoPersistencia.getMessage());
			}

			if (announcement != null)
				throw new ExistingServiceException();
		}
	}
	/**
	 * Executes the service.
	 */
	public boolean run(
		Integer infoExecutionCourseCode,
		Integer announcementCode,
		String announcementNewTitle,
		String announcementNewInformation)
		throws FenixServiceException {

		ISite site = null;
		Timestamp date = null;
		IAnnouncement iAnnouncement = null;
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			persistentSite = persistentSupport.getIPersistentSite();
			persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

			// read announcement 
			Announcement announcement = new Announcement(announcementCode);
			iAnnouncement = (IAnnouncement) persistentAnnouncement.readByOId(announcement, true);
			if (iAnnouncement == null) {
				throw new InvalidArgumentsServiceException();
			}
			InfoAnnouncement infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(iAnnouncement);
			String announcementOldTitle = infoAnnouncement.getTitle();
			date = infoAnnouncement.getCreationDate();

			// read executionCourse and site
			IExecutionCourse executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(new DisciplinaExecucao(infoExecutionCourseCode), false);
			site = persistentSite.readByExecutionCourse(executionCourse);

			checkIfAnnouncementExists(announcementOldTitle, date, announcementNewTitle, site);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		Timestamp lastModificationDate = new Timestamp(new Date(System.currentTimeMillis()).getTime());
		iAnnouncement.setTitle(announcementNewTitle);
		iAnnouncement.setLastModifiedDate(lastModificationDate);
		iAnnouncement.setInformation(announcementNewInformation);

		return true;
	}
}
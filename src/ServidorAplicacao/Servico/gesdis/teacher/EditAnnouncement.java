/*
 * EditarAnuncio.java
 *
 */
package ServidorAplicacao.Servico.gesdis.teacher;
/**
 *
 * @author  EP15
 * @author Ivo Brandão
 */
import java.sql.Timestamp;
import java.util.Date;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import Dominio.IAnnouncement;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
public class EditAnnouncement implements IServico {
	ISuportePersistente persistentSupport = null;
	private IPersistentSite persistentSite = null;
	private IPersistentSection persistentSection = null;
	private IPersistentAnnouncement persistentAnnouncement = null;
	private IPersistentExecutionYear persistentExecutionYear = null;
	private IPersistentExecutionPeriod persistentExecutionPeriod = null;
	private IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	private static EditAnnouncement service = new EditAnnouncement();
	/**
	 * The singleton access method of this class.
	 */
	public static EditAnnouncement getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditAnnouncement() {
	}
	/**
	 * Devolve o nome do servico
	 */
	public final String getNome() {
		return "EditAnnouncement";
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
					persistentAnnouncement
						.readAnnouncementByTitleAndCreationDateAndSite(
						announcementTitle,
						date,
						announcementSite);
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
				throw new FenixServiceException(
					excepcaoPersistencia.getMessage());
			}

			if (announcement != null)
				throw new ExistingServiceException();
		}
	}
	/**
	 * Executes the service.
	 */
	public boolean run(
		InfoSite infoSite,
		InfoAnnouncement infoAnnouncement,
		String announcementNewTitle,
		String announcementNewInformation)
		throws FenixServiceException {
		ISite site = null;
		Timestamp date = null;
		String announcementOldTitle = infoAnnouncement.getTitle();

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		persistentExecutionYear =
			persistentSupport.getIPersistentExecutionYear();
		persistentExecutionPeriod =
			persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionCourse =
			persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentSite = persistentSupport.getIPersistentSite();

		persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();
		InfoExecutionCourse infoExecutionCourse =
			infoSite.getInfoExecutionCourse();
		InfoExecutionPeriod infoExecutionPeriod =
			infoExecutionCourse.getInfoExecutionPeriod();
		InfoExecutionYear infoExecutionYear =
			infoExecutionPeriod.getInfoExecutionYear();
		IAnnouncement announcement = null;

		try {
			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName(
					infoExecutionYear.getYear());
			IExecutionPeriod executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					infoExecutionPeriod.getName(),
					executionYear);
			IDisciplinaExecucao executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					executionPeriod);
			site = persistentSite.readByExecutionCourse(executionCourse);
			date = infoAnnouncement.getCreationDate();
			checkIfAnnouncementExists(
				announcementOldTitle,
				date,
				announcementNewTitle,
				site);
			announcement =
				persistentAnnouncement
					.readAnnouncementByTitleAndCreationDateAndSite(
					announcementOldTitle,
					date,
					site);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		if (announcement == null)
			throw new InvalidArgumentsServiceException();
		Timestamp lastModificationDate =
			new Timestamp(new Date(System.currentTimeMillis()).getTime());
		announcement.setTitle(announcementNewTitle);
		announcement.setLastModifiedDate(lastModificationDate);
		announcement.setInformation(announcementNewInformation);
		try {
			persistentAnnouncement.lockWrite(announcement);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return true;
	}
}
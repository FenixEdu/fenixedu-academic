/*
 * InserirAnuncio.java
 */
package ServidorAplicacao.Servico.gesdis.teacher;
/**
 * @author  EP15
 * @author Ivo Brandão
 */
import java.util.Calendar;
import java.util.Date;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoSite;
import Dominio.Announcement;
import Dominio.IAnnouncement;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
public class InsertAnnouncement implements IServico {

    private ISuportePersistente persistentSupport = null;
    private IPersistentExecutionYear persistentExecutionYear = null;
    private IPersistentExecutionPeriod persistentExecutionPeriod = null;
    private IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	private IPersistentSite persistentSite = null;
	private IPersistentAnnouncement persistentAnnouncement = null;
    private static InsertAnnouncement service = new InsertAnnouncement();
    /**
     * The singleton access method of this class.
     */
    public static InsertAnnouncement getService() {
        return service;
    }
    /**
     * The constructor of this class.
     */
    private InsertAnnouncement() { }
    /**
     * The name of the service
     */
    public final String getNome() {
        return "InsertAnnouncement";
    }

    private void checkIfAnnouncementExists(String announcementTitle, ISite announcementSite, Date currentDate) throws FenixServiceException {
        IAnnouncement announcement = null;
        persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

		try {
		announcement =
			persistentAnnouncement
				.readAnnouncementByTitleAndCreationDateAndSite(
				announcementTitle,
				currentDate,
				announcementSite);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		
        if (announcement != null) throw new ExistingServiceException();
    }
    /**
     * Executes the service.
     */
    public boolean run (InfoSite infoSite, String newAnnouncementTitle, String newAnnouncementInformation) throws FenixServiceException {
		ISite site = null;

		//retrieve current date
		Calendar calendar = Calendar.getInstance();
		
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

        persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
        persistentSite = persistentSupport.getIPersistentSite();

		InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();
		InfoExecutionPeriod infoExecutionPeriod = infoExecutionCourse.getInfoExecutionPeriod();
		InfoExecutionYear infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();

		try {
			IExecutionYear executionYear = 
				persistentExecutionYear.readExecutionYearByName(infoExecutionYear.getYear());
			IExecutionPeriod executionPeriod = 
				persistentExecutionPeriod.readByNameAndExecutionYear(infoExecutionPeriod.getName(), executionYear);
			IDisciplinaExecucao executionCourse = 
				persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(), 
					executionPeriod); 
			site = persistentSite.readByExecutionCourse(executionCourse);
		} catch (ExcepcaoPersistencia excepcaoPersistencia){
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		
		checkIfAnnouncementExists(newAnnouncementTitle, site, calendar.getTime());

		IAnnouncement newAnnouncement = null;
		try {
			newAnnouncement =
				new Announcement(
					null,
					newAnnouncementTitle,
					calendar.getTime(),
					null,
					newAnnouncementInformation,
					site,
					null);
			persistentAnnouncement.lockWrite(newAnnouncement);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
        
		return true;
    }
}

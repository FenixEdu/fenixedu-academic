package net.sourceforge.fenixedu.applicationTier.Servicos.gesdis;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Ivo Brandão
 */
public class ReadLastAnnouncementServiceTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public ReadLastAnnouncementServiceTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadLastAnnouncement";
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadLastAnnouncementServiceTest.class);

        return suite;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        ISuportePersistente persistentSupport = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse executionCourse = null;
        ISite site = null;

        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSupport.iniciarTransaccao();

            IPersistentExecutionYear ipey = persistentSupport.getIPersistentExecutionYear();
            executionYear = ipey.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod ipep = persistentSupport.getIPersistentExecutionPeriod();
            executionPeriod = ipep.readByNameAndExecutionYear("2º Semestre", executionYear);

            IPersistentExecutionCourse idep = persistentSupport.getIPersistentExecutionCourse();
            executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",
                    executionPeriod);

            IPersistentSite ips = persistentSupport.getIPersistentSite();
            site = ips.readByExecutionCourse(executionCourse);

            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }

        InfoSite infoSite = Cloner.copyISite2InfoSite(site);
        Object[] args = { infoSite };
        return args;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {
        IAnnouncement announcement = null;

        ISuportePersistente persistentSupport = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse executionCourse = null;
        ISite site = null;

        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSupport.iniciarTransaccao();

            IPersistentExecutionYear ipey = persistentSupport.getIPersistentExecutionYear();
            executionYear = ipey.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod ipep = persistentSupport.getIPersistentExecutionPeriod();
            executionPeriod = ipep.readByNameAndExecutionYear("2º Semestre", executionYear);

            IPersistentExecutionCourse idep = persistentSupport.getIPersistentExecutionCourse();
            executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",
                    executionPeriod);

            IPersistentSite ips = persistentSupport.getIPersistentSite();
            site = ips.readByExecutionCourse(executionCourse);

            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            System.out.println("getObjectToCompare1:failed setting up the test data" + e);
        }

        //	InfoSite infoSite = Cloner.copyISite2InfoSite(site);

        //read existing
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2003);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Timestamp date = new Timestamp(calendar.getTime().getTime());

        InfoAnnouncement infoAnnouncement = null;
        try {
            PersistenceSupportFactory.getDefaultPersistenceSupport().iniciarTransaccao();
            announcement = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentAnnouncement()
                    .readAnnouncementByTitleAndCreationDateAndSite("announcement2deTFCI", date, site);
            infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);
            PersistenceSupportFactory.getDefaultPersistenceSupport().confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            System.out.println("getObjectToCompare2:failed setting up the test data" + e);
        }

        return infoAnnouncement;
    }
}
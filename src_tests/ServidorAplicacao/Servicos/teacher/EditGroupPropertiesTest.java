package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */

public class EditGroupPropertiesTest extends TestCaseReadServices {

    IExecutionCourse executionCourse = null;

    IGroupProperties groupProperties = null;

    /**
     * @param testName
     */
    public EditGroupPropertiesTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditGroupProperties";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        ISuportePersistente persistentSupport = null;
        IPersistentExecutionYear persistentExecutionYear = null;
        IPersistentExecutionPeriod persistentExecutionPeriod = null;
        IPersistentGroupProperties persistentGroupProperties = null;
        IPersistentExecutionCourse persistentExecutionCourse = null;

        try {
            persistentSupport = SuportePersistenteOJB.getInstance();
            persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
            persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
            persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
            persistentSupport.iniciarTransaccao();

            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);
            executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(
                    "TFCII", executionPeriod);
            groupProperties = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(
                    executionCourse, "projecto B");

            persistentSupport.confirmarTransaccao();

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }

        InfoGroupProperties infoGroupProperties = Cloner
                .copyIGroupProperties2InfoGroupProperties(groupProperties);
        infoGroupProperties.setName("projecto L");
        Object[] args = { executionCourse.getIdInternal(), infoGroupProperties };
        return args;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        ISuportePersistente persistentSupport = null;
        IPersistentExecutionYear persistentExecutionYear = null;
        IPersistentExecutionPeriod persistentExecutionPeriod = null;
        IPersistentGroupProperties persistentGroupProperties = null;
        IPersistentExecutionCourse persistentExecutionCourse = null;

        try {
            persistentSupport = SuportePersistenteOJB.getInstance();
            persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
            persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
            persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
            persistentSupport.iniciarTransaccao();

            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);
            executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(
                    "TFCII", executionPeriod);
            groupProperties = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(
                    executionCourse, "projecto B");

            persistentSupport.confirmarTransaccao();

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }
        InfoGroupProperties infoGroupProperties = Cloner
                .copyIGroupProperties2InfoGroupProperties(groupProperties);
        infoGroupProperties.setGroupMaximumNumber(new Integer(0));
        Object[] args = { executionCourse.getIdInternal(), infoGroupProperties };
        return args;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;

    }

    protected Object getObjectToCompare() {
        return null;
    }

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }
}
/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.HashMap;

import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseCreateServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class CreateGroupPropertiesTest extends TestCaseCreateServices {

    ISuportePersistente persistentSupport = null;

    IPersistentExecutionYear persistentExecutionYear = null;

    IPersistentExecutionPeriod persistentExecutionPeriod = null;

    IPersistentExecutionCourse persistentExecutionCourse = null;

    IExecutionCourse executionCourse = null;

    /**
     * @param testName
     */
    public CreateGroupPropertiesTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "CreateGroupProperties";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        try {

            persistentSupport = SuportePersistenteOJB.getInstance();
            persistentSupport.iniciarTransaccao();
            persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
            persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();

            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);
            executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(
                    "TFCII", executionPeriod);
            persistentSupport.confirmarTransaccao();

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }

        IGroupProperties groupProperties = new GroupProperties(executionCourse, "projecto A");
        Object[] args = { executionCourse.getIdInternal(),
                Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties) };
        return args;

    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        try {

            persistentSupport = SuportePersistenteOJB.getInstance();
            persistentSupport.iniciarTransaccao();
            persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
            persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();

            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);
            executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(
                    "TFCII", executionPeriod);

            persistentSupport.confirmarTransaccao();

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }

        IGroupProperties groupProperties = new GroupProperties(executionCourse, "newName");

        InfoGroupProperties infoGroupProperties = Cloner
                .copyIGroupProperties2InfoGroupProperties(groupProperties);

        Object[] args = { new Integer(25), infoGroupProperties };

        return args;

    }

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
     */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

}
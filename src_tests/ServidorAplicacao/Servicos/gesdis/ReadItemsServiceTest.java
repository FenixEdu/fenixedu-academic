/*
 * Created on 27/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */

/**
 * @author lmac2
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.gesdis;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class ReadItemsServiceTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public ReadItemsServiceTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadItems";
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadItemsServiceTest.class);

        return suite;
    }

    protected void setUp() {

        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        ISuportePersistente persistentSupport = null;
        IPersistentExecutionYear persistentExecutionYear = null;
        IPersistentExecutionPeriod persistentExecutionPeriod = null;
        IPersistentExecutionCourse persistentExecutionCourse = null;
        IPersistentSite persistentSite = null;
        IPersistentSection persistentSection = null;
        ISection section = null;

        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
            persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
            persistentSite = persistentSupport.getIPersistentSite();
            persistentSection = persistentSupport.getIPersistentSection();

            persistentSupport.iniciarTransaccao();

            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");

            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);

            IExecutionCourse executionCourse = persistentExecutionCourse
                    .readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);

            ISite site = persistentSite.readByExecutionCourse(executionCourse);

            section = persistentSection.readBySiteAndSectionAndName(site, null, "Seccao1deTFCI");
            persistentSupport.confirmarTransaccao();

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }
        Object[] args = { Cloner.copyISection2InfoSection(section) };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {

        return null;

    }
}
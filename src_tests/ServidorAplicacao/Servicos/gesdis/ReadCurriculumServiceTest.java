/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.gesdis;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 */
public class ReadCurriculumServiceTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public ReadCurriculumServiceTest(String testName) {
        super(testName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadCurriculum";

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        ISuportePersistente sp = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse executionCourse = null;
        Object[] args = new Object[1];
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            executionCourse = idep
                    .readByExecutionCourseInitialsAndExecutionPeriod("PO", executionPeriod);

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            args[1] = infoExecutionCourse;

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }

        return args;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        ISuportePersistente sp = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse executionCourse = null;
        Object[] args = new Object[1];
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",
                    executionPeriod);

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            args[1] = infoExecutionCourse;

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }

        return args;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 1;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {
        //        ISuportePersistente sp = null;
        //        IExecutionYear executionYear = null;
        //        IExecutionPeriod executionPeriod = null;
        //// IExecutionCourse executionCourse = null;
        //        try
        //        {
        //            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        //            sp.iniciarTransaccao();
        //
        //            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
        //            executionYear = ieyp.readExecutionYearByName("2002/2003");
        //
        //            IPersistentExecutionPeriod iepp =
        // sp.getIPersistentExecutionPeriod();
        //
        //            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre",
        // executionYear);
        //
        //            IPersistentExecutionCourse idep =
        // sp.getIPersistentExecutionCourse();
        //            idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",
        // executionPeriod);
        //
        //            sp.confirmarTransaccao();
        //        } catch (ExcepcaoPersistencia e)
        //        {
        //            System.out.println("failed setting up the test data");
        //        }
        //
        //// InfoExecutionCourse infoExecutionCourse =
        //// Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
        //        //// return new
        // InfoCurriculum("bla","bla","bla",null,null,null,infoExecutionCourse);
        return null;
    }

}
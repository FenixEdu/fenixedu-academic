/*
 * Created on 24/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.gesdis.teacher;

import java.util.HashMap;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseCreateServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *  
 */
public class InsertCurriculumServiceTest extends TestCaseCreateServices {

    /**
     * @param testName
     */
    public InsertCurriculumServiceTest(String testName) {
        super(testName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "InsertCurriculum";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        InfoCurriculum oldCurriculum = new InfoCurriculum();

        ISuportePersistente sp = null;
        //		IExecutionYear executionYear = null;
        //		IExecutionPeriod executionPeriod = null;
        //		IDisciplinaExecucao executionCourse = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            //			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            //			executionYear = ieyp.readExecutionYearByName("2002/2003");

            //			IPersistentExecutionPeriod iepp =
            //				sp.getIPersistentExecutionPeriod();

            //			executionPeriod =
            //				iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            //			IDisciplinaExecucaoPersistente idep =
            //				sp.getIDisciplinaExecucaoPersistente();
            //			executionCourse =
            //				idep.readByExecutionCourseInitialsAndExecutionPeriod(
            //					"TFCI",
            //					executionPeriod);
            //			IPersistentCurriculum persistentCurriculum =
            //				sp.getIPersistentCurriculum();

            //			ICurriculum curriculum =
            //				persistentCurriculum.readCurriculumByExecutionCourse(
            //					executionCourse);
            sp.confirmarTransaccao();

            //// oldCurriculum =
            // Cloner.copyICurriculum2InfoCurriculum(curriculum);

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
            e.printStackTrace();
        }
        Object[] args = { oldCurriculum };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        InfoCurriculum oldCurriculum = new InfoCurriculum();

        ISuportePersistente sp = null;
        //		IExecutionYear executionYear = null;
        //		IExecutionPeriod executionPeriod = null;
        //		IDisciplinaExecucao executionCourse = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            //			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            //			executionYear = ieyp.readExecutionYearByName("2002/2003");

            //			IPersistentExecutionPeriod iepp =
            //				sp.getIPersistentExecutionPeriod();

            //			executionPeriod =
            //				iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            //			IDisciplinaExecucaoPersistente idep =
            //				sp.getIDisciplinaExecucaoPersistente();
            //			executionCourse =
            //				idep.readByExecutionCourseInitialsAndExecutionPeriod(
            //					"IP",
            //					executionPeriod);

            sp.confirmarTransaccao();
            //			InfoExecutionCourse infoExecutionCourse =
            //				Cloner.copyIExecutionCourse2InfoExecutionCourse(
            //					executionCourse);

            oldCurriculum = new InfoCurriculum();
            oldCurriculum.setGeneralObjectives("bla");
            oldCurriculum.setOperacionalObjectives("bla");
            oldCurriculum.setProgram("bla");
            oldCurriculum.setGeneralObjectivesEn(null);
            oldCurriculum.setOperacionalObjectivesEn(null);
            oldCurriculum.setProgramEn(null);
            //			oldCurriculum.setInfoExecutionCourse(infoExecutionCourse);

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
            e.printStackTrace();
        }
        Object[] args = { oldCurriculum };
        return args;
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
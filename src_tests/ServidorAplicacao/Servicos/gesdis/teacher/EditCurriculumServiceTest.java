/*
 * Created on 18/Mar/2003
 *
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.InfoCurriculum;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *  
 */
public class EditCurriculumServiceTest extends TestCaseDeleteAndEditServices {

    /**
     * @param testName
     */
    public EditCurriculumServiceTest(String testName) {
        super(testName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditCurriculum";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        InfoCurriculum oldCurriculum = new InfoCurriculum();
        InfoCurriculum newCurriculum = new InfoCurriculum();
        ISuportePersistente sp = null;
        //	IExecutionYear executionYear = null;
        //IExecutionPeriod executionPeriod = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            //	IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            //	executionYear = ieyp.readExecutionYearByName("2002/2003");

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
            //			ICurriculum curriculum =
            //				persistentCurriculum.readCurriculumByExecutionCourse(
            //					executionCourse);
            sp.confirmarTransaccao();

            //			oldCurriculum =
            // Cloner.copyICurriculum2InfoCurriculum(curriculum);
            //			newCurriculum =
            // Cloner.copyICurriculum2InfoCurriculum(curriculum);
            //			newCurriculum.setGeneralObjectives("blablablabla");
            newCurriculum.setGeneralObjectivesEn(null);
        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
            e.printStackTrace();
        }
        Object[] args = { oldCurriculum, newCurriculum };
        return args;
    }

}
/*
 * Created on 23/Abr/2003
 *  
 */
package ServidorAplicacao.Servicos.gesdis;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadEvaluationServiceTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public ReadEvaluationServiceTest(String testName) {
        super(testName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadEvaluation";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        //		ISuportePersistente sp = null;
        //		IExecutionYear executionYear = null;
        //		IExecutionPeriod executionPeriod = null;
        //		IExecutionCourse executionCourse = null;
        //		try {
        //			sp = SuportePersistenteOJB.getInstance();
        //			sp.iniciarTransaccao();
        //
        //			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
        //			executionYear = ieyp.readExecutionYearByName("2002/2003");
        //
        //			IPersistentExecutionPeriod iepp =
        //				sp.getIPersistentExecutionPeriod();
        //
        //			executionPeriod =
        //				iepp.readByNameAndExecutionYear("2º Semestre", executionYear);
        //
        //			IPersistentExecutionCourse idep =
        //				sp.getIPersistentExecutionCourse();
        //			executionCourse =
        //				idep.readByExecutionCourseInitialsAndExecutionPeriod(
        //					"PO",
        //					executionPeriod);
        //
        //			sp.confirmarTransaccao();
        //		} catch (ExcepcaoPersistencia e) {
        //			System.out.println("failed setting up the test data");
        //		}

        return null;
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
        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            executionCourse = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",
                    executionPeriod);
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            Object[] args = { infoExecutionCourse };
            sp.confirmarTransaccao();
            return args;
        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {

        //		ISuportePersistente sp = null;
        //			IExecutionYear executionYear = null;
        //			IExecutionPeriod executionPeriod = null;
        //			IExecutionCourse executionCourse = null;
        //			try {
        //				sp = SuportePersistenteOJB.getInstance();
        //				sp.iniciarTransaccao();
        //
        //				IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
        //				executionYear = ieyp.readExecutionYearByName("2002/2003");
        //
        //				IPersistentExecutionPeriod iepp =
        //					sp.getIPersistentExecutionPeriod();
        //
        //				executionPeriod =
        //					iepp.readByNameAndExecutionYear("2º Semestre", executionYear);
        //
        //				IPersistentExecutionCourse idep =
        //					sp.getIPersistentExecutionCourse();
        //				executionCourse =
        //					idep.readByExecutionCourseInitialsAndExecutionPeriod(
        //						"TFCI",
        //						executionPeriod);
        //
        //				sp.confirmarTransaccao();
        //			} catch (ExcepcaoPersistencia e) {
        //				System.out.println("failed setting up the test data");
        //			}
        //
        //			InfoExecutionCourse infoExecutionCourse =
        //				Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
        //		//InfoEvaluationMethod infoEvaluation = new
        // InfoEvaluationMethod(infoExecutionCourse,"bla",null);
        //		//return infoEvaluation;
        return null;
    }

}
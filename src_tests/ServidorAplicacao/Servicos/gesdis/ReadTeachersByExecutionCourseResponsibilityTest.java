/*
 * Created on 25/Mar/2003
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
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
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *  
 */
public class ReadTeachersByExecutionCourseResponsibilityTest extends TestCaseReadServices
{

    /**
	 * @param testName
	 */
    public ReadTeachersByExecutionCourseResponsibilityTest(String testName)
    {
        super(testName);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
    protected String getNameOfServiceToBeTested()
    {

        return "ReadTeachersByExecutionCourseResponsibility";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        // I don't see in which situation this can be unsucessful!!!
        return null;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        SuportePersistenteOJB persistentSupport = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse executionCourse = null;
        try
        {
            persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse =
                persistentSupport.getIPersistentExecutionCourse();
            IPersistentExecutionYear persistentExecutionYear =
                persistentSupport.getIPersistentExecutionYear();
            IPersistentExecutionPeriod persistentExecutionPeriod =
                persistentSupport.getIPersistentExecutionPeriod();
            persistentSupport.iniciarTransaccao();
            executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            executionPeriod =
                persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
            executionCourse =
                persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(
                    "TFCI",
                    executionPeriod);
            InfoExecutionCourse infoExecutionCourse =
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            Object[] args = { infoExecutionCourse };
            persistentSupport.confirmarTransaccao();
            return args;
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("failed in the test setup");
        }
        return null;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
    protected int getNumberOfItemsToRetrieve()
    {
        return 1;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
    protected Object getObjectToCompare()
    {
        return null;
    }

}

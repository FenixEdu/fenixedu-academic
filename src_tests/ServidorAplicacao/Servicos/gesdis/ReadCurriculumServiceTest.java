/*
 * Created on 14/Mar/2003
 *  
 */
package ServidorAplicacao.Servicos.gesdis;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 */
public class ReadCurriculumServiceTest extends TestCaseReadServices
{

    /**
	 * @param testName
	 */
    public ReadCurriculumServiceTest(String testName)
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
        return "ReadCurriculum";

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        ISuportePersistente sp = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IDisciplinaExecucao executionCourse = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IDisciplinaExecucaoPersistente idep = sp.getIDisciplinaExecucaoPersistente();
            executionCourse =
                idep.readByExecutionCourseInitialsAndExecutionPeriod("PO", executionPeriod);

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("failed setting up the test data");
        }

        InfoExecutionCourse infoExecutionCourse =
            Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
        Object[] args = { infoExecutionCourse };
        return args;

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        ISuportePersistente sp = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IDisciplinaExecucao executionCourse = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IDisciplinaExecucaoPersistente idep = sp.getIDisciplinaExecucaoPersistente();
            executionCourse =
                idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("failed setting up the test data");
        }

        InfoExecutionCourse infoExecutionCourse =
            Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
        Object[] args = { infoExecutionCourse };
        return args;

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
        ISuportePersistente sp = null;
        IExecutionYear executionYear = null;
        IExecutionPeriod executionPeriod = null;
        IDisciplinaExecucao executionCourse = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IDisciplinaExecucaoPersistente idep = sp.getIDisciplinaExecucaoPersistente();
            executionCourse =
                idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("failed setting up the test data");
        }

        InfoExecutionCourse infoExecutionCourse =
            Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
        //// return new InfoCurriculum("bla","bla","bla",null,null,null,infoExecutionCourse);
        return null;
    }

}

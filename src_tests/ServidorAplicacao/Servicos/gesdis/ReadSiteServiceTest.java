/*
 * Created on 14/Mar/2003
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
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
 * @author jmota
 */
public class ReadSiteServiceTest extends TestCaseReadServices
{

    /**
	 * @param testName
	 */
    public ReadSiteServiceTest(String testName)
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
        return "ReadSite";

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;

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
        IExecutionCourse executionCourse = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            executionCourse =
                idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);

            InfoExecutionCourse infoExecutionCourse =
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            Object[] args = { infoExecutionCourse };
            sp.confirmarTransaccao();
            return args;
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("failed setting up the test data");
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

        return 0;

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
        IExecutionCourse executionCourse = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            executionYear = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();

            executionPeriod = iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            executionCourse =
                idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);

            InfoExecutionCourse infoExecutionCourse =
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);

            InfoSite infoSite = new InfoSite(infoExecutionCourse);

            sp.confirmarTransaccao();

            return infoSite;
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("failed setting up the test data");
        }

        //the site no longer knows his sections
        //	infoSections.add(new InfoSection( "Seccao1deTFCI", new Integer(0),
        // infoSite, null, null, null));
        //	infoSite.setInfoSections(infoSections);

        return null;
    }

}

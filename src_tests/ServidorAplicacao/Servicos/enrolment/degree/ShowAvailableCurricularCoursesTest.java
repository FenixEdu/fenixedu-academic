package ServidorAplicacao.Servicos.enrolment.degree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import Dominio.ICurricularCourse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ShowAvailableCurricularCoursesTest
    extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
{

    private static String EXPECTED_CURRICULAR_COURSES_PROPERTY = "ExpectedCurricularCoursesFilePath";
    private static String EXPECTED_SECONDARY_CREDITS_PROPERTY = "CreditsInSecundaryArea";
    private static String EXPECTED_SPECIALIZATION_CREDITS_PROPERTY = "CreditsInSpecializationArea";

    /**
     * @param name
     */
    protected ShowAvailableCurricularCoursesTest(String testName)
    {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ShowAvailableCurricularCourses";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/student/testShowAvailableCurricularCoursesDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/student/testShowAvailableCurricularCoursesExpectedDataSet.xml";
    }

    protected String getConfigFilePath()
    {
        return "config/gera.properties";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "16", "pass", getApplication()};
        return args;
    }
    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "fiado", "pass", getApplication()};
        return args;
    }

    protected Object[] getArguments()
    {
        Integer studentNumber = new Integer(54503);
        Object[] args = { studentNumber };
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments()
    {
        return getArguments();
    }

    /** ********** Inicio dos testes ao serviço ************* */

    public void testShowAvailableCurricularCourses()
    {

        String[] args = getAuthenticatedAndAuthorizedUser();
        IUserView id = authenticateUser(args);
        Object[] args2 = getArguments();
        InfoStudentEnrolmentContext infoSEC = null;

        try
        {
            infoSEC =
                (InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
                    id,
                    getNameOfServiceToBeTested(),
                    args2);
        }
        catch (FenixServiceException e)
        {
            fail("Executing Service: " + getNameOfServiceToBeTested());
            e.printStackTrace();
        }

        boolean result = CompareExpectedCCAndCreditsWithDataReturnedFromService(infoSEC);

        if (result)
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantSubsidySuccessfull");
        else
            fail("Returned curricular courses dont macth the expected curricular courses");
    }

    /**
     * @param infoSEC
     * @return
     */
    private boolean CompareExpectedCCAndCreditsWithDataReturnedFromService(InfoStudentEnrolmentContext infoSEC)
    {
        Integer secondaryCredits = infoSEC.getCreditsInSecundaryArea();
        Integer specializationCredits = infoSEC.getCreditsInSpecializationArea();
        List curricularCourses = infoSEC.getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled();
        List curricularCoursesNames = filterCurricularCoursesNames(curricularCourses);
        List expectedCCAndCredits = readExpectedCCAndCredits();
        List expectedCurricularCoursesNames = (List) expectedCCAndCredits.get(0);
        sortCurricularCourses(curricularCoursesNames);
        sortCurricularCourses(expectedCurricularCoursesNames);

        Integer expectedSecondaryCredits = (Integer) expectedCCAndCredits.get(1);
        Integer expectedSpecializationCredits = (Integer) expectedCCAndCredits.get(2);

        return (
            curricularCoursesNames.equals(expectedCurricularCoursesNames)
                && (specializationCredits.equals(expectedSpecializationCredits))
                && (secondaryCredits.equals(expectedSecondaryCredits)));
    }

    /**
     * @return
     */
    private List readExpectedCCAndCredits()
    {
        List ccAndCredits = null;
        try
        {
            ResourceBundle bundle = new PropertyResourceBundle(new FileInputStream(getConfigFilePath()));
            String expectedCCFilePath = bundle.getString(EXPECTED_CURRICULAR_COURSES_PROPERTY);
            Integer creditsInSecundaryArea =
                new Integer(bundle.getString(EXPECTED_SECONDARY_CREDITS_PROPERTY));
            Integer creditsInSpecializationArea =
                new Integer(bundle.getString(EXPECTED_SPECIALIZATION_CREDITS_PROPERTY));
            List curricularCoursesNames = readFile(expectedCCFilePath);
        }
        catch (MissingResourceException ex)
        {
            ex.printStackTrace();
        }
        catch (FileNotFoundException ex)
        {
            fail("File " + getConfigFilePath() + " not found.");
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            fail("IOException reading file " + getConfigFilePath() + ex);
            ex.printStackTrace();
        }

        return ccAndCredits;
    }

    private static List readFile(String filePath) throws IOException
    {
        ArrayList lista = new ArrayList();
        BufferedReader leitura = null;
        String linhaFicheiro = null;

        File file = new File(filePath);
        InputStream ficheiro = new FileInputStream(file);
        leitura =
            new BufferedReader(
                new InputStreamReader(ficheiro, "8859_1"),
                new Long(file.length()).intValue());
        do
        {
            linhaFicheiro = leitura.readLine();

            if (linhaFicheiro != null)
                lista.add(linhaFicheiro);
        }
        while ((linhaFicheiro != null));

        return lista;
    }

    /**
     * @param curricularCourses
     * @return
     */
    private List filterCurricularCoursesNames(List curricularCourses)
    {
        ArrayList curricularCoursesName = new ArrayList();

        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext())
            curricularCoursesName.add(((ICurricularCourse) iter.next()).getName());

        return curricularCoursesName;
    }

    private static void sortCurricularCourses(List curricularCourseList)
    {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("name"));

        Collections.sort(curricularCourseList, comparatorChain);
    }
}
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
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ShowAvailableCurricularCoursesTest extends TestCase
{
    private dbaccess dbAcessPoint = null;

    private static String EXPECTED_SECONDARY_CREDITS_PROPERTY = "CreditsInSecundaryArea";

    private static String EXPECTED_SPECIALIZATION_CREDITS_PROPERTY = "CreditsInSpecializationArea";

    private static String USER = "";

    private static String ITERATION = "";

    private static String STUDENT_NUMBER = "StudentNumber";

    protected void setUp()
    {
        System.out.println("setup start");
        try
        {
        
            dbAcessPoint = new dbaccess();
           
            dbAcessPoint.openConnection();
           
//            dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
           
           
            try
            {
                dbAcessPoint.loadDataBase(getDataSetFilePath());
            }
            catch (RuntimeException e)
            {
                System.out.println(e);
            }
          
            dbAcessPoint.closeConnection();
           
            System.out.println("setup end");
        }
        catch (Exception ex)
        {
            System.out.println("Setup failed: " + ex);
        }

    }

    protected void tearDown()
    {
    }

    /**
     * @param name
     */
    public ShowAvailableCurricularCoursesTest(String testName, String iteration)
    {
        super(testName);
        ITERATION = iteration;
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
        return "etc/LEEC_Enrollment_Tests_Arguments/" + ITERATION
                + "/input.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/LEEC_Enrollment_Tests_Arguments/" + ITERATION
                + "/input.xml";
    }

    protected String getConfigFilePath()
    {
        return "etc/LEEC_Enrollment_Tests_Arguments/" + ITERATION
                + "/test.properties";
    }

    protected String getExpectedResultFilePath()
    {
        return "etc/LEEC_Enrollment_Tests_Arguments/" + ITERATION
                + "/output.txt";

    }
    
    protected String getDatabaseBackupFilePath()
    {
        return "etc/LEEC_Enrollment_Tests_Arguments/" + ITERATION
        + "/backupDataSet.xml";

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        System.out.println("getUser start");
        try
        {
            ResourceBundle bundle = new PropertyResourceBundle(
                    new FileInputStream(getConfigFilePath()));
            USER = bundle.getString(STUDENT_NUMBER);
            System.out.println("getUser end");
        }
        catch (FileNotFoundException e)
        {
            // 
        }
        catch (IOException e)
        {
            //
        }
        String[] args = {"L" + USER, "pass", getApplication()};
        return args;
    }

    protected Object[] getArguments()
    {

        try
        {
            ResourceBundle bundle = new PropertyResourceBundle(
                    new FileInputStream(getConfigFilePath()));

            Integer studentNumber = new Integer(bundle
                    .getString(STUDENT_NUMBER));
            Object[] args = {studentNumber};
            return args;
        }
        catch (FileNotFoundException e)
        {
            //
        }
        catch (IOException e)
        {
            //
        }
        Object[] args = {};
        return args;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments()
    {
        return getArguments();
    }

    /** ********** Inicio dos testes ao serviço ************* */

    public boolean testShowAvailableCurricularCoursesTest()
    {
        System.out.println("test start");
        String[] args = getAuthenticatedAndAuthorizedUser();
        IUserView id = authenticateUser(args);
        Object[] args2 = getArguments();
        InfoStudentEnrolmentContext infoSEC = null;

        try
        {
            infoSEC = (InfoStudentEnrolmentContext) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);
        }
        catch (FenixServiceException e)
        {
            System.out.println("service execution failed:" + e);
            fail("Executing Service: " + getNameOfServiceToBeTested());
            e.printStackTrace();
        }
        System.out.println("2");
        System.out.println("service executed: comparing results");
        boolean result = CompareExpectedCCAndCreditsWithDataReturnedFromService(infoSEC);
        System.out.println("3");
        if (result)
        {
            System.out
                    .println(getNameOfServiceToBeTested()
                            + " was SUCCESSFULY runned by test: testCreateGrantSubsidySuccessfull");

        }
        else
        {
            fail("Returned curricular courses dont macth the expected curricular courses");
            return false;

        }
        return true;
    }

    /**
     * @param infoSEC
     * @return
     */
    private boolean CompareExpectedCCAndCreditsWithDataReturnedFromService(
            InfoStudentEnrolmentContext infoSEC)
    {

        //  Integer secondaryCredits = infoSEC.getCreditsInSecundaryArea();
        //        Integer specializationCredits = infoSEC
        //                .getCreditsInSpecializationArea();
        List curricularCourses = infoSEC
                .getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled();

        List curricularCoursesNames = (List) CollectionUtils.collect(
                curricularCourses, new Transformer()
                {

                    public Object transform(Object arg0)
                    {
                        InfoCurricularCourse curricularCourse = null;
                        try
                        {
                            curricularCourse = (InfoCurricularCourse) arg0;
                        }
                        catch (RuntimeException e)
                        {
                            return "XPTO";
                        }
                        return curricularCourse.getName();
                    }
                });
        if (infoSEC.getStudentCurrentSemesterInfoEnrollments() != null)
        {

            List forcedEnrollments = (List) CollectionUtils.collect(infoSEC
                    .getStudentCurrentSemesterInfoEnrollments(),
                    new Transformer()
                    {

                        public Object transform(Object arg0)
                        {
                            InfoEnrolment infoEnrolment = (InfoEnrolment) arg0;
                            if (infoEnrolment.getInfoCurricularCourse()
                                    .getMandatory().booleanValue())
                            {
                                return infoEnrolment.getInfoCurricularCourse()
                                        .getName();
                            }
                            else
                            {
                                return "XPTO";
                            }

                        }
                    });
            curricularCoursesNames.addAll(forcedEnrollments);
        }
        CollectionUtils.filter(curricularCoursesNames, new Predicate()
        {

            public boolean evaluate(Object arg0)
            {
                return !arg0.equals("XPTO");
            }
        });

        List expectedCCAndCredits = readExpectedCCAndCredits();

        List expectedCurricularCoursesNames = (List) expectedCCAndCredits
                .get(0);

        // sortCurricularCourses(curricularCoursesNames);
        System.out.println("curricularCoursesNames:" + curricularCoursesNames);
        System.out.println("expectedCurricularCoursesNames:"
                + expectedCurricularCoursesNames);
        // sortCurricularCourses(expectedCurricularCoursesNames);

        //        Integer expectedSecondaryCredits = (Integer) expectedCCAndCredits
        //                .get(1);
        //        Integer expectedSpecializationCredits = (Integer)
        // expectedCCAndCredits
        //                .get(2);

        return CollectionUtils.subtract(curricularCoursesNames,
                expectedCurricularCoursesNames).isEmpty()
                && CollectionUtils.subtract(expectedCurricularCoursesNames,
                        curricularCoursesNames).isEmpty();
        //                && (specializationCredits.equals(expectedSpecializationCredits)) &&
        // (secondaryCredits
        //                .equals(expectedSecondaryCredits))
        // );
    }

    /**
     * @return
     */
    private List readExpectedCCAndCredits()
    {
        List ccAndCredits = new ArrayList();
        try
        {

            ResourceBundle bundle = new PropertyResourceBundle(
                    new FileInputStream(getConfigFilePath()));

            Integer creditsInSecundaryArea;
            try
            {
                creditsInSecundaryArea = new Integer(bundle
                        .getString(EXPECTED_SECONDARY_CREDITS_PROPERTY));
            }
            catch (NumberFormatException e)
            {
                creditsInSecundaryArea = new Integer(0);
            }

            Integer creditsInSpecializationArea;
            try
            {
                creditsInSpecializationArea = new Integer(bundle
                        .getString(EXPECTED_SPECIALIZATION_CREDITS_PROPERTY));
            }
            catch (NumberFormatException e1)
            {
                creditsInSpecializationArea = new Integer(0);
            }

            List curricularCoursesNames = readFile(getExpectedResultFilePath());

            ccAndCredits.add(curricularCoursesNames);

            ccAndCredits.add(creditsInSecundaryArea);

            ccAndCredits.add(creditsInSpecializationArea);

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

        List lista = new ArrayList();
        BufferedReader leitura = null;
        String linhaFicheiro = null;

        File file = new File(filePath);

        InputStream ficheiro = new FileInputStream(file);

        leitura = new BufferedReader(new InputStreamReader(ficheiro, "8859_1"),
                new Long(file.length()).intValue());

        do
        {
            linhaFicheiro = leitura.readLine();

            if (linhaFicheiro != null)
            {
                lista.add(linhaFicheiro.trim());
            }
        }
        while ((linhaFicheiro != null));

        return lista;
    }

   

    protected IUserView authenticateUser(String[] arguments)
    {
        SuportePersistenteOJB.resetInstance();
        String args[] = arguments;

        try
        {
            return (IUserView) ServiceManagerServiceFactory.executeService(
                    null, "Autenticacao", args);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Authenticating User!" + ex);
            return null;
        }
    }

}
/*
 * Created on 6/Fev/2004
 */
package ServidorAplicacao.Servicos.enrolment.degree;


import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * @author jmota
 */
public class LeecEnrollmentTestSuite extends TestSuite
{

    public static void main(String[] args)
    {
        String number = args[0];
        TestSuite testSuite = new LeecEnrollmentTestSuite();
    
        testSuite.addTest(new ShowAvailableCurricularCoursesTest("Teste "
                + number, number));
        TestResult testResult = new TestResult();
       
        testSuite.run(testResult);
        System.out.println("Test " + number + " Result: " + testResult);

    }
}

/*
 * Created on 6/Fev/2004
 */
package ServidorAplicacao.Servicos.enrolment.degree;

import java.util.Enumeration;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * @author jmota
 */
public class LeecEnrollmentTestSuite {

    public static void main(String[] args) {
        String number = args[0];
        System.out.println("Running Test " + number);

        TestSuite testSuite = new TestSuite();
        TestResult testResult = new TestResult();
        TestCase test = new ShowAvailableCurricularCoursesTest("testShowAvailableCurricularCoursesTest",
                number);

        testSuite.addTest(test);
        //        testSuite.runTest(new ShowAvailableCurricularCoursesTest("Teste "
        //                + number, number), testResult);
        testSuite.run(testResult);
        System.out.println("Test " + number + " Result: ");
        System.out.println("Sucess? : " + testResult.wasSuccessful());
        System.out.println("Errors : " + testResult.errorCount());
        System.out.println("Error List: ");
        printEnumeration(testResult.errors());
        System.out.println("Failures : " + testResult.failureCount());
        System.out.println("Failure List : ");
        printEnumeration(testResult.failures());

    }

    protected static void printEnumeration(Enumeration enum) {

        while (enum.hasMoreElements()) {
            System.out.println(enum.nextElement());
        }
    }
}
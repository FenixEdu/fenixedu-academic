package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoSiteStudentGroup;
import ServidorAplicacao.Servicos.TestCaseReadServices;

/**
 * @author asnr and scpo
 *  
 */
public class PrepareCreateStudentGroupTest extends TestCaseReadServices {

    public PrepareCreateStudentGroupTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PrepareCreateStudentGroupTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "PrepareCreateStudentGroup";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { new Integer(26), new Integer(1) };
        return result;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {
        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        infoSiteStudentGroup.setInfoSiteStudentInformationList(new ArrayList());
        infoSiteStudentGroup.setNrOfElements(new Integer(1));
        return infoSiteStudentGroup;

    }

}
/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansByDegreeTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadDegreeCurricularPlansByDegreeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDegreeCurricularPlansByDegree";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(8) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }
}
/*
 * Created on 1/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

/**
 * @author lmac1
 */

public class ReadAllExecutionYearsTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadAllExecutionYearsTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionYears";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 4;
    }
}
/*
 * Created on 27/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

/**
 * @author lmac1
 */
public class ReadExecutionPeriodsTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadExecutionPeriodsTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionPeriods";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 8;
    }
}
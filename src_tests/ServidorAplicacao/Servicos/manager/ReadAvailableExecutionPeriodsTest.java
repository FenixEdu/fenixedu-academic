/*
 * Created on 15/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmac1
 */
public class ReadAvailableExecutionPeriodsTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadAvailableExecutionPeriodsTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadAvailableExecutionPeriods";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        List unavailableExecutionPeriods = new ArrayList(1);
        unavailableExecutionPeriods.add(new Integer(1));
        Object[] args = { unavailableExecutionPeriods };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 7;
    }
}
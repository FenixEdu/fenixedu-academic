/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmac1
 */

public class DeleteExecutionDegreesOfDegreeCurricularPlanTest extends TestCaseManagerDeleteServices {

    public DeleteExecutionDegreesOfDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteExecutionDegreesOfDegreeCurricularPlan";
    }

    protected List getArgumentsOfServiceToBeTestedSuccessfuly() {
        List entry = new ArrayList(2);
        entry.add(new Integer(14));
        entry.add(new Integer(100));
        return entry;
    }

    protected List expectedActionErrorsArguments() {
        List result = new ArrayList();
        result.add("2002/2003");
        return result;
    }

    protected List getArgumentsOfServiceToBeTestedUnSuccessfuly() {
        List list = new ArrayList(1);
        list.add(new Integer(10));
        return list;
    }
}
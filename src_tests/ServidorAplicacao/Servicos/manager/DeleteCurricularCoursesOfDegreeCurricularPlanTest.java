/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmac1
 */

public class DeleteCurricularCoursesOfDegreeCurricularPlanTest extends TestCaseManagerDeleteServices {

    public DeleteCurricularCoursesOfDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteCurricularCoursesOfDegreeCurricularPlan";
    }

    protected List getArgumentsOfServiceToBeTestedSuccessfuly() {
        List entry = new ArrayList(2);
        entry.add(new Integer(1));
        entry.add(new Integer(100));
        return entry;
    }

    protected List expectedActionErrorsArguments() {
        List result = new ArrayList();
        result.add("Aprendizagem");
        result.add("APR");
        return result;
    }

    protected List getArgumentsOfServiceToBeTestedUnSuccessfuly() {
        List list = new ArrayList(1);
        list.add(new Integer(23));
        return list;
    }
}
/*
 * Created on 2/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlansTest extends TestCaseManagerDeleteServices {

    public DeleteDegreeCurricularPlansTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteDegreeCurricularPlans";
    }

    protected List getArgumentsOfServiceToBeTestedSuccessfuly() {
        List entry = new ArrayList(2);
        entry.add(new Integer(5));
        entry.add(new Integer(100));
        return entry;
    }

    protected List expectedActionErrorsArguments() {
        List result = new ArrayList();
        result.add("plano1");
        return result;
    }

    protected List getArgumentsOfServiceToBeTestedUnSuccessfuly() {
        List list = new ArrayList(1);
        list.add(new Integer(1));
        return list;
    }
}
/*
 * Created on 3/Out/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmac1
 */
public class DeleteExecutionCoursesTest extends TestCaseManagerDeleteServices {

    public DeleteExecutionCoursesTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteExecutionCourses";
    }

    protected List getArgumentsOfServiceToBeTestedSuccessfuly() {
        List entry = new ArrayList(2);
        entry.add(new Integer(32));
        entry.add(new Integer(35));
        return entry;
    }

    protected List expectedActionErrorsArguments() {
        List result = new ArrayList();
        result.add("TFCI");
        return result;
    }

    protected List getArgumentsOfServiceToBeTestedUnSuccessfuly() {
        List list = new ArrayList(1);
        list.add(new Integer(24));
        return list;
    }
}


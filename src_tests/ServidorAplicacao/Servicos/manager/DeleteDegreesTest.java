/*
 * Created on 22/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmac1
 */

public class DeleteDegreesTest extends TestCaseManagerDeleteServices {

    public DeleteDegreesTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteDegrees";
    }

    protected List getArgumentsOfServiceToBeTestedSuccessfuly() {
        List entry = new ArrayList(2);
        entry.add(new Integer(12));
        entry.add(new Integer(100));
        return entry;
    }

    protected List expectedActionErrorsArguments() {
        List result = new ArrayList();
        result.add("Licenciatura de Engenharia Informatica e de Computadores");
        return result;
    }

    protected List getArgumentsOfServiceToBeTestedUnSuccessfuly() {
        List list = new ArrayList(1);
        list.add(new Integer(8));
        return list;
    }
}
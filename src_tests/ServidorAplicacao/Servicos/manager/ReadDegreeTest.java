/*
 * Created on 25/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegree;

/**
 * @author lmac1
 */

public class ReadDegreeTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadDegreeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDegree";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(100) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(9) };
        return args;
    }

    protected Object getObjectToCompare() {
        return new InfoDegree("MEEC", "Engenharia Electrotecnica e de Computadores");
    }
}
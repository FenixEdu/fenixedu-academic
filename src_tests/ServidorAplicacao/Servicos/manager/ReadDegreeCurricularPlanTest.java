/*
 * Created on 1/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlanTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDegreeCurricularPlan";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(100) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(1) };
        return args;
    }

    protected Object getObjectToCompare() {

        InfoDegree infoDegree = new InfoDegree("LEIC",
                "Licenciatura de Engenharia Informatica e de Computadores");

        return new InfoDegreeCurricularPlan("plano1", infoDegree);
    }
}
/*
 * Created on 1/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;

/**
 * @author lmac1
 */

public class ReadExecutionDegreeTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadExecutionDegreeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionDegree";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(100) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(11) };
        return args;
    }

    protected Object getObjectToCompare() {

        InfoDegree infoDegree = new InfoDegree("LEIC",
                "Licenciatura de Engenharia Informatica e de Computadores");

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1",
                infoDegree);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");

        return new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
    }
}
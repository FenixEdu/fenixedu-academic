/*
 * Created on 1/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;

/**
 * @author lmac1
 */

public class ReadCurricularCourseTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadCurricularCourseTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCurricularCourse";
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

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1",
                infoDegree);

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoCurricularCourse.setName("Analise Matematica I");
        infoCurricularCourse.setCode("AMI");

        return infoCurricularCourse;
    }
}
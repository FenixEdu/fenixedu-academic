/*
 * Created on 2/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.util.CurricularCourseType;

/**
 * @author lmac1
 */
public class InsertCurricularCourseAtDegreeCurricularPlanTest extends
        TestCaseManagerInsertAndEditServices {

    public InsertCurricularCourseAtDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertCurricularCourseAtDegreeCurricularPlan";
    }

    //	insert curricular course with name,code already existing in DB but with
    // another key_degree_curricular_plan
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoDegreeCurricularPlan infoDegreeCP = new InfoDegreeCurricularPlan();
        infoDegreeCP.setIdInternal(new Integer(2));

        CurricularCourseType curricularCourseType = new CurricularCourseType();
        curricularCourseType.setCurricularCourseType(new Integer(1));

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setName("Analise Matematica I");
        infoCurricularCourse.setCode("AMI");
        infoCurricularCourse.setType(curricularCourseType);
        infoCurricularCourse.setMandatory(new Boolean(true));
        infoCurricularCourse.setBasic(new Boolean(true));
        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCP);

        Object[] args = { infoCurricularCourse };
        return args;
    }

    //	insert curricular course with name,code and key_degree_curricular_plan
    // already existing in DB
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoDegreeCurricularPlan infoDegreeCP = new InfoDegreeCurricularPlan();
        infoDegreeCP.setIdInternal(new Integer(1));

        CurricularCourseType curricularCourseType = new CurricularCourseType();
        curricularCourseType.setCurricularCourseType(new Integer(1));

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setName("Analise Matematica I");
        infoCurricularCourse.setCode("AMI");
        infoCurricularCourse.setType(curricularCourseType);
        infoCurricularCourse.setMandatory(new Boolean(true));
        infoCurricularCourse.setBasic(new Boolean(true));
        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCP);

        Object[] args = { infoCurricularCourse };
        return args;
    }

}
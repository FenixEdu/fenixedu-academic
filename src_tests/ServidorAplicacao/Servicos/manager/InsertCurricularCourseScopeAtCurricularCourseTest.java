/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourseTest extends
        TestCaseManagerInsertAndEditServices {

    public InsertCurricularCourseScopeAtCurricularCourseTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertCurricularCourseScopeAtCurricularCourse";
    }

    //	insert curricular course scope with semester and curricularCourse already
    // existing in DB and another branch
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
        infoCurricularSemester.setIdInternal(new Integer(3));

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setIdInternal(new Integer(14));

        //		this isn't the branch that corresponds to the keys of
        // curricularSemester and curicularCourse
        InfoBranch infoBranch = new InfoBranch();
        infoBranch.setIdInternal(new Integer(1));

        InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
        infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);
        infoCurricularCourseScope.setInfoBranch(infoBranch);
        //		infoCurricularCourseScope.setMaxIncrementNac(new Integer(3));
        //		infoCurricularCourseScope.setMinIncrementNac(new Integer(2));
        //		infoCurricularCourseScope.setWeigth(new Integer(4));

        Object[] args = { infoCurricularCourseScope };
        return args;
    }

    //	insert curricular course with semester, curricularCourse and branch
    // already existing in DB
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
        infoCurricularSemester.setIdInternal(new Integer(3));

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setIdInternal(new Integer(14));

        InfoBranch infoBranch = new InfoBranch();
        infoBranch.setIdInternal(new Integer(2));

        InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
        infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);
        infoCurricularCourseScope.setInfoBranch(infoBranch);
        //		infoCurricularCourseScope.setMaxIncrementNac(new Integer(3));
        //		infoCurricularCourseScope.setMinIncrementNac(new Integer(2));
        //		infoCurricularCourseScope.setWeigth(new Integer(4));

        Object[] args = { infoCurricularCourseScope };
        return args;
    }
}
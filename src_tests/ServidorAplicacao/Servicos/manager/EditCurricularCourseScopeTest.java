/*
 * Created on 3/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;

/**
 * @author lmac1
 */
public class EditCurricularCourseScopeTest extends TestCaseManagerInsertAndEditServices {

    public EditCurricularCourseScopeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditCurricularCourseScope";
    }

    //	edit curricular course scope existing in BD with another branch
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
        infoCurricularSemester.setIdInternal(new Integer(5));

        //		this isn't the branch that corresponds to the keys of
        // curricularSemester and curicularCourse
        InfoBranch infoBranch = new InfoBranch();
        infoBranch.setIdInternal(new Integer(3));

        InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
        infoCurricularCourseScope.setInfoBranch(infoBranch);
        //infoCurricularCourseScope.setMaxIncrementNac(new Integer(3));
        //infoCurricularCourseScope.setMinIncrementNac(new Integer(2));
        //infoCurricularCourseScope.setWeigth(new Integer(4));
        infoCurricularCourseScope.setIdInternal(new Integer(1));

        Object[] args = { infoCurricularCourseScope };
        return args;
    }

    //	edit curricular course scope with
    // key_curricular_semester,key_curricular_course and key_branch already
    // existing in DB
    //keeping the key_degree_curricular_plan

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
        infoCurricularSemester.setIdInternal(new Integer(4));

        InfoBranch infoBranch = new InfoBranch();
        infoBranch.setIdInternal(new Integer(1));

        InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
        infoCurricularCourseScope.setInfoBranch(infoBranch);
        //		infoCurricularCourseScope.setMaxIncrementNac(new Integer(3));
        //		infoCurricularCourseScope.setMinIncrementNac(new Integer(2));
        //		infoCurricularCourseScope.setWeigth(new Integer(4));
        infoCurricularCourseScope.setIdInternal(new Integer(10));

        Object[] args = { infoCurricularCourseScope };
        return args;

    }

}
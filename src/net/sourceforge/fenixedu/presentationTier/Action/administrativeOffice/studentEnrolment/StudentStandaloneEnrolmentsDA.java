package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentStandaloneEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

@Mapping(path = "/studentStandaloneEnrolments", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
	@Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
	@Forward(name = "showDegreeModulesToEnrol", path = "/studentEnrolments.do?method=prepareFromExtraEnrolment")

})
public class StudentStandaloneEnrolmentsDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected NoCourseGroupEnrolmentBean createNoCourseGroupEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester) {
	return new StudentStandaloneEnrolmentBean(studentCurricularPlan, executionSemester);
    }

    @Override
    protected String getActionName() {
	return "studentStandaloneEnrolments";
    }

}

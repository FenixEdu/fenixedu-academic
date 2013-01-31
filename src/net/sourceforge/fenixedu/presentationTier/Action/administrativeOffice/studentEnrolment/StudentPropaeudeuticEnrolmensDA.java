package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentPropaeudeuticEnrolmentsBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentPropaeudeuticEnrolments", module = "academicAdministration")
@Forwards({ @Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
		@Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
		@Forward(name = "showDegreeModulesToEnrol", path = "/studentEnrolments.do?method=prepareFromExtraEnrolment")

})
public class StudentPropaeudeuticEnrolmensDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

	@Override
	protected StudentPropaeudeuticEnrolmentsBean createNoCourseGroupEnrolmentBean(StudentCurricularPlan studentCurricularPlan,
			ExecutionSemester executionSemester) {
		return new StudentPropaeudeuticEnrolmentsBean(studentCurricularPlan, executionSemester);
	}

	@Override
	protected String getActionName() {
		return "studentPropaeudeuticEnrolments";
	}

	@Override
	protected NoCourseGroupCurriculumGroupType getGroupType() {
		return NoCourseGroupCurriculumGroupType.PROPAEDEUTICS;
	}

}

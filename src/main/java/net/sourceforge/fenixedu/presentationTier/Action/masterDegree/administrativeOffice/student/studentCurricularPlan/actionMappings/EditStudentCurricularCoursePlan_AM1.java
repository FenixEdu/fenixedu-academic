package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan.EditStudentCurricularCoursePlan;


@Mapping(path = "/editStudentCurricularPlan", module = "masterDegreeAdministrativeOffice", formBean = "studentCurricularPlanForm", validate = false)
@Forwards(value = {
	@Forward(name = "editStudentCurricularCoursePlan", path = "df.page.editStudentCurricularCoursePlan"),
	@Forward(name = "ShowStudentCurricularCoursePlan", path = "/showStudentCurricularCoursePlan.do")})
public class EditStudentCurricularCoursePlan_AM1 extends EditStudentCurricularCoursePlan {

}

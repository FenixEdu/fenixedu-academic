package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "departmentAdmOffice",
		path = "/tsdSupport",
		input = "/tsdSupport.do?method=prepareForTeacherServiceDistributionSupportServices&page=0",
		attribute = "tsdSupportForm",
		formBean = "tsdSupportForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(
				name = "showTeacherServiceDistributionSupportServices",
				path = "/departmentMember/teacherServiceDistribution/showValuationGroupingSupportServices.jsp"),
		@Forward(
				name = "showTeacherServiceDistributionCreationForm",
				path = "df.page.teacherServiceDistribution.showTeacherServiceDistributionCreationForm"),
		@Forward(
				name = "showTeacherServiceDistributionPermissionServicesFormByPerson",
				path = "/departmentMember/teacherServiceDistribution/showValuationGroupingPermissionServicesFormByPerson.jsp"),
		@Forward(
				name = "showTeacherServiceDistributionEditionForm",
				path = "df.page.teacherServiceDistribution.showTeacherServiceDistributionEditionForm"),
		@Forward(
				name = "showTeacherServiceDistributionPermissionServicesForm",
				path = "/departmentMember/teacherServiceDistribution/showValuationGroupingPermissionServicesForm.jsp") })
public class TeacherServiceDistributionSupportActionForDepartmentAdmOffice
		extends
		net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.TeacherServiceDistributionSupportAction {
}
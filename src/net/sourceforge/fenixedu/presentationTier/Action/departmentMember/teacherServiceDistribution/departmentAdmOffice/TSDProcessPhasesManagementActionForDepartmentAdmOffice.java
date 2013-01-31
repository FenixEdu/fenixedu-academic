package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "departmentAdmOffice",
		path = "/tsdProcessPhasesManagement",
		input = "/tsdProcessPhasesManagement.do?method=prepareForTSDProcessPhasesManagement&page=0",
		attribute = "tsdProcessPhasesManagementForm",
		formBean = "tsdProcessPhasesManagementForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(
				name = "showOmissionValuesValuationForm",
				path = "/departmentMember/teacherServiceDistribution/showOmissionValuesValuationForm.jsp"),
		@Forward(
				name = "showTSDProcessPhasesManagementForm",
				path = "/departmentMember/teacherServiceDistribution/showValuationPhasesManagementForm.jsp"),
		@Forward(
				name = "tsdProcessPhaseDataManagementOptions",
				path = "/departmentMember/teacherServiceDistribution/valuationPhaseDataManagementOptions.jsp") })
public class TSDProcessPhasesManagementActionForDepartmentAdmOffice
		extends
		net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.TSDProcessPhasesManagementAction {
}
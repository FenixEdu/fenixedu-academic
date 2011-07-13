package net.sourceforge.fenixedu.presentationTier.Action.employee.departmentMember;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentMember", path = "/assiduousnessResponsible", input = "show-employee-list", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "show-clockings", path = "/assiduousnessResponsible/showClockings.jsp"),
		@Forward(name = "show-employee-work-sheet", path = "/assiduousnessResponsible/showWorkSheet.jsp"),
		@Forward(name = "show-employee-list", path = "/assiduousnessResponsible/showEmployeeList.jsp"),
		@Forward(name = "show-schedule", path = "/assiduousnessResponsible/showSchedule.jsp"),
		@Forward(name = "show-vacations", path = "/assiduousnessResponsible/showVacations.jsp"),
		@Forward(name = "show-justifications", path = "/assiduousnessResponsible/showJustifications.jsp") })
public class AssiduousnessResponsibleDispatchActionForDepartmentMember extends net.sourceforge.fenixedu.presentationTier.Action.employee.AssiduousnessResponsibleDispatchAction {
}
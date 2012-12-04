package net.sourceforge.fenixedu.presentationTier.Action.employee.departmentMember;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentMember", path = "/assiduousnessResponsible", input = "show-employee-list", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "show-clockings", path = "/assiduousnessResponsible/showClockings.jsp", tileProperties = @Tile(title = "private.department.responsibleforattendance.staff")),
	@Forward(name = "show-employee-work-sheet", path = "/assiduousnessResponsible/showWorkSheet.jsp", tileProperties = @Tile(title = "private.department.responsibleforattendance.staff")),
	@Forward(name = "show-employee-list", path = "/assiduousnessResponsible/showEmployeeList.jsp", tileProperties = @Tile(title = "private.department.responsibleforattendance.staff")),
	@Forward(name = "show-schedule", path = "/assiduousnessResponsible/showSchedule.jsp", tileProperties = @Tile(title = "private.department.responsibleforattendance.staff")),
	@Forward(name = "show-vacations", path = "/assiduousnessResponsible/showVacations.jsp", tileProperties = @Tile(title = "private.department.responsibleforattendance.staff")),
	@Forward(name = "show-justifications", path = "/assiduousnessResponsible/showJustifications.jsp", tileProperties = @Tile(title = "private.department.responsibleforattendance.staff")) })
public class AssiduousnessResponsibleDispatchActionForDepartmentMember extends
	net.sourceforge.fenixedu.presentationTier.Action.employee.AssiduousnessResponsibleDispatchAction {
}
package net.sourceforge.fenixedu.presentationTier.Action.employee.employee;

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

@Mapping(module = "employee", path = "/assiduousnessResponsible", input = "show-employee-list", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "show-employee-work-sheet", path = "/assiduousnessResponsible/showWorkSheet.jsp"),
		@Forward(name = "show-employee-list", path = "/assiduousnessResponsible/showEmployeeList.jsp") })
public class AssiduousnessResponsibleDispatchActionForEmployee extends net.sourceforge.fenixedu.presentationTier.Action.employee.AssiduousnessResponsibleDispatchAction {
}
package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.institucionalProjectsManagement;

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

@Mapping(module = "institucionalProjectsManagement", path = "/projectReport", attribute = "rubricForm", formBean = "rubricForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "index", path = "/projectsManagement/firstPage.jsp"),
		@Forward(name = "showsummaryReport", path = "/projectsManagement/showSummaryReport.jsp"),
		@Forward(name = "showcabimentosReport", path = "/projectsManagement/showMovementsReport.jsp"),
		@Forward(name = "showprojectBudgetaryBalanceReport", path = "/projectsManagement/showProjectBudgetaryBalanceReport.jsp"),
		@Forward(name = "showcompleteExpensesReport", path = "/projectsManagement/showCompleteExpensesReport.jsp"),
		@Forward(name = "showopeningProjectFileReport", path = "/projectsManagement/showOpeningProjectFileReport.jsp"),
		@Forward(name = "showadiantamentosReport", path = "/projectsManagement/showMovementsReport.jsp"),
		@Forward(name = "coordinatorsList", path = "/projectsManagement/coordinatorsList.jsp"),
		@Forward(name = "showrevenueReport", path = "/projectsManagement/showRevenueReport.jsp"),
		@Forward(name = "helpPage", path = "/projectsManagement/helpPage.jsp"),
		@Forward(name = "showexpensesReport", path = "/projectsManagement/showExpensesReport.jsp"),
		@Forward(name = "projectList", path = "/projectsManagement/projectList.jsp") })
public class ProjectReportsDispatchActionForInstitucionalProjectsManagement extends net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.ProjectReportsDispatchAction {
}
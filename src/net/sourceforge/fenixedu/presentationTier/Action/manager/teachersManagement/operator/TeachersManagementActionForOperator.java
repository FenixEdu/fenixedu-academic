package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		module = "operator",
		path = "/teachersManagement",
		input = "/teachersManagement.do?method=prepareDissociateEC&page=0",
		attribute = "teacherManagementForm",
		formBean = "teacherManagementForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(name = "mainPage", path = "/operator/welcomeScreen.jsp"),
		@Forward(name = "firstPage", path = "/manager/teachersManagement/welcomeScreen.jsp", tileProperties = @Tile(
				navLocal = "/operator/teacherManagement/teacherManagementMainMenu.jsp")) })
public class TeachersManagementActionForOperator extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.TeachersManagementAction {
}
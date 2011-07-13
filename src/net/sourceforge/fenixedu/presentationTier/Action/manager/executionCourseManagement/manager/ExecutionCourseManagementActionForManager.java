package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.manager;

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

@Mapping(module = "manager", path = "/executionCourseManagement", scope = "session", parameter = "method")
@Forwards(value = {
		@Forward(name = "mainPage", path = "/manager/welcomScreen.jsp"),
		@Forward(name = "firstPage", path = "/manager/executionCourseManagement/welcomeScreen.jsp") })
public class ExecutionCourseManagementActionForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.ExecutionCourseManagementAction {
}
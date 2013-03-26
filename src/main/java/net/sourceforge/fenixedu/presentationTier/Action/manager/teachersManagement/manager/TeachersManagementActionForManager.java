package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/teachersManagement", input = "/teachersManagement.do?method=prepareDissociateEC&page=0",
        attribute = "teacherManagementForm", formBean = "teacherManagementForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "mainPage", path = "/manager/welcomScreen.jsp"),
        @Forward(name = "firstPage", path = "/manager/teachersManagement/welcomeScreen.jsp") })
public class TeachersManagementActionForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement.TeachersManagementAction {
}
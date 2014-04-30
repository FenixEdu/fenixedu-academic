package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement;

import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPeopleApp;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerPeopleApp.class, path = "teachers-management", titleKey = "label.manager.teachersManagement")
@Mapping(module = "manager", path = "/teachersManagement", parameter = "/manager/teachersManagement/welcomeScreen.jsp")
public class TeachersManagementAction extends ForwardAction {

}
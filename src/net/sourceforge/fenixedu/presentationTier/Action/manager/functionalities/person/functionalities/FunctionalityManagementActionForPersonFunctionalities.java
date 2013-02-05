package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.person.functionalities;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "person/functionalities", path = "/private/functionality", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "view.module", path = "/manager/functionalities/view-module.jsp"),
        @Forward(name = "edit", path = "/manager/functionalities/edit-functionality.jsp"),
        @Forward(name = "manage", path = "/manager/functionalities/manage-functionality.jsp"),
        @Forward(name = "view", path = "/manager/functionalities/view-functionality.jsp"),
        @Forward(name = "confirm", path = "/manager/functionalities/confirm-delete.jsp"),
        @Forward(name = "create", path = "/manager/functionalities/create-functionality.jsp") })
public class FunctionalityManagementActionForPersonFunctionalities extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.FunctionalityManagementAction {
}
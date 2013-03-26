package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.manager.functionalities;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager/functionalities", path = "/private/module", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "view.module", path = "/manager/functionalities/view-module.jsp"),
        @Forward(name = "edit", path = "/manager/functionalities/edit-module.jsp"),
        @Forward(name = "view", path = "/manager/functionalities/view-module.jsp"),
        @Forward(name = "create", path = "/manager/functionalities/create-module.jsp"),
        @Forward(name = "upload", path = "/manager/functionalities/upload-structure.jsp") })
public class ModuleManagementActionForManagerFunctionalities extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.ModuleManagementAction {
}
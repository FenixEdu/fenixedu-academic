package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.manager.functionalities;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager/functionalities", path = "/groupLanguage", scope = "session")
@Forwards(value = { @Forward(name = "show", path = "/manager/functionalities/group-language.jsp") })
public class GroupLanguageActionForManagerFunctionalities extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.GroupLanguageAction {
}
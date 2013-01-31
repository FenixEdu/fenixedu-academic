package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.person.functionalities;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "person/functionalities", path = "/groupLanguage", scope = "session")
@Forwards(value = { @Forward(name = "show", path = "/manager/functionalities/group-language.jsp") })
public class GroupLanguageActionForPersonFunctionalities extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.GroupLanguageAction {
}
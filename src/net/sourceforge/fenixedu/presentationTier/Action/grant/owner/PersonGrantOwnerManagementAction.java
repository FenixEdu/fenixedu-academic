package net.sourceforge.fenixedu.presentationTier.Action.grant.owner;

import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.PersonManagementAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "facultyAdmOffice", path = "/findPerson", input = "findPerson", attribute = "findPersonForm", formBean = "findPersonForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp"),
	@Forward(name = "displayPerson", path = "/manager/personManagement/displayPerson.jsp"),
	@Forward(name = "findPerson", path = "/manager/personManagement/findPerson.jsp") })
public class PersonGrantOwnerManagementAction extends PersonManagementAction {

}
package net.sourceforge.fenixedu.presentationTier.Action.grant.owner;

import net.sourceforge.fenixedu.presentationTier.Action.personnelSection.ManagePeople;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/personnelManagePeople", module = "facultyAdmOffice")
@Forwards({ @Forward(name = "searchPeople", path = "/personnelSection/people/searchPeople.jsp"),
	@Forward(name = "createPerson", path = "/personnelSection/people/createPerson.jsp"),
	@Forward(name = "createPersonFillInfo", path = "/personnelSection/people/createPersonFillInfo.jsp"),
	@Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp") })
public class ManageGrantOwnerPeople extends ManagePeople {
}

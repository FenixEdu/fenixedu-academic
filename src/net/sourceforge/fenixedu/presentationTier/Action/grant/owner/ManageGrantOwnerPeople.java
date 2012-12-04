package net.sourceforge.fenixedu.presentationTier.Action.grant.owner;

import net.sourceforge.fenixedu.presentationTier.Action.personnelSection.ManagePeople;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/personnelManagePeople", module = "facultyAdmOffice")
@Forwards({
	@Forward(name = "searchPeople", path = "/personnelSection/people/searchPeople.jsp", tileProperties = @Tile(title = "private.teachingstaffandresearcher.managementscholarship.scholarshipsearch")),
	@Forward(name = "createPerson", path = "/personnelSection/people/createPerson.jsp", tileProperties = @Tile(title = "private.teachingstaffandresearcher.managementscholarship.scholarshipsearch")),
	@Forward(name = "createPersonFillInfo", path = "/personnelSection/people/createPersonFillInfo.jsp", tileProperties = @Tile(title = "private.teachingstaffandresearcher.managementscholarship.scholarshipsearch")),
	@Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp", tileProperties = @Tile(title = "private.teachingstaffandresearcher.managementscholarship.scholarshipsearch")) })
public class ManageGrantOwnerPeople extends ManagePeople {
}

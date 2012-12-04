package net.sourceforge.fenixedu.presentationTier.Action.research.researcher;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/prizes/prizeManagement", scope = "session", parameter = "method")
@Forwards(value = {
	@Forward(name = "editUnits", path = "/researcher/prizes/associateUnitToPrize.jsp", tileProperties = @Tile(  title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.prizes")),
	@Forward(name = "editPrize", path = "/researcher/prizes/editPrize.jsp", tileProperties = @Tile(  title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.prizes")),
	@Forward(name = "viewPrize", path = "/researcher/prizes/showPrize.jsp", tileProperties = @Tile(  title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.prizes")),
	@Forward(name = "listPrizes", path = "/researcher/prizes/listPrizes.jsp", tileProperties = @Tile(  title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.prizes")),
	@Forward(name = "personAssociation", path = "/researcher/prizes/associatePersonToPrize.jsp", tileProperties = @Tile(  title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.prizes")),
	@Forward(name = "createPrize", path = "/researcher/prizes/createPrize.jsp", tileProperties = @Tile(  title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.prizes")) })
public class PrizeManagementForResearcher extends net.sourceforge.fenixedu.presentationTier.Action.research.PrizeManagement {
}
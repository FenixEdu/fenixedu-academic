package net.sourceforge.fenixedu.presentationTier.Action.research.researcher;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/prizes/prizeManagement", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "editUnits", path = "/researcher/prizes/associateUnitToPrize.jsp"),
		@Forward(name = "editPrize", path = "/researcher/prizes/editPrize.jsp"),
		@Forward(name = "viewPrize", path = "/researcher/prizes/showPrize.jsp"),
		@Forward(name = "listPrizes", path = "/researcher/prizes/listPrizes.jsp"),
		@Forward(name = "personAssociation", path = "/researcher/prizes/associatePersonToPrize.jsp"),
		@Forward(name = "createPrize", path = "/researcher/prizes/createPrize.jsp") })
public class PrizeManagementForResearcher extends net.sourceforge.fenixedu.presentationTier.Action.research.PrizeManagement {
}
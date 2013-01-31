package net.sourceforge.fenixedu.presentationTier.Action.coordinator.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/degreeCurricularPlan/equivalencyPlan", scope = "session", parameter = "method")
@Forwards(value = {
		@Forward(
				name = "showPlan",
				path = "/scientificCouncil/degreeCurricularPlan/showEquivalencyPlan.jsp",
				tileProperties = @Tile(title = "private.scientificcouncil.bolognaprocess.planequivalence")),
		@Forward(
				name = "addEquivalency",
				path = "/scientificCouncil/degreeCurricularPlan/addEquivalency.jsp",
				tileProperties = @Tile(title = "private.scientificcouncil.bolognaprocess.planequivalence")) })
public class EquivalencyPlanDAForScientificCouncil extends
		net.sourceforge.fenixedu.presentationTier.Action.coordinator.EquivalencyPlanDA {
}
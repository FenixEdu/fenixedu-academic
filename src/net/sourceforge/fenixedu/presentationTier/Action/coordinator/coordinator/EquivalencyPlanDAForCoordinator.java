package net.sourceforge.fenixedu.presentationTier.Action.coordinator.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/degreeCurricularPlan/equivalencyPlan", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showPlan", path = "show-equivalency.plan") })
public class EquivalencyPlanDAForCoordinator extends
		net.sourceforge.fenixedu.presentationTier.Action.coordinator.EquivalencyPlanDA {
}
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/degreeCurricularPlan/equivalencyPlan", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showPlan", path = "/manager/degreeCurricularPlan/showEquivalencyPlan.jsp"),
        @Forward(name = "addEquivalency", path = "/manager/degreeCurricularPlan/addEquivalency.jsp") })
public class EquivalencyPlanDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.coordinator.EquivalencyPlanDA {
}
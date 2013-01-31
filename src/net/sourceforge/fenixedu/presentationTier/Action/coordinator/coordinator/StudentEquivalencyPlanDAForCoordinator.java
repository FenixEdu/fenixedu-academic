package net.sourceforge.fenixedu.presentationTier.Action.coordinator.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/degreeCurricularPlan/studentEquivalencyPlan", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showPlan", path = "show-student-equivalency.plan") })
public class StudentEquivalencyPlanDAForCoordinator extends
		net.sourceforge.fenixedu.presentationTier.Action.coordinator.StudentEquivalencyPlanDA {
}
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.coordinator;

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

@Mapping(module = "coordinator", path = "/degreeCurricularPlan/studentEquivalencyPlan", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showPlan", path = "show-student-equivalency.plan") })
public class StudentEquivalencyPlanDAForCoordinator extends net.sourceforge.fenixedu.presentationTier.Action.coordinator.StudentEquivalencyPlanDA {
}
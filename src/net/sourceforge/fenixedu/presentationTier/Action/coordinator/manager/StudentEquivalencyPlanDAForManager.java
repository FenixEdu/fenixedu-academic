package net.sourceforge.fenixedu.presentationTier.Action.coordinator.manager;

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

@Mapping(module = "manager", path = "/degreeCurricularPlan/studentEquivalencyPlan", scope = "session", parameter = "method")
@Forwards(value = {
		@Forward(name = "showPlan", path = "/manager/degreeCurricularPlan/showStudentEquivalencyPlan.jsp"),
		@Forward(name = "addEquivalency", path = "/manager/degreeCurricularPlan/addStudentEquivalency.jsp") })
public class StudentEquivalencyPlanDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.coordinator.StudentEquivalencyPlanDA {
}
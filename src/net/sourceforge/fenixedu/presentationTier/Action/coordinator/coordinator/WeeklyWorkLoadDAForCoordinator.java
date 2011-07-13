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

@Mapping(module = "coordinator", path = "/weeklyWorkLoad", input = "/weeklyWorkLoad.do?method=prepare", attribute = "weeklyWorkLoadForm", formBean = "weeklyWorkLoadForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showWeeklyWorkLoad", path = "/coordinator/weeklyWorkLoad.jsp") })
public class WeeklyWorkLoadDAForCoordinator extends net.sourceforge.fenixedu.presentationTier.Action.coordinator.WeeklyWorkLoadDA {
}
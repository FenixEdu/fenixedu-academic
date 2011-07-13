package net.sourceforge.fenixedu.presentationTier.Action.teacher.coordinator;

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

@Mapping(module = "coordinator", path = "/searchECAttends", attribute = "studentsByCurricularCourseForm", formBean = "studentsByCurricularCourseForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "/teacher/viewAttendsSearch.jsp") })
public class SearchExecutionCourseAttendsActionForCoordinator extends net.sourceforge.fenixedu.presentationTier.Action.teacher.SearchExecutionCourseAttendsAction {
}
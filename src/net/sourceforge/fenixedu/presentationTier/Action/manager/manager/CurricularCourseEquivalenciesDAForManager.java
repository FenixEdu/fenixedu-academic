package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

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

@Mapping(module = "manager", path = "/curricularCourseEquivalencies", input = "/curricularCourseEquivalencies.do?method=prepare&page=0", attribute = "curricularCourseEquivalenciesForm", formBean = "curricularCourseEquivalenciesForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "showEquivalencies", path = "/manager/equivalences/curricularCourseEquivalencies.jsp"),
		@Forward(name = "showCreateEquivalencyForm", path = "/manager/equivalences/createCurricularCourseEquivalencies.jsp") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, key = "error.exists.curricular.course.equivalency", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class CurricularCourseEquivalenciesDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.CurricularCourseEquivalenciesDA {
}
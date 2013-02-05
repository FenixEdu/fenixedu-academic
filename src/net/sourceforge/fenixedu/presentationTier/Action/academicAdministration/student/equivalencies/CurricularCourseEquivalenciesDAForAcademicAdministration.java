package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.student.equivalencies;

import net.sourceforge.fenixedu.presentationTier.Action.manager.CurricularCourseEquivalenciesDA;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/curricularCourseEquivalencies",
        input = "/curricularCourseEquivalencies.do?method=prepare&page=0", attribute = "curricularCourseEquivalenciesForm",
        formBean = "curricularCourseEquivalenciesForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showEquivalencies", path = "/academicAdministration/equivalences/curricularCourseEquivalencies.jsp"),
        @Forward(name = "showCreateEquivalencyForm",
                path = "/academicAdministration/equivalences/createCurricularCourseEquivalencies.jsp") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
        key = "error.exists.curricular.course.equivalency",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class CurricularCourseEquivalenciesDAForAcademicAdministration extends CurricularCourseEquivalenciesDA {

}

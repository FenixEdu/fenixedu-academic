package net.sourceforge.fenixedu.presentationTier.Action.gesdis.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.gesdis.TeachingReportAction;
import net.sourceforge.fenixedu.presentationTier.config.FenixNotAuthorizedExceptionHandler;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/viewTeachingReport", input = "/viewTeachingReport.do?method=read",
        formBean = "teachingReportForm", functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "successfull-read", path = "/gesdis/viewTeachingReport.jsp"))
@Exceptions(@ExceptionHandling(type = NotAuthorizedException.class, key = "error.exception.notAuthorized",
        handler = FenixNotAuthorizedExceptionHandler.class, scope = "request"))
public class TeachingReportActionForCoordinator extends TeachingReportAction {
}
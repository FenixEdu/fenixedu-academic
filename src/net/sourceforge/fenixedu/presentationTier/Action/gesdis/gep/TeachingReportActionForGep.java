package net.sourceforge.fenixedu.presentationTier.Action.gesdis.gep;

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

@Mapping(module = "gep", path = "/viewTeachingReport", input = "/viewTeachingReport.do?method=read", attribute = "teachingReportForm", formBean = "teachingReportForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "successfull-read", path = "/gesdis/viewTeachingReport.jsp") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException.class, key = "error.exception.notAuthorized", handler = net.sourceforge.fenixedu.presentationTier.config.FenixNotAuthorizedExceptionHandler.class, scope = "request") })
public class TeachingReportActionForGep extends net.sourceforge.fenixedu.presentationTier.Action.gesdis.TeachingReportAction {
}
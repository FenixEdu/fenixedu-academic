package net.sourceforge.fenixedu.presentationTier.Action.gesdis.teacher;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "teacher",
		path = "/teachingReport",
		input = "/teachingReport.do?page=0&method=prepareEdit",
		attribute = "teachingReportForm",
		formBean = "teachingReportForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "successfull-read", path = "view-teaching-report"),
		@Forward(name = "show-form", path = "teaching-report-management") })
@Exceptions(value = {
		@ExceptionHandling(
				type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException.class,
				key = "error.exception.notAuthorized",
				handler = net.sourceforge.fenixedu.presentationTier.config.FenixNotAuthorizedExceptionHandler.class,
				scope = "request"),
		@ExceptionHandling(
				type = net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException.class,
				key = "error.exception.notAuthorized",
				handler = net.sourceforge.fenixedu.presentationTier.config.FenixNotAuthorizedExceptionHandler.class,
				scope = "request") })
public class TeachingReportActionForTeacher extends net.sourceforge.fenixedu.presentationTier.Action.gesdis.TeachingReportAction {
}
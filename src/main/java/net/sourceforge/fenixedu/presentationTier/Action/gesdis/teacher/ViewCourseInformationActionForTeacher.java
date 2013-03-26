package net.sourceforge.fenixedu.presentationTier.Action.gesdis.teacher;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "teacher", path = "/viewCourseInformation", input = "/teacherAdministrationViewer.do?method=instructions",
        attribute = "viewCourseInformationForm", formBean = "viewCourseInformationForm", scope = "request")
@Forwards(value = { @Forward(name = "successfull-read", path = "view-course-information") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException.class,
        key = "error.exception.notAuthorized",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixNotAuthorizedExceptionHandler.class, scope = "request") })
public class ViewCourseInformationActionForTeacher extends
        net.sourceforge.fenixedu.presentationTier.Action.gesdis.ViewCourseInformationAction {
}
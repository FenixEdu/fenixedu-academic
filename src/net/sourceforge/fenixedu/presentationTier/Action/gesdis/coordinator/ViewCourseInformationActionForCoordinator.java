package net.sourceforge.fenixedu.presentationTier.Action.gesdis.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "coordinator",
		path = "/courseInformation",
		input = "/courseInformation.do",
		attribute = "courseInformationForm",
		formBean = "courseInformationForm",
		scope = "request")
@Forwards(value = { @Forward(name = "successfull-read", path = "/gesdis/viewCourseInformation.jsp") })
public class ViewCourseInformationActionForCoordinator extends
		net.sourceforge.fenixedu.presentationTier.Action.gesdis.ViewCourseInformationAction {
}
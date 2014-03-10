package net.sourceforge.fenixedu.presentationTier.Action.gesdis.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.gesdis.ViewCourseInformationAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/courseInformation", formBean = "courseInformationForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "successfull-read", path = "/gesdis/viewCourseInformation.jsp"))
public class ViewCourseInformationActionForCoordinator extends ViewCourseInformationAction {
}
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/readTeacherInformation", scope = "request")
@Forwards(value = { @Forward(name = "show", path = "/teacher/information/viewTeacherInformation.jsp") })
public class ReadTeacherInformationActionForCoordinator extends
        net.sourceforge.fenixedu.presentationTier.Action.coordinator.ReadTeacherInformationAction {
}
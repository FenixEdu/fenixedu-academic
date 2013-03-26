package net.sourceforge.fenixedu.presentationTier.Action.coordinator.gep;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "gep", path = "/readTeacherInformation", scope = "request")
@Forwards(value = { @Forward(name = "show", path = "/teacher/information/viewTeacherInformation.jsp") })
public class ReadTeacherInformationActionForGep extends
        net.sourceforge.fenixedu.presentationTier.Action.coordinator.ReadTeacherInformationAction {
}
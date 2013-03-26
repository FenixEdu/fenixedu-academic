package net.sourceforge.fenixedu.presentationTier.Action.commons.student.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/viewCurriculumGraph", scope = "request", parameter = "method")
public class ViewCurriculumGraphDispatchActionForCoordinator extends
        net.sourceforge.fenixedu.presentationTier.Action.commons.student.ViewCurriculumGraphDispatchAction {
}
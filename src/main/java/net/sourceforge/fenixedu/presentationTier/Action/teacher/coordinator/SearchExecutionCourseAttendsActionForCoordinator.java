package net.sourceforge.fenixedu.presentationTier.Action.teacher.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.SearchExecutionCourseAttendsAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/searchECAttends", formBean = "studentsByCurricularCourseForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "search", path = "/teacher/viewAttendsSearch.jsp"))
public class SearchExecutionCourseAttendsActionForCoordinator extends SearchExecutionCourseAttendsAction {
}
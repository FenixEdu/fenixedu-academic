package net.sourceforge.fenixedu.presentationTier.Action.coordinator.student.curriculum;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewStudentCurriculum", module = "coordinator", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "ShowStudentCurricularPlans", path = "/coordinator/curriculum/viewCurricularPlans_bd.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/coordinator/student/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "ShowStudentCurriculumForCoordinator",
                path = "/coordinator/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "NotAuthorized", path = "/coordinator/student/notAuthorized_bd.jsp") })
public class CurriculumDispatchActionForCoordinator extends CurriculumDispatchAction {

}

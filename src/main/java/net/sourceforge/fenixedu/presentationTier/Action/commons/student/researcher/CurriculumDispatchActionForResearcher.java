package net.sourceforge.fenixedu.presentationTier.Action.commons.student.researcher;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.research.FinalWorkManagementActionForResearcher;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/viewStudentCurriculum", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm",
        functionality = FinalWorkManagementActionForResearcher.class)
@Forwards(value = { @Forward(name = "ShowStudentCurriculumForCoordinator", path = "df.page.showStudentCurriculumForCoordinator"),
        @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "ShowStudentCurricularPlans", path = "/student/curriculum/viewCurricularPlans_bd.jsp"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized_bd.jsp") })
public class CurriculumDispatchActionForResearcher extends CurriculumDispatchAction {
}
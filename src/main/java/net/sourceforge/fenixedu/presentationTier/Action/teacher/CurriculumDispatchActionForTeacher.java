package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewStudentCurriculum", module = "teacher", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm",
        functionality = FinalWorkManagementAction.class)
public class CurriculumDispatchActionForTeacher extends CurriculumDispatchAction {

}

package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.students;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "internationalRelatOffice", path = "/viewCurriculum",
        formBean = "studentCurricularPlanAndEnrollmentsSelectionForm",
        functionality = SearchForStudentsForInternationalRelatOffice.class)
@Forwards(@Forward(name = "ShowStudentCurriculum", path = "/internationalRelatOffice/displayStudentCurriculum_bd.jsp"))
public class CurriculumDispatchActionForInternationalRelatOffice extends CurriculumDispatchAction {
}
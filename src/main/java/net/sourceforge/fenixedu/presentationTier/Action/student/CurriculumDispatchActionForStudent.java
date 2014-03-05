package net.sourceforge.fenixedu.presentationTier.Action.student;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentViewApp.class, path = "curriculum", titleKey = "link.student.curriculum")
@Mapping(path = "/viewStudentCurriculum", module = "student", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm")
@Forwards({ @Forward(name = "chooseRegistration", path = "/student/curriculum/chooseRegistration.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized_bd.jsp") })
public class CurriculumDispatchActionForStudent extends CurriculumDispatchAction {

}

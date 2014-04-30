package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.student;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA;
import net.sourceforge.fenixedu.presentationTier.Action.student.CurriculumDispatchActionForStudent;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "student", path = "/registration", functionality = CurriculumDispatchActionForStudent.class)
@Forwards(value = {
        @Forward(name = "view-registration-curriculum", path = "/student/curriculum/viewRegistrationCurriculum.jsp"),
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp") })
public class RegistrationDAForStudent extends RegistrationDA {
}
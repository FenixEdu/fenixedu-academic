package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "teacher", path = "/registration", functionality = FinalWorkManagementAction.class)
@Forwards({
        @Forward(name = "/student/curriculum/viewRegistrationCurriculum.jsp", path = "view-registration-curriculum"),
        @Forward(name = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp",
                path = "chooseCycleForViewRegistrationCurriculum") })
public class RegistrationDAForTeacher extends RegistrationDA {
}
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "pedagogicalCouncil", path = "/registration", functionality = TutorshipStudentCurriculum.class)
@Forwards({
        @Forward(name = "view-registration-curriculum", path = "/pedagogicalCouncil/tutorship/viewRegistrationCurriculum.jsp"),
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp") })
public class RegistrationDAForPedagogicalCouncil extends RegistrationDA {
}
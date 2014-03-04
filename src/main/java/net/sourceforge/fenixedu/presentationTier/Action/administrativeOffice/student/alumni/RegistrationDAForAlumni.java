package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA;
import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniViewCurriculum;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "alumni", path = "/registration", functionality = AlumniViewCurriculum.class)
@Forwards({
        @Forward(name = "view-registration-curriculum", path = "/student/curriculum/viewRegistrationCurriculum.jsp"),
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp") })
public class RegistrationDAForAlumni extends RegistrationDA {
}
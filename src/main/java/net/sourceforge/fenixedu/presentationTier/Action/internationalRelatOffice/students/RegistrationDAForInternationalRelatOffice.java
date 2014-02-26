package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.students;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "internationalRelatOffice", path = "/registration",
        functionality = SearchForStudentsForInternationalRelatOffice.class)
@Forwards({
        @Forward(name = "view-registration-curriculum", path = "/internationalRelatOffice/viewRegistrationCurriculum.jsp"),
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp") })
public class RegistrationDAForInternationalRelatOffice extends RegistrationDA {
}
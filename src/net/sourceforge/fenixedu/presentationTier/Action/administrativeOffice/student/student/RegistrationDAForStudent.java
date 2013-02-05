package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.student;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/registration", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "view-registration-curriculum", path = "/student/curriculum/viewRegistrationCurriculum.jsp",
                tileProperties = @Tile(title = "private.student.view.curriculum")),
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp", tileProperties = @Tile(
                        title = "private.student.view.curriculum")) })
public class RegistrationDAForStudent extends
        net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA {
}
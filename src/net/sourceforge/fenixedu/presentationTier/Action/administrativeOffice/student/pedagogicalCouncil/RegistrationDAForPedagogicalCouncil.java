package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.pedagogicalCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "pedagogicalCouncil", path = "/registration", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "view-registration-curriculum", path = "/pedagogicalCouncil/tutorship/viewRegistrationCurriculum.jsp"),
		@Forward(name = "registrationConclusion", path = "registrationConclusion"),
		@Forward(
				name = "chooseCycleForViewRegistrationCurriculum",
				path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp") })
public class RegistrationDAForPedagogicalCouncil extends
		net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA {
}
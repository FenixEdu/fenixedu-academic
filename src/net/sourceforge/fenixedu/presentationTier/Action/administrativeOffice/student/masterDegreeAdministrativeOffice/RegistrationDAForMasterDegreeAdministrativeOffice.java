package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.masterDegreeAdministrativeOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "masterDegreeAdministrativeOffice", path = "/registration", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "view-registration-curriculum", path = "view-registration-curriculum"),
		@Forward(name = "chooseCycleForViewRegistrationCurriculum", path = "chooseCycleForViewRegistrationCurriculum") })
public class RegistrationDAForMasterDegreeAdministrativeOffice extends
		net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA {
}
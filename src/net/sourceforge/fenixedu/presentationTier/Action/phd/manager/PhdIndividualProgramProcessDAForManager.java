package net.sourceforge.fenixedu.presentationTier.Action.phd.manager;

import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdIndividualProgramProcess", module = "manager")
@Forwards({

	@Forward(name = "manageProcesses", path = "/phd/manager/manageProcesses.jsp"),

	@Forward(name = "viewProcess", path = "/phd/manager/viewProcess.jsp"),

	@Forward(name = "manageGuidingInformation", path = "/phd/manager/manageGuidingInformation.jsp"),

	@Forward(name = "managePhdIndividualProgramProcessState", path = "/phd/manager/managePhdIndividualProgramProcessState.jsp"),

	@Forward(name = "manageAlerts", path = "/phd/manager/manageAlerts.jsp"),

	@Forward(name = "viewAlertMessages", path = "/phd/manager/viewAlertMessages.jsp"),

	@Forward(name = "viewAlertMessageArchive", path = "/phd/manager/viewAlertMessageArchive.jsp"),

	@Forward(name = "viewAlertMessage", path = "/phd/manager/viewAlertMessage.jsp"),

	@Forward(name = "viewProcessAlertMessages", path = "/phd/manager/viewProcessAlertMessages.jsp"),

	@Forward(name = "viewProcessAlertMessageArchive", path = "/phd/manager/viewProcessAlertMessageArchive.jsp"),

	@Forward(name = "manageStudyPlan", path = "/phd/manager/manageStudyPlan.jsp"),

	@Forward(name = "viewCurriculum", path = "/phd/manager/viewCurriculum.jsp"),

	@Forward(name = "manageEnrolmentPeriods", path = "/phd/manager/periods/manageEnrolmentPeriods.jsp"),

	@Forward(name = "managePhdIndividualProcessConfiguration", path = "/phd/manager/configuration/managePhdIndividualProcessConfiguration.jsp"),

	@Forward(name = "managePhdIndividualProcessEmails", path = "/phd/manager/viewProcessEmails.jsp"),

	@Forward(name = "viewPhdIndividualProcessEmail", path = "/phd/manager/viewPhdEmail.jsp"),

	@Forward(name = "viewMigrationProcess", path = "/phd/manager/viewMigrationProcess.jsp"),

	@Forward(name = "viewAllMigratedProcesses", path = "/phd/manager/viewAllMigratedProcesses.jsp"),

	@Forward(name = "viewPhdParticipants", path = "/phd/manager/participant/viewPhdParticipants.jsp")
})
public class PhdIndividualProgramProcessDAForManager extends PhdIndividualProgramProcessDA {

}

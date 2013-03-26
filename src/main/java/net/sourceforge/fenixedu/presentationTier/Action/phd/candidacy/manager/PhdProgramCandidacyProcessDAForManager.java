package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.manager;

import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "manager")
@Forwards({

@Forward(name = "manageProcesses", path = "/phdIndividualProgramProcess.do?method=manageProcesses"),

@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/manager/manageCandidacyDocuments.jsp"),

@Forward(name = "manageCandidacyReview", path = "/phd/candidacy/manager/manageCandidacyReview.jsp"),

@Forward(name = "viewProcess", path = "/phdIndividualProgramProcess.do?method=viewProcess"),

@Forward(name = "manageNotifications", path = "/phd/candidacy/manager/manageNotifications.jsp"),

@Forward(name = "manageStates", path = "/phd/candidacy/manager/manageStates.jsp") })
public class PhdProgramCandidacyProcessDAForManager extends PhdProgramCandidacyProcessDA {

}

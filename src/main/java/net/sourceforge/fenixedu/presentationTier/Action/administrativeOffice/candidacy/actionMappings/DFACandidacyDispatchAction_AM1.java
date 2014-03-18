package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy.DFACandidacyDispatchAction;


@Mapping(path = "/dfaCandidacy", module = "masterDegreeAdministrativeOffice", input = "df.page.chooseDFAExecutionDegree", formBean = "dfaCandidacyForm")
@Forwards(value = {
	@Forward(name = "chooseExecutionDegree", path = "df.page.chooseDFAExecutionDegree"),
	@Forward(name = "showCreatedCandidacy", path = "df.page.showCreatedCandidacy"),
	@Forward(name = "prepareGenPass", path = "df.page.prepareGenPass"),
	@Forward(name = "showCandidacyGeneratePass", path = "df.page.showCandidacyGeneratePass"),
	@Forward(name = "generatePassword", path = "/candidacy/showNewPass.jsp"),
	@Forward(name = "fillCandidateData", path = "df.page.fillCandidateData"),
	@Forward(name = "prepareValidateCandidacyData", path = "df.page.prepareValidateCandidacyData"),
	@Forward(name = "showCandidacyValidateData", path = "df.page.showCandidacyValidateData"),
	@Forward(name = "showCandidacyAlterData", path = "df.page.showCandidacyAlterData"),
	@Forward(name = "viewCandidacyDetails", path = "df.page.viewCandidacyDetails"),
	@Forward(name = "chooseCandidacy", path = "df.page.chooseCandidacy"),
	@Forward(name = "alterSuccess", path = "df.page.alterSuccessPersonalData"),
	@Forward(name = "candidacyRegistration", path = "df.page.candidacyRegistration"),
	@Forward(name = "candidacyRegistrationSuccess", path = "df.page.candidacyRegistrationSuccess"),
	@Forward(name = "printRegistrationInformation", path = "/candidacy/candidacyRegistrationTemplate.jsp")})
public class DFACandidacyDispatchAction_AM1 extends DFACandidacyDispatchAction {

}

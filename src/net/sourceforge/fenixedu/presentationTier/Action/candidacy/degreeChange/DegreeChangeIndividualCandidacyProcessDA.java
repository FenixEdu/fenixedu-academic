package net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/degreeChange/fillCandidacyInformation.jsp"),
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/degreeChange/editCandidacyInformation.jsp"),
	@Forward(name = "introduce-candidacy-result", path = "/candidacy/degreeChange/introduceCandidacyResult.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
	@Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp")

})
public class DegreeChangeIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return DegreeChangeCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return DegreeChangeIndividualCandidacyProcess.class;
    }
    
    @Override
    protected DegreeChangeCandidacyProcess getParentProcess(HttpServletRequest request) {
        return (DegreeChangeCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected DegreeChangeIndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (DegreeChangeIndividualCandidacyProcess) super.getProcess(request);
    }
    
    @Override
    protected DegreeChangeIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
        return (DegreeChangeIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final DegreeChangeIndividualCandidacyProcessBean bean = new DegreeChangeIndividualCandidacyProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));
	bean.setChoosePersonBean(new ChoosePersonBean());
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

}

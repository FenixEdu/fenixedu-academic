package net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/graduatedPerson/fillCandidacyInformation.jsp"),
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/graduatedPerson/editCandidacyInformation.jsp"),
	@Forward(name = "introduce-candidacy-result", path = "/candidacy/graduatedPerson/introduceCandidacyResult.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
	@Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp")

})
public class DegreeCandidacyForGraduatedPersonIndividualProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return DegreeCandidacyForGraduatedPersonProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return DegreeCandidacyForGraduatedPersonIndividualProcess.class;
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonProcess getParentProcess(HttpServletRequest request) {
	return (DegreeCandidacyForGraduatedPersonProcess) super.getParentProcess(request);
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonIndividualProcess getProcess(HttpServletRequest request) {
	return (DegreeCandidacyForGraduatedPersonIndividualProcess) super.getProcess(request);
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonIndividualProcessBean getIndividualCandidacyProcessBean() {
	return (DegreeCandidacyForGraduatedPersonIndividualProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = new DegreeCandidacyForGraduatedPersonIndividualProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));
	bean.setChoosePersonBean(new ChoosePersonBean());
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = new DegreeCandidacyForGraduatedPersonIndividualProcessBean();
	bean.setPersonBean(new PersonBean(getProcess(request).getCandidacyPerson()));
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditCandidacyPersonalInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-personal-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), new DegreeCandidacyForGraduatedPersonIndividualProcessBean(
		getProcess(request)));
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditCandidacyInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-information");
	}

	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("individualCandidacyResultBean", new DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean(
		getProcess(request)));
	return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("individualCandidacyResultBean", getCandidacyResultBean());
	return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "IntroduceCandidacyResult", getCandidacyResultBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("individualCandidacyResultBean", getCandidacyResultBean());
	    return mapping.findForward("introduce-candidacy-result");
	}

	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean getCandidacyResultBean() {
	return (DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean) getRenderedObject("individualCandidacyResultBean");
    }

    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	request.setAttribute("degree", getProcess(request).getCandidacySelectedDegree());
	return mapping.findForward("create-registration");
    }

    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "CreateRegistration");
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("degree", getProcess(request).getCandidacySelectedDegree());
	    return mapping.findForward("create-registration");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }
}

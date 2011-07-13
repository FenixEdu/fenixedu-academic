package net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeTransfer;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( {
	// @Forward(name = "intro", path =
	// "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "intro", path = "/caseHandlingDegreeTransferCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/degreeTransfer/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
	@Forward(name = "fill-common-candidacy-information", path = "/candidacy/fillCommonCandidacyInformation.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/degreeTransfer/fillCandidacyInformation.jsp"),
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
	@Forward(name = "edit-common-candidacy-information", path = "/candidacy/editCommonCandidacyInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/degreeTransfer/editCandidacyInformation.jsp"),
	@Forward(name = "edit-candidacy-curricularCourses-information", path = "/candidacy/degreeTransfer/editCandidacyCurricularCoursesInformation.jsp"),
	@Forward(name = "introduce-candidacy-result", path = "/candidacy/degreeTransfer/introduceCandidacyResult.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
	@Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp"),
	@Forward(name = "prepare-edit-candidacy-documents", path = "/candidacy/editCandidacyDocuments.jsp"),
	@Forward(name = "change-process-checked-state", path = "/candidacy/changeProcessCheckedState.jsp"),
	@Forward(name = "change-payment-checked-state", path = "/candidacy/changePaymentCheckedState.jsp"),
	@Forward(name = "reject-candidacy", path = "/candidacy/rejectCandidacy.jsp")

})
public class DegreeTransferIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return DegreeTransferCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return DegreeTransferIndividualCandidacyProcess.class;
    }

    @Override
    protected DegreeTransferCandidacyProcess getParentProcess(HttpServletRequest request) {
	return (DegreeTransferCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected DegreeTransferIndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (DegreeTransferIndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected DegreeTransferIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
	return (DegreeTransferIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));
	bean.setChoosePersonBean(new ChoosePersonBean());

	/*
	 * 18/07/2009 - A informacao para o RAIDs nao e introduzidas mas temos
	 * de criar este bean
	 */
	bean.setCandidacyInformationBean(new CandidacyInformationBean());

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    protected void createCandidacyPrecedentDegreeInformation(IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
	    StudentCurricularPlan studentCurricularPlan) {
	final CandidacyPrecedentDegreeInformationBean info = new CandidacyPrecedentDegreeInformationBean();

	info.setDegreeDesignation(studentCurricularPlan.getName());
	info.setInstitutionUnitName(rootDomainObject.getInstitutionUnit().getUnitName());
	info.initCurricularCoursesInformation(studentCurricularPlan);

	bean.setPrecedentDegreeInformation(info);
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean();
	bean.setPersonBean(new PersonBean(getProcess(request).getPersonalDetails()));
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

    public ActionForward prepareExecuteEditCommonCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean();
	bean.setCandidacyInformationBean(getProcess(request).getCandidacyInformationBean());
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("edit-common-candidacy-information");
    }

    public ActionForward executeEditCommonCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-common-candidacy-information");
    }

    public ActionForward executeEditCommonCandidacyInformation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    final Set<String> messages = getIndividualCandidacyProcessBean().getCandidacyInformationBean().validate();
	    if (!messages.isEmpty()) {
		for (final String message : messages) {
		    addActionMessage(request, message);
		}
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("edit-common-candidacy-information");
	    }

	    executeActivity(getProcess(request), "EditCommonCandidacyInformation", getIndividualCandidacyProcessBean());

	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-common-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean(getProcess(request));
	bean.setCandidacyInformationBean(new CandidacyInformationBean(getProcess(request).getCandidacy()));
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
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
	    DegreeTransferIndividualCandidacyProcessBean bean = (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

	    copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());

	    executeActivity(getProcess(request), "EditCandidacyInformation", bean);
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyCurricularCoursesInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), new DegreeTransferIndividualCandidacyProcessBean(
		getProcess(request)));
	return mapping.findForward("edit-candidacy-curricularCourses-information");
    }

    public ActionForward executeEditCandidacyCurricularCoursesInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-curricularCourses-information");
    }

    public ActionForward executeEditCandidacyCurricularCoursesInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditCandidacyCurricularCoursesInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-curricularCourses-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("individualCandidacyResultBean",
		new DegreeTransferIndividualCandidacyResultBean(getProcess(request)));
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

    private DegreeTransferIndividualCandidacyResultBean getCandidacyResultBean() {
	return getRenderedObject("individualCandidacyResultBean");
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

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
	    IndividualCandidacyProcess process) {
	// TODO Auto-generated method stub

    }

    public ActionForward prepareExecuteChangeProcessCheckedState(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), new DegreeTransferIndividualCandidacyProcessBean(
		getProcess(request)));

	return mapping.findForward("change-process-checked-state");
    }

    public ActionForward prepareExecuteChangePaymentCheckedState(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), new DegreeTransferIndividualCandidacyProcessBean(
		getProcess(request)));

	return mapping.findForward("change-payment-checked-state");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DegreeTransferIndividualCandidacyProcessBean bean = (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

	boolean isValid = hasInvalidViewState();
	if (!isValid) {
	    invalidateDocumentFileRelatedViewStates();
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("fill-candidacy-information");
	}

	/*
	 * 18/07/2009 - Since we step candidacy information form we must copy
	 * some fields
	 */
	bean.copyInformationToCandidacyBean();

	copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());

	return super.createNewProcess(mapping, form, request, response);
    }

}

package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class IndividualCandidacyProcessDA extends CaseHandlingDispatchAction {

    abstract protected Class getParentProcessType();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setParentProcess(request);
	return super.execute(mapping, actionForm, request, response);
    }

    protected void setParentProcess(HttpServletRequest request) {
	final Integer parentProcessId = getIntegerFromRequest(request, "parentProcessId");
	if (parentProcessId != null) {
	    request.setAttribute("parentProcess", rootDomainObject.readProcessByOID(parentProcessId));
	} else {
	    setProcess(request);
	    if (hasProcess(request)) {
		request.setAttribute("parentProcess", getProcess(request).getCandidacyProcess());
	    }
	}
    }

    protected CandidacyProcess getParentProcess(final HttpServletRequest request) {
	return (CandidacyProcess) request.getAttribute("parentProcess");
    }

    protected boolean hasParentProcess(final HttpServletRequest request) {
	return getParentProcess(request) != null;
    }

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setMainCandidacyProcessInformation(request, getParentProcess(request));
	return mapping.findForward("intro");
    }

    @Override
    protected IndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (IndividualCandidacyProcess) super.getProcess(request);
    }

    protected void setMainCandidacyProcessInformation(final HttpServletRequest request, final CandidacyProcess process) {
	request.setAttribute("process", process);
	request.setAttribute("processName", getParentProcessType().getSimpleName());
	request.setAttribute("canCreateProcess", canCreateProcess(getParentProcessType().getName()));
	request.setAttribute("processActivities", process.getAllowedActivities(AccessControl.getUserView()));
	request.setAttribute("canCreateChildProcess", canCreateProcess(getProcessType().getName()));
	request.setAttribute("childProcessName", getProcessType().getSimpleName());
	request.setAttribute("childProcesses", process.getChildProcesses());
	request.setAttribute("executionIntervalId", process.getCandidacyExecutionInterval().getIdInternal());
	request.setAttribute("executionIntervals", ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(process
		.getCandidacyPeriod().getClass()));
    }

    /**
     * Represents the id of the bean used in candidacy pages
     */
    protected String getIndividualCandidacyProcessBeanName() {
	return "individualCandidacyProcessBean";
    }

    protected IndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
	return (IndividualCandidacyProcessBean) getRenderedObject(getIndividualCandidacyProcessBeanName());
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	if (!hasParentProcess(request)) {
	    addActionMessage(request, "error.IndividualCandidacy.invalid.candidacyProcess");
	    return listProcesses(mapping, form, request, response);
	} else {
	    setStartInformation(form, request, response);
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    /**
     * Used by
     * {@link #prepareCreateNewProcess(ActionMapping,ActionForm,HttpServletRequest,HttpServletResponse)}
     * method to init request with necessary information to start creating a new
     * process
     */
    abstract protected void setStartInformation(final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response);

    public ActionForward prepareCreateNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward searchPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

	final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();

	if (!choosePersonBean.hasPerson()) {
	    if (choosePersonBean.isFirstTimeSearch()) {
		final Collection<Person> persons = Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber());
		choosePersonBean.setFirstTimeSearch(false);
		if (showSimilarPersons(choosePersonBean, persons)) {
		    RenderUtils.invalidateViewState();
		    return mapping.findForward("prepare-create-new-process");
		}
	    }
	    bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
		    choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));
	    return mapping.findForward("fill-personal-information");

	} else {
	    bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
	    return mapping.findForward("fill-personal-information");
	}
    }

    protected boolean showSimilarPersons(final ChoosePersonBean choosePersonBean, final Collection<Person> persons) {
	if (!persons.isEmpty()) {
	    return true;
	}
	return !Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(),
		Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty();
    }

    public ActionForward searchAgainPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	bean.getChoosePersonBean().setFirstTimeSearch(true);
	RenderUtils.invalidateViewState();
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward selectPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

	if (!bean.hasChoosenPerson()) {
	    addActionMessage(request, "error.candidacy.must.select.any.person");
	    return mapping.findForward("prepare-create-new-process");
	}

	bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
	bean.removeChoosePersonBean();
	return mapping.findForward("fill-personal-information");
    }

    public ActionForward fillPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
	final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
		choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));
	bean.removeChoosePersonBean();
	return mapping.findForward("fill-personal-information");
    }

    public ActionForward fillPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("fill-personal-information");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    request.setAttribute("process", executeService("CreateNewProcess", getProcessType().getName(),
		    getIndividualCandidacyProcessBean()));
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("fill-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteCandidacyPayment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("prepare-candidacy-payment");
    }

    public ActionForward prepareExecuteCancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("cancel-candidacy");
    }

    public ActionForward executeCancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "CancelCandidacy", null);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return mapping.findForward("cancel-candidacy");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

}

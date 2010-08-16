package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.ExemptPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.RequestPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdRegistrationConclusionBean;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

abstract public class CommonPhdIndividualProgramProcessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final PhdIndividualProgramProcess process = getProcess(request);

	if (process != null) {
	    request.setAttribute("processAlertMessagesToNotify", process.getUnreadedAlertMessagesFor(getLoggedPerson(request)));

	}

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
	RenderUtils.invalidateViewState();

	return forwardToManageProcesses(mapping, request, searchBean);
    }

    private SearchPhdIndividualProgramProcessBean getOrCreateSearchBean(HttpServletRequest request) {
	SearchPhdIndividualProgramProcessBean searchBean = (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");

	if (searchBean == null) {
	    searchBean = initializeSearchBean(request);
	}
	return searchBean;
    }

    protected ActionForward forwardToManageProcesses(ActionMapping mapping, HttpServletRequest request,
	    SearchPhdIndividualProgramProcessBean searchBean) {
	request.setAttribute("searchProcessBean", searchBean);
	request.setAttribute("candidacyCategory", getCandidacyCategory());
	request.setAttribute("seminarCategory", getSeminarCategory());
	request.setAttribute("thesisCategory", getThesisCategory());
	request.setAttribute("concludedThisYearContainer", getConcludedContainer());
	return mapping.findForward("manageProcesses");
    }

    abstract protected PhdInactivePredicateContainer getConcludedContainer();

    abstract protected List<PredicateContainer<?>> getThesisCategory();

    abstract protected List<PredicateContainer<?>> getSeminarCategory();

    abstract protected List<PredicateContainer<?>> getCandidacyCategory();

    abstract protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request);

    public ActionForward viewInactiveProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
	RenderUtils.invalidateViewState();

	request.setAttribute("searchProcessBean", searchBean);
	request.setAttribute("suspendedContainer", PhdInactivePredicateContainer.SUSPENDED);
	request.setAttribute("concludedContainer", PhdInactivePredicateContainer.CONCLUDED);
	request.setAttribute("abolishedContainer", PhdInactivePredicateContainer.ABOLISHED);
	return mapping.findForward("viewInactiveProcesses");
    }

    public ActionForward searchAllProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
	RenderUtils.invalidateViewState();

	List<PhdIndividualProgramProcess> processes;
	try {
	    processes = PhdIndividualProgramProcess.search(searchBean.getPredicates());
	} catch (NumberFormatException ex) {
	    addActionMessage("searchError", request, "error.invalidFormat");
	    return forwardToManageProcesses(mapping, request, searchBean);
	}
	if (processes.isEmpty()) {
	    addActionMessage("searchResults", request, "message.noResults");
	    return forwardToManageProcesses(mapping, request, searchBean);
	}
	if (processes.size() == 1) {
	    request.setAttribute("process", processes.get(0));
	    return mapping.findForward("viewProcess");
	}
	request.setAttribute("searchProcessBean", searchBean);
	request.setAttribute("processes", processes);
	return mapping.findForward("searchResults");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdIndividualProgramProcess process = getProcess(request);
	if (process != null && process.hasRegistration()) {
	    request.setAttribute("registrationConclusionBean", new PhdRegistrationConclusionBean(process.getRegistration()));
	}

	return forwardToViewProcess(mapping, request);
    }

    protected ActionForward forwardToViewProcess(ActionMapping mapping, HttpServletRequest request) {
	request.setAttribute("backMethod", getFromRequest(request, "backMethod"));
	return mapping.findForward("viewProcess");
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	return (PhdIndividualProgramProcess) super.getProcess(request);
    }

    protected DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
	return rootDomainObject.readDegreeCurricularPlanByOID(getIntegerFromRequest(request, "degreeCurricularPlanID"));
    }

    // Alerts Management
    public ActionForward viewAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("alertMessages", getLoggedPerson(request).getPhdAlertMessages());

	return mapping.findForward("viewAlertMessages");
    }

    public ActionForward markAlertMessageAsReaded(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getAlertMessage(request).markAsReaded(getLoggedPerson(request));

	return globalMessagesView(request) ? viewAlertMessages(mapping, form, request, response) : viewProcessAlertMessages(
		mapping, form, request, response);
    }

    private boolean globalMessagesView(HttpServletRequest request) {
	return StringUtils.isEmpty(request.getParameter("global")) || request.getParameter("global").equals("true");
    }

    private PhdAlertMessage getAlertMessage(HttpServletRequest request) {
	return getDomainObject(request, "alertMessageId");
    }

    public ActionForward viewProcessAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("alertMessages", getProcess(request).getAlertMessagesFor(getLoggedPerson(request)));

	return mapping.findForward("viewProcessAlertMessages");
    }

    // End of Alerts Management

    // Request Public Presentation Seminar Comission

    public ActionForward prepareRequestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("requestPublicPresentationSeminarComissionBean", new PublicPresentationSeminarProcessBean());

	return mapping.findForward("requestPublicPresentationSeminarComission");

    }

    public ActionForward prepareRequestPublicPresentationSeminarComissionInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("requestPublicPresentationSeminarComission",
		getRenderedObject("requestPublicPresentationComissionBean"));

	return mapping.findForward("requestPublicPresentationSeminarComission");
    }

    public ActionForward requestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) getRenderedObject("requestPublicPresentationSeminarComissionBean");

	request.setAttribute("requestPublicPresentationSeminarComissionBean", bean);

	return executeActivity(RequestPublicPresentationSeminarComission.class, bean, request, mapping,
		"requestPublicPresentationSeminarComission", "viewProcess");
    }

    public ActionForward prepareExemptPublicPresentationSeminarComission(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("exemptPublicPresentationSeminarComission");
    }

    public ActionForward exemptPublicPresentationSeminarComission(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), ExemptPublicPresentationSeminarComission.class,
		    new PublicPresentationSeminarProcessBean());

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareExemptPublicPresentationSeminarComission(mapping, actionForm, request, response);
	}

	return viewProcess(mapping, actionForm, request, response);
    }

    // End of Request Public Presentation Seminar Comission

    // View curriculum

    public ActionForward viewCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("curriculumFilter", new PhdCurriculumFilterOptions(getProcess(request).getRegistration()));
	return mapping.findForward("viewCurriculum");
    }

    public ActionForward changeViewCurriculumFilterOptions(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("curriculumFilter", getRenderedObject("curriculumFilter"));
	return mapping.findForward("viewCurriculum");
    }

    // End of view curriculum
}

package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ConferenceArticlesBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.CreateIssueBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultEventAssociationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPublicationsManagementDispatchAction extends ResultsManagementAction {

    public ActionForward listPublications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setRequestAttributesToList(request, getLoggedPerson(request));
	return mapping.findForward("ListPublications");
    }

    public ActionForward showPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);

	setRequestAttributes(request, publication);
	return mapping.findForward("ViewEditPublication");
    }

    public ActionForward prepareEditJournal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	ResultPublicationBean publicationBean;
	IViewState userView = RenderUtils.getViewState("publicationBean");
	if (userView != null) {
	    publicationBean = (ResultPublicationBean) userView.getMetaObject().getObject();
	} else {
	    final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
	    publicationBean = ResultPublicationBean.getBeanToEdit(publication);
	}

	request.setAttribute("publicationBean", publicationBean);
	return mapping.findForward("editJournal");
    }

    public ActionForward createJournalToAssociate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	return createJournalWorkFlow(mapping, form, request, response, "editJournal", "ViewEditPublication",
		"editJournal", "EditResultPublication");
    }

    public ActionForward selectJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	if (getFromRequest(request, "new") != null) {
	    ArticleBean bean = (ArticleBean) getRenderedObject("publicationBean");
	    if (bean.getScientificJournal() != null) {
		addActionMessage(request, "label.doNotCreateJournalIsSelected");
		request.setAttribute("publicationBean", bean);
		return mapping.findForward("editJournal");
	    }
	    return createJournalToAssociate(mapping, form, request, response);
	} else {
	    ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject("publicationBean");
	    request.setAttribute("publicationBean", publicationBean);
	    RenderUtils.invalidateViewState();
	    return mapping.findForward("editJournal");
	}
    }

    public ActionForward prepareSelectJournal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject("publicationBean");

	ArticleBean bean = (ArticleBean) publicationBean;
	bean.setScientificJournal(null);
	bean.setJournalIssue(null);
	bean.setScientificJournalName(null);

	request.setAttribute("publicationBean", publicationBean);
	return mapping.findForward("editJournal");
    }

    public ActionForward showResultForOthers(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	ResearchResult result = getResultFromRequest(request);
	if (result instanceof ResearchResultPublication) {
	    setRequestAttributes(request, (ResearchResultPublication) result);
	}
	request.setAttribute("result", result);
	return mapping.findForward("ShowResult");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject(null);

	if (publicationBean == null) {
	    ResultPublicationType type = ResultPublicationType.getDefaultType();
	    publicationBean = ResultPublicationBean.getBeanToCreate(type);

	    publicationBean.setPerson(getLoggedPerson(request));
	}
	request.setAttribute("publicationBean", publicationBean);
	return mapping.findForward("PreparedToCreate");
    }

    public ActionForward createWrapper(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	if (getFromRequest(request, "new") != null) {
	    return createJournal(mapping, form, request, response);
	} else {
	    return create(mapping, form, request, response);
	}
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject("publicationBean");
	ResearchResultPublication publication = null;

	if (getFromRequest(request, "confirm") != null) {
	    try {
		if (bean instanceof ConferenceArticlesBean
			&& !((ConferenceArticlesBean) bean).getCreateEvent()) {
		    bean.setCreateEvent(true);
		    ResultEventAssociationBean eventBean = new ResultEventAssociationBean();
		    request.setAttribute("eventEditionBean", eventBean);
		    request.setAttribute("publicationBean", bean);
		    return mapping.findForward("PreparedToCreate");
		}
		if (bean instanceof ArticleBean) {
		    ArticleBean articleBean = (ArticleBean) bean;
		    if (articleBean.getJournalIssue() == null) {
			bean.setCreateJournal(Boolean.TRUE);
			RenderUtils.invalidateViewState();
			request.setAttribute("publicationBean", bean);
			return mapping.findForward("PreparedToCreate");
		    }
		}
		final Object[] args = { bean };
		publication = (ResearchResultPublication) executeService(request, "CreateResultPublication",
			args);
	    } catch (DomainException ex) {
		addActionMessage(request, ex.getKey());
		request.setAttribute("publicationBean", bean);
		return mapping.findForward("PreparedToCreate");
	    } catch (Exception ex) {
		return listPublications(mapping, form, request, response);
	    }
	} else {
	    return listPublications(mapping, form, request, response);
	}

	request.setAttribute("resultId", publication.getIdInternal());
	setRequestAttributes(request, publication);
	return mapping.findForward("ViewEditPublication");

    }

    private ActionForward createJournalWorkFlow(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response, String forwardOnNextStep,
	    String forwardOnFinish, String forwardOnError, String service) throws FenixFilterException,
	    FenixServiceException {

	final ArticleBean bean = (ArticleBean) getRenderedObject("publicationBean");
	CreateIssueBean issueBean = (CreateIssueBean) getRenderedObject("createMagazine");

	if (issueBean != null) {
	    if (issueBean.getJournal() == null && issueBean.getJournalName() != null) {
		issueBean.setScientificJournalName(issueBean.getJournalName());
		request.setAttribute("createJournal", true);
	    }
	    if (!issueBean.isJournalFormValid()) {
		addActionMessage(request, "label.journalNeedsNameAndLocation");
		request.setAttribute("issueBean", issueBean);
		request.setAttribute("publicationBean", bean);
		return mapping.findForward(forwardOnNextStep);
	    }
	    if (issueBean.getIssueAlreadyChosen()) {
		ResearchResultPublication publication;
		try {
		    Object[] args = { issueBean };
		    JournalIssue issue = (JournalIssue) executeService("CreateJournalIssue", args);
		    ArticleBean articleBean = (ArticleBean) bean;
		    articleBean.setJournalIssue(issue);
		    articleBean.setCreateJournal(false);
		    final Object[] args2 = { bean };
		    publication = (ResearchResultPublication) executeService(request, service, args2);
		} catch (DomainException e) {
		    addActionMessage(request, e.getKey());
		    request.setAttribute("publicationBean", bean);
		    return mapping.findForward(forwardOnError);
		}
		request.setAttribute("resultId", publication.getIdInternal());
		setRequestAttributes(request, publication);
		return mapping.findForward(forwardOnFinish);
	    }
	} else {
	    issueBean = new CreateIssueBean();
	    issueBean.setJournal(bean.getScientificJournal());
	    issueBean.setScientificJournalName(bean.getScientificJournalName());
	}

	request.setAttribute("issueBean", issueBean);
	request.setAttribute("publicationBean", bean);
	return mapping.findForward(forwardOnNextStep);

    }

    private ActionForward changeSpecialIssue(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response, String forwardTo)
	    throws FenixFilterException, FenixServiceException {

	final ArticleBean bean = (ArticleBean) getRenderedObject("publicationBean");
	CreateIssueBean issueBean = (CreateIssueBean) getRenderedObject("createMagazine");

	request.setAttribute("issueBean", issueBean);
	request.setAttribute("publicationBean", bean);
	RenderUtils.invalidateViewState();
	return mapping.findForward(forwardTo);
    }

    public ActionForward changeSpecialIssueInEditon(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	return changeSpecialIssue(mapping, form, request, response, "editJournal");
    }

    public ActionForward changeSpecialIssueInCreation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	return changeSpecialIssue(mapping, form, request, response, "PreparedToCreate");
    }

    public ActionForward createJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	return createJournalWorkFlow(mapping, form, request, response, "PreparedToCreate",
		"ViewEditPublication", "PreparedToCreate", "CreateResultPublication");
    }

    public ActionForward showAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ResearchResultPublication result = getResearchResultPublication(request);
	result = getResearchResultPublication(request);
	setRequestAttributes(request, result);

	request.setAttribute("fileBean", getResultDocumentFileBean(request, result));
	request.setAttribute("unitBean", getResultUnitBean(request, result));

	return mapping.findForward("associatingInCreation");
    }

    private ResultDocumentFileSubmissionBean getResultDocumentFileBean(HttpServletRequest request,
	    ResearchResultPublication result) {
	IViewState viewState = RenderUtils.getViewState("editBean");
	ResultDocumentFileSubmissionBean fileBean = (viewState != null) ? (ResultDocumentFileSubmissionBean) viewState
		.getMetaObject().getObject()
		: new ResultDocumentFileSubmissionBean(result);
	return fileBean;
    }

    private ResultUnitAssociationCreationBean getResultUnitBean(HttpServletRequest request,
	    ResearchResultPublication result) {
	IViewState viewState = RenderUtils.getViewState("unitBean");
	ResultUnitAssociationCreationBean unitBean = (viewState != null) ? (ResultUnitAssociationCreationBean) viewState
		.getMetaObject().getObject()
		: new ResultUnitAssociationCreationBean(result);
	return unitBean;
    }

    private ResearchResultPublication getResearchResultPublication(HttpServletRequest request) {
	ResearchResultPublication result;
	String resultId = request.getParameter("resultId");
	if (resultId != null) {
	    result = (ResearchResultPublication) RootDomainObject.readDomainObjectByOID(ResearchResult.class,
		    Integer.valueOf(resultId));
	    request.setAttribute("resultId", result.getIdInternal());
	} else {
	    result = (ResearchResultPublication) RootDomainObject.readDomainObjectByOID(ResearchResult.class,
		    (Integer) request.getAttribute("resultId"));
	}
	return result;
    }

    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);

	if (bean == null) {
	    ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
	    bean = ResultPublicationBean.getBeanToEdit(publication);
	    bean.setPerson(getLoggedPerson(request));
	}

	request.setAttribute("publicationBean", bean);
	return mapping.findForward("PreparedToEdit");
    }

    public ActionForward editData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);
	ResearchResult publicationChanged = ResearchResult.readByOid(bean.getIdInternal());

	if (getFromRequest(request, "confirm") != null) {

	    try {
		final Object[] args = { bean };
		publicationChanged = (ResearchResultPublication) executeService(request,
			"EditResultPublication", args);
	    } catch (DomainException ex) {
		addActionMessage(request, ex.getMessage());
		request.setAttribute("publicationBean", bean);
		return mapping.findForward("PreparedToEdit");
	    } catch (Exception ex) {
		addActionMessage(request, ex.getMessage());
		return listPublications(mapping, form, request, response);
	    }
	} else {
	    if (publicationChanged instanceof Unstructured)
		return listPublications(mapping, form, request, response);
	}

	request.setAttribute("resultId", publicationChanged.getIdInternal());
	return showPublication(mapping, form, request, response);
    }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
	setRequestAttributes(request, publication);

	request.setAttribute("confirm", "yes");
	return mapping.findForward("PreparedToDelete");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final Integer resultId = getRequestParameterAsInteger(request, "resultId");

	if (getFromRequest(request, "cancel") != null) {
	    final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
	    setRequestAttributes(request, publication);
	    return mapping.findForward("ViewEditPublication");
	}
	if (getFromRequest(request, "confirm") != null) {
	    try {
		final Object[] args = { resultId };
		executeService(request, "DeleteResultPublication", args);
	    } catch (Exception e) {
		addActionMessage(request, e.getMessage());
		return listPublications(mapping, form, request, response);
	    }
	}

	return mapping.findForward("PublicationDeleted");
    }

    public ActionForward changeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject("publicationBean");

	if (bean != null) {
	    ResultPublicationType type = bean.getPublicationType();
	    if (type != null) {
		bean = bean.convertTo(type);
		if (bean.getIdInternal() != null) {
		    final ResearchResultPublication result = (ResearchResultPublication) ResearchResult
			    .readByOid(bean.getIdInternal());
		    if (result != null) {
			if (!(ResultPublicationType.getTypeFromPublication(result) == type)) {
			    if (result.hasAnyResultDocumentFiles()) {
				request.setAttribute("typeChanged", "true");
			    }
			} else {
			    bean = ResultPublicationBean.getBeanToEdit(result);
			    bean.setPerson(getLoggedPerson(request));
			    request.setAttribute("typeChanged", "false");
			}
		    }
		}
	    }
	}

	RenderUtils.invalidateViewState();

	request.setAttribute("publicationBean", bean);
	if (bean != null && bean.getIdInternal() != null) {
	    return mapping.findForward("PreparedToEdit");
	}
	return mapping.findForward("PreparedToCreate");
    }

    public ActionForward prepareCreateEvent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ResultEventAssociationBean eventBean = (ResultEventAssociationBean) getRenderedObject("eventEditionBean");
	ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject("publicationBean");
	request.setAttribute("eventEditionBean", eventBean);
	request.setAttribute("publicationBean", publicationBean);
	return mapping.findForward("PreparedToCreate");
    }

    public ActionForward createEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ResultEventAssociationBean eventBean = (ResultEventAssociationBean) getRenderedObject("eventEditionBean");
	RenderUtils.invalidateViewState("eventEditionBean");
	ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject("publicationBean");

	return createEventWorkFlow(mapping, form, request, response, eventBean, publicationBean,
		"PreparedToCreate", "ViewEditPublication", "PreparedToCreate", "CreateResultPublication");
    }

    public ActionForward createEventWorkFlow(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response, ResultEventAssociationBean eventBean,
	    ResultPublicationBean publicationBean, String forwardOnNextStep, String forwardOnFinish,
	    String forwardOnError, String service) {

	if (isCurrentState(request, "goToNextStep")) {
	    if (eventBean.getNewEventState() || eventBean.getNewEventEditionState()
		    || eventBean.getSelectEventEditionState()) {
		return createEditPublication(mapping, form, request, response, eventBean, publicationBean,
			forwardOnFinish, forwardOnError, service);
	    } else {
		eventBean.setNextStepBeanState();
	    }
	} else if (isCurrentState(request, "createNewEventEdition")) {
	    eventBean.setNewEventEditionBeanState();
	} else if (isCurrentState(request, "createNewEvent")) {
	    eventBean.setNewEventBeanState();
	} else {
	    return listPublications(mapping, form, request, response);
	}

	request.setAttribute("eventEditionBean", eventBean);
	request.setAttribute("publicationBean", publicationBean);
	return mapping.findForward(forwardOnNextStep);
    }

    public ActionForward createEditPublication(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response, ResultEventAssociationBean eventBean,
	    ResultPublicationBean publicationBean, String forwardOnFinish, String forwardOnError,
	    String service) {
	ResearchResultPublication publication = null;

	try {
	    final Object[] args = { eventBean };
	    EventEdition eventEdition = (eventBean.getEventEdition() == null ? (EventEdition) executeService(
		    request, "CreateResearchEventEdition", args) : eventBean.getEventEdition());
	    ((ConferenceArticlesBean) publicationBean).setEventEdition(eventEdition);
	    final Object[] args2 = { publicationBean };
	    publication = (ResearchResultPublication) executeService(request, service, args2);
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("eventEditionBean", eventBean);
	    request.setAttribute("publicationBean", publicationBean);
	    return mapping.findForward(forwardOnError);
	} catch (Exception ex) {
	    return listPublications(mapping, form, request, response);
	}

	request.setAttribute("resultId", publication.getIdInternal());
	setRequestAttributes(request, publication);
	return mapping.findForward(forwardOnFinish);
    }

    public ActionForward prepareEditEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ResultPublicationBean publicationBean;
	IViewState userView = RenderUtils.getViewState("publicationBean");
	if (userView != null) {
	    publicationBean = (ResultPublicationBean) userView.getMetaObject().getObject();
	} else {
	    final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
	    publicationBean = ResultPublicationBean.getBeanToEdit(publication);
	}

	request.setAttribute("publicationBean", publicationBean);
	return mapping.findForward("editEvent");
    }

    public ActionForward prepareSelectEventToAssociate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject("publicationBean");
	((ConferenceArticlesBean) publicationBean).setEvent(null);
	((ConferenceArticlesBean) publicationBean).setEventEdition(null);

	ResultEventAssociationBean eventBean = new ResultEventAssociationBean();

	return createEventWorkFlow(mapping, form, request, response, eventBean, publicationBean, "editEvent",
		null, null, null);
    }

    public ActionForward prepareCreateEventToAssociate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	ResultEventAssociationBean eventBean = (ResultEventAssociationBean) getRenderedObject("eventEditionBean");
	ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject("publicationBean");
	request.setAttribute("eventEditionBean", eventBean);
	request.setAttribute("publicationBean", publicationBean);
	return mapping.findForward("editEvent");
    }

    public ActionForward createEventToAssociate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject("publicationBean");
	ResultEventAssociationBean eventBean = (ResultEventAssociationBean) getRenderedObject("eventEditionBean");
	RenderUtils.invalidateViewState("eventEditionBean");

	if (eventBean == null) {
	    eventBean = new ResultEventAssociationBean();
	    if (((ConferenceArticlesBean) publicationBean).getEvent() != null) {
		eventBean.setEventEdition(((ConferenceArticlesBean) publicationBean).getEventEdition());
		eventBean.setEvent(((ConferenceArticlesBean) publicationBean).getEvent());
		eventBean.setEventAlreadyChosen(true);
	    }
	}

	return createEventWorkFlow(mapping, form, request, response, eventBean, publicationBean, "editEvent",
		"ViewEditPublication", "editEvent", "EditResultPublication");
    }

    /**
         * Auxiliary methods
         */
    private boolean isCurrentState(HttpServletRequest request, String state) {
	return (getFromRequest(request, state) != null);
    }

    private void setRequestAttributes(HttpServletRequest request, ResearchResultPublication publication) {
	request.setAttribute("result", publication);
	if (publication instanceof Unstructured)
	    request.setAttribute("resultPublicationType", "Unstructured");
	else
	    request.setAttribute("resultPublicationType", ResultPublicationType
		    .getTypeFromPublication(publication));

	if (publication.getIsPossibleSelectPersonRole()) {
	    request.setAttribute("participationsSchema", "resultParticipation.full");
	}
    }

    private void setRequestAttributesToList(HttpServletRequest request, Person person) {

	request.setAttribute("books", ResearchResultPublication.sort(person.getBooks()));
	request.setAttribute("national-articles", ResearchResultPublication.sort(person
		.getArticles(ScopeType.NATIONAL)));
	request.setAttribute("international-articles", ResearchResultPublication.sort(person
		.getArticles(ScopeType.INTERNATIONAL)));
	request.setAttribute("national-inproceedings", ResearchResultPublication.sort(person
		.getInproceedings(ScopeType.NATIONAL)));
	request.setAttribute("international-inproceedings", ResearchResultPublication.sort(person
		.getInproceedings(ScopeType.INTERNATIONAL)));
	request.setAttribute("proceedings", ResearchResultPublication.sort(person.getProceedings()));
	request.setAttribute("theses", ResearchResultPublication.sort(person.getTheses()));
	request.setAttribute("manuals", ResearchResultPublication.sort(person.getManuals()));
	request
		.setAttribute("technicalReports", ResearchResultPublication
			.sort(person.getTechnicalReports()));
	request.setAttribute("otherPublications", ResearchResultPublication.sort(person
		.getOtherPublications()));
	request.setAttribute("unstructureds", ResearchResultPublication.sort(person.getUnstructureds()));
	request.setAttribute("inbooks", ResearchResultPublication.sort(person.getInbooks()));

	request.setAttribute("person", getLoggedPerson(request));
    }

    // TODO: Verifiy if this method is necessary
    /*
         * private ResultPublicationType getTypeFromRequest(HttpServletRequest
         * request) { final String typeStr = (String) getFromRequest(request,
         * "publicationType"); ResultPublicationType type = null; if (typeStr !=
         * null) { type = ResultPublicationType.valueOf(typeStr); } return type; }
         */

}
package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateJournalIssue;
import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateResearchEventEdition;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.CreateResultUnitAssociation;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication.CreateResultPublication;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication.DeleteResultPublication;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication.EditResultPublication;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearIntervalBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ConferenceArticlesBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.CreateIssueBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultEventAssociationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.file.FileManagerException;

public class ResultPublicationsManagementDispatchAction extends ResultsManagementAction {

    public ActionForward listPublications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        if (request.getParameter("unitId") != null) {
            return mapping.findForward("ListUnitPublications");
        } else {
            setRequestAttributesToList(request, getLoggedPerson(request));
            return mapping.findForward("ListPublications");
        }
    }

    public ActionForward goBackFromDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("backFromDetails");
    }

    public ActionForward showPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);

        setRequestAttributes(request, publication);
        return mapping.findForward("ViewEditPublication");
    }

    public ActionForward prepareEditJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
        return mapping.findForward("editJournal");
    }

    public ActionForward createJournalToAssociate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return createJournalWorkFlow(mapping, form, request, response, "editJournal", "ViewEditPublication", "editJournal", false);
    }

    public ActionForward selectJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        if (getFromRequest(request, "new") != null) {
            ArticleBean bean = getRenderedObject("publicationBean");
            if (bean.getScientificJournal() != null) {
                addActionMessage(request, "label.doNotCreateJournalIsSelected");
                request.setAttribute("publicationBean", bean);
                return mapping.findForward("editJournal");
            }
            return createJournalToAssociate(mapping, form, request, response);
        } else {
            ResultPublicationBean publicationBean = getRenderedObject("publicationBean");
            request.setAttribute("publicationBean", publicationBean);
            RenderUtils.invalidateViewState();
            return mapping.findForward("editJournal");
        }
    }

    public ActionForward prepareSelectJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ResultPublicationBean publicationBean = getRenderedObject("publicationBean");

        ArticleBean bean = (ArticleBean) publicationBean;
        bean.setScientificJournal(null);
        bean.setJournalIssue(null);
        bean.setScientificJournalName(null);

        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("editJournal");
    }

    public ActionForward showResultForOthers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ResearchResult result = getResultFromRequest(request);
        if (result instanceof ResearchResultPublication) {
            setRequestAttributes(request, (ResearchResultPublication) result);
        }
        request.setAttribute("result", result);
        return mapping.findForward("ShowResult");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResultPublicationBean publicationBean = getRenderedObject(null);

        if (publicationBean == null) {
            ResultPublicationType type = ResultPublicationType.getDefaultType();
            publicationBean = ResultPublicationBean.getBeanToCreate(type);

            publicationBean.setPerson(getLoggedPerson(request));
        }
        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("PreparedToCreate");
    }

    public ActionForward createWrapper(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        if (getFromRequest(request, "new") != null) {
            return createJournal(mapping, form, request, response);
        } else {
            return create(mapping, form, request, response);
        }
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final ResultPublicationBean bean = getRenderedObject("publicationBean");
        ResearchResultPublication publication = null;

        try {
            if (bean instanceof ConferenceArticlesBean && !((ConferenceArticlesBean) bean).getCreateEvent()) {
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
            publication = CreateResultPublication.runCreateResultPublication(bean);
            if (bean.getUnit() != null) {
                request.setAttribute("unit", bean.getUnit());
            }
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            request.setAttribute("publicationBean", bean);
            return mapping.findForward("PreparedToCreate");
        } catch (FileManagerException e) {
            e.printStackTrace();
            addActionMessage(request, "label.communicationError");
            return listPublications(mapping, form, request, response);
        } catch (Exception ex) {
            addActionMessage(request, ex.getMessage());
            return listPublications(mapping, form, request, response);
        }

        request.setAttribute("resultId", publication.getExternalId());
        setRequestAttributes(request, publication);
        request.setAttribute("publicationCreated", true);
        return mapping.findForward("forwardToUnitAssociation");

    }

    private ActionForward createJournalWorkFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forwardOnNextStep, String forwardOnFinish, String forwardOnError, boolean create)
            throws FenixServiceException {

        final ArticleBean bean = getRenderedObject("publicationBean");
        CreateIssueBean issueBean = getRenderedObject("createMagazine");

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

                    JournalIssue issue = CreateJournalIssue.run(issueBean);
                    ArticleBean articleBean = bean;
                    articleBean.setJournalIssue(issue);
                    articleBean.setCreateJournal(false);
                    final Object[] args2 = { bean };
                    if (create) {
                        publication = CreateResultPublication.runCreateResultPublication(bean);
                    } else {
                        publication = EditResultPublication.runEditResultPublication(bean);
                    }
                    if (bean.getUnit() != null) {
                        request.setAttribute("unit", bean.getUnit());
                    }
                } catch (DomainException e) {
                    addActionMessage(request, e.getKey());
                    request.setAttribute("publicationBean", bean);
                    return mapping.findForward(forwardOnError);
                }
                request.setAttribute("resultId", publication.getExternalId());
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

    private ActionForward changeSpecialIssue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forwardTo) throws FenixServiceException {

        final ArticleBean bean = getRenderedObject("publicationBean");
        CreateIssueBean issueBean = getRenderedObject("createMagazine");

        request.setAttribute("issueBean", issueBean);
        request.setAttribute("publicationBean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward(forwardTo);
    }

    public ActionForward changeSpecialIssueInEditon(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return changeSpecialIssue(mapping, form, request, response, "editJournal");
    }

    public ActionForward changeSpecialIssueInCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return changeSpecialIssue(mapping, form, request, response, "PreparedToCreate");
    }

    public ActionForward createJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return createJournalWorkFlow(mapping, form, request, response, "PreparedToCreate", "ViewEditPublication",
                "PreparedToCreate", true);
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
        ResultDocumentFileSubmissionBean fileBean =
                (viewState != null) ? (ResultDocumentFileSubmissionBean) viewState.getMetaObject().getObject() : new ResultDocumentFileSubmissionBean(
                        result);
        return fileBean;
    }

    private ResultUnitAssociationCreationBean getResultUnitBean(HttpServletRequest request, ResearchResultPublication result) {
        IViewState viewState = RenderUtils.getViewState("unitBean");
        ResultUnitAssociationCreationBean unitBean =
                (viewState != null) ? (ResultUnitAssociationCreationBean) viewState.getMetaObject().getObject() : new ResultUnitAssociationCreationBean(
                        result);
        return unitBean;
    }

    private ResearchResultPublication getResearchResultPublication(HttpServletRequest request) {
        ResearchResultPublication result;
        String resultId = request.getParameter("resultId");
        if (resultId != null) {
            result = (ResearchResultPublication) AbstractDomainObject.fromExternalId(resultId);
            request.setAttribute("resultId", result.getExternalId());
        } else {
            result = (ResearchResultPublication) AbstractDomainObject.fromExternalId((String) request.getAttribute("resultId"));
        }
        return result;
    }

    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ResultPublicationBean bean = getRenderedObject(null);

        if (bean == null) {
            ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
            bean = ResultPublicationBean.getBeanToEdit(publication);
            bean.setPerson(getLoggedPerson(request));
        }

        request.setAttribute("publicationBean", bean);
        return mapping.findForward("PreparedToEdit");
    }

    public ActionForward editData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final ResultPublicationBean bean = getRenderedObject(null);
        ResearchResult publicationChanged = ResearchResult.readByOid(bean.getExternalId());

        if (getFromRequest(request, "confirm") != null) {

            try {
                publicationChanged = EditResultPublication.runEditResultPublication(bean);
            } catch (DomainException ex) {
                addActionMessage(request, ex.getMessage());
                request.setAttribute("publicationBean", bean);
                return mapping.findForward("PreparedToEdit");
            } catch (FileManagerException e) {
                e.printStackTrace();
                addActionMessage(request, "label.communicationError");
                return listPublications(mapping, form, request, response);
            } catch (Exception e) {
                addActionMessage(request, e.getMessage());
                return listPublications(mapping, form, request, response);
            }
        } else {
            if (publicationChanged instanceof Unstructured) {
                return listPublications(mapping, form, request, response);
            }
        }

        request.setAttribute("resultId", publicationChanged.getExternalId());
        bean.setExternalId(publicationChanged.getExternalId());
        return showPublication(mapping, form, request, response);
    }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
        setRequestAttributes(request, publication);

        request.setAttribute("confirm", "yes");
        return mapping.findForward("PreparedToDelete");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final String resultId = getRequestParameterAsString(request, "resultId");

        if (getFromRequest(request, "cancel") != null) {
            final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
            setRequestAttributes(request, publication);
            return mapping.findForward("ViewEditPublication");
        }
        if (getFromRequest(request, "confirm") != null) {
            try {

                DeleteResultPublication.run(resultId);
            } catch (FileManagerException e) {
                e.printStackTrace();
                addActionMessage(request, "label.communicationError");
                return listPublications(mapping, form, request, response);
            } catch (Exception e) {
                addActionMessage(request, e.getMessage());
                return listPublications(mapping, form, request, response);
            }
        }

        return mapping.findForward("PublicationDeleted");
    }

    public ActionForward changeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResultPublicationBean bean = getRenderedObject("publicationBean");

        if (bean != null) {
            ResultPublicationType type = bean.getPublicationType();
            if (type != null) {
                bean = bean.convertTo(type);
                if (bean.getExternalId() != null) {
                    final ResearchResultPublication result =
                            (ResearchResultPublication) ResearchResult.readByOid(bean.getExternalId());
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
        if (bean != null && bean.getExternalId() != null) {
            return mapping.findForward("PreparedToEdit");
        }
        return mapping.findForward("PreparedToCreate");
    }

    public ActionForward prepareCreateEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ResultEventAssociationBean eventBean = getRenderedObject("eventEditionBean");
        ResultPublicationBean publicationBean = getRenderedObject("publicationBean");
        request.setAttribute("eventEditionBean", eventBean);
        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("PreparedToCreate");
    }

    public ActionForward createEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ResultEventAssociationBean eventBean = getRenderedObject("eventEditionBean");
        RenderUtils.invalidateViewState("eventEditionBean");
        ResultPublicationBean publicationBean = getRenderedObject("publicationBean");

        return createEventWorkFlow(mapping, form, request, response, eventBean, publicationBean, "PreparedToCreate",
                "ViewEditPublication", "PreparedToCreate", "CreateResultPublication");
    }

    public ActionForward createEventWorkFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, ResultEventAssociationBean eventBean, ResultPublicationBean publicationBean,
            String forwardOnNextStep, String forwardOnFinish, String forwardOnError, String service) {

        if (isCurrentState(request, "goToNextStep")) {
            if (eventBean.getNewEventState() || eventBean.getNewEventEditionState() || eventBean.getSelectEventEditionState()) {
                return createEditPublication(mapping, form, request, response, eventBean, publicationBean, forwardOnFinish,
                        forwardOnError, service);
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

    public ActionForward createEditPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, ResultEventAssociationBean eventBean, ResultPublicationBean publicationBean,
            String forwardOnFinish, String forwardOnError, String service) {
        ResearchResultPublication publication = null;

        try {

            EventEdition eventEdition =
                    (eventBean.getEventEdition() == null ? (EventEdition) CreateResearchEventEdition.run(eventBean) : eventBean
                            .getEventEdition());
            ((ConferenceArticlesBean) publicationBean).setEventEdition(eventEdition);
            final Object[] args2 = { publicationBean };

            if ("CreateResultPublication".equals(service)) {
                publication = CreateResultPublication.runCreateResultPublication(publicationBean);
            } else if ("EditResultPublication".equals(service)) {
                publication = EditResultPublication.runEditResultPublication(publicationBean);
            }

            if (publicationBean.getUnit() != null) {
                request.setAttribute("unit", publicationBean.getUnit());
            }
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            request.setAttribute("eventEditionBean", eventBean);
            request.setAttribute("publicationBean", publicationBean);
            return mapping.findForward(forwardOnError);
        } catch (FileManagerException e) {
            e.printStackTrace();
            addActionMessage(request, "label.communicationError");
            return listPublications(mapping, form, request, response);
        } catch (Exception e) {
            addActionMessage(request, e.getMessage());
            return listPublications(mapping, form, request, response);
        }

        request.setAttribute("resultId", publication.getExternalId());
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
        ResultEventAssociationBean eventBean = new ResultEventAssociationBean();
        request.setAttribute("eventEditionBean", eventBean);
        return mapping.findForward("editEvent");
    }

    public ActionForward prepareSelectEventToAssociate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResultPublicationBean publicationBean = getRenderedObject("publicationBean");
        ((ConferenceArticlesBean) publicationBean).setEvent(null);
        ((ConferenceArticlesBean) publicationBean).setEventEdition(null);

        ResultEventAssociationBean eventBean = new ResultEventAssociationBean();

        return createEventWorkFlow(mapping, form, request, response, eventBean, publicationBean, "editEvent", null, null, null);
    }

    public ActionForward prepareCreateEventToAssociate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResultEventAssociationBean eventBean = getRenderedObject("eventEditionBean");
        ResultPublicationBean publicationBean = getRenderedObject("publicationBean");
        request.setAttribute("eventEditionBean", eventBean);
        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("editEvent");
    }

    public ActionForward createEventToAssociate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResultPublicationBean publicationBean = getRenderedObject("publicationBean");
        ResultEventAssociationBean eventBean = getRenderedObject("eventEditionBean");
        RenderUtils.invalidateViewState("eventEditionBean");

        if (eventBean == null) {
            eventBean = new ResultEventAssociationBean();
            if (((ConferenceArticlesBean) publicationBean).getEvent() != null) {
                eventBean.setEventEdition(((ConferenceArticlesBean) publicationBean).getEventEdition());
                eventBean.setEvent(((ConferenceArticlesBean) publicationBean).getEvent());
            }
        }

        return createEventWorkFlow(mapping, form, request, response, eventBean, publicationBean, "editEvent",
                "ViewEditPublication", "editEvent", "EditResultPublication");
    }

    public ActionForward prepareSetPreferredPublications(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ExecutionYear first = AbstractDomainObject.fromExternalId(request.getParameter("firstOID"));
        ExecutionYear last = AbstractDomainObject.fromExternalId(request.getParameter("lastOID"));

        setRequestAttributesToList(request, getLoggedPerson(request), first, last);
        request.setAttribute("first", first);
        request.setAttribute("last", last);
        request.setAttribute("preferredSetting", true);

        RenderUtils.invalidateViewState();
        return mapping.findForward("ListPublications");
    }

    public ActionForward setUnitToAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Person person = getLoggedPerson(request);
        request.setAttribute("personId", getLoggedPerson(request).getExternalId());
        request.setAttribute("units", getUnits(person));
        return mapping.findForward("setUnitToAll");
    }

    private Collection<Unit> getUnits(Person person) {
        Set<Unit> units = new HashSet<Unit>();
        for (ResearchUnit unit : person.getWorkingResearchUnits()) {
            units.add(unit);
        }
        if (person.hasEmployee() && person.getEmployee().getCurrentWorkingPlace() != null) {
            units.add(person.getEmployee().getCurrentWorkingPlace());
        }
        if (person.hasTeacher()) {
            for (ExecutionCourse course : person.getTeacher().getCurrentExecutionCourses()) {
                for (Degree degree : course.getDegreesSortedByDegreeName()) {
                    if (degree.getUnit() != null) {
                        units.add(degree.getUnit());
                    }
                    for (CurricularCourse curricularCourse : course.getAssociatedCurricularCourses()) {
                        CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
                        CompetenceCourseGroupUnit groupUnit =
                                (competenceCourse != null) ? competenceCourse.getCompetenceCourseGroupUnit() : null;
                        if (groupUnit != null) {
                            Collection<? extends Party> scientificAreaUnits =
                                    groupUnit.getParentParties(ScientificAreaUnit.class);
                            for (Party scientificAreaUnit : scientificAreaUnits) {
                                units.add((ScientificAreaUnit) scientificAreaUnit);
                            }
                        }
                    }

                }
            }
        }
        return units;
    }

    public ActionForward addUnitToAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Person person = getLoggedPerson(request);
        Unit unit = (Unit) AbstractDomainObject.fromExternalId(request.getParameter("unitID"));
        for (ResearchResultPublication publication : getLoggedPerson(request).getResearchResultPublications()) {
            if (publication.getClass().getSimpleName().equalsIgnoreCase("unstructured") == false) {
                ResultUnitAssociationCreationBean bean = new ResultUnitAssociationCreationBean(publication);
                bean.setSuggestion(false);
                bean.setUnit(unit);
                try {
                    CreateResultUnitAssociation.run(bean);
                } catch (FileManagerException e) {
                    e.printStackTrace();
                    addActionMessage(request, "label.communicationError");
                } catch (Exception e) {
                    addActionMessage(request, e.getMessage());
                }
            }
        }
        addActionMessage(request, "message.confirm.add.unit.to.all.publications.successful");
        request.setAttribute("personId", person.getExternalId());
        request.setAttribute("units", getUnits(person));
        return mapping.findForward("setUnitToAll");
    }

    /**
     * Auxiliary methods
     */
    private boolean isCurrentState(HttpServletRequest request, String state) {
        return (getFromRequest(request, state) != null);
    }

    private void setRequestAttributes(HttpServletRequest request, ResearchResultPublication publication) {
        request.setAttribute("result", publication);
        if (publication instanceof Unstructured) {
            request.setAttribute("resultPublicationType", "Unstructured");
        } else {
            request.setAttribute("resultPublicationType", ResultPublicationType.getTypeFromPublication(publication));
        }

        if (publication.getIsPossibleSelectPersonRole()) {
            request.setAttribute("participationsSchema", "resultParticipation.full");
        }
    }

    private void setRequestAttributesToList(HttpServletRequest request, Person person, ExecutionYear firstExecutionYear,
            ExecutionYear finalExecutionYear) {
        request.setAttribute("books", ResearchResultPublication.sort(person.getBooks(firstExecutionYear, finalExecutionYear)));
        request.setAttribute("national-articles",
                ResearchResultPublication.sort(person.getArticles(ScopeType.NATIONAL, firstExecutionYear, finalExecutionYear)));
        request.setAttribute("international-articles", ResearchResultPublication.sort(person.getArticles(ScopeType.INTERNATIONAL,
                firstExecutionYear, finalExecutionYear)));
        request.setAttribute("national-inproceedings", ResearchResultPublication.sort(person.getInproceedings(ScopeType.NATIONAL,
                firstExecutionYear, finalExecutionYear)));
        request.setAttribute("international-inproceedings", ResearchResultPublication.sort(person.getInproceedings(
                ScopeType.INTERNATIONAL, firstExecutionYear, finalExecutionYear)));
        request.setAttribute("proceedings",
                ResearchResultPublication.sort(person.getProceedings(firstExecutionYear, finalExecutionYear)));
        request.setAttribute("theses", ResearchResultPublication.sort(person.getTheses(firstExecutionYear, finalExecutionYear)));
        request.setAttribute("manuals", ResearchResultPublication.sort(person.getManuals(firstExecutionYear, finalExecutionYear)));
        request.setAttribute("technicalReports",
                ResearchResultPublication.sort(person.getTechnicalReports(firstExecutionYear, finalExecutionYear)));
        request.setAttribute("otherPublications",
                ResearchResultPublication.sort(person.getOtherPublications(firstExecutionYear, finalExecutionYear)));
        request.setAttribute("unstructureds", ResearchResultPublication.sort(person.getUnstructureds()));
        request.setAttribute("inbooks", ResearchResultPublication.sort(person.getInbooks(firstExecutionYear, finalExecutionYear)));

        request.setAttribute("person", getLoggedPerson(request));
    }

    private void setRequestAttributesToList(HttpServletRequest request, Person person) {

        ExecutionYearIntervalBean bean = getRenderedObject("executionYearIntervalBean");

        if (bean == null) {
            bean = new ExecutionYearIntervalBean();
        }
        ExecutionYear firstExecutionYear = bean.getFirstExecutionYear();
        ExecutionYear finalExecutionYear = bean.getFinalExecutionYear();

        ExecutionYear firstOfAll = ExecutionYear.readFirstExecutionYear();

        if (firstExecutionYear == null) {
            firstExecutionYear = firstOfAll;
        }
        if (finalExecutionYear == null || finalExecutionYear.isBefore(firstExecutionYear)) {
            finalExecutionYear = ExecutionYear.readLastExecutionYear();
        }

        if (person.getResearchResultPublications().size() > 100
                && (finalExecutionYear.getBeginCivilYear() - firstExecutionYear.getBeginCivilYear()) > 5
                && firstExecutionYear == firstOfAll) {
            firstExecutionYear =
                    finalExecutionYear.getPreviousExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
                            .getPreviousExecutionYear().getPreviousExecutionYear();
        }

        bean.setFinalExecutionYear(finalExecutionYear);
        bean.setFirstExecutionYear(firstExecutionYear);

        request.setAttribute("executionYearIntervalBean", bean);
        setRequestAttributesToList(request, person, firstExecutionYear, finalExecutionYear);

    }

    // TODO: Verifiy if this method is necessary
    /*
     * private ResultPublicationType getTypeFromRequest(HttpServletRequest
     * request) { final String typeStr = (String) getFromRequest(request,
     * "publicationType"); ResultPublicationType type = null; if (typeStr !=
     * null) { type = ResultPublicationType.valueOf(typeStr); } return type; }
     */

}
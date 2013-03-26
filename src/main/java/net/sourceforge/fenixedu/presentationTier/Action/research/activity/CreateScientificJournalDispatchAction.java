package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateResearchActivityParticipation;
import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateScientificJournal;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchScientificJournalCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/activities/createScientificJournal", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "CreateScientificJournal", path = "/researcher/activities/createScientificJournalParticipation.jsp",
                tileProperties = @Tile(
                        title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.activities")),
        @Forward(name = "Success", path = "/activities/activitiesManagement.do?method=listActivities", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.activities")) })
public class CreateScientificJournalDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute("party", getLoggedPerson(request));
        return mapping.findForward("CreateScientificJournal");
    }

    public ActionForward prepareJournalSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchScientificJournalCreationBean bean = getJournalBean(request);
        if (bean == null) {
            bean = new ResearchScientificJournalCreationBean();
        }

        request.setAttribute("journalBean", bean);
        request.setAttribute("journalCreationSchema", "scientificJournalCreation.journalName");

        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareCreateScientificJournalParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ResearchScientificJournalCreationBean bean = getJournalBean(request);
        if (bean == null) {
            return prepareJournalSearch(mapping, form, request, response);
        }

        if (bean.getScientificJournal() != null) {
            request.setAttribute("existentJournalBean", bean);
            request.setAttribute("journalCreationSchema", "journalCreation.existentJournal");
            return prepare(mapping, form, request, response);
        } else {
            request.setAttribute("inexistentJournalBean", bean);
            request.setAttribute("journalCreationSchema", "journalCreation.inexistentJournal");
            return prepare(mapping, form, request, response);
        }
    }

    public ActionForward createExistentJournalParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        ResearchScientificJournalCreationBean bean = getJournalBean(request);
        if (bean == null) {
            return prepareJournalSearch(mapping, form, request, response);
        }

        if (bean.getRole() != null) {
            try {
                CreateResearchActivityParticipation.run(bean.getScientificJournal(), bean.getRole(), person,
                        bean.getRoleMessage(), bean.getBeginDate(), bean.getEndDate());
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
                request.setAttribute("existentJournalBean", bean);
                request.setAttribute("journalCreationSchema", "journalCreation.existentJournal");
                return prepare(mapping, form, request, response);
            }
        }

        return mapping.findForward("Success");
    }

    public ActionForward createInexistentJournalParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);

        ResearchScientificJournalCreationBean bean = getJournalBean(request);
        if (bean == null) {
            return prepareJournalSearch(mapping, form, request, response);
        }

        ScientificJournal journal = null;
        try {
            journal =
                    CreateScientificJournal.run(bean.getScientificJournalName(), (bean.getIssn() != null ? bean.getIssn() : ""),
                            bean.getPublisher(), bean.getLocationType());
            CreateResearchActivityParticipation.run(journal, bean.getRole(), person, bean.getRoleMessage(), bean.getBeginDate(),
                    bean.getEndDate());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("inexistentJournalBean", bean);
            request.setAttribute("journalCreationSchema", "journalCreation.inexistentJournal");
            return prepare(mapping, form, request, response);
        }

        return mapping.findForward("Success");
    }

    public ResearchScientificJournalCreationBean getJournalBean(HttpServletRequest request) {
        ResearchScientificJournalCreationBean bean = null;
        if (RenderUtils.getViewState() != null) {
            bean = (ResearchScientificJournalCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            return bean;
        }
        return bean;
    }

}
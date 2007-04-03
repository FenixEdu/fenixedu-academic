package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchJournalIssueCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.CreateIssueBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import sun.security.x509.IssuerAlternativeNameExtension;

public class CreateJournalIssueDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("party", getLoggedPerson(request));
	return mapping.findForward("CreateIssue");
    }

    public ActionForward prepareJournalIssueSearch(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ResearchJournalIssueCreationBean bean = new ResearchJournalIssueCreationBean();
	request.setAttribute("bean", bean);

	return prepare(mapping, form, request, response);
    }

    public ActionForward createPostback(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ResearchJournalIssueCreationBean bean = (ResearchJournalIssueCreationBean) RenderUtils.getViewState(
		"name").getMetaObject().getObject();
	CreateIssueBean issueBean = (CreateIssueBean) RenderUtils.getViewState("issueBean").getMetaObject()
		.getObject();
	request.setAttribute("newJournal", "true");
	request.setAttribute("issueBean", issueBean);
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState();
	return prepare(mapping, form, request, response);
    }

    public ActionForward createScientificJournal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ResearchJournalIssueCreationBean bean = (ResearchJournalIssueCreationBean) RenderUtils.getViewState(
		"name").getMetaObject().getObject();
	CreateIssueBean issueBean = (CreateIssueBean) RenderUtils.getViewState("issueBean").getMetaObject()
		.getObject();
	if (!issueBean.isJournalFormValid()) {
	    request.setAttribute("newJournal", "true");
	    request.setAttribute("issueBean", issueBean);

	} else {
	    JournalIssue issue = (JournalIssue) executeService("CreateJournalIssue",
		    new Object[] { issueBean });
	    bean.setJournalIssue(issue);
	    bean.setScientificJournal(issue.getScientificJournal());
	}
	request.setAttribute("bean", bean);
	return prepare(mapping, form, request, response);
    }

    public ActionForward prepareJournalIssueParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ResearchJournalIssueCreationBean bean = getJournalIssueBean(request);
	if (bean == null) {
	    return prepareJournalIssueSearch(mapping, form, request, response);
	}
	if (bean.getJournalIssue() != null) {
	    return createJournalIssueParticipation(mapping, form, request, response);
	}
	if (request.getParameter("prepareCreateNewJournal") != null) {
	    request.setAttribute("newJournal", "true");
	    CreateIssueBean issueBean = new CreateIssueBean();
	    issueBean.setScientificJournalName(bean.getScientificJournalName());
	    request.setAttribute("issueBean", issueBean);
	}
	if (request.getParameter("createNewJournal") != null) {
	    return createScientificJournal(mapping, form, request, response);
	}
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState();
	return prepare(mapping, form, request, response);

    }

    public ActionForward createJournalIssueParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getLoggedPerson(request);
	ResearchJournalIssueCreationBean bean = getJournalIssueBean(request);
	if (bean == null)
	    return prepareJournalIssueSearch(mapping, form, request, response);

	if (bean.getRole() != null) {
	    try {
		executeService(request, "CreateResearchActivityParticipation", new Object[] {
			bean.getJournalIssue(), bean.getRole(), person });

	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage(), null);
		request.setAttribute("existentJournalBean", bean);
		return prepare(mapping, form, request, response);
	    }
	}

	return mapping.findForward("Success");
    }

    public ActionForward createInexistentJournalIssueParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Person person = getLoggedPerson(request);

	ResearchJournalIssueCreationBean bean = getJournalIssueBean(request);
	if (bean == null)
	    return prepareJournalIssueSearch(mapping, form, request, response);

	try {
	    executeService(request, "CreateResearchActivityParticipation", new Object[] {
		    bean.getJournalIssue(), bean.getRole(), person });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), null);
	    request.setAttribute("inexistentJournalBean", bean);
	    return prepare(mapping, form, request, response);
	}

	return mapping.findForward("Success");
    }

    private ResearchJournalIssueCreationBean getJournalIssueBean(HttpServletRequest request) {
	ResearchJournalIssueCreationBean bean = null;
	if (RenderUtils.getViewState() != null) {
	    bean = (ResearchJournalIssueCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
	    return bean;
	}
	return bean;
    }

}

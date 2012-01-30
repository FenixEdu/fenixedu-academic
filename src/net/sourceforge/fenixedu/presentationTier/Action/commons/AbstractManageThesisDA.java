package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonChange;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonTarget;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class AbstractManageThesisDA extends FenixDispatchAction {

    public ActionForward viewOperationsThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("thesis", getThesis(request));
	return mapping.findForward("viewOperationsThesis");
    }

    protected Integer getId(String id) {
	if (id == null || id.equals("")) {
	    return null;
	}

	try {
	    return new Integer(id);
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    protected Thesis getThesis(HttpServletRequest request) {
	Integer id = getId(request.getParameter("thesisID"));
	if (id == null) {
	    return null;
	} else {
	    return RootDomainObject.getInstance().readThesisByOID(id);
	}
    }

    public ActionForward changePersonType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisBean bean = getRenderedObject("bean");
	RenderUtils.invalidateViewState("bean");

	if (bean == null) {
	    return searchStudent(mapping, actionForm, request, response);
	}

	bean.setPersonName(null);
	bean.setRawPersonName(null);
	bean.setUnitName(null);
	bean.setRawUnitName(null);

	request.setAttribute("bean", bean);
	return mapping.findForward("select-person");
    }

    public ActionForward searchStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisBean bean = new ThesisBean();

	request.setAttribute("bean", bean);
	return mapping.findForward("search-student");
    }

    public ActionForward selectExternalPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisBean bean = getRenderedObject("bean");
	boolean create = request.getParameter("create") != null;

	if (bean == null) {
	    return editProposal(mapping, actionForm, request, response);
	}

	request.setAttribute("bean", bean);

	Person selectedPerson = bean.getPerson();
	if (selectedPerson == null) {
	    if (!create) {
		if (bean.getRawPersonName() == null || bean.getRawPersonName().trim().length() == 0) {
		    addActionMessage("info", request, "thesis.selectPerson.external.name.required");
		} else {
		    request.setAttribute("proposeCreation", true);
		}

		return mapping.findForward("select-person");
	    } else {
		RenderUtils.invalidateViewState("bean");
		return mapping.findForward("select-unit");
	    }
	} else {
	    DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
	    Thesis thesis = getThesis(request);
	    ChangeThesisPerson.run(degreeCurricularPlan, thesis,
		    new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));

	    return editProposal(mapping, actionForm, request, response);
	}
    }

    public abstract ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception;

    protected DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
	return DegreeCurricularPlan.fromExternalId(request.getParameter("degreeCurricularPlanID"));
    }

    public ActionForward selectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisBean bean = getRenderedObject("bean");

	if (bean == null) {
	    return editProposal(mapping, actionForm, request, response);
	}

	request.setAttribute("bean", bean);

	Person selectedPerson = bean.getPerson();
	if (selectedPerson == null) {
	    addActionMessage("info", request, "thesis.selectPerson.internal.required");
	    return mapping.findForward("select-person");
	} else {
	    DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
	    Thesis thesis = getThesis(request);
	    final PersonTarget personTarget = bean.getTargetType();
	    if (personTarget == PersonTarget.president) {
		final Enrolment enrolment = thesis.getEnrolment();
		final ExecutionYear executionYear = enrolment.getExecutionYear();
		if (selectedPerson == null || !degreeCurricularPlan.isScientificCommissionMember(executionYear, selectedPerson)) {
		    addActionMessage("info", request, "thesis.selectPerson.president.required.scientific.commission");
		    return mapping.findForward("select-person");
		}
	    }
	    ChangeThesisPerson.run(degreeCurricularPlan, thesis,
		    new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));

	    return editProposal(mapping, actionForm, request, response);
	}
    }

    public ActionForward selectPersonInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisBean bean = getRenderedObject("bean");

	if (bean == null) {
	    return editProposal(mapping, actionForm, request, response);
	}

	request.setAttribute("bean", bean);
	return mapping.findForward("select-person");
    }

}
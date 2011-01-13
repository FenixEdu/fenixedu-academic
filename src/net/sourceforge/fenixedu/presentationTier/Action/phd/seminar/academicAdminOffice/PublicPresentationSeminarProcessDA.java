package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.AddState;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.RemoveLastState;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.RevertToWaitingComissionForValidation;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.RevertToWaitingForComissionConstitution;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.CommonPublicPresentationSeminarDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/publicPresentationSeminarProcess", module = "academicAdminOffice")
@Forwards( {

	@Forward(name = "submitComission", path = "/phd/seminar/academicAdminOffice/submitComission.jsp"),

	@Forward(name = "validateComission", path = "/phd/seminar/academicAdminOffice/validateComission.jsp"),

	@Forward(name = "schedulePresentationDate", path = "/phd/seminar/academicAdminOffice/schedulePresentationDate.jsp"),

	@Forward(name = "uploadReport", path = "/phd/seminar/academicAdminOffice/uploadReport.jsp"),

	@Forward(name = "validateReport", path = "/phd/seminar/academicAdminOffice/validateReport.jsp"),

@Forward(name = "manageStates", path = "/phd/seminar/academicAdminOffice/manageStates.jsp")

})
public class PublicPresentationSeminarProcessDA extends CommonPublicPresentationSeminarDA {

    public ActionForward revertToWaitingForComissionConstitution(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), RevertToWaitingForComissionConstitution.class,
		    getRenderedObject("submitComissionBean"));

	    addSuccessMessage(request, "message.comission.submitted.with.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return mapping.findForward("submitComission");
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward revertToWaitingComissionValidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    ExecuteProcessActivity.run(getProcess(request), RevertToWaitingComissionForValidation.class,
		    getRenderedObject("submitComissionBean"));

	    addSuccessMessage(request, "message.comission.submitted.with.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return mapping.findForward("submitComission");
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward manageStates(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final PublicPresentationSeminarProcessBean bean = new PublicPresentationSeminarProcessBean(getProcess(request)
		.getIndividualProgramProcess());

	request.setAttribute("processBean", bean);
	return mapping.findForward("manageStates");
    }

    public ActionForward addState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PublicPresentationSeminarProcessBean bean = getRenderedObject("processBean");

	try {
	    ExecuteProcessActivity.run(getProcess(request), AddState.class, bean);
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("processBean", bean);

	    return manageStates(mapping, form, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward addStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PublicPresentationSeminarProcessBean bean = getRenderedObject("processBean");
	request.setAttribute("processBean", bean);

	return mapping.findForward("manageStates");
    }

    public ActionForward removeLastState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecuteProcessActivity.run(getProcess(request), RemoveLastState.class, null);
	return manageStates(mapping, form, request, response);
    }

}

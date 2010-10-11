package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.RevertToWaitingComissionForValidation;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.RevertToWaitingForComissionConstitution;
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

@Forward(name = "validateReport", path = "/phd/seminar/academicAdminOffice/validateReport.jsp")

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
}

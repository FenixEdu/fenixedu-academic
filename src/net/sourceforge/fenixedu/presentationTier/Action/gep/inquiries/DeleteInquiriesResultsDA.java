/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.DeleteExecutionCourseResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DeleteProfessorshipResultsBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
@Mapping(path = "/deleteInquiryResults", module = "gep")
@Forwards({ @Forward(name = "deleteResults", path = "/gep/inquiries/deleteInquiryResults.jsp", tileProperties = @Tile(title = "private.gep.surveys.deleteresults")) })
public class DeleteInquiriesResultsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("deleteExecutionCourseResults", new DeleteExecutionCourseResultsBean());
	request.setAttribute("deleteProfessorshipResults", new DeleteProfessorshipResultsBean());
	return mapping.findForward("deleteResults");
    }

    public ActionForward deleteExecutionCourseResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	DeleteExecutionCourseResultsBean deleteExecutionCourseResults = getRenderedObject("deleteExecutionCourseResults");
	RenderUtils.invalidateViewState();

	try {
	    if (deleteExecutionCourseResults.deleteResults()) {
		request.setAttribute("deleteSuccessful", "true");
	    } else {
		request.setAttribute("nothingDeleted", "true");
	    }

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}

	request.setAttribute("deleteExecutionCourseResults", deleteExecutionCourseResults);
	request.setAttribute("deleteProfessorshipResults", new DeleteProfessorshipResultsBean());
	return mapping.findForward("deleteResults");
    }

    public ActionForward deleteTeacherResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	DeleteProfessorshipResultsBean deleteProfessorshipResults = getRenderedObject("deleteProfessorshipResults");
	RenderUtils.invalidateViewState();

	try {
	    if (deleteProfessorshipResults.deleteResults()) {
		request.setAttribute("deleteSuccessful", "true");
	    } else {
		request.setAttribute("nothingDeleted", "true");
	    }
	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}

	request.setAttribute("deleteExecutionCourseResults", new DeleteExecutionCourseResultsBean());
	request.setAttribute("deleteProfessorshipResults", deleteProfessorshipResults);
	return mapping.findForward("deleteResults");
    }

    public ActionForward deleteAllTeachersResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	DeleteProfessorshipResultsBean deleteAllProfessorshipResults = getRenderedObject("deleteAllProfessorshipResults");
	RenderUtils.invalidateViewState();

	try {
	    if (deleteAllProfessorshipResults.deleteAllTeachersResults()) {
		request.setAttribute("deleteSuccessful", "true");
	    } else {
		request.setAttribute("nothingDeleted", "true");
	    }
	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}

	request.setAttribute("deleteExecutionCourseResults", new DeleteExecutionCourseResultsBean());
	request.setAttribute("deleteProfessorshipResults", deleteAllProfessorshipResults);
	return mapping.findForward("deleteResults");
    }
}
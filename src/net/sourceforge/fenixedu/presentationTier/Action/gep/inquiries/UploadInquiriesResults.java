/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.ResultsFileBean;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.UploadStudentInquiriesCourseResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.UploadStudentInquiriesTeachingResultsBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
@Mapping(path = "/uploadInquiriesResults", module = "gep")
@Forwards( { @Forward(name = "prepareUploadPage", path = "/gep/inquiries/uploadInquiriesResults.jsp"),
	@Forward(name = "uploadCoursesPage", path = "/gep/inquiries/uploadStudentInquiriesResultsCurricularCourses.jsp"),
	@Forward(name = "uploadTeachersPage", path = "/gep/inquiries/uploadStudentInquiriesResultsTeachers.jsp") })
public class UploadInquiriesResults extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("uploadFileBean", new ResultsFileBean());
	return mapping.findForward("prepareUploadPage");
    }

    public ActionForward prepareCurricularCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("uploadFileBean", new ResultsFileBean());
	return mapping.findForward("uploadCoursesPage");
    }

    public ActionForward prepareTeachers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("uploadTeachingFileBean", new UploadStudentInquiriesTeachingResultsBean());
	return mapping.findForward("uploadTeachersPage");
    }

    public ActionForward submitResultsFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	ResultsFileBean resultsBean = getRenderedObject("uploadFileBean");
	RenderUtils.invalidateViewState("uploadFileBean");

	try {
	    final String stringResults = readFile(resultsBean);
	    if (resultsBean.getNewResults()) {
		InquiryResult.importResults(stringResults, resultsBean.getResultsDate());
	    } else {
		InquiryResult.updateRows(stringResults, resultsBean.getResultsDate());
	    }
	    request.setAttribute("success", "true");
	} catch (IOException e) {
	    addErrorMessage(request, e.getMessage(), e.getMessage());
	} catch (DomainException e) {
	    System.out.println(e.getMessage());
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}
	return prepare(mapping, actionForm, request, response);
    }

    private String readFile(ResultsFileBean resultsBean) throws IOException {
	return FileUtils.readFile(resultsBean.getInputStream());
    }

    public ActionForward submitTeachingFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	//	UploadStudentInquiriesTeachingResultsBean resultsBean = getRenderedObject("uploadTeachingFileBean");
	//	RenderUtils.invalidateViewState("uploadTeachingFileBean");
	//
	//	try {
	//	    final String[] splittedResults = readFile(resultsBean);
	//	    StudentInquiriesTeachingResult.importResults(splittedResults[0], splittedResults[1], resultsBean);
	//	    addActionMessage(request, "message.StudentInquiriesResult.uploadSucess");
	//	} catch (IOException e) {
	//	    addErrorMessage(request, e.getMessage(), e.getMessage());
	//	} catch (DomainException e) {
	//	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	//	}
	return prepareTeachers(mapping, actionForm, request, response);
    }

    public ActionForward deleteCurricularCoursesData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	UploadStudentInquiriesCourseResultsBean resultsBean = getRenderedObject("deleteCourseDataBean");
	RenderUtils.invalidateViewState();

	try {
	    if (StudentInquiriesCourseResult.resetCourseAndTeachingResults(resultsBean)) {
		addActionMessage(request, "message.StudentInquiriesResult.delete.sucess");
	    } else {
		addActionMessage(request, "message.StudentInquiriesResult.delete.dataNotFound");
	    }
	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}
	return prepareCurricularCourses(mapping, actionForm, request, response);
    }

    public ActionForward deleteTeachingData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	UploadStudentInquiriesTeachingResultsBean resultsBean = getRenderedObject("deleteTeachingDataBean");
	RenderUtils.invalidateViewState();

	try {
	    if (StudentInquiriesTeachingResult.deleteTeachingResults(resultsBean)) {
		addActionMessage(request, "message.StudentInquiriesResult.delete.sucess");
	    } else {
		addActionMessage(request, "message.StudentInquiriesResult.delete.dataNotFound");
	    }
	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
	}
	return prepareTeachers(mapping, actionForm, request, response);
    }
}
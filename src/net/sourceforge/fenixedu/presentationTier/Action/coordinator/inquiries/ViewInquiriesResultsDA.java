package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.ViewInquiriesResultPageDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewInquiriesResults", module = "coordinator", formBeanClass = ViewInquiriesResultPageDTO.class)
@Forwards( { @Forward(name = "inquiryResults", path = "/coordinator/inquiries/viewInquiriesResults.jsp"),
	@Forward(name = "showFilledTeachingInquiry", path = "/inquiries/showFilledTeachingInquiry.jsp", useTile = false),
	@Forward(name = "showCourseInquiryResult", path = "/inquiries/showCourseInquiryResult.jsp", useTile = false),
	@Forward(name = "showTeachingInquiryResult", path = "/inquiries/showTeachingInquiryResult.jsp", useTile = false) })
public class ViewInquiriesResultsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("executionDegrees", getExecutionDegrees(request, actionForm));
	request.setAttribute("executionCourses", Collections.EMPTY_LIST);
	((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(getIntegerFromRequest(request,
		"degreeCurricularPlanID"));

	return actionMapping.findForward("inquiryResults");
    }

    private List<ExecutionDegree> getExecutionDegrees(HttpServletRequest request, ActionForm actionForm) {
	Integer degreeCurricularPlanID = getIntegerFromRequest(request, "degreeCurricularPlanID");
	if (degreeCurricularPlanID == null || degreeCurricularPlanID == 0) {
	    degreeCurricularPlanID = ((ViewInquiriesResultPageDTO) actionForm).getDegreeCurricularPlanID();
	}
	return rootDomainObject.readDegreeCurricularPlanByOID(getIntegerFromRequest(request, "degreeCurricularPlanID"))
		.getExecutionDegrees();
    }

    public ActionForward selectExecutionDegree(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ViewInquiriesResultPageDTO resultPageDTO = (ViewInquiriesResultPageDTO) actionForm;

	ExecutionDegree executionDegree = resultPageDTO.getExecutionDegree();
	if (executionDegree == null) {
	    return prepare(actionMapping, actionForm, request, response);
	}

	Collection<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
	for (StudentInquiriesCourseResult studentInquiriesCourseResult : executionDegree.getStudentInquiriesCourseResults()) {
	    executionCourses.add(studentInquiriesCourseResult.getExecutionCourse());
	}
	Collections.sort((List<ExecutionCourse>) executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);

	request.setAttribute("executionDegrees", getExecutionDegrees(request, actionForm));
	request.setAttribute("executionCourses", executionCourses);

	return actionMapping.findForward("inquiryResults");
    }

    public ActionForward selectExecutionCourse(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final ExecutionCourse executionCourse = ((ViewInquiriesResultPageDTO) actionForm).getExecutionCourse();
	request.setAttribute("executionCourse", executionCourse);
	if (executionCourse != null) {
	    request.setAttribute("studentInquiriesCourseResults", populateStudentInquiriesCourseResults(executionCourse,
		    ((ViewInquiriesResultPageDTO) actionForm).getExecutionDegree()));
	}
	return selectExecutionDegree(actionMapping, actionForm, request, response);
    }

    private Collection<StudentInquiriesCourseResultBean> populateStudentInquiriesCourseResults(
	    final ExecutionCourse executionCourse, final ExecutionDegree executionDegree) {
	Map<ExecutionDegree, StudentInquiriesCourseResultBean> courseResultsMap = new HashMap<ExecutionDegree, StudentInquiriesCourseResultBean>();
	for (StudentInquiriesCourseResult studentInquiriesCourseResult : executionCourse.getStudentInquiriesCourseResults()) {
	    if (studentInquiriesCourseResult.getExecutionDegree() == executionDegree) {
		courseResultsMap.put(studentInquiriesCourseResult.getExecutionDegree(), new StudentInquiriesCourseResultBean(
			studentInquiriesCourseResult));
	    }
	}

	for (Professorship otherTeacherProfessorship : executionCourse.getProfessorships()) {
	    for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
		    .getStudentInquiriesTeachingResults()) {
		if (studentInquiriesTeachingResult.getExecutionDegree() == executionDegree
			&& studentInquiriesTeachingResult.getInternalDegreeDisclosure()) {
		    courseResultsMap.get(studentInquiriesTeachingResult.getExecutionDegree()).addStudentInquiriesTeachingResult(
			    studentInquiriesTeachingResult);
		}
	    }
	}

	return courseResultsMap.values();
    }

    public ActionForward showInquiryCourseResult(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("inquiryResult", RootDomainObject.getInstance().readStudentInquiriesCourseResultByOID(
		getIntegerFromRequest(request, "resultId")));
	return actionMapping.findForward("showCourseInquiryResult");
    }

    public ActionForward showInquiryTeachingResult(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("inquiryResult", RootDomainObject.getInstance().readStudentInquiriesTeachingResultByOID(
		getIntegerFromRequest(request, "resultId")));
	return actionMapping.findForward("showTeachingInquiryResult");
    }

    public ActionForward showFilledTeachingInquiry(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("teachingInquiry", RootDomainObject.getInstance().readTeachingInquiryByOID(
		getIntegerFromRequest(request, "filledTeachingInquiryId")));
	return actionMapping.findForward("showFilledTeachingInquiry");
    }

}

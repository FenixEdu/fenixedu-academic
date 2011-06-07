package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGeneralResultBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;
import net.sourceforge.fenixedu.domain.inquiries.ResultsInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/viewCourseResults", module = "publico")
public class ViewCourseInquiryPublicResults extends ViewInquiryPublicResults {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return getCourseResultsActionForward(mapping, actionForm, request, response);
    }

    public static ActionForward getCourseResultsActionForward(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(request.getParameter("executionCourseOID"));
	ExecutionSemester executionPeriod = executionCourse.getExecutionPeriod();

	CurricularCourseInquiryTemplate curricularCourseInquiryTemplate = CurricularCourseInquiryTemplate
		.getTemplateByExecutionPeriod(executionPeriod);
	if (curricularCourseInquiryTemplate == null) {
	    request.setAttribute("notAvailableMessage", "message.inquiries.publicResults.notAvailable.m1");
	    return mapping.findForward("execution-course-student-inquiries-result-notAvailable");
	}

	DegreeCurricularPlan dcp = AbstractDomainObject.fromExternalId(request.getParameter("degreeCurricularPlanOID"));
	ExecutionDegree executionDegree = dcp.getExecutionDegreeByAcademicInterval(executionPeriod.getAcademicInterval());
	List<InquiryResult> results = executionCourse.getInquiryResultsByExecutionDegreeAndForTeachers(executionDegree);
	boolean hasNotRelevantData = executionCourse.hasNotRelevantDataFor(executionDegree);

	ResultsInquiryTemplate resultsInquiryTemplate = ResultsInquiryTemplate.getTemplateByExecutionPeriod(executionPeriod);
	List<InquiryBlock> resultBlocks = resultsInquiryTemplate.getInquiryBlocks();

	GroupResultsSummaryBean ucGroupResultsSummaryBean = getGeneralResults(results, resultBlocks, 1, 1);
	GroupResultsSummaryBean answersResultsSummaryBean = getGeneralResults(results, resultBlocks, 1, 2);
	GroupResultsSummaryBean nonAnswersResultsSummaryBean = getGeneralResults(results, resultBlocks, 1, 3);
	Collections.sort(nonAnswersResultsSummaryBean.getQuestionsResults(), new BeanComparator("questionResult.value"));
	Collections.reverse(nonAnswersResultsSummaryBean.getQuestionsResults());

	CurricularCourse curricularCourse = executionCourse.getCurricularCourseFor(dcp);
	Double ects = curricularCourse.getEctsCredits(executionPeriod);
	Double contactLoadEcts = curricularCourse.getContactLoad() / 28;
	Double autonumousWorkEcts = ects - contactLoadEcts;

	GroupResultsSummaryBean workLoadaSummaryBean = getGeneralResults(results, resultBlocks, 2, 1);
	request.setAttribute("contactLoadEcts", contactLoadEcts);
	request.setAttribute("autonumousWorkEcts", autonumousWorkEcts);
	GroupResultsSummaryBean ucGeneralDataSummaryBean = getGeneralResults(results, resultBlocks, 4, 1);
	GroupResultsSummaryBean ucEvaluationsGroupBean = getGeneralResults(results, resultBlocks, 3, 1);
	InquiryQuestion estimatedEvaluationQuestion = getEstimatedEvaluationsQuestion(curricularCourseInquiryTemplate
		.getInquiryBlocks());
	QuestionResultsSummaryBean estimatedEvaluationBeanQuestion = new QuestionResultsSummaryBean(estimatedEvaluationQuestion,
		getResultsForQuestion(results, estimatedEvaluationQuestion), null, null);

	GroupResultsSummaryBean totalAnswers = getGeneralResults(results, resultBlocks, 1, 6);
	request.setAttribute("totalAnswers", totalAnswers.getQuestionsResults().get(0));

	InquiryQuestion teachersSummaryQuestion = getTeacherShiftQuestion(resultBlocks);
	List<TeacherShiftTypeGeneralResultBean> teachersSummaryBeans = getTeachersShiftsResults(executionCourse,
		teachersSummaryQuestion);
	Collections.sort(teachersSummaryBeans, new BeanComparator("professorship.person.name"));
	Collections.sort(teachersSummaryBeans, new BeanComparator("shiftType"));

	ResultClassification auditResult = getAuditResult(results);
	if (auditResult != null) {
	    request.setAttribute("auditResult", auditResult.name());
	}

	request.setAttribute("ucGroupResultsSummaryBean", ucGroupResultsSummaryBean);
	request.setAttribute("answersResultsSummaryBean", answersResultsSummaryBean);
	request.setAttribute("nonAnswersResultsSummaryBean", nonAnswersResultsSummaryBean);
	request.setAttribute("workLoadaSummaryBean", workLoadaSummaryBean);
	request.setAttribute("ucGeneralDataSummaryBean", ucGeneralDataSummaryBean);
	request.setAttribute("ucEvaluationsGroupBean", ucEvaluationsGroupBean);
	request.setAttribute("estimatedEvaluationBeanQuestion", estimatedEvaluationBeanQuestion);
	request.setAttribute("teachersSummaryBeans", teachersSummaryBeans);

	CurricularCourseInquiryTemplate courseInquiryTemplate = CurricularCourseInquiryTemplate
		.getTemplateByExecutionPeriod(executionPeriod);
	List<BlockResultsSummaryBean> blockResultsSummaryBeans = new ArrayList<BlockResultsSummaryBean>();
	if (!hasNotRelevantData) {
	    for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocks()) {
		blockResultsSummaryBeans.add(new BlockResultsSummaryBean(inquiryBlock, results, null, null));
	    }
	    Collections.sort(blockResultsSummaryBeans, new BeanComparator("inquiryBlock.blockOrder"));
	}
	request.setAttribute("hasNotRelevantData", hasNotRelevantData);
	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("executionPeriod", executionPeriod);
	request.setAttribute("executionDegree", executionDegree);
	request.setAttribute("resultsDate", results.get(0).getResultDate());
	request.setAttribute("blockResultsSummaryBeans", blockResultsSummaryBeans);

	request.setAttribute("publicContext", true);
	return new ActionForward(null, "/inquiries/showCourseInquiryResult_v3.jsp", false, "/teacher");
    }

    private static List<InquiryResult> getResultsForQuestion(List<InquiryResult> results, InquiryQuestion inquiryQuestion) {
	List<InquiryResult> questionResults = new ArrayList<InquiryResult>();
	for (InquiryResult inquiryResult : results) {
	    if (inquiryResult.getInquiryQuestion() == inquiryQuestion) {
		questionResults.add(inquiryResult);
	    }
	}
	return questionResults;
    }

    private static InquiryQuestion getEstimatedEvaluationsQuestion(List<InquiryBlock> inquiryBlocks) {
	for (InquiryBlock inquiryBlock : inquiryBlocks) {
	    for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
		    if (!inquiryQuestion.getPresentResults()) {
			return inquiryQuestion;
		    }
		}
	    }
	}
	return null;
    }

    private static List<TeacherShiftTypeGeneralResultBean> getTeachersShiftsResults(ExecutionCourse executionCourse,
	    InquiryQuestion teachersSummaryQuestion) {
	List<TeacherShiftTypeGeneralResultBean> teachersSummaries = new ArrayList<TeacherShiftTypeGeneralResultBean>();
	for (InquiryResult inquiryResult : executionCourse.getInquiryResults()) {
	    if (inquiryResult.getInquiryQuestion() == teachersSummaryQuestion) {
		teachersSummaries.add(new TeacherShiftTypeGeneralResultBean(inquiryResult.getProfessorship(), inquiryResult
			.getShiftType(), inquiryResult));
	    }
	}
	return teachersSummaries;
    }

    private static InquiryQuestion getTeacherShiftQuestion(List<InquiryBlock> resultBlocks) {
	for (InquiryBlock inquiryBlock : resultBlocks) {
	    if (inquiryBlock.getBlockOrder() == 6) {
		for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		    if (groupQuestion.getGroupOrder() == 1) {
			for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
			    if (inquiryQuestion.getQuestionOrder() == 1) {
				return inquiryQuestion;
			    }
			}
		    }
		}
	    }
	}
	return null;
    }

    private static ResultClassification getAuditResult(List<InquiryResult> results) {
	for (InquiryResult inquiryResult : results) {
	    if (InquiryResultType.AUDIT.equals(inquiryResult.getResultType())) {
		return inquiryResult.getResultClassification();
	    }
	}
	return null;
    }
}

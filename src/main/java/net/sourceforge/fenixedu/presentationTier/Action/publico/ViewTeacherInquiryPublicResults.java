package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.GroupResultType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultsInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewTeacherResults", module = "publico")
public class ViewTeacherInquiryPublicResults extends ViewInquiryPublicResults {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return getTeacherResultsActionForward(mapping, actionForm, request, response);
    }

    public static ActionForward getTeacherResultsActionForward(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        Professorship professorship = FenixFramework.getDomainObject(request.getParameter("professorshipOID"));
        ShiftType shiftType = ShiftType.valueOf(request.getParameter("shiftType"));

        List<InquiryResult> inquiryResults = professorship.getInquiryResults(shiftType);

        ExecutionSemester executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
        ResultsInquiryTemplate resultsInquiryTemplate = ResultsInquiryTemplate.getTemplateByExecutionPeriod(executionPeriod);
        Collection<InquiryBlock> resultBlocks = resultsInquiryTemplate.getInquiryBlocks();

        GroupResultsSummaryBean teacherGroupResultsSummaryBean =
                getGeneralResults(inquiryResults, resultBlocks, GroupResultType.TEACHER_RESULTS);
        request.setAttribute("teacherGroupResultsSummaryBean", teacherGroupResultsSummaryBean);

        InquiryResult teacherEvaluation = getTeacherEvaluation(inquiryResults);
        request.setAttribute("teacherEvaluation", teacherEvaluation);

        StudentTeacherInquiryTemplate teacherInquiryTemplate =
                StudentTeacherInquiryTemplate.getTemplateByExecutionPeriod(executionPeriod);
        List<BlockResultsSummaryBean> blockResultsSummaryBeans = new ArrayList<BlockResultsSummaryBean>();
        for (InquiryBlock inquiryBlock : teacherInquiryTemplate.getInquiryBlocks()) {
            blockResultsSummaryBeans.add(new BlockResultsSummaryBean(inquiryBlock, inquiryResults, null, null));
        }
        Collections.sort(blockResultsSummaryBeans, new BeanComparator("inquiryBlock.blockOrder"));
        request.setAttribute("executionCourse", professorship.getExecutionCourse());
        request.setAttribute("shiftType", shiftType);
        request.setAttribute("professorship", professorship);
        request.setAttribute("executionPeriod", executionPeriod);
        request.setAttribute("blockResultsSummaryBeans", blockResultsSummaryBeans);
        request.setAttribute("resultsDate", inquiryResults.iterator().next().getResultDate());

        setTeacherScaleColorException(executionPeriod, request);
        request.setAttribute("publicContext", true);
        return new ActionForward(null, "/inquiries/showTeacherInquiryResult_v3.jsp", false, "/teacher");
    }

    private static InquiryResult getTeacherEvaluation(List<InquiryResult> inquiryResults) {
        for (InquiryResult inquiryResult : inquiryResults) {
            if (InquiryResultType.TEACHER_EVALUATION.equals(inquiryResult.getResultType())) {
                return inquiryResult;
            }
        }
        return null;
    }

    public static void setTeacherScaleColorException(ExecutionSemester executionSemester, HttpServletRequest request) {
        if (executionSemester.getSemester() == 1 && executionSemester.getYear().equals("2010/2011")) {
            request.setAttribute("first-sem-2010", "first-sem-2010");
        }
    }
}

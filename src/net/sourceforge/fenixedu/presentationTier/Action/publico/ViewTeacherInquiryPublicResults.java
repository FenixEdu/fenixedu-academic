package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultsInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ViewTeacherInquiryPublicResults extends ViewInquiryPublicResults {

    public static ActionForward getTeacherResultsActionForward(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	Professorship professorship = AbstractDomainObject.fromExternalId(request.getParameter("professorshipOID"));
	ShiftType shiftType = ShiftType.valueOf(request.getParameter("shiftType"));

	List<InquiryResult> inquiryResults = professorship.getInquiriyResults(shiftType);

	ExecutionSemester executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
	ResultsInquiryTemplate resultsInquiryTemplate = ResultsInquiryTemplate.getTemplateByExecutionPeriod(executionPeriod);
	List<InquiryBlock> resultBlocks = resultsInquiryTemplate.getInquiryBlocks();

	GroupResultsSummaryBean teacherGroupResultsSummaryBean = getGeneralResults(inquiryResults, resultBlocks, 5, 1);

	request.setAttribute("teacherGroupResultsSummaryBean", teacherGroupResultsSummaryBean);

	StudentTeacherInquiryTemplate teacherInquiryTemplate = StudentTeacherInquiryTemplate
		.getTemplateByExecutionPeriod(executionPeriod);
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

	request.setAttribute("publicContext", true);
	return new ActionForward(null, "/inquiries/showTeacherInquiryResult_v3.jsp", false, "/teacher");
    }
}

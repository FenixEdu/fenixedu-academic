package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorResultsBean extends GlobalCommentsResultsBean {

    private static final long serialVersionUID = 1L;
    private List<BlockResultsSummaryBean> curricularBlockResults;
    private ExecutionDegree executionDegree;

    public CoordinatorResultsBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person coordinator,
	    InquiryGlobalComment globalComment, boolean backToResume) {
	super(executionCourse, coordinator, globalComment, backToResume);
	setExecutionDegree(executionDegree);
	initCurricularBlocksResults(executionCourse, executionDegree, coordinator);
    }

    private void initCurricularBlocksResults(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person person) {
	CurricularCourseInquiryTemplate courseInquiryTemplate = CurricularCourseInquiryTemplate
		.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
	setCurricularBlockResults(new ArrayList<BlockResultsSummaryBean>());
	List<InquiryResult> results = executionCourse.getInquiryResultsByExecutionDegreeAndForTeachers(executionDegree);
	if (results != null && results.size() > 5) {
	    for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocks()) {
		getCurricularBlockResults().add(new BlockResultsSummaryBean(inquiryBlock, results, person, getPersonCategory()));
	    }
	}
	Collections.sort(getCurricularBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    @Override
    protected InquiryGlobalComment createGlobalComment() {
	return new InquiryGlobalComment(getExecutionCourse(), getExecutionDegree());
    }

    @Override
    protected ResultPersonCategory getPersonCategory() {
	return ResultPersonCategory.DEGREE_COORDINATOR;
    }

    public List<BlockResultsSummaryBean> getCurricularBlockResults() {
	return curricularBlockResults;
    }

    public void setCurricularBlockResults(List<BlockResultsSummaryBean> curricularBlockResults) {
	this.curricularBlockResults = curricularBlockResults;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree;
    }
}

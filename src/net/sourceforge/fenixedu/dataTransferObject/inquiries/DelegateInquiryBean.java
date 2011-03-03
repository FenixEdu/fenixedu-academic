package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class DelegateInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TeacherShiftTypeResultsBean> teachersResults;
    private List<BlockResultsSummaryBean> curricularBlockResults;

    public DelegateInquiryBean(ExecutionCourse executionCourse, List<InquiryResult> results) {
	initCurricularBlocksResults(executionCourse, results);
	initTeachersResults(executionCourse);
    }

    private void initCurricularBlocksResults(ExecutionCourse executionCourse, List<InquiryResult> results) {
	CurricularCourseInquiryTemplate courseInquiryTemplate = CurricularCourseInquiryTemplate
		.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
	setCurricularBlockResults(new ArrayList<BlockResultsSummaryBean>());
	for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocks()) {
	    getCurricularBlockResults().add(new BlockResultsSummaryBean(inquiryBlock, results));
	}
	Collections.sort(getCurricularBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    private void initTeachersResults(ExecutionCourse executionCourse) {
	setTeachersResults(new ArrayList<TeacherShiftTypeResultsBean>());
	for (Professorship professorship : executionCourse.getProfessorships()) {
	    List<InquiryResult> professorshipResults = professorship.getInquiriyResults();
	    if (!professorshipResults.isEmpty()) {
		for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		    getTeachersResults().add(
			    new TeacherShiftTypeResultsBean(professorship, shiftType, executionCourse.getExecutionPeriod(),
				    professorship.getInquiriyResults(shiftType)));
		}
	    }
	}

	Collections.sort(getTeachersResults(), new BeanComparator("professorship.person.name"));
	Collections.sort(getTeachersResults(), new BeanComparator("shiftType"));
    }

    private Set<ShiftType> getShiftTypes(List<InquiryResult> professorshipResults) {
	Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
	for (InquiryResult inquiryResult : professorshipResults) {
	    shiftTypes.add(inquiryResult.getShiftType());
	}
	return shiftTypes;
    }

    public List<TeacherShiftTypeResultsBean> getTeachersResults() {
	return teachersResults;
    }

    public void setTeachersResults(List<TeacherShiftTypeResultsBean> teachersResults) {
	this.teachersResults = teachersResults;
    }

    public List<BlockResultsSummaryBean> getCurricularBlockResults() {
	return curricularBlockResults;
    }

    public void setCurricularBlockResults(List<BlockResultsSummaryBean> curricularBlockResults) {
	this.curricularBlockResults = curricularBlockResults;
    }

    public boolean isValid() {
	// TODO Auto-generated method stub
	return true;
    }

    @Service
    public static void saveChanges(DelegateInquiryBean delegateInquiryBean) {
	for (BlockResultsSummaryBean blockResultsSummaryBean : delegateInquiryBean.getCurricularBlockResults()) {
	    for (GroupResultsSummaryBean groupResultsSummaryBean : blockResultsSummaryBean.getGroupsResults()) {
		for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
		    if (!StringUtils.isEmpty(questionResultsSummaryBean.getEditableComment())) {
			System.out.println("GOT YOU!!! :D");
		    }
		}
	    }
	}

    }

}

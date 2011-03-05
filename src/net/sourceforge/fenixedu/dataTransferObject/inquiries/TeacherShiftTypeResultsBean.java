package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;

public class TeacherShiftTypeResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Professorship professorship;
    private ShiftType shiftType;
    private List<BlockResultsSummaryBean> blockResults = new ArrayList<BlockResultsSummaryBean>();

    public TeacherShiftTypeResultsBean(Professorship professorship, ShiftType shiftType, ExecutionSemester executionPeriod,
	    List<InquiryResult> inquiryResults, Person person) {
	setProfessorship(professorship);
	setShiftType(shiftType);

	StudentTeacherInquiryTemplate inquiryTemplate = StudentTeacherInquiryTemplate
		.getTemplateByExecutionPeriod(executionPeriod);
	setBlockResults(new ArrayList<BlockResultsSummaryBean>());
	for (InquiryBlock inquiryBlock : inquiryTemplate.getInquiryBlocks()) {
	    getBlockResults().add(
		    new BlockResultsSummaryBean(inquiryBlock, inquiryResults, person, ResultPersonCategory.DELEGATE));
	}
	Collections.sort(getBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    public Professorship getProfessorship() {
	return professorship;
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = professorship;
    }

    public ShiftType getShiftType() {
	return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
	this.shiftType = shiftType;
    }

    public List<BlockResultsSummaryBean> getBlockResults() {
	return blockResults;
    }

    public void setBlockResults(List<BlockResultsSummaryBean> blockResults) {
	this.blockResults = blockResults;
    }
}

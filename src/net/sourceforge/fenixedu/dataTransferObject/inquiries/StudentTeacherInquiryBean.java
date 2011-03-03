package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeacherDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class StudentTeacherInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<InquiryBlockDTO> teacherInquiryBlocks;
    private ExecutionCourse executionCourse;
    private TeacherDTO teacherDTO;
    private ShiftType shiftType;
    private boolean filled = false;

    public StudentTeacherInquiryBean(final TeacherDTO teacherDTO, final ExecutionCourse executionCourse,
	    final ShiftType shiftType, StudentInquiryTemplate studentTeacherInquiryTemplate) {
	setTeacherInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
	for (InquiryBlock inquiryBlock : studentTeacherInquiryTemplate.getInquiryBlocksSet()) {
	    getTeacherInquiryBlocks().add(new InquiryBlockDTO(inquiryBlock, null));
	}
	setExecutionCourse(executionCourse);
	setShiftType(shiftType);
	setTeacherDTO(teacherDTO);
    }

    public String validateTeacherInquiry() {
	String validationResult = null;
	for (InquiryBlockDTO inquiryBlockDTO : getTeacherInquiryBlocks()) {
	    validationResult = inquiryBlockDTO.validate(getTeacherInquiryBlocks());
	    if (!Boolean.valueOf(validationResult)) {
		return validationResult;
	    }
	}
	return Boolean.toString(true);
    }

    public boolean isInquiryFilledIn() {
	for (InquiryBlockDTO inquiryBlockDTO : getTeacherInquiryBlocks()) {
	    for (InquiryGroupQuestionBean groupQuestionBean : inquiryBlockDTO.getInquiryGroups()) {
		for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
		    if (!StringUtils.isEmpty(questionDTO.getResponseValue())) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public Set<InquiryBlockDTO> getTeacherInquiryBlocks() {
	return teacherInquiryBlocks;
    }

    public void setTeacherInquiryBlocks(Set<InquiryBlockDTO> teacherInquiryBlocks) {
	this.teacherInquiryBlocks = teacherInquiryBlocks;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public TeacherDTO getTeacherDTO() {
	return teacherDTO;
    }

    public void setTeacherDTO(TeacherDTO teacherDTO) {
	this.teacherDTO = teacherDTO;
    }

    public ShiftType getShiftType() {
	return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
	this.shiftType = shiftType;
    }

    public boolean isFilled() {
	return filled;
    }

    public void setFilled(boolean filled) {
	this.filled = filled;
    }
}

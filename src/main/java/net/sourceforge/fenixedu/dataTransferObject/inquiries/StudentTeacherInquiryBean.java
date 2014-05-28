/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeacherDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.MandatoryCondition;
import net.sourceforge.fenixedu.domain.inquiries.QuestionCondition;
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
        setGroupsVisibility(getTeacherInquiryBlocks());
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

    public void setGroupsVisibility(Set<InquiryBlockDTO> inquiryBlocks) {
        for (InquiryBlockDTO inquiryBlockDTO : inquiryBlocks) {
            Set<InquiryGroupQuestionBean> groups = inquiryBlockDTO.getInquiryGroups();
            for (InquiryGroupQuestionBean group : groups) {
                setGroupVisibility(inquiryBlocks, group);
            }
        }
    }

    private void setGroupVisibility(Set<InquiryBlockDTO> inquiryBlocks, InquiryGroupQuestionBean groupQuestionBean) {
        for (QuestionCondition questionCondition : groupQuestionBean.getInquiryGroupQuestion().getQuestionConditions()) {
            if (questionCondition instanceof MandatoryCondition) {
                MandatoryCondition condition = (MandatoryCondition) questionCondition;
                InquiryQuestionDTO inquiryDependentQuestionBean =
                        getInquiryQuestionBean(condition.getInquiryDependentQuestion(), inquiryBlocks);
                boolean isMandatory =
                        inquiryDependentQuestionBean.getFinalValue() == null ? false : condition.getConditionValuesAsList()
                                .contains(inquiryDependentQuestionBean.getFinalValue());
                if (isMandatory) {
                    groupQuestionBean.setVisible(true);
                } else {
                    groupQuestionBean.setVisible(false);
                    for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                        questionDTO.setResponseValue(null);
                    }
                }
            }
        }
    }

    private InquiryQuestionDTO getInquiryQuestionBean(InquiryQuestion inquiryQuestion, Set<InquiryBlockDTO> inquiryBlocks) {
        for (InquiryBlockDTO blockDTO : inquiryBlocks) {
            for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
                for (InquiryQuestionDTO inquiryQuestionDTO : groupQuestionBean.getInquiryQuestions()) {
                    if (inquiryQuestionDTO.getInquiryQuestion() == inquiryQuestion) {
                        return inquiryQuestionDTO;
                    }
                }
            }
        }
        return null;
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

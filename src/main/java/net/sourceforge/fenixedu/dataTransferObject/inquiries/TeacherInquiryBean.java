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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.MandatoryCondition;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.QuestionCondition;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class TeacherInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TeacherShiftTypeResultsBean> teachersResults;
    private Set<InquiryBlockDTO> teacherInquiryBlocks;
    private Professorship professorship;

    public TeacherInquiryBean(TeacherInquiryTemplate teacherInquiryTemplate, Professorship professorship) {
        setProfessorship(professorship);
        initTeachersResults(professorship, professorship.getPerson());
        initTeacherInquiry(teacherInquiryTemplate, professorship);
        setGroupsVisibility();
    }

    private void initTeacherInquiry(TeacherInquiryTemplate teacherInquiryTemplate, Professorship professorship) {
        setTeacherInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
        for (InquiryBlock inquiryBlock : teacherInquiryTemplate.getInquiryBlocks()) {
            getTeacherInquiryBlocks().add(new InquiryBlockDTO(getInquiryTeacherAnswer(), inquiryBlock));
        }

    }

    private void initTeachersResults(Professorship professorship, Person person) {
        setTeachersResults(new ArrayList<TeacherShiftTypeResultsBean>());
        Collection<InquiryResult> professorshipResults = professorship.getInquiryResults();
        if (!professorshipResults.isEmpty()) {
            for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                List<InquiryResult> teacherShiftResults = professorship.getInquiryResults(shiftType);
                if (!teacherShiftResults.isEmpty()) {
                    getTeachersResults().add(
                            new TeacherShiftTypeResultsBean(professorship, shiftType, professorship.getExecutionCourse()
                                    .getExecutionPeriod(), teacherShiftResults, person, ResultPersonCategory.TEACHER));
                }
            }
        }
        Collections.sort(getTeachersResults(), new BeanComparator("professorship.person.name"));
        Collections.sort(getTeachersResults(), new BeanComparator("shiftType"));
    }

    private Set<ShiftType> getShiftTypes(Collection<InquiryResult> professorshipResults) {
        Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
        for (InquiryResult inquiryResult : professorshipResults) {
            shiftTypes.add(inquiryResult.getShiftType());
        }
        return shiftTypes;
    }

    public void setGroupsVisibility() {
        for (InquiryBlockDTO inquiryBlockDTO : getTeacherInquiryBlocks()) {
            Set<InquiryGroupQuestionBean> groups = inquiryBlockDTO.getInquiryGroups();
            for (InquiryGroupQuestionBean group : groups) {
                setGroupVisibility(getTeacherInquiryBlocks(), group);
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

    public List<TeacherShiftTypeResultsBean> getTeachersResults() {
        return teachersResults;
    }

    public void setTeachersResults(List<TeacherShiftTypeResultsBean> teachersResults) {
        this.teachersResults = teachersResults;
    }

    public String validateInquiry() {
        String validationResult = null;
        for (InquiryBlockDTO inquiryBlockDTO : getTeacherInquiryBlocks()) {
            validationResult = inquiryBlockDTO.validateMandatoryConditions(getTeacherInquiryBlocks());
            if (!Boolean.valueOf(validationResult)) {
                return validationResult;
            }
        }
        return Boolean.toString(true);
    }

    @Atomic
    public void saveChanges(Person person, ResultPersonCategory teacher) {
        for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : getTeachersResults()) {
            saveComments(person, teacher, teacherShiftTypeResultsBean.getBlockResults());
        }
        for (InquiryBlockDTO blockDTO : getTeacherInquiryBlocks()) {
            for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
                for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                    if (!StringUtils.isEmpty(questionDTO.getResponseValue()) || questionDTO.getQuestionAnswer() != null) {
                        if (questionDTO.getQuestionAnswer() != null) {
                            questionDTO.getQuestionAnswer().setAnswer(questionDTO.getResponseValue());
                            questionDTO.getQuestionAnswer().getInquiryAnswer().setResponseDateTime(new DateTime());
                        } else {
                            if (getInquiryTeacherAnswer() == null) {
                                new InquiryTeacherAnswer(getProfessorship());
                            }
                            new QuestionAnswer(getInquiryTeacherAnswer(), questionDTO.getInquiryQuestion(),
                                    questionDTO.getFinalValue());
                            getInquiryTeacherAnswer().setResponseDateTime(new DateTime());
                        }
                    }
                }
            }
        }
    }

    private void saveComments(Person person, ResultPersonCategory teacher, List<BlockResultsSummaryBean> blocksResults) {
        for (BlockResultsSummaryBean blockResultsSummaryBean : blocksResults) {
            for (GroupResultsSummaryBean groupResultsSummaryBean : blockResultsSummaryBean.getGroupsResults()) {
                for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
                    InquiryResult questionResult = questionResultsSummaryBean.getQuestionResult();
                    if (questionResult != null) {
                        InquiryResultComment inquiryResultComment =
                                questionResultsSummaryBean.getQuestionResult().getInquiryResultComment(person, teacher);
                        if (!StringUtils.isEmpty(questionResultsSummaryBean.getEditableComment()) || inquiryResultComment != null) {
                            if (inquiryResultComment == null) {
                                inquiryResultComment =
                                        new InquiryResultComment(questionResult, person, teacher, questionResultsSummaryBean
                                                .getQuestionResult().getInquiryResultComments().size() + 1);
                            }
                            inquiryResultComment.setComment(questionResultsSummaryBean.getEditableComment());
                        }
                    }
                }
            }
        }
    }

    public void setProfessorship(Professorship professorship) {
        this.professorship = professorship;
    }

    public Professorship getProfessorship() {
        return professorship;
    }

    public void setTeacherInquiryBlocks(Set<InquiryBlockDTO> teacherInquiryBlocks) {
        this.teacherInquiryBlocks = teacherInquiryBlocks;
    }

    public Set<InquiryBlockDTO> getTeacherInquiryBlocks() {
        return teacherInquiryBlocks;
    }

    public InquiryTeacherAnswer getInquiryTeacherAnswer() {
        return getProfessorship().getInquiryTeacherAnswer();
    }
}

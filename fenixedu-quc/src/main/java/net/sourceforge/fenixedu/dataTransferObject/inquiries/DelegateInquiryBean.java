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
package org.fenixedu.academic.dto.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.inquiries.CurricularCourseInquiryTemplate;
import org.fenixedu.academic.domain.inquiries.DelegateInquiryTemplate;
import org.fenixedu.academic.domain.inquiries.InquiryBlock;
import org.fenixedu.academic.domain.inquiries.InquiryDelegateAnswer;
import org.fenixedu.academic.domain.inquiries.InquiryResult;
import org.fenixedu.academic.domain.inquiries.InquiryResultComment;
import org.fenixedu.academic.domain.inquiries.QuestionAnswer;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;
import org.fenixedu.academic.domain.student.YearDelegate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class DelegateInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TeacherShiftTypeResultsBean> teachersResults;
    private List<BlockResultsSummaryBean> curricularBlockResults;
    private Set<InquiryBlockDTO> delegateInquiryBlocks;
    private InquiryDelegateAnswer inquiryDelegateAnswer;
    private YearDelegate yearDelegate;
    private ExecutionCourse executionCourse;
    private ExecutionDegree executionDegree;

    public DelegateInquiryBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree,
            DelegateInquiryTemplate delegateInquiryTemplate, List<InquiryResult> results, YearDelegate yearDelegate,
            InquiryDelegateAnswer inquiryDelegateAnswer) {
        setYearDelegate(yearDelegate);
        setExecutionCourse(executionCourse);
        setExecutionDegree(executionDegree);
        initCurricularBlocksResults(executionCourse, results, yearDelegate.getPerson());
        initTeachersResults(executionCourse, yearDelegate.getPerson());
        initDelegateInquiry(delegateInquiryTemplate, yearDelegate, executionCourse, inquiryDelegateAnswer);
    }

    private void initCurricularBlocksResults(ExecutionCourse executionCourse, List<InquiryResult> results, Person person) {
        CurricularCourseInquiryTemplate courseInquiryTemplate =
                CurricularCourseInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        setCurricularBlockResults(new ArrayList<BlockResultsSummaryBean>());
        for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocksSet()) {
            getCurricularBlockResults().add(
                    new BlockResultsSummaryBean(inquiryBlock, results, person, ResultPersonCategory.DELEGATE));
        }
        Collections.sort(getCurricularBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    private void initTeachersResults(ExecutionCourse executionCourse, Person person) {
        setTeachersResults(new ArrayList<TeacherShiftTypeResultsBean>());
        for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
            Collection<InquiryResult> professorshipResults = professorship.getInquiryResultsSet();
            if (!professorshipResults.isEmpty()) {
                for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                    List<InquiryResult> teacherShiftResults = InquiryResult.getInquiryResults(professorship, shiftType);
                    if (!teacherShiftResults.isEmpty()) {
                        getTeachersResults().add(
                                new TeacherShiftTypeResultsBean(professorship, shiftType, executionCourse.getExecutionPeriod(),
                                        teacherShiftResults, person, ResultPersonCategory.DELEGATE));
                    }
                }
            }
        }

        Collections.sort(getTeachersResults(), new BeanComparator("professorship.person.name"));
        Collections.sort(getTeachersResults(), new BeanComparator("shiftType"));
    }

    private void initDelegateInquiry(DelegateInquiryTemplate delegateInquiryTemplate, YearDelegate yearDelegate,
            ExecutionCourse executionCourse, InquiryDelegateAnswer inquiryDelegateAnswer) {
        setDelegateInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
        setInquiryDelegateAnswer(inquiryDelegateAnswer);
        for (InquiryBlock inquiryBlock : delegateInquiryTemplate.getInquiryBlocksSet()) {
            getDelegateInquiryBlocks().add(new InquiryBlockDTO(inquiryDelegateAnswer, inquiryBlock));
        }
    }

    private Set<ShiftType> getShiftTypes(Collection<InquiryResult> professorshipResults) {
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

    public Set<InquiryBlockDTO> getDelegateInquiryBlocks() {
        return delegateInquiryBlocks;
    }

    public void setDelegateInquiryBlocks(Set<InquiryBlockDTO> delegateInquiryBlocks) {
        this.delegateInquiryBlocks = delegateInquiryBlocks;
    }

    public boolean isValid() {
        return true;
    }

    public void setInquiryDelegateAnswer(InquiryDelegateAnswer inquiryDelegateAnswer) {
        this.inquiryDelegateAnswer = inquiryDelegateAnswer;
    }

    public InquiryDelegateAnswer getInquiryDelegateAnswer() {
        return inquiryDelegateAnswer;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public YearDelegate getYearDelegate() {
        return yearDelegate;
    }

    public void setYearDelegate(YearDelegate yearDelegate) {
        this.yearDelegate = yearDelegate;
    }

    @Atomic
    public void saveChanges(Person person, ResultPersonCategory delegate) {
        saveComments(person, delegate, getCurricularBlockResults());
        for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : getTeachersResults()) {
            saveComments(person, delegate, teacherShiftTypeResultsBean.getBlockResults());
        }
        for (InquiryBlockDTO blockDTO : getDelegateInquiryBlocks()) {
            for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
                for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                    if (!StringUtils.isEmpty(questionDTO.getResponseValue()) || questionDTO.getQuestionAnswer() != null) {
                        if (questionDTO.getQuestionAnswer() != null) {
                            questionDTO.getQuestionAnswer().setAnswer(questionDTO.getResponseValue());
                            questionDTO.getQuestionAnswer().getInquiryAnswer().setResponseDateTime(new DateTime());
                        } else {
                            if (getInquiryDelegateAnswer() == null) {
                                setInquiryDelegateAnswer(new InquiryDelegateAnswer(getYearDelegate(), getExecutionCourse(),
                                        getExecutionDegree()));
                            }
                            new QuestionAnswer(getInquiryDelegateAnswer(), questionDTO.getInquiryQuestion(),
                                    questionDTO.getFinalValue());
                            getInquiryDelegateAnswer().setResponseDateTime(new DateTime());
                        }
                    }
                }
            }
        }
    }

    private void saveComments(Person person, ResultPersonCategory delegate, List<BlockResultsSummaryBean> blocksResults) {
        for (BlockResultsSummaryBean blockResultsSummaryBean : blocksResults) {
            for (GroupResultsSummaryBean groupResultsSummaryBean : blockResultsSummaryBean.getGroupsResults()) {
                for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
                    InquiryResult questionResult = questionResultsSummaryBean.getQuestionResult();
                    if (questionResult != null) {
                        InquiryResultComment inquiryResultComment =
                                questionResultsSummaryBean.getQuestionResult().getInquiryResultComment(person, delegate);
                        if (!StringUtils.isEmpty(questionResultsSummaryBean.getEditableComment()) || inquiryResultComment != null) {
                            if (inquiryResultComment == null) {
                                inquiryResultComment =
                                        new InquiryResultComment(questionResult, person, delegate, questionResultsSummaryBean
                                                .getQuestionResult().getInquiryResultCommentsSet().size() + 1);
                            }
                            inquiryResultComment.setComment(questionResultsSummaryBean.getEditableComment());
                        }
                    }
                }
            }
        }
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }
}

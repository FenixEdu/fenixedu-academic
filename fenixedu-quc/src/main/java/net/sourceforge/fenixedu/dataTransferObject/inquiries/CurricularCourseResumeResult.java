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
import org.fenixedu.academic.domain.inquiries.DelegateInquiryTemplate;
import org.fenixedu.academic.domain.inquiries.InquiryAnswer;
import org.fenixedu.academic.domain.inquiries.InquiryConnectionType;
import org.fenixedu.academic.domain.inquiries.InquiryDelegateAnswer;
import org.fenixedu.academic.domain.inquiries.InquiryQuestion;
import org.fenixedu.academic.domain.inquiries.InquiryResult;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;
import org.fenixedu.academic.domain.student.YearDelegate;

import org.apache.commons.beanutils.BeanComparator;

public class CurricularCourseResumeResult extends BlockResumeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionCourse executionCourse;
    private ExecutionDegree executionDegree;
    private YearDelegate yearDelegate;
    private List<TeacherShiftTypeResultsBean> teachersResults;
    private boolean showAllComments;
    private boolean allowComment;

    public CurricularCourseResumeResult(ExecutionCourse executionCourse, ExecutionDegree executionDegree,
            YearDelegate yearDelegate) {
        setExecutionCourse(executionCourse);
        setExecutionDegree(executionDegree);
        setYearDelegate(yearDelegate);
        setPerson(yearDelegate.getPerson());
        setPersonCategory(ResultPersonCategory.DELEGATE);
        setFirstHeaderKey("label.inquiry.curricularUnit");
        setFirstPresentationName(executionCourse.getName());
        initResultBlocks();
        initTeachersResults(executionCourse);
    }

    public CurricularCourseResumeResult(ExecutionCourse executionCourse, ExecutionDegree executionDegree, String firstHeaderKey,
            String firstPresentationName, Person person, ResultPersonCategory personCategory, boolean regentViewHimself,
            boolean initTeachersResults, boolean backToResume, boolean showAllComments, boolean allowComment) {
        setExecutionCourse(executionCourse);
        setExecutionDegree(executionDegree);
        setFirstHeaderKey(firstHeaderKey);
        setFirstPresentationName(firstPresentationName);
        setPerson(person);
        setPersonCategory(personCategory);
        setRegentViewHimself(regentViewHimself);
        initResultBlocks();
        if (initTeachersResults) {
            initTeachersResults(executionCourse);
        }
        setBackToResume(backToResume);
        setShowAllComments(showAllComments);
        setAllowComment(allowComment);
    }

    @Override
    protected void initResultBlocks() {
        setResultBlocks(new TreeSet<InquiryResult>(new BeanComparator("inquiryQuestion.questionOrder")));
        for (InquiryResult inquiryResult : getExecutionCourse().getInquiryResultsSet()) {
            if ((inquiryResult.getExecutionDegree() == getExecutionDegree() || (inquiryResult.getExecutionDegree() == null && inquiryResult
                    .getProfessorship() == null)) && InquiryConnectionType.GROUP.equals(inquiryResult.getConnectionType())) { //change to COURSE_EVALUATION
                getResultBlocks().add(inquiryResult);
            }
        }
    }

    private void initTeachersResults(ExecutionCourse executionCourse) {
        setTeachersResults(new ArrayList<TeacherShiftTypeResultsBean>());
        for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
            Collection<InquiryResult> professorshipResults = professorship.getInquiryResultsSet();
            if (!professorshipResults.isEmpty()) {
                for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                    List<InquiryResult> teacherShiftResults = InquiryResult.getInquiryResults(professorship, shiftType);
                    if (!teacherShiftResults.isEmpty()) {
                        getTeachersResults().add(
                                new TeacherShiftTypeResultsBean(professorship, shiftType, executionCourse.getExecutionPeriod(),
                                        teacherShiftResults, null, null));
                    }
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

    @Override
    protected InquiryAnswer getInquiryAnswer() {
        InquiryDelegateAnswer inquiryDelegateAnswer = null;
        for (InquiryDelegateAnswer delegateAnswer : getYearDelegate().getInquiryDelegateAnswersSet()) {
            if (delegateAnswer.getExecutionCourse() == getExecutionCourse()) {
                inquiryDelegateAnswer = delegateAnswer;
            }
        }
        return inquiryDelegateAnswer;
    }

    @Override
    protected int getNumberOfInquiryQuestions() {
        DelegateInquiryTemplate inquiryTemplate =
                DelegateInquiryTemplate.getTemplateByExecutionPeriod(getExecutionCourse().getExecutionPeriod());
        return inquiryTemplate.getNumberOfQuestions();
    }

    @Override
    protected List<InquiryResult> getInquiryResultsByQuestion(InquiryQuestion inquiryQuestion) {
        List<InquiryResult> inquiryResults = new ArrayList<InquiryResult>();
        for (InquiryResult inquiryResult : getExecutionCourse().getInquiryResultsSet()) {
            if (inquiryResult.getExecutionDegree() == getExecutionDegree()
                    || (inquiryResult.getExecutionDegree() == null && inquiryResult.getShiftType() != null)) {
                if (inquiryResult.getInquiryQuestion() == inquiryQuestion && inquiryResult.getResultClassification() != null) {
                    inquiryResults.add(inquiryResult);
                }
            }
        }
        return inquiryResults;
    }

    @Override
    public int hashCode() {
        return getExecutionCourse().hashCode() + getExecutionDegree().hashCode();
    }

    public void setYearDelegate(YearDelegate yearDelegate) {
        this.yearDelegate = yearDelegate;
    }

    public YearDelegate getYearDelegate() {
        return yearDelegate;
    }

    public void setTeachersResults(List<TeacherShiftTypeResultsBean> teachersResults) {
        this.teachersResults = teachersResults;
    }

    public List<TeacherShiftTypeResultsBean> getTeachersResults() {
        return teachersResults;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setShowAllComments(boolean showAllComments) {
        this.showAllComments = showAllComments;
    }

    public boolean isShowAllComments() {
        return showAllComments;
    }

    public void setAllowComment(boolean allowComment) {
        this.allowComment = allowComment;
    }

    public boolean isAllowComment() {
        return allowComment;
    }
}
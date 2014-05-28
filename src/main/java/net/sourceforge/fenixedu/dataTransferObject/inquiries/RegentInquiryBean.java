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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryRegentAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class RegentInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsMap;
    private Map<ExecutionDegree, List<BlockResultsSummaryBean>> curricularBlockResultsMap;
    private Set<InquiryBlockDTO> regentInquiryBlocks;
    private Professorship professorship;

    public RegentInquiryBean(RegentInquiryTemplate regentInquiryTemplate, Professorship professorship) {
        setProfessorship(professorship);
        initCurricularBlocksResults(professorship.getExecutionCourse(), professorship.getPerson());
        initTeachersResults(professorship, professorship.getPerson());
        initRegentInquiry(regentInquiryTemplate, professorship);
    }

    private void initRegentInquiry(RegentInquiryTemplate regentInquiryTemplate, Professorship professorship) {
        setRegentInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
        for (InquiryBlock inquiryBlock : regentInquiryTemplate.getInquiryBlocks()) {
            getRegentInquiryBlocks().add(new InquiryBlockDTO(getInquiryRegentAnswer(), inquiryBlock));
        }

    }

    private void initCurricularBlocksResults(ExecutionCourse executionCourse, Person person) {
        setCurricularBlockResultsMap(new HashMap<ExecutionDegree, List<BlockResultsSummaryBean>>());
        CurricularCourseInquiryTemplate courseInquiryTemplate =
                CurricularCourseInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
            ArrayList<BlockResultsSummaryBean> blockResults = new ArrayList<BlockResultsSummaryBean>();
            List<InquiryResult> results = executionCourse.getInquiryResultsByExecutionDegreeAndForTeachers(executionDegree);
            if (results != null && results.size() > 5) {
                for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocks()) {
                    blockResults.add(new BlockResultsSummaryBean(inquiryBlock, results, person, ResultPersonCategory.REGENT));
                }
                Collections.sort(blockResults, new BeanComparator("inquiryBlock.blockOrder"));
                getCurricularBlockResultsMap().put(executionDegree, blockResults);
            }
        }
    }

    private void initTeachersResults(Professorship professorship, Person person) {
        setTeachersResultsMap(new HashMap<Professorship, List<TeacherShiftTypeResultsBean>>());
        for (Professorship teacherProfessorship : professorship.getExecutionCourse().getProfessorships()) {
            ArrayList<TeacherShiftTypeResultsBean> teachersResults = new ArrayList<TeacherShiftTypeResultsBean>();
            Collection<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResults();
            if (!professorshipResults.isEmpty()) {
                for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                    List<InquiryResult> teacherShiftResults = teacherProfessorship.getInquiryResults(shiftType);
                    if (!teacherShiftResults.isEmpty()) {
                        teachersResults.add(new TeacherShiftTypeResultsBean(teacherProfessorship, shiftType, teacherProfessorship
                                .getExecutionCourse().getExecutionPeriod(), teacherShiftResults, person,
                                ResultPersonCategory.REGENT));
                    }
                }
                Collections.sort(teachersResults, new BeanComparator("professorship.person.name"));
                Collections.sort(teachersResults, new BeanComparator("shiftType"));
                getTeachersResultsMap().put(teacherProfessorship, teachersResults);
            }
        }
    }

    private Set<ShiftType> getShiftTypes(Collection<InquiryResult> professorshipResults) {
        Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
        for (InquiryResult inquiryResult : professorshipResults) {
            shiftTypes.add(inquiryResult.getShiftType());
        }
        return shiftTypes;
    }

    public String validateInquiry() {
        String validationResult = null;
        for (InquiryBlockDTO inquiryBlockDTO : getRegentInquiryBlocks()) {
            validationResult = inquiryBlockDTO.validateMandatoryConditions(getRegentInquiryBlocks());
            if (!Boolean.valueOf(validationResult)) {
                return validationResult;
            }
        }
        return Boolean.toString(true);
    }

    @Atomic
    public void saveChanges(Person person, ResultPersonCategory regent) {
        for (ExecutionDegree executionDegree : getCurricularBlockResultsMap().keySet()) {
            saveComments(person, regent, getCurricularBlockResultsMap().get(executionDegree));
        }
        for (Professorship professorship : getTeachersResultsMap().keySet()) {
            for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : getTeachersResultsMap().get(professorship)) {
                saveComments(person, regent, teacherShiftTypeResultsBean.getBlockResults());
            }
        }
        for (InquiryBlockDTO blockDTO : getRegentInquiryBlocks()) {
            for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
                for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                    if (!StringUtils.isEmpty(questionDTO.getResponseValue()) || questionDTO.getQuestionAnswer() != null) {
                        if (questionDTO.getQuestionAnswer() != null) {
                            questionDTO.getQuestionAnswer().setAnswer(questionDTO.getResponseValue());
                            questionDTO.getQuestionAnswer().getInquiryAnswer().setResponseDateTime(new DateTime());
                        } else {
                            if (getInquiryRegentAnswer() == null) {
                                new InquiryRegentAnswer(getProfessorship());
                            }
                            new QuestionAnswer(getInquiryRegentAnswer(), questionDTO.getInquiryQuestion(),
                                    questionDTO.getFinalValue());
                            getInquiryRegentAnswer().setResponseDateTime(new DateTime());
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

    public void setRegentInquiryBlocks(Set<InquiryBlockDTO> regentInquiryBlocks) {
        this.regentInquiryBlocks = regentInquiryBlocks;
    }

    public Set<InquiryBlockDTO> getRegentInquiryBlocks() {
        return regentInquiryBlocks;
    }

    public InquiryRegentAnswer getInquiryRegentAnswer() {
        return getProfessorship().getInquiryRegentAnswer();
    }

    public void setCurricularBlockResultsMap(Map<ExecutionDegree, List<BlockResultsSummaryBean>> curricularBlockResultsMap) {
        this.curricularBlockResultsMap = curricularBlockResultsMap;
    }

    public Map<ExecutionDegree, List<BlockResultsSummaryBean>> getCurricularBlockResultsMap() {
        return curricularBlockResultsMap;
    }

    public void setTeachersResultsMap(Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsMap) {
        this.teachersResultsMap = teachersResultsMap;
    }

    public Map<Professorship, List<TeacherShiftTypeResultsBean>> getTeachersResultsMap() {
        return teachersResultsMap;
    }
}

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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentInquiryDTO implements Serializable {

    private InquiriesRegistry inquiriesRegistry;

    Map<TeacherDTO, Collection<? extends TeacherInquiryDTO>> teachersInquiries =
            new TreeMap<TeacherDTO, Collection<? extends TeacherInquiryDTO>>(new BeanComparator("name"));

    private InquiriesBlock firstPageFirstBlock;

    private InquiriesBlock firstPageSecondBlock;

    private InquiriesBlock firstPageThirdBlock;

    private InquiriesBlock firstPageFourthBlock;

    private InquiriesBlock studyMethodBlock;

    private InquiriesBlock firstPageFifthBlock;

    private InquiriesBlock secondPageFirstBlock;

    private InquiriesBlock secondPageSecondBlock;

    private InquiriesBlock secondPageThirdBlock;

    private DateTime startDateTime;

    private StudentInquiryDTO(InquiriesRegistry inquiriesRegistry) {

        this.inquiriesRegistry = inquiriesRegistry;
        this.startDateTime = new DateTime();

        buildQuestionBlocks();

        final ExecutionCourse executionCourse = inquiriesRegistry.getExecutionCourse();
        final Set<ShiftType> shiftTypes = executionCourse.getShiftTypes();

        fillTeachersInquiriesWithAffiliatedTeachers(executionCourse, shiftTypes);
        fillTeachersInquiriesWithNonAffiliatedTeachers(executionCourse, shiftTypes);

    }

    private void fillTeachersInquiriesWithNonAffiliatedTeachers(final ExecutionCourse executionCourse,
            final Set<ShiftType> shiftTypes) {
        for (final NonAffiliatedTeacher nonAffiliatedTeacher : executionCourse.getNonAffiliatedTeachers()) {
            final NonAffiliatedTeacherDTO nonAffiliatedTeacherDTO = new NonAffiliatedTeacherDTO(nonAffiliatedTeacher);
            Collection<TeacherInquiryDTO> nonAffiliatedTeachers = new ArrayList<TeacherInquiryDTO>();
            for (final ShiftType shiftType : shiftTypes) {
                nonAffiliatedTeachers.add(new TeacherInquiryDTO(nonAffiliatedTeacherDTO, executionCourse, shiftType));
            }
            teachersInquiries.put(nonAffiliatedTeacherDTO, nonAffiliatedTeachers);
        }
    }

    private void fillTeachersInquiriesWithAffiliatedTeachers(final ExecutionCourse executionCourse,
            final Set<ShiftType> shiftTypes) {
        Map<Person, Map<ShiftType, TeacherInquiryDTO>> teachersShifts = new HashMap<Person, Map<ShiftType, TeacherInquiryDTO>>();
        for (final Professorship professorship : executionCourse.getProfessorships()) {

            final Person person = professorship.getPerson();
            if (!teachersShifts.containsKey(person)) {
                teachersShifts.put(person, new HashMap<ShiftType, TeacherInquiryDTO>());
            }

            final Map<ShiftType, TeacherInquiryDTO> teacherShift = teachersShifts.get(person);
            final TeacherDTO teacherDTO = new AffiliatedTeacherDTO(person);

            for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServices()) {
                for (ShiftType shiftType : degreeTeachingService.getShift().getTypes()) {
                    if (!teacherShift.containsKey(shiftType)) {
                        teacherShift.put(shiftType, new TeacherInquiryDTO(teacherDTO, executionCourse, shiftType));
                    }
                }
            }

            if (teacherShift.isEmpty()) {
                for (final ShiftType shiftType : shiftTypes) {
                    teacherShift.put(shiftType, new TeacherInquiryDTO(teacherDTO, executionCourse, shiftType));
                }
            }

        }

        for (Entry<Person, Map<ShiftType, TeacherInquiryDTO>> entry : teachersShifts.entrySet()) {
            teachersInquiries.put(new AffiliatedTeacherDTO(entry.getKey()), new ArrayList<TeacherInquiryDTO>(entry.getValue()
                    .values()));
        }
    }

    public static StudentInquiryDTO makeNew(InquiriesRegistry inquiriesRegistry) {
        return new StudentInquiryDTO(inquiriesRegistry);
    }

    public InquiriesBlock getFirstBlock() {
        return firstPageFirstBlock;
    }

    public Map<TeacherDTO, Collection<? extends TeacherInquiryDTO>> getTeachersInquiries() {
        return teachersInquiries;
    }

    public InquiriesBlock getFirstPageFirstBlock() {
        return firstPageFirstBlock;
    }

    public InquiriesBlock getFirstPageSecondBlock() {
        return firstPageSecondBlock;
    }

    public InquiriesBlock getFirstPageThirdBlock() {
        return firstPageThirdBlock;
    }

    public InquiriesBlock getFirstPageFourthBlock() {
        return firstPageFourthBlock;
    }

    public InquiriesBlock getFirstPageFifthBlock() {
        return firstPageFifthBlock;
    }

    public InquiriesBlock getSecondPageFirstBlock() {
        return secondPageFirstBlock;
    }

    public InquiriesBlock getSecondPageSecondBlock() {
        return secondPageSecondBlock;
    }

    public InquiriesBlock getSecondPageThirdBlock() {
        return secondPageThirdBlock;
    }

    public InquiriesRegistry getInquiriesRegistry() {
        return inquiriesRegistry;
    }

    public long getAnswerDuration() {
        return this.startDateTime == null ? 0 : new DateTime().getMillis() - this.startDateTime.getMillis();
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
        final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();

        retrieveAnswersFromBlock(answers, firstPageFirstBlock, fullLabels);
        retrieveAnswersFromBlock(answers, firstPageSecondBlock, fullLabels);
        retrieveAnswersFromBlock(answers, firstPageThirdBlock, fullLabels);
        retrieveAnswersFromBlock(answers, firstPageFourthBlock, fullLabels);
        retrieveAnswersFromBlock(answers, studyMethodBlock, fullLabels);
        retrieveAnswersFromBlock(answers, firstPageFifthBlock, fullLabels);
        retrieveAnswersFromBlock(answers, secondPageFirstBlock, fullLabels);
        retrieveAnswersFromBlock(answers, secondPageSecondBlock, fullLabels);
        retrieveAnswersFromBlock(answers, secondPageThirdBlock, fullLabels);

        return answers;
    }

    static void retrieveAnswersFromBlock(final Map<String, InquiriesQuestion> answers, InquiriesBlock inquiriesBlock,
            boolean fullLabels) {
        for (final InquiriesQuestion inquiriesQuestion : inquiriesBlock.getQuestions()) {
            if (fullLabels) {
                answers.put(inquiriesQuestion.getLabel(), inquiriesQuestion);
            } else {
                final String label = inquiriesQuestion.getLabel();
                answers.put(label.substring(label.lastIndexOf('.') + 1), inquiriesQuestion);
            }
        }
    }

    private void buildQuestionBlocks() {
        this.firstPageFirstBlock =
                new InquiriesBlock(StringUtils.EMPTY, false, "header.studentInquiries.firstPageFirstBlock.nonEvaluated",
                        "header.studentInquiries.firstPageFirstBlock.flunked",
                        "header.studentInquiries.firstPageFirstBlock.10_12", "header.studentInquiries.firstPageFirstBlock.13_14",
                        "header.studentInquiries.firstPageFirstBlock.15_16", "header.studentInquiries.firstPageFirstBlock.17_18",
                        "header.studentInquiries.firstPageFirstBlock.19_20");
        this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFirstBlock.classificationInThisCU", false, "nonEvaluated", "flunked", "10_12",
                "13_14", "15_16", "17_18", "19_20"));

        this.firstPageSecondBlock = new InquiriesBlock("title.studentInquiries.firstPageSecondBlock", false);
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonComplexProjects"));
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonExtenseProjects"));
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonManyProjects"));
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonLackOfPreviousPreparation"));
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonCurricularProgramExtension"));
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonLackOfAttendanceOfLessons"));
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonCuOrganizationProblems"));
        this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonPersonalOrTeamProblems"));
        this.firstPageSecondBlock.addQuestion(new TextBoxQuestion(
                "label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonOtherReasons", false));

        this.firstPageThirdBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.studentInquiries.firstPageThirdBlock.totallyDisagree",
                        "header.studentInquiries.two", "header.studentInquiries.firstPageThirdBlock.disagree",
                        "header.studentInquiries.four", "header.studentInquiries.firstPageThirdBlock.neitherAgreeOrDisagree",
                        "header.studentInquiries.six", "header.studentInquiries.firstPageThirdBlock.agree",
                        "header.studentInquiries.eight", "header.studentInquiries.firstPageThirdBlock.totallyAgree");
        this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageThirdBlock.previousKnowledgeEnoughToCUAttendance", 1, 9, false)
                .setToolTip("tooltip.studentInquiries.firstPageThirdBlock.previousKnowledgeEnoughToCUAttendance"));

        this.firstPageFourthBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.studentInquiries.firstPageFourthBlock.passive",
                        "header.studentInquiries.firstPageFourthBlock.activeWhenRequired",
                        "header.studentInquiries.firstPageFourthBlock.activeByOwnWill");
        this.firstPageFourthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFourthBlock.activityParticipation", 1, 3, false));

        this.studyMethodBlock = new InquiriesBlock("label.studentInquiries.studyMethod", false);
        this.studyMethodBlock.addQuestion(new CheckBoxQuestion("label.studentInquiries.studyMethodAttendToClasses"));
        this.studyMethodBlock.addQuestion(new CheckBoxQuestion("label.studentInquiries.studyMethodSuggestedBibliography"));
        this.studyMethodBlock.addQuestion(new CheckBoxQuestion("label.studentInquiries.studyMethodTeacherDocuments"));
        this.studyMethodBlock.addQuestion(new CheckBoxQuestion("label.studentInquiries.studyMethodStudentDocuments"));
        this.studyMethodBlock.addQuestion(new TextBoxQuestion("label.studentInquiries.studyMethodOther", false));

        this.firstPageFifthBlock =
                new InquiriesBlock("header.studentInquiries.firstPageFifthBlock", true,
                        "header.studentInquiries.firstPageFifthBlock.unknown",
                        "header.studentInquiries.firstPageFifthBlock.doesntApply", "header.studentInquiries.totallyDisagree",
                        "header.studentInquiries.two", "header.studentInquiries.disagree", "header.studentInquiries.four",
                        "header.studentInquiries.neitherAgreeOrDisagree", "header.studentInquiries.six",
                        "header.studentInquiries.agree", "header.studentInquiries.eight", "header.studentInquiries.totallyAgree");
        this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFifthBlock.knowledgeAndComprehensionOfCU", -1, 9, false));
        this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFifthBlock.knowledgeApplicationOfCU", -1, 9, false));
        this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFifthBlock.criticSenseAndReflexiveSpirit", -1, 9, false));
        this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFifthBlock.cooperationAndComunicationCapacity", -1, 9, false));
        this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFifthBlock.autonomousLearningCapacity", -1, 9, false));
        this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.firstPageFifthBlock.socialAndProfessionalContextAnalysis", -1, 9, false));

        this.secondPageFirstBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.studentInquiries.secondPageFirstBlock.totallyDisagree",
                        "header.studentInquiries.two", "header.studentInquiries.secondPageFirstBlock.disagree",
                        "header.studentInquiries.four", "header.studentInquiries.secondPageFirstBlock.neitherAgreeOrDisagree",
                        "header.studentInquiries.six", "header.studentInquiries.secondPageFirstBlock.agree",
                        "header.studentInquiries.eight", "header.studentInquiries.secondPageFirstBlock.totallyAgree");
        this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.secondPageFirstBlock.predictedProgramTeached", 1, 9, false));
        this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.secondPageFirstBlock.wellStructuredOfCU", 1, 9, false)
                .setToolTip("tooltip.studentInquiries.secondPageFirstBlock.wellStructuredOfCU"));
        this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.secondPageFirstBlock.recomendendBibliographyImportance", 1, 9, false)
                .setToolTip("tooltip.studentInquiries.secondPageFirstBlock.recomendendBibliographyImportance"));
        this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.secondPageFirstBlock.goodGuidanceMaterial", 1, 9, false));

        this.secondPageSecondBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.studentInquiries.secondPageFirstBlock.totallyDisagree",
                        "header.studentInquiries.two", "header.studentInquiries.secondPageFirstBlock.disagree",
                        "header.studentInquiries.four", "header.studentInquiries.secondPageFirstBlock.neitherAgreeOrDisagree",
                        "header.studentInquiries.six", "header.studentInquiries.secondPageFirstBlock.agree",
                        "header.studentInquiries.eight", "header.studentInquiries.secondPageFirstBlock.totallyAgree");
        this.secondPageSecondBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.secondPageSecondBlock.fairEvaluationMethods", 1, 9, false)
                .setToolTip("tooltip.studentInquiries.secondPageSecondBlock.fairEvaluationMethods"));

        this.secondPageThirdBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.studentInquiries.secondPageThirdBlock.veryBad",
                        "header.studentInquiries.two", "header.studentInquiries.secondPageThirdBlock.bad",
                        "header.studentInquiries.four", "header.studentInquiries.secondPageThirdBlock.neitherGoodOrBad",
                        "header.studentInquiries.six", "header.studentInquiries.secondPageThirdBlock.good",
                        "header.studentInquiries.eight", "header.studentInquiries.secondPageThirdBlock.veryGood");
        this.secondPageThirdBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.secondPageThirdBlock.globalClassificationOfCU", 1, 9, false));
    }

    public InquiriesBlock getStudyMethodBlock() {
        return studyMethodBlock;
    }

}

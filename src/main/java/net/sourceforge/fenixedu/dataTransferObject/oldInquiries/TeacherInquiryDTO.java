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
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TeacherInquiryDTO implements Serializable {

    private InquiriesBlock thirdPageFirstBlock;

    private InquiriesBlock thirdPageThirdBlock;

    private InquiriesBlock thirdPageFourthBlock;

    private InquiriesBlock thirdPageFifthBlock;

    private InquiriesBlock thirdPageSixthBlock;

    private ExecutionCourse executionCourse;

    private TeacherDTO teacherDTO;

    private ShiftType shiftType;

    private boolean filled = false;

    protected TeacherInquiryDTO(final TeacherDTO teacherDTO, final ExecutionCourse executionCourse, final ShiftType shiftType) {
        this.executionCourse = executionCourse;
        this.shiftType = shiftType;
        this.teacherDTO = teacherDTO;
        buildQuestionBlocks();
    }

    public InquiriesBlock getThirdPageFirstBlock() {
        return thirdPageFirstBlock;
    }

    public InquiriesBlock getThirdPageThirdBlock() {
        return thirdPageThirdBlock;
    }

    public InquiriesBlock getThirdPageFourthBlock() {
        return thirdPageFourthBlock;
    }

    public InquiriesBlock getThirdPageFifthBlock() {
        return thirdPageFifthBlock;
    }

    public InquiriesBlock getThirdPageSixthBlock() {
        return thirdPageSixthBlock;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public TeacherDTO getTeacherDTO() {
        return teacherDTO;
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
        final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();

        StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageFirstBlock, fullLabels);
        StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageThirdBlock, fullLabels);
        StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageFourthBlock, fullLabels);
        StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageFifthBlock, fullLabels);
        StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageSixthBlock, fullLabels);

        return answers;
    }

    private void buildQuestionBlocks() {

        this.thirdPageFirstBlock =
                new InquiriesBlock(StringUtils.EMPTY, false, "header.studentInquiries.thirdPageFirstBlock.never",
                        "header.studentInquiries.two", "header.studentInquiries.thirdPageFirstBlock.sometimes",
                        "header.studentInquiries.four", "header.studentInquiries.thirdPageFirstBlock.often",
                        "header.studentInquiries.six", "header.studentInquiries.thirdPageFirstBlock.always") {

                    @Override
                    public boolean validate() {
                        if (!super.validate()) {
                            return false;
                        }

                        if (getQuestion("label.studentInquiries.thirdPageFirstBlock.classesFrequency").getValueAsInteger() <= 3) {

                            if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonSchedule")
                                    .isEmpty()) {
                                return true;
                            }
                            if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonTeacher")
                                    .isEmpty()) {
                                return true;
                            }
                            if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonContents")
                                    .isEmpty()) {
                                return true;
                            }
                            if (!getQuestion(
                                    "label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonFlunkeeStudent")
                                    .isEmpty()) {
                                return true;
                            }
                            if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonOther")
                                    .isEmpty()) {
                                return true;
                            }

                            return false;
                        } else {
                            return true;
                        }
                    }
                };

        this.thirdPageFirstBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageFirstBlock.classesFrequency", 1, 7, false).setRequired(true));

        QuestionHeader thirdPageSecondBlockHeader = new QuestionHeader("title.studentInquiries.thirdPageSecondBlock");
        this.thirdPageFirstBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonSchedule", thirdPageSecondBlockHeader));
        this.thirdPageFirstBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonTeacher"));
        this.thirdPageFirstBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonContents"));
        this.thirdPageFirstBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonFlunkeeStudent"));
        this.thirdPageFirstBlock.addQuestion(new CheckBoxQuestion(
                "label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonOther"));

        this.thirdPageThirdBlock =
                new InquiriesBlock(StringUtils.EMPTY, false, "header.studentInquiries.thirdPage.totallyDisagree",
                        "header.studentInquiries.two", "header.studentInquiries.thirdPage.disagree",
                        "header.studentInquiries.four", "header.studentInquiries.thirdPage.neitherAgreeOrDisagree",
                        "header.studentInquiries.six", "header.studentInquiries.thirdPage.agree",
                        "header.studentInquiries.eight", "header.studentInquiries.thirdPage.totallyAgree");
        this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageThirdBlock.teacherAcomplishedScheduleAndActivities", 1, 9, false));
        this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageThirdBlock.suitedClassesRythm", 1, 9, false)
                .setToolTip("toolTip.studentInquiries.thirdPageThirdBlock.suitedClassesRythm"));

        this.thirdPageFourthBlock =
                new InquiriesBlock(StringUtils.EMPTY, false, "header.studentInquiries.thirdPage.totallyDisagree",
                        "header.studentInquiries.two", "header.studentInquiries.thirdPage.disagree",
                        "header.studentInquiries.four", "header.studentInquiries.thirdPage.neitherAgreeOrDisagree",
                        "header.studentInquiries.six", "header.studentInquiries.thirdPage.agree",
                        "header.studentInquiries.eight", "header.studentInquiries.thirdPage.totallyAgree");
        this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageFourthBlock.teacherCommited", 1, 9, false)
                .setToolTip("toolTip.studentInquiries.thirdPageFourthBlock.teacherCommited"));
        this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageFourthBlock.teacherExposedContentsAtractively", 1, 9, false));
        this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageFourthBlock.teacherShowedSecurity", 1, 9, false));
        this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageFourthBlock.teacherExposedContentsClearly", 1, 9, false));

        this.thirdPageFifthBlock =
                new InquiriesBlock(StringUtils.EMPTY, false, "header.studentInquiries.thirdPage.totallyDisagree",
                        "header.studentInquiries.two", "header.studentInquiries.thirdPage.disagree",
                        "header.studentInquiries.four", "header.studentInquiries.thirdPage.neitherAgreeOrDisagree",
                        "header.studentInquiries.six", "header.studentInquiries.thirdPage.agree",
                        "header.studentInquiries.eight", "header.studentInquiries.thirdPage.totallyAgree");
        this.thirdPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageFifthBlock.teacherStimulatedParticipation", 1, 9, false));
        this.thirdPageFifthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageFifthBlock.teacherOpenToClearDoubts", 1, 9, false));

        this.thirdPageSixthBlock =
                new InquiriesBlock(StringUtils.EMPTY, false, "header.studentInquiries.veryBad", "header.studentInquiries.two",
                        "header.studentInquiries.bad", "header.studentInquiries.four",
                        "header.studentInquiries.neitherGoodOrBad", "header.studentInquiries.six",
                        "header.studentInquiries.good", "header.studentInquiries.eight", "header.studentInquiries.veryGood");
        this.thirdPageSixthBlock.addQuestion(new RadioGroupQuestion(
                "label.studentInquiries.thirdPageSixthBlock.teacherGlobalClassification", 1, 9, false));
    }
}

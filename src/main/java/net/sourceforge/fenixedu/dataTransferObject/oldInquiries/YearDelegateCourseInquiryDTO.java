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
import net.sourceforge.fenixedu.domain.student.YearDelegate;
import net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class YearDelegateCourseInquiryDTO implements Serializable {

    private ExecutionCourse executionCourse;

    private YearDelegate delegate;

    private InquiriesBlock firstBlock;

    private InquiriesBlock secondBlock;

    private InquiriesBlock thirdBlock;

    private InquiriesBlock fourthBlock;

    private InquiriesBlock fifthBlock;

    private InquiriesBlock sixthBlock;

    private InquiriesBlock seventhBlock;

    private InquiriesBlock eighthBlock;

    private DateTime startDateTime;

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public YearDelegate getDelegate() {
        return delegate;
    }

    public long getAnswerDuration() {
        return this.startDateTime == null ? 0 : new DateTime().getMillis() - this.startDateTime.getMillis();
    }

    public YearDelegateCourseInquiryDTO(final ExecutionCourse executionCourse, final YearDelegate delegate) {
        super();
        this.executionCourse = executionCourse;
        this.delegate = delegate;
        this.startDateTime = new DateTime();
        buildQuestionBlocks(null);
    }

    public YearDelegateCourseInquiryDTO(final YearDelegateCourseInquiry inquiry) {
        super();
        this.executionCourse = inquiry.getExecutionCourse();
        this.delegate = inquiry.getDelegate();
        this.startDateTime = new DateTime();
        buildQuestionBlocks(inquiry);
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
        final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();
        retrieveAnswersFromBlock(answers, firstBlock, fullLabels);
        retrieveAnswersFromBlock(answers, secondBlock, fullLabels);
        retrieveAnswersFromBlock(answers, thirdBlock, fullLabels);
        retrieveAnswersFromBlock(answers, fourthBlock, fullLabels);
        retrieveAnswersFromBlock(answers, fifthBlock, fullLabels);
        retrieveAnswersFromBlock(answers, sixthBlock, fullLabels);
        retrieveAnswersFromBlock(answers, seventhBlock, fullLabels);
        retrieveAnswersFromBlock(answers, eighthBlock, fullLabels);
        return answers;
    }

    static private void retrieveAnswersFromBlock(final Map<String, InquiriesQuestion> answers, InquiriesBlock inquiriesBlock,
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

    public boolean isValid() {
        return getFirstBlock().validate() && getSecondBlock().validate() && getThirdBlock().validate()
                && getFourthBlock().validate() && getSixthBlock().validate() && getSeventhBlock().validate()
                && getEighthBlock().validate();
    }

    private void buildQuestionBlocks(final YearDelegateCourseInquiry inquiry) {

        this.firstBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.yearDelegateInquiries.belowExpected",
                        "header.yearDelegateInquiries.expected", "header.yearDelegateInquiries.aboveExpected");
        this.firstBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.workLoadClassification", 1, 3, false)
                .setInitialValue(inquiry == null ? null : inquiry.getWorkLoadClassification()));
        this.firstBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.workLoadClassificationReasons", false)
                .setRequired(false).setInitialValue(inquiry == null ? null : inquiry.getWorkLoadClassificationReasons()));

        this.secondBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.yearDelegateInquiries.totallyDisagree",
                        "header.yearDelegateInquiries.two", "header.yearDelegateInquiries.disagree",
                        "header.yearDelegateInquiries.four", "header.yearDelegateInquiries.neitherAgreeOrDisagree",
                        "header.yearDelegateInquiries.six", "header.yearDelegateInquiries.agree",
                        "header.yearDelegateInquiries.eight", "header.yearDelegateInquiries.totallyAgree");
        this.secondBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.enoughOnlineCUInformation", 1, 9, false)
                .setInitialValue(inquiry == null ? null : inquiry.getEnoughOnlineCUInformation()));
        this.secondBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.enoughOnlineCUInformationReasons", false)
                .setRequired(false).setInitialValue(inquiry == null ? null : inquiry.getEnoughOnlineCUInformationReasons()));
        this.secondBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.clearOnlineCUInformation", 1, 9, false)
                .setInitialValue(inquiry == null ? null : inquiry.getClearOnlineCUInformation()));
        this.secondBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.clearOnlineCUInformationReasons", false)
                .setRequired(false).setRequired(false)
                .setInitialValue(inquiry == null ? null : inquiry.getClearOnlineCUInformationReasons()));
        this.secondBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.explicitEvaluationMethods", 1, 9, false)
                .setInitialValue(inquiry == null ? null : inquiry.getExplicitEvaluationMethods()));
        this.secondBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.explicitEvaluationMethodsReasons", false)
                .setRequired(false).setInitialValue(inquiry == null ? null : inquiry.getExplicitEvaluationMethodsReasons()));
        this.secondBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.evaluationMethodsWellApplied", 1, 9,
                false).setInitialValue(inquiry == null ? null : inquiry.getEvaluationMethodsWellApplied()));
        this.secondBlock
                .addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.evaluationMethodsWellAppliedReasons", false)
                        .setRequired(false).setInitialValue(
                                inquiry == null ? null : inquiry.getEvaluationMethodsWellAppliedReasons()));

        this.thirdBlock =
                new InquiriesBlock("title.yearDelegateInquiries.evaluationMethodsDisclosed", true,
                        "header.yearDelegateInquiries.yes", "header.yearDelegateInquiries.no");
        this.thirdBlock.addQuestion(new RadioGroupQuestion(
                "label.yearDelegateInquiries.evaluationMethodsDisclosedToWorkingStudents", false)
                .addChoice("YES", StringUtils.EMPTY).addChoice("NO", StringUtils.EMPTY)
                .setInitialValue(inquiry == null ? null : inquiry.getEvaluationMethodsDisclosedToWorkingStudents()));
        this.thirdBlock.addQuestion(new RadioGroupQuestion(
                "label.yearDelegateInquiries.evaluationMethodsDisclosedToSpecialSeasonStudents", false)
                .addChoice("YES", StringUtils.EMPTY).addChoice("NO", StringUtils.EMPTY)
                .setInitialValue(inquiry == null ? null : inquiry.getEvaluationMethodsDisclosedToSpecialSeasonStudents()));

        this.fourthBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.yearDelegateInquiries.totallyDisagree",
                        "header.yearDelegateInquiries.two", "header.yearDelegateInquiries.disagree",
                        "header.yearDelegateInquiries.four", "header.yearDelegateInquiries.neitherAgreeOrDisagree",
                        "header.yearDelegateInquiries.six", "header.yearDelegateInquiries.agree",
                        "header.yearDelegateInquiries.eight", "header.yearDelegateInquiries.totallyAgree");
        this.fourthBlock.addQuestion(new RadioGroupQuestion(
                "label.yearDelegateInquiries.evaluationDatesScheduleActiveParticipation", 1, 9, false)
                .setInitialValue(inquiry == null ? null : inquiry.getEvaluationDatesScheduleActiveParticipation()));
        this.fourthBlock.addQuestion(new TextBoxQuestion(
                "label.yearDelegateInquiries.evaluationDatesScheduleActiveParticipationReasons", false).setRequired(false)
                .setRequired(false)
                .setInitialValue(inquiry == null ? null : inquiry.getEvaluationDatesScheduleActiveParticipationReasons()));
        this.fourthBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.supportMaterialAvailableOnTime", 1, 9,
                false).setInitialValue(inquiry == null ? null : inquiry.getSupportMaterialAvailableOnTime()));
        this.fourthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.supportMaterialAvailableOnTimeReasons",
                false).setRequired(false).setInitialValue(
                inquiry == null ? null : inquiry.getSupportMaterialAvailableOnTimeReasons()));
        this.fourthBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.previousKnowlegdeArticulation", 1, 9,
                false).setInitialValue(inquiry == null ? null : inquiry.getPreviousKnowlegdeArticulation()));
        this.fourthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.previousKnowlegdeArticulationReasons",
                false).setRequired(false).setInitialValue(
                inquiry == null ? null : inquiry.getPreviousKnowlegdeArticulationReasons()));

        this.fifthBlock = new InquiriesBlock(false);
        this.fifthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.suggestedBestPractices", true).setRequired(
                false).setInitialValue(inquiry == null ? null : inquiry.getSuggestedBestPractices()));

        this.sixthBlock = new InquiriesBlock(true);
        this.sixthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.strongAndWeakPointsOfCUTeachingProcess",
                true).setInitialValue(inquiry == null ? null : inquiry.getStrongAndWeakPointsOfCUTeachingProcess()));

        this.seventhBlock = new InquiriesBlock(true);
        this.seventhBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.finalCommentsAndImproovements", true)
                .setInitialValue(inquiry == null ? null : inquiry.getFinalCommentsAndImproovements()));

        this.eighthBlock =
                new InquiriesBlock(StringUtils.EMPTY, true, "header.yearDelegateInquiries.yes", "header.yearDelegateInquiries.no");
        final RadioGroupQuestion reportDisclosureAuthorizationRadio =
                new RadioGroupQuestion("label.yearDelegateInquiries.reportDisclosureAuthorization", false);
        reportDisclosureAuthorizationRadio
                .setValue(inquiry != null && inquiry.getReportDisclosureAuthorization() != null ? inquiry
                        .getReportDisclosureAuthorization() : "YES");
        this.eighthBlock.addQuestion(reportDisclosureAuthorizationRadio.addChoice("YES", StringUtils.EMPTY).addChoice("NO",
                StringUtils.EMPTY));
    }

    public InquiriesBlock getFirstBlock() {
        return firstBlock;
    }

    public void setFirstBlock(InquiriesBlock firstBlock) {
        this.firstBlock = firstBlock;
    }

    public InquiriesBlock getSecondBlock() {
        return secondBlock;
    }

    public void setSecondBlock(InquiriesBlock secondBlock) {
        this.secondBlock = secondBlock;
    }

    public InquiriesBlock getThirdBlock() {
        return thirdBlock;
    }

    public void setThirdBlock(InquiriesBlock thirdBlock) {
        this.thirdBlock = thirdBlock;
    }

    public InquiriesBlock getFourthBlock() {
        return fourthBlock;
    }

    public void setFourthBlock(InquiriesBlock fourthBlock) {
        this.fourthBlock = fourthBlock;
    }

    public InquiriesBlock getFifthBlock() {
        return fifthBlock;
    }

    public void setFifthBlock(InquiriesBlock fifthBlock) {
        this.fifthBlock = fifthBlock;
    }

    public InquiriesBlock getSixthBlock() {
        return sixthBlock;
    }

    public void setSixthBlock(InquiriesBlock sixthBlock) {
        this.sixthBlock = sixthBlock;
    }

    public InquiriesBlock getSeventhBlock() {
        return seventhBlock;
    }

    public void setSeventhBlock(InquiriesBlock seventhBlock) {
        this.seventhBlock = seventhBlock;
    }

    public InquiriesBlock getEighthBlock() {
        return eighthBlock;
    }

    public void setEighthBlock(InquiriesBlock eighthBlock) {
        this.eighthBlock = eighthBlock;
    }

}

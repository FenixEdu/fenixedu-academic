/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class TeacherInquiryDTO implements Serializable {

    private InquiriesBlock thirdPageFirstBlock;

    private InquiriesBlock thirdPageThirdBlock;

    private InquiriesBlock thirdPageSixthBlock;

    private DomainReference<ExecutionCourse> executionCourse;

    private ShiftType shiftType;

    private boolean filled = false;

    protected TeacherInquiryDTO(final ExecutionCourse executionCourse, ShiftType shiftType) {
	this.executionCourse = new DomainReference<ExecutionCourse>(executionCourse);
	this.shiftType = shiftType;
	buildQuestionBlocks();
    }

    public InquiriesBlock getThirdPageFirstBlock() {
	return thirdPageFirstBlock;
    }

    public InquiriesBlock getThirdPageThirdBlock() {
	return thirdPageThirdBlock;
    }

    public InquiriesBlock getThirdPageSixthBlock() {
	return thirdPageSixthBlock;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse.getObject();
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

    public abstract String getTeacherName();

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
	final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();

	StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageFirstBlock, fullLabels);
	StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageThirdBlock, fullLabels);
	StudentInquiryDTO.retrieveAnswersFromBlock(answers, thirdPageSixthBlock, fullLabels);

	return answers;
    }

    private void buildQuestionBlocks() {
	this.thirdPageFirstBlock = new InquiriesBlock("title.studentInquiries.thirdPageFirstBlock",
		"header.studentInquiries.thirdPageFirstBlock.never", StringUtils.EMPTY,
		"header.studentInquiries.thirdPageFirstBlock.sometimes", StringUtils.EMPTY,
		"header.studentInquiries.thirdPageFirstBlock.often", StringUtils.EMPTY,
		"header.studentInquiries.thirdPageFirstBlock.always");
	this.thirdPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFirstBlock.classesFrequency", 1, 7, true));

	QuestionHeader thirdPageSecondBlockHeader = new QuestionHeader("title.studentInquiries.thirdPageSecondBlock",
		StringUtils.EMPTY);
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

	this.thirdPageThirdBlock = new InquiriesBlock(StringUtils.EMPTY, "header.studentInquiries.thirdPage.totallyDisagree",
		StringUtils.EMPTY, "header.studentInquiries.thirdPage.disagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.agree", StringUtils.EMPTY, "header.studentInquiries.thirdPage.totallyAgree");
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageThirdBlock.teacherAcomplishedScheduleAndActivities", 1, 9, true));
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageThirdBlock.suitedClassesRythm", 1, 9, true));

	QuestionHeader thirdPageFourthBlockHeader = new QuestionHeader("title.studentInquiries.thirdPageFourthBlock",
		"header.studentInquiries.secondPageFirstBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.disagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.agree", StringUtils.EMPTY, "header.studentInquiries.thirdPage.totallyAgree");
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherCommited", 1, 9, true, thirdPageFourthBlockHeader));
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherExposedContentsAtractively", 1, 9, true));
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherShowedSecurity", 1, 9, true));
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherExposedContentsClearly", 1, 9, true));

	QuestionHeader thirdPageFifthBlockHeader = new QuestionHeader("title.studentInquiries.thirdPageFifthBlock",
		"header.studentInquiries.secondPageFirstBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.disagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.agree", StringUtils.EMPTY, "header.studentInquiries.thirdPage.totallyAgree");
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFifthBlock.teacherStimulatedParticipation", 1, 9, true,
		thirdPageFifthBlockHeader));
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFifthBlock.teacherOpenToClearDoubts", 1, 9, true));

	this.thirdPageSixthBlock = new InquiriesBlock("title.studentInquiries.thirdPageFirstBlock",
		"header.studentInquiries.thirdPageSixthBlock.improving", "header.studentInquiries.thirdPageSixthBlock.good",
		"header.studentInquiries.thirdPageSixthBlock.veryGood");
	this.thirdPageSixthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageSixthBlock.teacherGlobalClassification", 1, 3, true));
    }

}

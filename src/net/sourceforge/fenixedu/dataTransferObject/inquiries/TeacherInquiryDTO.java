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
public class TeacherInquiryDTO implements Serializable {

    private InquiriesBlock thirdPageFirstBlock;

    private InquiriesBlock thirdPageThirdBlock;

    private InquiriesBlock thirdPageFourthBlock;

    private InquiriesBlock thirdPageFifthBlock;

    private InquiriesBlock thirdPageSixthBlock;

    private DomainReference<ExecutionCourse> executionCourse;

    private TeacherDTO teacherDTO;

    private ShiftType shiftType;

    private boolean filled = false;

    protected TeacherInquiryDTO(final TeacherDTO teacherDTO, final ExecutionCourse executionCourse, final ShiftType shiftType) {
	this.executionCourse = new DomainReference<ExecutionCourse>(executionCourse);
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

	this.thirdPageFirstBlock = new InquiriesBlock(StringUtils.EMPTY, false,
		"header.studentInquiries.thirdPageFirstBlock.never", StringUtils.EMPTY,
		"header.studentInquiries.thirdPageFirstBlock.sometimes", StringUtils.EMPTY,
		"header.studentInquiries.thirdPageFirstBlock.often", StringUtils.EMPTY,
		"header.studentInquiries.thirdPageFirstBlock.always") {

	    @Override
	    public boolean validate() {
		if (!super.validate()) {
		    return false;
		}

		if (getQuestion("label.studentInquiries.thirdPageFirstBlock.classesFrequency").getValueAsInteger() < 3) {

		    if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonSchedule").isEmpty()) {
			return true;
		    }
		    if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonTeacher").isEmpty()) {
			return true;
		    }
		    if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonContents").isEmpty()) {
			return true;
		    }
		    if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonFlunkeeStudent")
			    .isEmpty()) {
			return true;
		    }
		    if (!getQuestion("label.studentInquiries.thirdPageSecondBlock.lowClassesFrequencyReasonOther").isEmpty()) {
			return true;
		    }

		    return false;
		} else {
		    return true;
		}
	    }
	};

	this.thirdPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFirstBlock.classesFrequency", 1, 7, true).setRequired(true));

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

	this.thirdPageThirdBlock = new InquiriesBlock(StringUtils.EMPTY, false,
		"header.studentInquiries.thirdPage.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.disagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.agree", StringUtils.EMPTY, "header.studentInquiries.thirdPage.totallyAgree");
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageThirdBlock.teacherAcomplishedScheduleAndActivities", 1, 9, true));
	this.thirdPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageThirdBlock.suitedClassesRythm", 1, 9, true)
		.setToolTip("toolTip.studentInquiries.thirdPageThirdBlock.suitedClassesRythm"));

	this.thirdPageFourthBlock = new InquiriesBlock(StringUtils.EMPTY, false,
		"header.studentInquiries.thirdPage.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.disagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.agree", StringUtils.EMPTY, "header.studentInquiries.thirdPage.totallyAgree");
	this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherCommited", 1, 9, true)
		.setToolTip("toolTip.studentInquiries.thirdPageFourthBlock.teacherCommited"));
	this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherExposedContentsAtractively", 1, 9, true));
	this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherShowedSecurity", 1, 9, true));
	this.thirdPageFourthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFourthBlock.teacherExposedContentsClearly", 1, 9, true));

	this.thirdPageFifthBlock = new InquiriesBlock(StringUtils.EMPTY, false,
		"header.studentInquiries.thirdPage.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.disagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.thirdPage.agree", StringUtils.EMPTY, "header.studentInquiries.thirdPage.totallyAgree");
	this.thirdPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFifthBlock.teacherStimulatedParticipation", 1, 9, true));
	this.thirdPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageFifthBlock.teacherOpenToClearDoubts", 1, 9, true));

	this.thirdPageSixthBlock = new InquiriesBlock(StringUtils.EMPTY, false,
		"header.studentInquiries.thirdPageSixthBlock.improving", "header.studentInquiries.thirdPageSixthBlock.good",
		"header.studentInquiries.thirdPageSixthBlock.veryGood");
	this.thirdPageSixthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.thirdPageSixthBlock.teacherGlobalClassification", 1, 3, true));
    }
}

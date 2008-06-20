/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;

import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentInquiryDTO implements Serializable {

    private DomainReference<InquiriesRegistry> inquiriesRegistry;

    private Collection<TeacherInquiryDTO> teachersInquiries = new ArrayList<TeacherInquiryDTO>();

    private InquiriesBlock firstPageFirstBlock;

    private InquiriesBlock firstPageSecondBlock;

    private InquiriesBlock firstPageThirdBlock;

    private InquiriesBlock firstPageFourthBlock;

    private InquiriesBlock firstPageFifthBlock;

    private InquiriesBlock secondBlock;

    private StudentInquiryDTO(InquiriesRegistry inquiriesRegistry) {

	this.inquiriesRegistry = new DomainReference<InquiriesRegistry>(inquiriesRegistry);

	buildQuestionBlocks();

	final ExecutionCourse executionCourse = inquiriesRegistry.getExecutionCourse();

	final Set<ShiftType> shiftTypes = executionCourse.getShiftTypes();

	for (final Professorship professorship : executionCourse.getProfessorships()) {
	    for (final ShiftType shiftType : shiftTypes) {
		this.teachersInquiries
			.add(new AffiliatedTeacherInquiryDTO(professorship.getTeacher(), executionCourse, shiftType));
	    }
	}

	for (final ShiftType shiftType : shiftTypes) {
	    for (final NonAffiliatedTeacher nonAffiliatedTeacher : executionCourse.getNonAffiliatedTeachers()) {
		this.teachersInquiries.add(new NonAffiliatedTeacherInquiryDTO(nonAffiliatedTeacher, executionCourse, shiftType));
	    }
	}

    }

    public static StudentInquiryDTO makeNew(InquiriesRegistry inquiriesRegistry) {
	return new StudentInquiryDTO(inquiriesRegistry);
    }

    public InquiriesBlock getFirstBlock() {
	return firstPageFirstBlock;
    }

    public InquiriesBlock getSecondBlock() {
	return secondBlock;
    }

    public Collection<TeacherInquiryDTO> getTeachersInquiries() {
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

    public InquiriesRegistry getInquiriesRegistry() {
	return inquiriesRegistry.getObject();
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
	final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();

	retrieveAnswersFromBlock(answers, firstPageFirstBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageThirdBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageFourthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageFifthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondBlock, fullLabels);

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
	this.firstPageFirstBlock = new InquiriesBlock("title.studentInquiries.firstPageFirstBlock",
		"header.studentInquiries.firstPageFirstBlock.nonEvaluated",
		"header.studentInquiries.firstPageFirstBlock.flunked", "header.studentInquiries.firstPageFirstBlock.10_12",
		"header.studentInquiries.firstPageFirstBlock.13_14", "header.studentInquiries.firstPageFirstBlock.15_16",
		"header.studentInquiries.firstPageFirstBlock.17_18", "header.studentInquiries.firstPageFirstBlock.19_20");
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFirstBlock.classificationInThisCU", false, "nonEvaluated", "flunked", "10_12",
		"13_14", "15_16", "17_18", "19_20"));

	this.firstPageSecondBlock = new InquiriesBlock("title.studentInquiries.firstPageSecondBlock");
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
	this.firstPageSecondBlock.addQuestion(new TextBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonOtherReasons"));

	this.firstPageThirdBlock = new InquiriesBlock(StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.disagree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.agree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.totallyAgree");
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageThirdBlock.previousKnowledgeEnoughToCUAttendance", 1, 9, true));

	this.firstPageFourthBlock = new InquiriesBlock(StringUtils.EMPTY, "header.studentInquiries.firstPageFourthBlock.passive",
		"header.studentInquiries.firstPageFourthBlock.activeWhenRequired",
		"header.studentInquiries.firstPageFourthBlock.activeByOwnWill");
	this.firstPageFourthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFourthBlock.activityParticipation", 1, 3, true));

	this.firstPageFifthBlock = new InquiriesBlock("title.studentInquiries.firstPageFifthBlock",
		"header.studentInquiries.firstPageFifthBlock.unknown",
		"header.studentInquiries.firstPageFifthBlock.didNotContribute",
		"header.studentInquiries.firstPageFifthBlock.didContribute",
		"header.studentInquiries.firstPageFifthBlock.contributedAlot");
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.knowledgeAndComprehensionOfCU", 0, 3, true));
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.knowledgeApplicationOfCU", 0, 3, true));
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.criticSenseAndReflexiveSpirit", 0, 3, true));
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.cooperationAndComunicationCapacity", 0, 3, true));

	this.secondBlock = new InquiriesBlock("title.studentInquiries.secondPageFirstBlock",
		"header.studentInquiries.secondPageFirstBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.disagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.agree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.totallyAgree");
	this.secondBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageFirstBlock.predictedProgramTeached", 1, 9, true));
	this.secondBlock.addQuestion(new RadioGroupQuestion("label.studentInquiries.secondPageFirstBlock.wellStructuredOfCU", 1,
		9, true));
	this.secondBlock.addQuestion(new RadioGroupQuestion("label.studentInquiries.secondPageFirstBlock.goodGuidanceMaterial",
		1, 9, true));
	this.secondBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageFirstBlock.recomendendBibliographyImportance", 1, 9, true));

	QuestionHeader secondPageSecondBlockHeader = new QuestionHeader("title.studentInquiries.secondPageSecondBlock",
		"header.studentInquiries.secondPageFirstBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.disagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.agree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.totallyAgree");
	this.secondBlock.addQuestion(new RadioGroupQuestion("label.studentInquiries.secondPageSecondBlock.fairEvaluationMethods",
		1, 9, true, secondPageSecondBlockHeader));

	QuestionHeader secondPageThirdBlockHeader = new QuestionHeader(StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.veryBad", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.bad", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.neitherGoodOrBad", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.good", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.veryGood");
	this.secondBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageThirdBlock.globalClassificationOfCU", 1, 9, true, secondPageThirdBlockHeader));
    }

}

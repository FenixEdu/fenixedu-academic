/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TeachingInquiryDTO implements Serializable {

    private DomainReference<Professorship> professorship;

    private InquiriesBlock firstPageFirstBlock;

    private InquiriesBlock firstPageSecondBlockFirstPart;

    private InquiriesBlock firstPageSecondBlockSecondPart;

    private InquiriesBlock firstPageSecondBlockThirdPart;

    private InquiriesBlock firstPageSecondBlockFourthPart;

    private InquiriesBlock firstPageThirdBlock;

    private InquiriesBlock secondPageFourthBlock;

    private InquiriesBlock secondPageFifthBlockFirstPart;

    private InquiriesBlock secondPageFifthBlockSecondPart;

    private InquiriesBlock secondPageSixthBlock;

    private InquiriesBlock secondPageSeventhBlock;

    private InquiriesBlock secondPageEighthBlock;

    private InquiriesBlock thirdPageNinthBlock;

    private InquiriesBlock secondPageFourthBlockThirdPart;

    private DateTime startDateTime;

    public TeachingInquiryDTO(Professorship professorship) {
	setProfessorship(professorship);
	this.startDateTime = new DateTime();
	buildQuestionBlocks();
    }

    public long getAnswerDuration() {
	return this.startDateTime == null ? 0 : new DateTime().getMillis() - this.startDateTime.getMillis();
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
	final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();
	retrieveAnswersFromBlock(answers, firstPageFirstBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlockFirstPart, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlockSecondPart, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlockThirdPart, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlockFourthPart, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageThirdBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFourthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFifthBlockFirstPart, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFifthBlockSecondPart, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageSixthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageSeventhBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageEighthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, thirdPageNinthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFourthBlockThirdPart, fullLabels);
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

	this.firstPageFirstBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.totallyDisagree",
		"header.teachingInquiries.two", "header.teachingInquiries.disagree", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherAgreeOrDisagree", "header.teachingInquiries.six",
		"header.teachingInquiries.agree", "header.teachingInquiries.eight", "header.teachingInquiries.totallyAgree");
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.loadAndClassTypeContributionToFullfilmentOfCUProgram", 1, 9, false));
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.teacherNumberSuitableForCUOperation", 1, 9, false));
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.establishedScheduleSuitable", 1, 9,
		false));
	this.firstPageFirstBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.establishedScheduleNotSuitableReason",
		false).setRequired(false));

	this.firstPageSecondBlockFirstPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.totallyDisagree", "header.teachingInquiries.two", "header.teachingInquiries.disagree",
		"header.teachingInquiries.four", "header.teachingInquiries.neitherAgreeOrDisagree",
		"header.teachingInquiries.six", "header.teachingInquiries.agree", "header.teachingInquiries.eight",
		"header.teachingInquiries.totallyAgree");
	this.firstPageSecondBlockFirstPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.studentsReadyForFollowingCU", 1, 9, false));
	this.firstPageSecondBlockFirstPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.evaluationMethodSuitableForCUTeachingTypeAndObjective", 1, 9, false));
	this.firstPageSecondBlockFirstPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.disturbingEventsInClasses", 1, 9, false));
	this.firstPageSecondBlockFirstPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.disturbingEventsInClassesDescription", false).setRequired(false));

	this.firstPageSecondBlockSecondPart = new InquiriesBlock("title.teachingInquiries.attendsOscillation", true,
		"header.teachingInquiries.notAppliable", "header.teachingInquiries.totallyDisagree",
		"header.teachingInquiries.two", "header.teachingInquiries.disagree", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherAgreeOrDisagree", "header.teachingInquiries.six",
		"header.teachingInquiries.agree", "header.teachingInquiries.eight", "header.teachingInquiries.totallyAgree");
	this.firstPageSecondBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.theoricalClassesAttendsOscillation", 0, 9, false));
	this.firstPageSecondBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.problemsClassesAttendsOscillation", 0, 9, false));
	this.firstPageSecondBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.labsClassesAttendsOscillation", 0, 9, false));
	this.firstPageSecondBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.seminarClassesAttendsOscillation", 0, 9, false));
	this.firstPageSecondBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.projectClassesAttendsOscillation", 0, 9, false));

	this.firstPageSecondBlockThirdPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.lessFivePercent", "header.teachingInquiries.twentyFivePercent",
		"header.teachingInquiries.fiftyPercent", "header.teachingInquiries.seventyFivePercent",
		"header.teachingInquiries.moreNinetyFivePercent");
	this.firstPageSecondBlockThirdPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.activeAndInteressedStudentsRatio", false).addChoice("5", StringUtils.EMPTY).addChoice(
		"25", StringUtils.EMPTY).addChoice("50", StringUtils.EMPTY).addChoice("75", StringUtils.EMPTY).addChoice("95",
		StringUtils.EMPTY));

	this.firstPageSecondBlockFourthPart = new InquiriesBlock("title.teachingInquiries.rateFromNonSatisfatoryToExcellent",
		true, "header.teachingInquiries.nonSatisfatory", "header.teachingInquiries.two",
		"header.teachingInquiries.three", "header.teachingInquiries.excellent");
	this.firstPageSecondBlockFourthPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.studentsPerformance", 1,
		4, false));

	this.firstPageThirdBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.nonSatisfatory",
		"header.teachingInquiries.two", "header.teachingInquiries.three", "header.teachingInquiries.excellent");
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.classesAndOtherActivitiesFrequency",
		1, 4, false));
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.classesAndOtherActivitiesPonctuality", 1, 4, false));
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.globalQualityOfTeachingInCU", 1, 4,
		false));
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.pedagogicalActivitiesDeveloped", 1,
		4, false));
	this.firstPageThirdBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.relativePedagogicalInitiatives", true)
		.setRequired(false));

	this.secondPageFourthBlock = new InquiriesBlock("title.teachingInquiries.cuEvaluationMethod.writtenProofs", true,
		"header.teachingInquiries.number");
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfExams", false).setInteger(
		true).setToolTip("tooltip.teachingInquiries.numberOfExams"));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfTests", false).setInteger(
		true).setToolTip("tooltip.teachingInquiries.numberOfTests"));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfQuizzesAndMiniTests", false)
		.setInteger(true).setToolTip("tooltip.teachingInquiries.numberOfQuizzesAndMiniTests"));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfElectronicQuizzes", false)
		.setInteger(true).setToolTip("tooltip.teachingInquiries.numberOfElectronicQuizzes"));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.numberOfStudyVisitsOrOtherActivitiesReports", false).setInteger(true).setHeader(
		new QuestionHeader("title.teachingInquiries.cuEvaluationMethod.worksOrProjects",
			"header.teachingInquiries.number")));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfWorksOrProjects", false)
		.setInteger(true).setToolTip("tooltip.teachingInquiries.numberOfWorksOrProjects"));

	this.secondPageFourthBlockThirdPart = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.language.pt",
		"header.teachingInquiries.language.en", "header.teachingInquiries.language.both",
		"header.teachingInquiries.language.others");
	this.secondPageFourthBlockThirdPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.teachingLanguage", false)
		.addChoice("PT", StringUtils.EMPTY).addChoice("EN", StringUtils.EMPTY).addChoice("BOTH", StringUtils.EMPTY)
		.addChoice("OTHER", StringUtils.EMPTY));

	this.secondPageFifthBlockFirstPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.belowExpected", "header.teachingInquiries.expected",
		"header.teachingInquiries.aboveExpected");
	this.secondPageFifthBlockFirstPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.workLoadClassification",
		1, 3, false));
	this.secondPageFifthBlockFirstPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.workLoadClassificationReasons", false).setRequired(false));

	this.secondPageFifthBlockSecondPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.totallyDisagree", "header.teachingInquiries.two", "header.teachingInquiries.disagree",
		"header.teachingInquiries.four", "header.teachingInquiries.neitherAgreeOrDisagree",
		"header.teachingInquiries.six", "header.teachingInquiries.agree", "header.teachingInquiries.eight",
		"header.teachingInquiries.totallyAgree");
	this.secondPageFifthBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.positionOfCUInStudentCurricularPlan", 1, 9, false));

	this.secondPageSixthBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.notAppliable",
		"header.teachingInquiries.withDifficulty", "header.teachingInquiries.withEase",
		"header.teachingInquiries.withQuiteEase");
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.comprehensionAndKnowledgeOfCU", 0,
		3, false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.comprehensionApplicationOfCU", 0,
		3, false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.criticalSenseAndReflexiveSpirit",
		0, 3, false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.cooperationAndCommunicationCapacity", 0, 3, false));

	this.secondPageSeventhBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.veryBad",
		"header.teachingInquiries.two", "header.teachingInquiries.bad", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherGoodOrBad", "header.teachingInquiries.six", "header.teachingInquiries.good",
		"header.teachingInquiries.eight", "header.teachingInquiries.veryGood");
	this.secondPageSeventhBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.globalClassificationOfThisCU", 1,
		9, false));

	this.secondPageEighthBlock = new InquiriesBlock(true);
	this.secondPageEighthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.strongPointsOfCUTeachingProcess",
		true));
	this.secondPageEighthBlock
		.addQuestion(new TextBoxQuestion("label.teachingInquiries.weakPointsOfCUTeachingProcess", true));
	this.secondPageEighthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.finalCommentsAndImproovements", true)
		.setRequired(false));

	this.thirdPageNinthBlock = new InquiriesBlock(true);
	this.thirdPageNinthBlock.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.negativeResultsResolutionAndImproovementPlanOfAction", true));

    }

    public Professorship getProfessorship() {
	return professorship == null ? null : professorship.getObject();
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = new DomainReference<Professorship>(professorship);
    }

    public InquiriesBlock getFirstPageFirstBlock() {
	return firstPageFirstBlock;
    }

    public void setFirstPageFirstBlock(InquiriesBlock firstPageFirstBlock) {
	this.firstPageFirstBlock = firstPageFirstBlock;
    }

    public InquiriesBlock getFirstPageSecondBlockFirstPart() {
	return firstPageSecondBlockFirstPart;
    }

    public void setFirstPageSecondBlockFirstPart(InquiriesBlock firstPageSecondBlockFirstPart) {
	this.firstPageSecondBlockFirstPart = firstPageSecondBlockFirstPart;
    }

    public InquiriesBlock getFirstPageSecondBlockSecondPart() {
	return firstPageSecondBlockSecondPart;
    }

    public void setFirstPageSecondBlockSecondPart(InquiriesBlock firstPageSecondBlockSecondPart) {
	this.firstPageSecondBlockSecondPart = firstPageSecondBlockSecondPart;
    }

    public InquiriesBlock getFirstPageSecondBlockThirdPart() {
	return firstPageSecondBlockThirdPart;
    }

    public void setFirstPageSecondBlockThirdPart(InquiriesBlock firstPageSecondBlockThirdPart) {
	this.firstPageSecondBlockThirdPart = firstPageSecondBlockThirdPart;
    }

    public InquiriesBlock getFirstPageSecondBlockFourthPart() {
	return firstPageSecondBlockFourthPart;
    }

    public void setFirstPageSecondBlockFourthPart(InquiriesBlock firstPageSecondBlockFourthPart) {
	this.firstPageSecondBlockFourthPart = firstPageSecondBlockFourthPart;
    }

    public InquiriesBlock getFirstPageThirdBlock() {
	return firstPageThirdBlock;
    }

    public void setFirstPageThirdBlock(InquiriesBlock firstPageThirdBlock) {
	this.firstPageThirdBlock = firstPageThirdBlock;
    }

    public InquiriesBlock getSecondPageFourthBlock() {
	return secondPageFourthBlock;
    }

    public void setSecondPageFourthBlock(InquiriesBlock secondPageFifthBlock) {
	this.secondPageFourthBlock = secondPageFifthBlock;
    }

    public InquiriesBlock getSecondPageFifthBlockFirstPart() {
	return secondPageFifthBlockFirstPart;
    }

    public void setSecondPageFifthBlockFirstPart(InquiriesBlock secondPageSixthBlockFirstPart) {
	this.secondPageFifthBlockFirstPart = secondPageSixthBlockFirstPart;
    }

    public InquiriesBlock getSecondPageFifthBlockSecondPart() {
	return secondPageFifthBlockSecondPart;
    }

    public void setSecondPageFifthBlockSecondPart(InquiriesBlock secondPageSixthBlockSecondPart) {
	this.secondPageFifthBlockSecondPart = secondPageSixthBlockSecondPart;
    }

    public InquiriesBlock getSecondPageSeventhBlock() {
	return secondPageSeventhBlock;
    }

    public void setSecondPageSeventhBlock(InquiriesBlock secondPageSeventhBlock) {
	this.secondPageSeventhBlock = secondPageSeventhBlock;
    }

    public InquiriesBlock getSecondPageEighthBlock() {
	return secondPageEighthBlock;
    }

    public void setSecondPageEighthBlock(InquiriesBlock secondPageEighthBlock) {
	this.secondPageEighthBlock = secondPageEighthBlock;
    }

    public InquiriesBlock getThirdPageNinthBlock() {
	return thirdPageNinthBlock;
    }

    public void setThirdPageNinthBlock(InquiriesBlock secondPageNinthBlock) {
	this.thirdPageNinthBlock = secondPageNinthBlock;
    }

    public InquiriesBlock getSecondPageSixthBlock() {
	return secondPageSixthBlock;
    }

    public void setSecondPageSixthBlock(InquiriesBlock secondPageSixthBlock) {
	this.secondPageSixthBlock = secondPageSixthBlock;
    }

    public InquiriesBlock getSecondPageFourthBlockThirdPart() {
	return secondPageFourthBlockThirdPart;
    }

    public void setSecondPageFourthBlockThirdPart(InquiriesBlock secondPageFourthBlockThirdPart) {
	this.secondPageFourthBlockThirdPart = secondPageFourthBlockThirdPart;
    }

}

/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TeachingInquiryDTO implements Serializable {

    private DomainReference<Professorship> professorship;

    private DomainReference<ExecutionDegree> executionDegree;

    private InquiriesBlock firstPageFirstBlock;

    private InquiriesBlock firstPageSecondBlockFirstPart;

    private InquiriesBlock firstPageSecondBlockSecondPart;

    private InquiriesBlock firstPageSecondBlockThirdPart;

    private InquiriesBlock firstPageSecondBlockFourthPart;

    private InquiriesBlock firstPageThirdBlock;

    private InquiriesBlock firstPageFourthBlock;

    private InquiriesBlock secondPageFifthBlock;

    private InquiriesBlock secondPageSixthBlockFirstPart;

    private InquiriesBlock secondPageSixthBlockSecondPart;

    private InquiriesBlock secondPageSeventhBlock;

    private InquiriesBlock secondPageEighthBlock;

    private InquiriesBlock secondPageNinthBlock;

    private InquiriesBlock secondPageTenthBlock;

    private InquiriesBlock secondPageEleventhBlock;

    public TeachingInquiryDTO(Professorship professorship, ExecutionDegree executionDegree) {
	setProfessorship(professorship);
	setExecutionDegree(executionDegree);
	buildQuestionBlocks();
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
		false));

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
		"label.teachingInquiries.disturbingEventsInClassesDescription", false));

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

	this.firstPageFourthBlock = new InquiriesBlock(true);
	this.firstPageFourthBlock
		.addQuestion(new TextBoxQuestion("label.teachingInquiries.relativePedagogicalInitiatives", true));

	this.secondPageFifthBlock = new InquiriesBlock("title.teachingInquiries.cuEvaluationMethod.writtenProofs", true,
		"header.teachingInquiries.number");
	this.secondPageFifthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfExams", false)
		.setToolTip("tooltip.teachingInquiries.numberOfExams"));
	this.secondPageFifthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfTests", false)
		.setToolTip("tooltip.teachingInquiries.numberOfTests"));
	this.secondPageFifthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfQuizzesAndMiniTests", false)
		.setToolTip("tooltip.teachingInquiries.numberOfQuizzesAndMiniTests"));
	this.secondPageFifthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfElectronicQuizzes", false)
		.setToolTip("tooltip.teachingInquiries.numberOfElectronicQuizzes"));

	this.secondPageFifthBlock.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.numberOfStudyVisitsOrOtherActivitiesReports", false).setHeader(new QuestionHeader(
		"title.teachingInquiries.cuEvaluationMethod.worksOrProjects", "header.teachingInquiries.number")));
	this.secondPageFifthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfWorksOrProjects", false)
		.setToolTip("tooltip.teachingInquiries.numberOfWorksOrProjects"));

	this.secondPageSixthBlockFirstPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.belowExpected", "header.teachingInquiries.expected",
		"header.teachingInquiries.aboveExpected");
	this.secondPageSixthBlockFirstPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.workLoadClassification",
		1, 3, false));
	this.secondPageSixthBlockFirstPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.workLoadClassificationReasons", false));

	this.secondPageSixthBlockSecondPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.totallyDisagree", "header.teachingInquiries.two", "header.teachingInquiries.disagree",
		"header.teachingInquiries.four", "header.teachingInquiries.neitherAgreeOrDisagree",
		"header.teachingInquiries.six", "header.teachingInquiries.agree", "header.teachingInquiries.eight",
		"header.teachingInquiries.totallyAgree");
	this.secondPageSixthBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.positionOfCUInStudentCurricularPlan", 1, 9, false));

	this.secondPageSeventhBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.notAppliable",
		"header.teachingInquiries.withDifficulty", "header.teachingInquiries.withEase",
		"header.teachingInquiries.withQuiteEase");
	this.secondPageSeventhBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.comprehensionAndKnowledgeOfCU",
		0, 3, false));
	this.secondPageSeventhBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.comprehensionApplicationOfCU", 0,
		3, false));
	this.secondPageSeventhBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.criticalSenseAndReflexiveSpirit",
		0, 3, false));
	this.secondPageSeventhBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.cooperationAndCommunicationCapacity", 0, 3, false));

	this.secondPageEighthBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.veryBad",
		"header.teachingInquiries.two", "header.teachingInquiries.bad", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherGoodOrBad", "header.teachingInquiries.six", "header.teachingInquiries.good",
		"header.teachingInquiries.eight", "header.teachingInquiries.veryGood");
	this.secondPageEighthBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.globalClassificationOfThisCU", 1,
		9, false));

	this.secondPageNinthBlock = new InquiriesBlock(true);
	this.secondPageNinthBlock.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.weakAndStrongPointsOfCUTeachingProcess", true));

	this.secondPageTenthBlock = new InquiriesBlock(true);
	this.secondPageTenthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.finalCommentsAndImproovements", true));

	this.secondPageEleventhBlock = new InquiriesBlock(true);
	this.secondPageEleventhBlock.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.negativeResultsResolutionAndImproovementPlanOfAction", true));

    }

    public Professorship getProfessorship() {
	return professorship == null ? null : professorship.getObject();
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = new DomainReference<Professorship>(professorship);
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree == null ? null : executionDegree.getObject();
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = new DomainReference<ExecutionDegree>(executionDegree);
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

    public InquiriesBlock getFirstPageFourthBlock() {
	return firstPageFourthBlock;
    }

    public void setFirstPageFourthBlock(InquiriesBlock firstPageFourthBlock) {
	this.firstPageFourthBlock = firstPageFourthBlock;
    }

    public InquiriesBlock getSecondPageFifthBlock() {
	return secondPageFifthBlock;
    }

    public void setSecondPageFifthBlock(InquiriesBlock secondPageFifthBlock) {
	this.secondPageFifthBlock = secondPageFifthBlock;
    }

    public InquiriesBlock getSecondPageSixthBlockFirstPart() {
	return secondPageSixthBlockFirstPart;
    }

    public void setSecondPageSixthBlockFirstPart(InquiriesBlock secondPageSixthBlockFirstPart) {
	this.secondPageSixthBlockFirstPart = secondPageSixthBlockFirstPart;
    }

    public InquiriesBlock getSecondPageSixthBlockSecondPart() {
	return secondPageSixthBlockSecondPart;
    }

    public void setSecondPageSixthBlockSecondPart(InquiriesBlock secondPageSixthBlockSecondPart) {
	this.secondPageSixthBlockSecondPart = secondPageSixthBlockSecondPart;
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

    public InquiriesBlock getSecondPageNinthBlock() {
	return secondPageNinthBlock;
    }

    public void setSecondPageNinthBlock(InquiriesBlock secondPageNinthBlock) {
	this.secondPageNinthBlock = secondPageNinthBlock;
    }

    public InquiriesBlock getSecondPageTenthBlock() {
	return secondPageTenthBlock;
    }

    public void setSecondPageTenthBlock(InquiriesBlock secondPageTenthBlock) {
	this.secondPageTenthBlock = secondPageTenthBlock;
    }

    public InquiriesBlock getSecondPageEleventhBlock() {
	return secondPageEleventhBlock;
    }

    public void setSecondPageEleventhBlock(InquiriesBlock secondPageEleventhBlock) {
	this.secondPageEleventhBlock = secondPageEleventhBlock;
    }

}

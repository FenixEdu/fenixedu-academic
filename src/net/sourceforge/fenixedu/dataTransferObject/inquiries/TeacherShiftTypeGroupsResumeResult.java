package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryConnectionType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;

public class TeacherShiftTypeGroupsResumeResult extends BlockResumeResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private Professorship professorship;
	private ShiftType shiftType;
	private InquiryResult globalTeacherResult;

	public TeacherShiftTypeGroupsResumeResult(Professorship professorship, ShiftType shiftType,
			ResultPersonCategory personCategory, String firstHeaderKey, String firstPresentationName, boolean regentViewHimself) {
		setProfessorship(professorship);
		setShiftType(shiftType);
		setPerson(professorship.getPerson());
		setPersonCategory(personCategory);
		setFirstHeaderKey(firstHeaderKey);
		setFirstPresentationName(firstPresentationName);
		setRegentViewHimself(regentViewHimself);
		initResultBlocks();
	}

	@Override
	protected void initResultBlocks() {
		setResultBlocks(new TreeSet<InquiryResult>(new BeanComparator("inquiryQuestion.questionOrder")));
		for (InquiryResult inquiryResult : getProfessorship().getInquiryResults(getShiftType())) {
			if (InquiryConnectionType.GROUP.equals(inquiryResult.getConnectionType())
					&& !inquiryResult.getInquiryQuestion().getAssociatedBlocks().isEmpty()) { //change to TEACHER_SHIFT_EVALUATION
				getResultBlocks().add(inquiryResult);
			} else if (InquiryResultType.TEACHER_SHIFT_TYPE.equals(inquiryResult.getResultType())) {
				setGlobalTeacherResult(inquiryResult);
			}
		}
	}

	@Override
	protected InquiryAnswer getInquiryAnswer() {
		return getProfessorship().getInquiryTeacherAnswer();
	}

	@Override
	protected int getNumberOfInquiryQuestions() {
		TeacherInquiryTemplate inquiryTemplate =
				TeacherInquiryTemplate.getTemplateByExecutionPeriod(getProfessorship().getExecutionCourse().getExecutionPeriod());
		return inquiryTemplate.getNumberOfRequiredQuestions();
	}

	@Override
	protected List<InquiryResult> getInquiryResultsByQuestion(InquiryQuestion inquiryQuestion) {
		List<InquiryResult> inquiryResults = new ArrayList<InquiryResult>();
		for (InquiryResult inquiryResult : getProfessorship().getInquiryResults(getShiftType())) {
			if (inquiryResult.getInquiryQuestion() == inquiryQuestion && inquiryResult.getResultClassification() != null) {
				inquiryResults.add(inquiryResult);
			}
		}
		return inquiryResults;
	}

	public void setProfessorship(Professorship professorship) {
		this.professorship = professorship;
	}

	public Professorship getProfessorship() {
		return professorship;
	}

	public void setShiftType(ShiftType shiftType) {
		this.shiftType = shiftType;
	}

	public ShiftType getShiftType() {
		return shiftType;
	}

	public void setGlobalTeacherResult(InquiryResult globalTeacherResult) {
		this.globalTeacherResult = globalTeacherResult;
	}

	public InquiryResult getGlobalTeacherResult() {
		return globalTeacherResult;
	}
}
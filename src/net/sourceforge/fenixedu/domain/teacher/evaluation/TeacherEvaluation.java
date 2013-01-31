package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.EmailAddressList;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public abstract class TeacherEvaluation extends TeacherEvaluation_Base {

	public TeacherEvaluation() {
		super();
		setCreatedDate(new DateTime());
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public TeacherEvaluationState getState() {
		if (getTeacherEvaluationProcess().getFacultyEvaluationProcess().getAutoEvaluationInterval().getStart().isAfterNow()) {
			return null;
		} else if (getAutoEvaluationLock() == null) {
			return TeacherEvaluationState.AUTO_EVALUATION;
		} else if (getEvaluationLock() == null) {
			return TeacherEvaluationState.EVALUATION;
		} else {
			return TeacherEvaluationState.EVALUATED;
		}
	}

	public abstract TeacherEvaluationType getType();

	public abstract Set<TeacherEvaluationFileType> getAutoEvaluationFileSet();

	public abstract Set<TeacherEvaluationFileType> getEvaluationFileSet();

	public abstract String getFilenameTypePrefix();

	public TeacherEvaluationMark getApprovedEvaluationMark() {
		return getTeacherEvaluationProcess().getApprovedEvaluationMark();
	}

	public void setApprovedEvaluationMark(TeacherEvaluationMark mark) {
		getTeacherEvaluationProcess().setApprovedEvaluationMark(mark);
	}

	@Service
	public void lickAutoEvaluationStamp() {
		internalLickingBusiness();
	}

	protected void internalLickingBusiness() {
		setAutoEvaluationLock(new DateTime());
	}

	@Service
	public void lickEvaluationStamp() {
		setEvaluationLock(new DateTime());

		final TeacherEvaluationProcess teacherEvaluationProcess = getTeacherEvaluationProcess();
		final Person evaluee = teacherEvaluationProcess.getEvaluee();
		if (evaluee != AccessControl.getPerson()) {
			final Person evaluator = teacherEvaluationProcess.getEvaluator();
			final Recipient recipient = new Recipient(Collections.singletonList(evaluee));
			final Recipient ccRecipient = new Recipient(Collections.singletonList(evaluator));
			final FacultyEvaluationProcess facultyEvaluationProcess = teacherEvaluationProcess.getFacultyEvaluationProcess();
			final String title = facultyEvaluationProcess.getTitle().getContent();
			final String body =
					BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
							"message.email.stamp.teacher.evaluation.process", title);
			final SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
			final Message message =
					new Message(systemSender, Collections.EMPTY_LIST, Collections.EMPTY_LIST, title, body, new EmailAddressList(
							Collections.EMPTY_LIST).toString());
			message.addTos(recipient);
			message.addCcs(ccRecipient);
		}
	}

	public void delete() {
		removeTeacherEvaluationProcess();
		for (final TeacherEvaluationFile teacherEvaluationFile : getTeacherEvaluationFileSet()) {
			teacherEvaluationFile.delete();
		}
		removeRootDomainObject();
		deleteDomainObject();
	}

	@Service
	public void rubAutoEvaluationStamp() {
		setAutoEvaluationLock(null);
	}

	@Service
	public void rubEvaluationStamp() {
		setEvaluationLock(null);
	}

	public abstract void copyAutoEvaluation();

	protected void internalCopyAutoEvaluation(TeacherEvaluation copy) {
		copy.setAutoEvaluationLock(getAutoEvaluationLock());
		for (TeacherEvaluationFile file : getTeacherEvaluationFileSet()) {
			if (file.getTeacherEvaluationFileType().isAutoEvaluationFile()) {
				file.copy(copy);
			}
		}
	}
}

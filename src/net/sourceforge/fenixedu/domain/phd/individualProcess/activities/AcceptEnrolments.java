package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;

public class AcceptEnrolments extends PhdIndividualProgramProcessActivity {

    @Override
    public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

	if (!process.isCoordinatorForPhdProgram(userView.getPerson())) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {

	final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) object;

	for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
	    if (process.getRegistration().hasEnrolments(enrolment)) {
		enrolment.setEnrolmentCondition(EnrollmentCondition.VALIDATED);
	    }
	}

	AlertService.alertStudent(process, AlertMessage.create(bean.getMailSubject()).isKey(false),
		AlertMessage.create(buildBody(bean)).isKey(false));

	// TODO: wich group should be used in academic office?
	// AlertService.alertAcademicOffice(process, permissionType,
	// subjectKey, bodyKey)

	return process;
    }

    private String buildBody(ManageEnrolmentsBean bean) {
	final StringBuilder sb = new StringBuilder();
	sb.append(AlertService.getMessageFromResource("label.phd.accepted.enrolments")).append("\n");
	for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
	    sb.append("- ").append(enrolment.getPresentationName()).append(enrolment.getExecutionPeriod().getQualifiedName())
		    .append("\n");
	}
	return sb.toString();
    }

}
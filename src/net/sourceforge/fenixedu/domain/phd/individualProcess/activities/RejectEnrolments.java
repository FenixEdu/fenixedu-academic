package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class RejectEnrolments extends PhdIndividualProgramProcessActivity {

	@Override
	public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

		if (!process.isCoordinatorForPhdProgram(userView.getPerson())) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {

		final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) object;
		final StudentCurricularPlan scp = process.getRegistration().getLastStudentCurricularPlan();
		final String mailBody = buildBody(bean);

		scp.enrol(bean.getSemester(), Collections.<IDegreeModuleToEvaluate> emptySet(),
				getCurriculumModules(bean.getEnrolmentsToValidate()), CurricularRuleLevel.ENROLMENT_WITH_RULES);

		AlertService.alertStudent(process, AlertMessage.create(bean.getMailSubject()).isKey(false), AlertMessage.create(mailBody)
				.isKey(false));

		// TODO: wich group should be used in academic office?
		// AlertService.alertAcademicOffice(process, permissionType,
		// subjectKey, bodyKey)

		return process;
	}

	private String buildBody(ManageEnrolmentsBean bean) {
		final StringBuilder sb = new StringBuilder();
		sb.append(AlertService.getMessageFromResource("label.phd.rejected.enrolments")).append("\n");
		for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
			sb.append("- ").append(enrolment.getPresentationName()).append(enrolment.getExecutionPeriod().getQualifiedName())
					.append("\n");
		}
		return sb.toString();
	}

	private List<CurriculumModule> getCurriculumModules(List<Enrolment> enrolmentsToValidate) {
		return new ArrayList<CurriculumModule>(enrolmentsToValidate);
	}

}
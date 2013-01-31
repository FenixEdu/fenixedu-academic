package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodManagementBean;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeEnrolmentPeriodValues extends FenixService {

	@Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
	@Service
	public static void run(final EnrolmentPeriodManagementBean periodManagementBean) {
		final Date startDate = periodManagementBean.getBegin().toDate();
		final Date endDate = periodManagementBean.getEnd().toDate();

		EnrolmentPeriodType enrolmentPeriodType = periodManagementBean.getType();
		ExecutionSemester executionSemester = periodManagementBean.getExecutionSemester();

		List<DegreeCurricularPlan> degreeCurricularPlanList = periodManagementBean.getDegreeCurricularPlanList();

		for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlanList) {
			List<EnrolmentPeriod> enrolmentPeriods = degreeCurricularPlan.getEnrolmentPeriods();

			for (EnrolmentPeriod enrolmentPeriod : enrolmentPeriods) {
				if (enrolmentPeriod.getExecutionPeriod() != executionSemester) {
					continue;
				}

				if (!enrolmentPeriodType.is(enrolmentPeriod)) {
					continue;
				}

				enrolmentPeriod.setStartDate(startDate);
				enrolmentPeriod.setEndDate(endDate);
			}
		}

	}

}
package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdDegreeCurricularPlansProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyArrayConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {

		Set<AcademicProgram> programs =
				AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
						AcademicOperationType.MANAGE_PHD_ENROLMENT_PERIODS);

		final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

		final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

		for (final ExecutionDegree executionDegree : bean.getSemester().getExecutionYear()
				.getExecutionDegreesByType(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)) {

			final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

			if (!hasEnrolmentPeriod(degreeCurricularPlan, bean.getSemester())
					&& programs.contains(degreeCurricularPlan.getDegree().getPhdProgram())) {
				result.add(degreeCurricularPlan);
			}

		}
		return result;
	}

	private boolean hasEnrolmentPeriod(DegreeCurricularPlan degreeCurricularPlan, ExecutionSemester semester) {
		return semester.getEnrolmentPeriod(EnrolmentPeriodInCurricularCourses.class, degreeCurricularPlan) != null;
	}
}

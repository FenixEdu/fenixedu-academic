package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class RegistrationConclusionProcess {

	@Service
	public static void run(final RegistrationConclusionBean conclusionBean) {
		final Registration registration = conclusionBean.getRegistration();

		if (registration.isBolonha()) {
			final CycleCurriculumGroup cycleCurriculumGroup = conclusionBean.getCycleCurriculumGroup();

			registration.conclude(cycleCurriculumGroup);

			if (conclusionBean.hasEnteredConclusionDate()) {

				checkEnteredConclusionDate(conclusionBean);

				cycleCurriculumGroup.editConclusionInformation(AccessControl.getPerson(), cycleCurriculumGroup.getFinalAverage(),
						new YearMonthDay(conclusionBean.getEnteredConclusionDate()), conclusionBean.getObservations());
			}

		} else {
			registration.conclude();

			if (conclusionBean.hasEnteredConclusionDate()) {

				checkEnteredConclusionDate(conclusionBean);

				registration.editConclusionInformation(AccessControl.getPerson(), registration.getFinalAverage(),
						new YearMonthDay(conclusionBean.getEnteredConclusionDate()), conclusionBean.getObservations());
			}
		}
	}

	private static void checkEnteredConclusionDate(final RegistrationConclusionBean conclusionBean) {
		final YearMonthDay startDate = conclusionBean.getRegistration().getStartDate();

		if (startDate.isAfter(conclusionBean.getEnteredConclusionDate())) {
			throw new DomainException("error.RegistrationConclusionProcess.start.date.is.after.entered.date");
		}

	}

}

package net.sourceforge.fenixedu.applicationTier.Servico.student.senior;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;

public class ReadStudentSenior extends Service {

	public Senior run(Person person) {
		Senior senior = null;

		final Registration registration = person.getStudentByType(DegreeType.DEGREE);
		if (registration == null) {
			return null;
		}

		final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			return null;
		}

		final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
		final Degree degree = degreeCurricularPlan.getDegree();
		senior = registration.getSenior();

		if (senior == null) {
			final int curricularYear = registration.getCurricularYear();
			if (curricularYear == degree.getDegreeType().getYears()) {
				senior = new Senior(registration);
			} else {
				return null;
			}
		}
		return senior;
	}
}

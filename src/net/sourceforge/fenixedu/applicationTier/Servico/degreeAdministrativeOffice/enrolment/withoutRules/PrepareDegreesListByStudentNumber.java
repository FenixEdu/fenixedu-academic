package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class PrepareDegreesListByStudentNumber extends Service {

	// student and degreeType used by filter
	public List<ExecutionDegree> run(final Student student, final DegreeType degreeType,
			final ExecutionPeriod executionPeriod) throws FenixServiceException {

		final List<ExecutionDegree> executionDegrees = (degreeType == null) ? ExecutionDegree
				.getAllByExecutionYear(executionPeriod.getExecutionYear()) : ExecutionDegree
				.getAllByExecutionYearAndDegreeType(executionPeriod.getExecutionYear(), degreeType);

		if (executionDegrees.isEmpty()) {
			throw new FenixServiceException("errors.impossible.operation");
		}
		return executionDegrees;
	}

}
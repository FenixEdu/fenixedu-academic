package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class PrepareDegreesListByStudentNumber extends Service {

    // student and degreeType used by filter
    public List<ExecutionDegree> run(final Registration registration, final DegreeType degreeType,
	    final ExecutionSemester executionSemester) throws FenixServiceException {

	if (registration.getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
	    return ExecutionDegree.getAllByExecutionYearAndDegreeType(
		    executionSemester.getExecutionYear(), DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
	}

	final List<ExecutionDegree> executionDegrees = (degreeType == null) ? ExecutionDegree
		.getAllByExecutionYear(executionSemester.getExecutionYear()) : ExecutionDegree
		.getAllByExecutionYearAndDegreeType(executionSemester.getExecutionYear(), degreeType);

	if (executionDegrees.isEmpty()) {
	    throw new FenixServiceException("errors.impossible.operation");
	}
	return executionDegrees;
    }
}
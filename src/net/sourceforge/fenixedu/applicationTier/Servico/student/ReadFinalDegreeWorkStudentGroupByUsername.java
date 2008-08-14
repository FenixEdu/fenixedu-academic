/*
 * Created on 2004/04/15
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkStudentGroupByUsername extends Service {

    public InfoGroup run(final Person personUser, final ExecutionYear executionYear) {
	final FinalDegreeWorkGroup finalDegreeWorkGroup = findFinalDegreeWorkGroup(personUser, executionYear);
	return InfoGroup.newInfoFromDomain(finalDegreeWorkGroup);
    }

    private FinalDegreeWorkGroup findFinalDegreeWorkGroup(final Person personUser, final ExecutionYear executionYear) {
	FinalDegreeWorkGroup finalDegreeWorkGroup = find(personUser, executionYear, DegreeType.BOLONHA_MASTER_DEGREE);
	if (finalDegreeWorkGroup == null) {
	    finalDegreeWorkGroup = find(personUser, executionYear, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	}
	if (finalDegreeWorkGroup == null) {
	    finalDegreeWorkGroup = find(personUser, executionYear, DegreeType.BOLONHA_DEGREE);
	}
	if (finalDegreeWorkGroup == null) {
	    finalDegreeWorkGroup = find(personUser, executionYear, DegreeType.DEGREE);
	}
	return finalDegreeWorkGroup;
    }

    private FinalDegreeWorkGroup find(final Person personUser, final ExecutionYear executionYear, final DegreeType degreeType) {
	for (final Registration registration : personUser.getStudent().getRegistrationsSet()) {
	    if (registration.getDegreeType() == degreeType) {
		final FinalDegreeWorkGroup finalDegreeWorkGroup = registration
			.findFinalDegreeWorkGroupForExecutionYear(executionYear);
		if (finalDegreeWorkGroup != null) {
		    return finalDegreeWorkGroup;
		}
	    }
	}
	return null;
    }

}

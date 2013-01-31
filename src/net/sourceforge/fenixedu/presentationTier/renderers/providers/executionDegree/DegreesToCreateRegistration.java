package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class DegreesToCreateRegistration extends DegreesByEmployeeUnit {

	@Override
	protected Collection<Degree> getDegrees() {
		return AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
				AcademicOperationType.CREATE_REGISTRATION);
	}

}

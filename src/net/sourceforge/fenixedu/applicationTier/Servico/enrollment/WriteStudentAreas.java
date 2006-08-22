package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

public class WriteStudentAreas extends Service {

	// some of these arguments may be null. they are only needed for filter
	public void run(Integer executionDegreeId, Registration registration, Integer specializationAreaID,
			Integer secundaryAreaID) throws FenixServiceException {

		if (registration == null) {
			throw new NonExistingServiceException("error.invalid.student");
		}

		final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			throw new NonExistingServiceException("error.no.studentCurricularPlan");
		}

		final Branch specializationArea = rootDomainObject.readBranchByOID(specializationAreaID);
		final Branch secundaryArea = (secundaryAreaID != null) ? rootDomainObject
				.readBranchByOID(secundaryAreaID) : null;

		studentCurricularPlan.setStudentAreas(specializationArea, secundaryArea);
	}
}
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.areas;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class WriteStudentAreasWithoutRestrictions extends Service {

	// student and degreeType used by filter
	public void run(Registration registration, DegreeType degreeType, Integer specializationAreaID,
			Integer secundaryAreaID) throws FenixServiceException {

		if (registration == null) {
			throw new NonExistingServiceException("error.invalid.student");
		}

		final StudentCurricularPlan studentCurricularPlan = registration.getActiveOrConcludedStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			throw new NonExistingServiceException("error.no.studentCurricularPlan");
		}

		final Branch specializationArea = rootDomainObject.readBranchByOID(specializationAreaID);
		final Branch secundaryArea = (secundaryAreaID != null) ? rootDomainObject.readBranchByOID(secundaryAreaID) : null;

		studentCurricularPlan.setStudentAreasWithoutRestrictions(specializationArea, secundaryArea);
	}
}
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteStudentAreas extends Service {
	public WriteStudentAreas() {
	}

	// some of these arguments may be null. they are only needed for filter
	public void run(Integer executionDegreeId, Integer studentCurricularPlanID,
			Integer persistentSupportecializationAreaID, Integer secundaryAreaID)
			throws FenixServiceException, ExcepcaoPersistencia {

		StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentObject
				.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

		if (studentCurricularPlan == null) {
			throw new NonExistingServiceException();
		}

		Branch specializationArea = (Branch) persistentObject.readByOID(
				Branch.class, persistentSupportecializationAreaID);

		Branch secundaryArea = null;
		if (secundaryAreaID != null) {
			secundaryArea = (Branch) persistentObject.readByOID(Branch.class,
					secundaryAreaID);
		}

		try {
			studentCurricularPlan.setStudentAreas(specializationArea,
					secundaryArea);
		} catch (DomainException e) {
			throw new ChosenAreasAreIncompatibleServiceException();
		}
	}

}
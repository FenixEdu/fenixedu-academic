package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICreditsInScientificArea;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInSpecificScientificArea;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class CreditsInSpecificScientificAreaVO extends VersionedObjectsBase
		implements IPersistentCreditsInSpecificScientificArea {

	public ICreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
			Integer studentCurricularPlanKey, Integer enrolmentKey, Integer scientificAreaKey)
			throws ExcepcaoPersistencia{
		
		IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) readByOID(StudentCurricularPlan.class, studentCurricularPlanKey);
		List<ICreditsInScientificArea> credits = studentCurricularPlan.getCreditsInScientificAreas();
		
		for (ICreditsInScientificArea credit : credits){
			if(credit.getEnrolment().getIdInternal().equals(enrolmentKey) &&
			   credit.getScientificArea().getIdInternal().equals(scientificAreaKey))
				return credit;
		}
		return null;
	}
}

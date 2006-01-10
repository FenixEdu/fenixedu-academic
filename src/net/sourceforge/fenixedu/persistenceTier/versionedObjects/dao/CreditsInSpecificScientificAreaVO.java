package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInSpecificScientificArea;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class CreditsInSpecificScientificAreaVO extends VersionedObjectsBase
		implements IPersistentCreditsInSpecificScientificArea {

	public CreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
			Integer studentCurricularPlanKey, Integer enrolmentKey, Integer scientificAreaKey)
			throws ExcepcaoPersistencia{
		
		StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) readByOID(StudentCurricularPlan.class, studentCurricularPlanKey);
		List<CreditsInScientificArea> credits = studentCurricularPlan.getCreditsInScientificAreas();
		
		for (CreditsInScientificArea credit : credits){
			if(credit.getEnrolment().getIdInternal().equals(enrolmentKey) &&
			   credit.getScientificArea().getIdInternal().equals(scientificAreaKey))
				return credit;
		}
		return null;
	}
}

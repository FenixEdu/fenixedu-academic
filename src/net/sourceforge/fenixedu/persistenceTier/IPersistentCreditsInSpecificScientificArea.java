package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.ICreditsInScientificArea;

/**
 * @author David Santos Jan 14, 2004
 */

public interface IPersistentCreditsInSpecificScientificArea extends IPersistentObject {

    public ICreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
            Integer studentCurricularPlanKey, Integer enrolmentKey,
            Integer scientificAreaKey) throws ExcepcaoPersistencia;
}
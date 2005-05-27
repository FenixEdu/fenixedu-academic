package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICreditsInScientificArea;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author David Santos Jan 14, 2004
 */

public interface IPersistentCreditsInSpecificScientificArea extends IPersistentObject {

    public ICreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
            Integer studentCurricularPlanKey, Integer enrolmentKey,
            Integer scientificAreaKey) throws ExcepcaoPersistencia;
}
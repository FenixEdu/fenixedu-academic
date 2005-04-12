package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author David Santos Jan 14, 2004
 */

public interface IPersistentCreditsInAnySecundaryArea extends IPersistentObject {
    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

    public ICreditsInAnySecundaryArea readByStudentCurricularPlanAndEnrollment(
            IStudentCurricularPlan studentCurricularPlan, IEnrolment enrolment)
            throws ExcepcaoPersistencia;
}
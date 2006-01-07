package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author David Santos Jan 14, 2004
 */

public interface IPersistentCreditsInAnySecundaryArea extends IPersistentObject {
    public List readAllByStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

    public CreditsInAnySecundaryArea readByStudentCurricularPlanAndEnrollment(
            StudentCurricularPlan studentCurricularPlan, Enrolment enrolment)
            throws ExcepcaoPersistencia;
}
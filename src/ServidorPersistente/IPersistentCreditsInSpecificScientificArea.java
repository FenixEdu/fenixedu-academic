package ServidorPersistente;

import java.util.List;

import Dominio.ICreditsInScientificArea;
import Dominio.IEnrollment;
import Dominio.IScientificArea;
import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos Jan 14, 2004
 */

public interface IPersistentCreditsInSpecificScientificArea extends IPersistentObject {
    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

    public ICreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
            IStudentCurricularPlan studentCurricularPlan, IEnrollment enrolment,
            IScientificArea scientificArea) throws ExcepcaoPersistencia;
}
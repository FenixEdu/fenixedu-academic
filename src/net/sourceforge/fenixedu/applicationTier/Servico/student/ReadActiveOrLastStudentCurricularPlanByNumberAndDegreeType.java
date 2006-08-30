package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveOrLastStudentCurricularPlanByNumberAndDegreeType extends Service {

    public InfoStudentCurricularPlan run(Integer studentNumber, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber,
                degreeType);
        if (registration == null) {
            return null;
        }
        StudentCurricularPlan studentCurricularPlan = registration
                .getActiveOrConcludedStudentCurricularPlan();

        if (studentCurricularPlan == null) {
            studentCurricularPlan = registration.getLastStudentCurricularPlan();
        }

        if (studentCurricularPlan != null) {
            return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
        }
        return null;
    }

}
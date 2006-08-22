package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertTutorShip extends Service {

    public Boolean verifyStudentOfThisDegree(Registration registration, DegreeType degreeType, String degreeCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();

        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(degreeCode);
    }

    public Boolean verifyStudentAlreadyTutor(Registration registration, Teacher teacher)
            throws FenixServiceException, ExcepcaoPersistencia {

        Tutor tutor = registration.getAssociatedTutor();

        if (tutor != null && !(tutor.getTeacher() == teacher)) {
            return true;
        }

        return false;
    }

}

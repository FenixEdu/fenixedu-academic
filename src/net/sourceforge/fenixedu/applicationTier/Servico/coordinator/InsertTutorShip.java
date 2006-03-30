package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

public class InsertTutorShip extends Service {

    public Boolean verifyStudentOfThisDegree(Student student, DegreeType degreeType, String degreeCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        StudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(student.getNumber(), degreeType);

        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(degreeCode);
    }

    public Boolean verifyStudentAlreadyTutor(Student student, Teacher teacher)
            throws FenixServiceException, ExcepcaoPersistencia {

        Tutor tutor = student.getAssociatedTutor();

        if (tutor != null && !(tutor.getTeacher() == teacher)) {
            return true;
        }

        return false;
    }

}

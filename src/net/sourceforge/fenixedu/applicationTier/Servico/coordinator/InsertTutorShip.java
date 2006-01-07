/*
 * Created on 4/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 * 
 */
public class InsertTutorShip implements IService {
    public Boolean verifyStudentOfThisDegree(Student student, DegreeType degreeType, String degreeCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        boolean result = false;

        ISuportePersistente sp;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();

        StudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(student.getNumber(), degreeType);

        result = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(
                degreeCode);

        return Boolean.valueOf(result);
    }

    public Boolean verifyStudentAlreadyTutor(Student student, Teacher teacher)
            throws FenixServiceException, ExcepcaoPersistencia {
        boolean result = false;
        Tutor tutor = null;

        ISuportePersistente sp;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTutor persistentTutor = sp.getIPersistentTutor();

        tutor = persistentTutor.readTeachersByStudent(student);

        if (tutor != null && !tutor.getTeacher().equals(teacher)) {
            result = true;
        }

        return new Boolean(result);
    }
}
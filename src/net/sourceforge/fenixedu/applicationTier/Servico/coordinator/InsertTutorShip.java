/*
 * Created on 4/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertTutorShip implements IService {
    public Boolean verifyStudentOfThisDegree(IStudent student, TipoCurso degreeType, String degreeCode)
            throws FenixServiceException {
        boolean result = false;

        ISuportePersistente sp;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(student.getNumber(), degreeType);

            result = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(
                    degreeCode);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }

        return Boolean.valueOf(result);
    }

    public Boolean verifyStudentAlreadyTutor(IStudent student, ITeacher teacher)
            throws FenixServiceException {
        boolean result = false;
        ITutor tutor = null;

        ISuportePersistente sp;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTutor persistentTutor = sp.getIPersistentTutor();

            tutor = persistentTutor.readTeachersByStudent(student);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }

        if (tutor != null && !tutor.getTeacher().equals(teacher)) {
            result = true;
        }

        return new Boolean(result);
    }
}
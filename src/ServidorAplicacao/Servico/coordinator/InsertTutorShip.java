/*
 * Created on 4/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.ITutor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

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
            sp = SuportePersistenteOJB.getInstance();
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
            sp = SuportePersistenteOJB.getInstance();
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
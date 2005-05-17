package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation implements IService {

    public EditEvaluation() {
    }

    public boolean run(Integer infoExecutionCourseCode, Integer infoEvaluationMethodCode,
            InfoEvaluationMethod infoEvaluationMethod) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                .readByIdExecutionCourse(infoExecutionCourseCode);

        if (evaluationMethod == null) {
            evaluationMethod = new EvaluationMethod();
            persistentEvaluationMethod.simpleLockWrite(evaluationMethod);
            evaluationMethod.setKeyExecutionCourse(infoExecutionCourseCode);

            IPersistentExecutionCourse persistenteExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistenteExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);
            evaluationMethod.setExecutionCourse(executionCourse);
            
        } else {
            persistentEvaluationMethod.simpleLockWrite(evaluationMethod);
        }

        evaluationMethod.setEvaluationElements(infoEvaluationMethod.getEvaluationElements());
        evaluationMethod.setEvaluationElementsEn(infoEvaluationMethod.getEvaluationElementsEn());

        return true;
    }

}
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation implements IService {

    public boolean run(Integer infoExecutionCourseCode, Integer infoEvaluationMethodCode,
            InfoEvaluationMethod infoEvaluationMethod) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse(); 
        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, infoExecutionCourseCode);

        persistentExecutionCourse.simpleLockWrite(executionCourse);
        if (executionCourse.getEvaluationMethod() == null) { // Create a new one
            executionCourse.createEvaluationMethod(infoEvaluationMethod.getEvaluationElements(),
                    infoEvaluationMethod.getEvaluationElementsEn());
        } else { // Edit existent
            executionCourse.getEvaluationMethod().edit(infoEvaluationMethod.getEvaluationElements(),
                    infoEvaluationMethod.getEvaluationElementsEn());
        }
        return true;
    }
}
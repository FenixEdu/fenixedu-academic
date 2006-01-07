package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadEvaluations implements IService {

    /**
     * Executes the service. Returns the current collection of
     * evaluations(tests, exams, projects and final evaluations)
     * @throws ExcepcaoPersistencia 
     */
    public List run(Integer executionCourseCode) throws FenixServiceException, ExcepcaoPersistencia {
            ISuportePersistente sp;
            ExecutionCourse executionCourse;

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);
            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }

            List evaluations = executionCourse.getAssociatedEvaluations();
            List infoEvaluations = new ArrayList();
            Iterator iterator = evaluations.iterator();
            while (iterator.hasNext()) {

                infoEvaluations.add(InfoEvaluation.newInfoFromDomain((Evaluation) iterator.next()));
            }

            return infoEvaluations;
    }
}
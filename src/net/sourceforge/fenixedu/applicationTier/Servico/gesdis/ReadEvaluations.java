package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadEvaluations implements IService {

    /**
     * Executes the service. Returns the current collection of
     * evaluations(tests, exams, projects and final evaluations)
     */
    public List run(Integer executionCourseCode) throws FenixServiceException {
        try {
            ISuportePersistente sp;
            IExecutionCourse executionCourse;

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);
            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }

            List evaluations = executionCourse.getAssociatedEvaluations();
            List infoEvaluations = new ArrayList();
            Iterator iterator = evaluations.iterator();
            while (iterator.hasNext()) {

                infoEvaluations.add(InfoEvaluation.newInfoFromDomain((IEvaluation) iterator.next()));
            }

            return infoEvaluations;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}
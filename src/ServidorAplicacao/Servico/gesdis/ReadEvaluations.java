package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEvaluation;
import Dominio.ExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

            sp = SuportePersistenteOJB.getInstance();
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
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IEvaluation;
import ServidorAplicacao.IServico;
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
public class ReadEvaluations implements IServico {

    private static ReadEvaluations service = new ReadEvaluations();

    /**
     * The singleton access method of this class.
     */

    public static ReadEvaluations getService() {

        return service;

    }

    /**
     * The ctor of this class.
     */

    private ReadEvaluations() {
    }

    /**
     * Devolve o nome do servico
     */

    public final String getNome() {

        return "ReadEvaluations";

    }

    /**
     * Executes the service. Returns the current collection of
     * evaluations(tests, exams, projects and final evaluations)
     */

    public List run(Integer executionCourseCode) throws FenixServiceException {
        try {
            ISuportePersistente sp;
            IExecutionCourse executionCourse;

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();

            executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, executionCourseCode);
            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }

            List evaluations = executionCourse.getAssociatedEvaluations();
            List infoEvaluations = new ArrayList();
            Iterator iterator = evaluations.iterator();
            while (iterator.hasNext()) {
                infoEvaluations.add(Cloner
                        .copyIEvaluation2InfoEvaluation((IEvaluation) iterator
                                .next()));
            }

            return infoEvaluations;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}
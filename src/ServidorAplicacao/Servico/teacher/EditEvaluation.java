package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEvaluationMethod;
import Dominio.EvaluationMethod;
import Dominio.ExecutionCourse;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation implements IService
{

    public EditEvaluation()
    {
    }

    public boolean run(Integer infoExecutionCourseCode, Integer infoEvaluationMethodCode,
            InfoEvaluationMethod infoEvaluationMethod) throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = new ExecutionCourse();
            executionCourse.setIdInternal(infoExecutionCourseCode);

            IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
            IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                    .readByIdExecutionCourse(executionCourse);

            if (evaluationMethod == null)
            {
                evaluationMethod = new EvaluationMethod();
                persistentEvaluationMethod.simpleLockWrite(evaluationMethod);
                evaluationMethod.setKeyExecutionCourse(infoExecutionCourseCode);

                IPersistentExecutionCourse persistenteExecutionCourse = sp
                        .getIPersistentExecutionCourse();
                evaluationMethod.setExecutionCourse((IExecutionCourse) persistenteExecutionCourse
                        .readByOId(executionCourse, false));
            }
            else
            {

                persistentEvaluationMethod.simpleLockWrite(evaluationMethod);
            }

            evaluationMethod.setEvaluationElements(infoEvaluationMethod.getEvaluationElements());
            evaluationMethod.setEvaluationElementsEn(infoEvaluationMethod.getEvaluationElementsEn());

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return true;
    }

}
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoSiteSubmitMarks;
import DataBeans.util.Cloner;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;

/**
 * @author Tânia Pousão
 *  
 */
public class ValidateSubmitMarks implements IServico
{
    private static ValidateSubmitMarks _service = new ValidateSubmitMarks();

    /**
	 * The actor of this class.
	 */
    private ValidateSubmitMarks()
    {

    }

    /**
	 * Returns Service Name
	 */
    public String getNome()
    {
        return "ValidateSubmitMarks";
    }

    /**
	 * Returns the _servico.
	 * 
	 * @return ReadExecutionCourse
	 */
    public static ValidateSubmitMarks getService()
    {
        return _service;
    }

    public InfoSiteSubmitMarks run(
        Integer executionCourseCode,
        Integer evaluationCode,
        UserView userView)
        throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //execution course and execution course's site
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = sp.getIPersistentEnrolmentEvaluation();

            IExecutionCourse executionCourse = new ExecutionCourse();
            executionCourse.setIdInternal(executionCourseCode);
            executionCourse =
                (IExecutionCourse)persistentExecutionCourse.readByOId(executionCourse, false);

            //evaluation
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            IEvaluation evaluation = new Evaluation();
            evaluation.setIdInternal(evaluationCode);
            evaluation = (IEvaluation)persistentEvaluation.readByOId(evaluation, false);

            //attend list
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            List attendList = persistentAttend.readByExecutionCourse(executionCourse);

            //verifySubmitMarks(attendList);
            
			List enrolmentListIds = (List)CollectionUtils.collect(attendList, new Transformer()
			{

				public Object transform(Object input)
				{
					IFrequenta attend = (IFrequenta)input;
					IEnrolment enrolment = attend.getEnrolment();
					return enrolment == null ? null : enrolment.getIdInternal();
				}
			});

			enrolmentListIds = (List)CollectionUtils.select(enrolmentListIds, new Predicate()
			{
				public boolean evaluate(Object arg0)
				{
					return arg0 != null;
				}
			});
			List alreadySubmiteMarks = enrolmentEvaluationDAO.readAlreadySubmitedMarks(enrolmentListIds);
            
			if(!alreadySubmiteMarks.isEmpty())
			{
				throw new FenixServiceException("errors.submitMarks.yetSubmited");
			}

            InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();
            
			infoSiteSubmitMarks.setInfoEvaluation(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
			
			return infoSiteSubmitMarks;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }
}
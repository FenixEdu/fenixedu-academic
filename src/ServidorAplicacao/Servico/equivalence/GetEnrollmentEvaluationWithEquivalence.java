package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author David Santos in May 17, 2004
 */

public class GetEnrollmentEvaluationWithEquivalence extends EnrollmentEquivalenceServiceUtils implements IService
{
	public GetEnrollmentEvaluationWithEquivalence()
	{
	}

	public List run(Integer studentNumber, TipoCurso degreeType, Integer enrollmentID) throws FenixServiceException
	{
		return (List) convertDataOutput(execute(convertDataInput(enrollmentID)));
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
	 */
	protected Object convertDataInput(Object object)
	{
		return object;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.Service#convertDataOutput(java.lang.Object)
	 */
	protected Object convertDataOutput(Object object)
	{
		List input = (List) object;
		List output = new ArrayList();
		
		for (int i = 0; i < input.size(); i++)
		{
			IEnrolmentEvaluation enrollmentEvaluation = (IEnrolmentEvaluation) input.get(i);
			
			InfoEnrolmentEvaluation infoEnrollmentEvaluation = Cloner
				.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrollmentEvaluation);
			infoEnrollmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(enrollmentEvaluation.getEnrolment()));

			output.add(i, infoEnrollmentEvaluation);
		}
		
		return output;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servico.Service#execute(java.lang.Object)
	 */
	protected Object execute(Object object) throws FenixServiceException
	{
		Integer enrollmentID = (Integer) object;
		
		List enrollmentEvaluations = new ArrayList();

		try
		{
			ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
			IPersistentEnrolment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
			IPersistentEnrolmentEquivalence enrollmentEquivalenceDAO = persistenceDAO.getIPersistentEnrolmentEquivalence();
			IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrollmentForEnrollmentEquivalenceDAO = persistenceDAO
				.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

			IEnrolment enrollment = (IEnrolment) enrollmentDAO.readByOID(Enrolment.class, enrollmentID);
			
			enrollmentEvaluations.add(0, getEnrollmentEvaluation(enrollment));
			
			if (enrollment != null)
			{
				IEnrolmentEquivalence enrollmentEquivalence = enrollmentEquivalenceDAO.readByEnrolment(enrollment);
				if (enrollmentEquivalence != null)
				{
					List equivalentEnrollmentsForEnrollmentEquivalence = equivalentEnrollmentForEnrollmentEquivalenceDAO
						.readByEnrolmentEquivalence(enrollmentEquivalence);

					if ((equivalentEnrollmentsForEnrollmentEquivalence != null)
						&& (!equivalentEnrollmentsForEnrollmentEquivalence.isEmpty()))
					{
						for (int j = 0; j < equivalentEnrollmentsForEnrollmentEquivalence.size(); j++)
						{
							IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalence =
								(IEquivalentEnrolmentForEnrolmentEquivalence) equivalentEnrollmentsForEnrollmentEquivalence
								.get(j);
							
							enrollmentEvaluations.add((j + 1), getEnrollmentEvaluation(equivalentEnrolmentForEnrolmentEquivalence
								.getEquivalentEnrolment()));
						}
					}
				}
			}
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}

		return enrollmentEvaluations;
	}

	private IEnrolmentEvaluation getEnrollmentEvaluation(IEnrolment enrollment)
	{
		List enrolmentEvaluations = enrollment.getEvaluations();

		// This sorts the list ascendingly so we need to reverse it to get the first object.
		Collections.sort(enrolmentEvaluations);
		Collections.reverse(enrolmentEvaluations);
		
		return (IEnrolmentEvaluation) enrolmentEvaluations.get(0);
	}
}
/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.GratuitySituation;
import Dominio.GratuityValues;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class EditGratuitySituationById implements IServico
{

	private static EditGratuitySituationById servico = new EditGratuitySituationById();

	/**
	 * The singleton access method of this class.
	 */
	public static EditGratuitySituationById getService()
	{
		return servico;
	}

	/**
	 * The actor of this class.
	 */
	private EditGratuitySituationById()
	{
	}

	/**
	 * Returns The Service Name
	 */
	public final String getNome()
	{
		return "EditGratuitySituationById";
	}

	public Object run(InfoGratuitySituation infoGratuitySituation) throws FenixServiceException
	{
		ISuportePersistente sp = null;
		try
		{
			if (infoGratuitySituation == null)
			{
				throw new FenixServiceException();
			}

			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuitySituation persistentGratuitySituation =
				sp.getIPersistentGratuitySituation();

			IGratuitySituation gratuitySituation = new GratuitySituation();
			gratuitySituation.setIdInternal(infoGratuitySituation.getIdInternal());

			gratuitySituation =
				(IGratuitySituation) persistentGratuitySituation.readByOId(gratuitySituation, true);
			if (gratuitySituation == null)
			{
				gratuitySituation = new GratuitySituation();

				IGratuityValues gratuityValues = new GratuityValues();
				if (infoGratuitySituation.getInfoGratuityValues() != null)
				{
					gratuityValues.setIdInternal(
						infoGratuitySituation.getInfoGratuityValues().getIdInternal());
					IPersistentGratuityValues persistentGratuityValues =
						sp.getIPersistentGrtuityValues();
					gratuityValues =
						(IGratuityValues) persistentGratuityValues.readByOId(gratuityValues, false);
					gratuitySituation.setGratuityValues(gratuityValues);
				}

				IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
				studentCurricularPlan.setIdInternal(
					infoGratuitySituation.getInfoStudentCurricularPlan().getIdInternal());
				IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
					sp.getIStudentCurricularPlanPersistente();
				studentCurricularPlan =
					(IStudentCurricularPlan) persistentStudentCurricularPlan.readByOId(
						studentCurricularPlan,
						false);
				gratuitySituation.setStudentCurricularPlan(studentCurricularPlan);

				persistentGratuitySituation.simpleLockWrite(gratuitySituation);
			}
			gratuitySituation.setExemptionDescription(infoGratuitySituation.getExemptionDescription());
			gratuitySituation.setExemptionPercentage(infoGratuitySituation.getExemptionPercentage());
			gratuitySituation.setExemptionType(infoGratuitySituation.getExemptionType());

			//gratuitySituation.setPayedValue(infoGratuitySituation.getPayedValue());
			//gratuitySituation.setRemainingValue(infoGratuitySituation.getRemainingValue());
			
			infoGratuitySituation = Cloner.copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}

		return infoGratuitySituation;
	}
}

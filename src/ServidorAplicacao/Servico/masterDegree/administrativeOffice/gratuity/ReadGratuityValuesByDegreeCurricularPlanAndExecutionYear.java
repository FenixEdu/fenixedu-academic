/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoGratuityValues;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.IGratuityValues;
import Dominio.IPaymentPhase;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentPaymentPhase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear implements IServico
{

	private static ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear servico =
		new ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear getService()
	{
		return servico;
	}

	/**
	 * The actor of this class.
	 */
	private ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear()
	{
	}

	/**
	 * Returns The Service Name
	 */

	public final String getNome()
	{
		return "ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear";
	}

	public Object run(Integer degreeCurricularPlanID, String executionYearName) throws FenixServiceException
	{
		if (degreeCurricularPlanID == null || executionYearName == null)
		{
			throw new FenixServiceException();
		}

		ISuportePersistente sp = null;
		IGratuityValues gratuityValues = null;
		List infoPaymentPhases = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();
			IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
			degreeCurricularPlan.setIdInternal(degreeCurricularPlanID);
			degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(degreeCurricularPlan, false);
			
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearName);
			
			
			//read execution degree
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);

			//read execution degree's gratuity values
			IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();
			gratuityValues =
				persistentGratuityValues.readGratuityValuesByExecutionDegree(executionDegree);
			if (gratuityValues != null)
			{
				//read payment phases for this gratuity value
				IPersistentPaymentPhase persistentPaymentPhase = sp.getIPersistentPaymentPhase();
				List paymentPhases = persistentPaymentPhase.readByGratuityValues(gratuityValues);
				if (paymentPhases != null)
				{
				}
			}
			
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}

		InfoGratuityValues infoGratuityValues = null;
		if (gratuityValues != null)
		{
			infoGratuityValues = Cloner.copyIGratuityValues2InfoGratuityValues(gratuityValues);
			
			infoPaymentPhases = new ArrayList();
			CollectionUtils.collect(gratuityValues.getPaymentPhaseList(), new Transformer()
					{
				public Object transform(Object input)
				{
					IPaymentPhase paymentPhase = (IPaymentPhase) input;
					return Cloner.copyIPaymentPhase2InfoPaymentPhase(paymentPhase);
				}
			}, infoPaymentPhases);
			
			infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);

		}

		return infoGratuityValues;
	}
}

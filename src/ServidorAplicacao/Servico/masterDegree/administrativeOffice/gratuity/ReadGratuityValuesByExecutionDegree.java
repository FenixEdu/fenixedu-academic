/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IGratuityValues;
import Dominio.IPaymentPhase;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentPaymentPhase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuityValuesByExecutionDegree implements IServico
{

	private static ReadGratuityValuesByExecutionDegree servico =
		new ReadGratuityValuesByExecutionDegree();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadGratuityValuesByExecutionDegree getService()
	{
		return servico;
	}

	/**
	 * The actor of this class.
	 */
	private ReadGratuityValuesByExecutionDegree()
	{
	}

	/**
	 * Returns The Service Name
	 */

	public final String getNome()
	{
		return "ReadGratuityValuesByExecutionDegree";
	}

	public Object run(Integer executionDegreeID) throws FenixServiceException
	{
		if (executionDegreeID == null)
		{
			throw new FenixServiceException();
		}

		ISuportePersistente sp = null;
		IGratuityValues gratuityValues = null;
		List infoPaymentPhases = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();
			IPersistentPaymentPhase persistentPaymentPhase = sp.getIPersistentPaymentPhase();

			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeID);
			gratuityValues =
				persistentGratuityValues.readGratuityValuesByExecutionDegree(executionDegree);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}

		InfoGratuityValues infoGratuityValues = null;
		if (gratuityValues != null)
		{
			System.out.println("encontrou propina1");
			infoGratuityValues = Cloner.copyIGratuityValues2InfoGratuityValues(gratuityValues);
			System.out.println("encontrou propina2");
			
			infoPaymentPhases = new ArrayList();
			CollectionUtils.collect(gratuityValues.getPaymentPhaseList(), new Transformer()
			{
				public Object transform(Object input)
				{
					IPaymentPhase paymentPhase = (IPaymentPhase) input;
					return Cloner.copyIPaymentPhase2InfoPaymentPhase(paymentPhase);
				}
			}, infoPaymentPhases);
			System.out.println("encontrou propina3");
			
			InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) CollectionUtils.find(infoPaymentPhases, new Predicate()
			{
				public boolean evaluate(Object input)
				{
					InfoPaymentPhase aInfoPaymentPhase = (InfoPaymentPhase)input;
					if(aInfoPaymentPhase.getDescription().equals(SessionConstants.REGISTRATION_PAYMENT)) {
						return true;
					}
					return false;
				}
			});
			System.out.println("encontrou propina4");
			
			if(infoPaymentPhase != null) {
				infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
			}
			System.out.println("encontrou propina5");
			
			infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);
		}
		return infoGratuityValues;
	}
}

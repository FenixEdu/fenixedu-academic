/*
 * Created on 14/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertGratuityData implements IServico
{
	private static InsertGratuityData service = new InsertGratuityData();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome()
	{
		return "InsertGratuityData";
	}

	/**
	 * @return Returns the service.
	 */
	public static InsertGratuityData getService()
	{
		return service;
	}

	/**
	 * Constructor
	 */
	public InsertGratuityData()
	{
	}

	public Object run(InfoGratuityValues infoGratuityValues) throws FenixServiceException
	{
		if (infoGratuityValues == null)
		{
			throw new FenixServiceException();
		}

		Double gratuityValue = null;

		if (infoGratuityValues.getAnualValue() != null)
		{
			gratuityValue = infoGratuityValues.getAnualValue();
		}
		else
		{
			if (infoGratuityValues.getProofRequestPayment() != null
				&& infoGratuityValues.getProofRequestPayment().equals(Boolean.TRUE))
			{
				gratuityValue = infoGratuityValues.getScholarShipValue();
			}
			else
			{
				gratuityValue =
					new Double(
						infoGratuityValues.getScholarShipValue().doubleValue()
							+ infoGratuityValues.getFinalProofValue().doubleValue());
			}
		}

		List paymentPhasesList = infoGratuityValues.getInfoPaymentPhases();
		if(paymentPhasesList != null && paymentPhasesList.size() > 0){
			ListIterator iterator = paymentPhasesList.listIterator();
			
			while (iterator.hasNext())
			{
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				
			}
			
		}
		
		
		
		return Boolean.TRUE;
	}
}

/*
 * Created on 6/Jan/2004
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.PaymentPhase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPaymentPhase;

/**
 * @author Tânia Pousão
 *  
 */
public class PaymentPhaseOJB extends ObjectFenixOJB implements IPersistentPaymentPhase
{
	public void deletePaymentPhasesOfThisGratuity(Integer gratuityValuesID) throws ExcepcaoPersistencia
	{
		try
		{

			String oqlQuery =
				"select all from "
					+ PaymentPhase.class.getName()
					+ " where gratuityValues.idInternal = $1";

			query.create(oqlQuery);
			query.bind(gratuityValuesID);
				
			List result = (List) query.execute();
			ListIterator iterator = result.listIterator();
			while (iterator.hasNext())
			{

				delete(iterator.next());
			}
		}
		catch (QueryException ex)
		{
			ex.printStackTrace();
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}

/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import DataBeans.InfoGratuitySituation;
import Dominio.GratuitySituation;
import Dominio.IGratuitySituation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
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

	public Boolean run(InfoGratuitySituation infoGratuitySituation) throws FenixServiceException
	{
		ISuportePersistente sp = null;
		try
		{
			if(infoGratuitySituation == null) {
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
				
				persistentGratuitySituation.simpleLockWrite(gratuitySituation);
				
				//gratuitySituation.setGratuityValues();
				//gratuitySituation.setStudentCurricularPlan();
			}
			
			gratuitySituation.setExemptionDescription(infoGratuitySituation.getExemptionDescription());
			gratuitySituation.setExemptionPercentage(infoGratuitySituation.getExemptionPercentage());
			gratuitySituation.setExemptionType(infoGratuitySituation.getExemptionType());
			gratuitySituation.setPayedValue(infoGratuitySituation.getPayedValue());
			gratuitySituation.setRemainingValue(infoGratuitySituation.getRemainingValue());
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}

		return Boolean.TRUE;
	}
}

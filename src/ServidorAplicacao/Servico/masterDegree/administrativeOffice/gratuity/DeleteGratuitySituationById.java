/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

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
public class DeleteGratuitySituationById implements IServico
{

    private static DeleteGratuitySituationById servico = new DeleteGratuitySituationById();

    /**
	 * The singleton access method of this class.
	 */
    public static DeleteGratuitySituationById getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private DeleteGratuitySituationById()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "DeleteGratuitySituationById";
    }

    public Boolean run(Integer gratuitySituationID) throws FenixServiceException
    {
    	ISuportePersistente sp = null;
    	
    	try
		{
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();
			
			IGratuitySituation gratuitySituation = new GratuitySituation();
			gratuitySituation.setIdInternal(gratuitySituationID);
			gratuitySituation = (IGratuitySituation) persistentGratuitySituation.readByOId(gratuitySituation, false);
			if(gratuitySituation == null){
				return Boolean.TRUE;
			}			
			
			persistentGratuitySituation.deleteByOID(GratuitySituation.class, gratuitySituationID);			
			
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}
    		    	
		return Boolean.TRUE;
    }
}

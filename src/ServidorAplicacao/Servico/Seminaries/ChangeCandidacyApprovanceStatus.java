/*
 * Created on 25/Set/2003, 16:56:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 25/Set/2003, 16:56:39
 * 
 */
public class ChangeCandidacyApprovanceStatus implements IService
{
	
	/**
	 * The actor of this class.
	 **/
	public ChangeCandidacyApprovanceStatus()
	{
	}
	
	public void run(List candidaciesIDs) throws BDException
	{
		try
		{
			ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
			IPersistentSeminaryCandidacy persistentCandidacy= persistenceSupport.getIPersistentSeminaryCandidacy();
			for (Iterator iterator= candidaciesIDs.iterator(); iterator.hasNext();)
			{
				ICandidacy candidacy=
					(ICandidacy) persistentCandidacy.readByOID(Candidacy.class, (Integer) iterator.next());
				persistentCandidacy.simpleLockWrite(candidacy);
				if (candidacy.getApproved() == null)
					candidacy.setApproved(new Boolean(false));
				candidacy.setApproved(new Boolean(!candidacy.getApproved().booleanValue()));
			}
		}
		catch (ExcepcaoPersistencia ex)
		{
			throw new BDException(
				"Got an error while trying to change the approved status from a list of candidacies",
				ex);
		}
	}
}

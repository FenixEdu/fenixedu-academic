/*
 * Created on 04 Mar 2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantSubsidy;
import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantSubsidy;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantSubsidiesByGrantContractAndState implements IService
{
	/**
	 * The constructor of this class.
	 */
	public ReadAllGrantSubsidiesByGrantContractAndState()
	{
	}

	public List run(Integer idContract, Integer state) throws FenixServiceException
	{
		List subsidies = null;
		IPersistentGrantSubsidy persistentGrantSubsidy = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
			subsidies = persistentGrantSubsidy.readAllSubsidiesByGrantContractAndState(idContract, state);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		if (subsidies == null)
			return new ArrayList();
        
        Iterator iterSubsidy = subsidies.iterator();
        List infoSubsidies = new ArrayList();
        while(iterSubsidy.hasNext())
        {
            InfoGrantSubsidy infoGrantSubsidy = Cloner.copyIGrantSubsidy2InfoGrantSubsidy((IGrantSubsidy)iterSubsidy.next());
            infoSubsidies.add(infoGrantSubsidy);
        }

		return infoSubsidies;
	}
}
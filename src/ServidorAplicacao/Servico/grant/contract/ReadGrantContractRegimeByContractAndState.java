/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractRegime;
import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantContractRegime;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContractRegime;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantContractRegimeByContractAndState implements IService
{
	/**
	 * The constructor of this class.
	 */
	public ReadGrantContractRegimeByContractAndState()
	{
    }

	public List run(Integer grantContractId, Integer state) throws FenixServiceException
	{
		List contractRegimes = null;
		
		IPersistentGrantContractRegime persistentGrantContractRegime = null;
		
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentGrantContractRegime = sp.getIPersistentGrantContractRegime();
			
			contractRegimes = persistentGrantContractRegime.readGrantContractRegimeByGrantContractAndState(grantContractId, state); 
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		if(contractRegimes == null)
			return new ArrayList();

		Iterator contractIter = contractRegimes.iterator();
		ArrayList infoContractRegimeList = new ArrayList();
        
		//gather information related to each contract regime
		while (contractIter.hasNext())
		{
			try
			{
				IGrantContractRegime grantContractRegime = (IGrantContractRegime) contractIter.next();
				InfoGrantContractRegime infoGrantContractRegime = copyIGrantContractRegime2InfoGrantContractRegime(grantContractRegime);
				infoContractRegimeList.add(infoGrantContractRegime);
			} 
			catch (Exception e)
			{
				throw new FenixServiceException(e.getMessage());
			}
		}
		Collections.reverse(infoContractRegimeList);
		return infoContractRegimeList;
	}
	
	private InfoGrantContractRegime copyIGrantContractRegime2InfoGrantContractRegime(IGrantContractRegime grantContractRegime)
	{
		InfoGrantContractRegime infoGrantContractRegime = null;
		
		if (grantContractRegime != null)
		{
			infoGrantContractRegime = new InfoGrantContractRegime();
			
			infoGrantContractRegime.setIdInternal(grantContractRegime.getIdInternal());
			infoGrantContractRegime.setState(grantContractRegime.getState());
			infoGrantContractRegime.setDateBeginContract(grantContractRegime.getDateBeginContract());
			infoGrantContractRegime.setDateEndContract(grantContractRegime.getDateEndContract());
			infoGrantContractRegime.setDateSendDispatchCC(grantContractRegime.getDateSendDispatchCC());
			infoGrantContractRegime.setDateSendDispatchCD(grantContractRegime.getDateSendDispatchCD());
			infoGrantContractRegime.setDateDispatchCC(grantContractRegime.getDateDispatchCC());
			infoGrantContractRegime.setDateDispatchCD(grantContractRegime.getDateDispatchCD());
			infoGrantContractRegime.setDateAcceptTerm(grantContractRegime.getDateAcceptTerm());
			
			InfoTeacher infoTeacher = null;
			infoTeacher = Cloner.copyITeacher2InfoTeacher(grantContractRegime.getTeacher());
			infoGrantContractRegime.setInfoTeacher(infoTeacher);
			
			InfoGrantContract infoGrantContract = null;
			infoGrantContract = Cloner.copyIGrantContract2InfoGrantContract(grantContractRegime.getGrantContract());
			infoGrantContractRegime.setInfoGrantContract(infoGrantContract);
		}
		return infoGrantContractRegime;
	}
	
}
/*
 * Created on Jan 29, 2004
 *
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractRegime;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContractRegime;
import Dominio.grant.contract.IGrantContractRegime;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * João Simas
 * Nuno Barbosa
 */
public class ReadGrantContractRegime extends ReadDomainObjectService
{
    public ReadGrantContractRegime()
    {
    }
    
    protected Class getDomainObjectClass()
    {
        return GrantContractRegime.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentGrantContractRegime();
    }

    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
    	return copyIGrantContractRegime2InfoGrantContractRegime((IGrantContractRegime) domainObject);
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
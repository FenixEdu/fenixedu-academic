/*
 * Created on 22/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantType;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantType;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantType extends ReadDomainObjectService implements IService
{
    /**
     * The constructor of this class.
     */
    public ReadGrantType()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentGrantType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return InfoGrantType.newInfoFromDomain((IGrantType) domainObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return GrantType.class;
    }
}
/*
 * Created on 03/12/2003
 * 
 */
package ServidorAplicacao.Servico.grant.owner;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.grant.owner.InfoGrantOwnerWithPerson;
import Dominio.IDomainObject;
import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantOwner extends ReadDomainObjectService implements IService {

    public ReadGrantOwner() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantOwner();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return InfoGrantOwnerWithPerson.newInfoFromDomain((IGrantOwner) domainObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return GrantOwner.class;
    }
}
/*
 * Created on Feb 12, 2004
 *
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantPaymentEntity;
import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * Pica
 * Barbosa
 */
public class ReadGrantPaymentEntity extends ReadDomainObjectService
{
    public ReadGrantPaymentEntity()
    {
    }
    
    protected Class getDomainObjectClass()
    {
        return GrantPaymentEntity.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentGrantPaymentEntity();
    }

    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return InfoGrantPaymentEntity.newInfoFromDomain((IGrantPaymentEntity) domainObject);
    }
}

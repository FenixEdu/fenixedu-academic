/*
 * Created on 23/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantPaymentEntity;
import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantPaymentEntity;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPaymentEntity extends EditDomainObjectService
{
    /**
     * The constructor of this class.
     */
    public EditGrantPaymentEntity()
    {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoGrantPaymentEntity2IGrantPaymentEntity((InfoGrantPaymentEntity) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentGrantPaymentEntity();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentGrantPaymentEntity pgs = sp.getIPersistentGrantPaymentEntity();
        IGrantPaymentEntity grantPaymentEntity = (IGrantPaymentEntity) domainObject;

        return pgs.readByOID(GrantPaymentEntity.class,grantPaymentEntity.getIdInternal());
    }
    
    public void run(InfoGrantPaymentEntity infoGrantPaymentEntity) throws FenixServiceException
    {
        super.run(new Integer(0), infoGrantPaymentEntity);
    }
}

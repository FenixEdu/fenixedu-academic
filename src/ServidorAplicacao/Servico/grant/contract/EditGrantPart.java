/*
 * Created on 23/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantPart;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantPart;
import Dominio.grant.contract.IGrantPart;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantPart;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPart extends EditDomainObjectService
{
    /**
     * The constructor of this class.
     */
    public EditGrantPart()
    {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoGrantPart2IGrantPart((InfoGrantPart) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentGrantPart();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentGrantPart pgs = sp.getIPersistentGrantPart();
        IGrantPart grantPart = (IGrantPart) domainObject;

        return pgs.readByOID(GrantPart.class,grantPart.getIdInternal());
    }
    
    public void run(InfoGrantPart infoGrantPart) throws FenixServiceException
    {
        super.run(new Integer(0), infoGrantPart);
    }
}

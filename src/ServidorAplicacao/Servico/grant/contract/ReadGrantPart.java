/*
 * Created on Jan 29, 2004
 *
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantPart;
import Dominio.grant.contract.IGrantPart;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * João Simas
 * Nuno Barbosa
 */
public class ReadGrantPart extends ReadDomainObjectService
{
    public ReadGrantPart()
    {
    }
    
    protected Class getDomainObjectClass()
    {
        return GrantPart.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentGrantPart();
    }

    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return Cloner.copyIGrantPart2InfoGrantPart((IGrantPart) domainObject);
    }
}

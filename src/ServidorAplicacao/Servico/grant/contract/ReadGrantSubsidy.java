/*
 * Created on Jan 29, 2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantSubsidy;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public class ReadGrantSubsidy extends ReadDomainObjectService
{
    public ReadGrantSubsidy()
    {}

    protected Class getDomainObjectClass()
    {
        return GrantSubsidy.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentGrantSubsidy();
    }

    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return Cloner.copyIGrantSubsidy2InfoGrantSubsidy((IGrantSubsidy) domainObject);
    }
}

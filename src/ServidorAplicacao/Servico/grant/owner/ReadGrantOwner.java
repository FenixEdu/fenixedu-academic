/*
 * Created on 03/12/2003
 * 
 */
package ServidorAplicacao.Servico.grant.owner;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class ReadGrantOwner extends ReadDomainObjectService implements IServico
{
    private static ReadGrantOwner service = new ReadGrantOwner();
    /**
     * The singleton access method of this class.
     */
    public static ReadGrantOwner getService()
    {
        return service;
    }
    /**
     * The constructor of this class.
     */
    private ReadGrantOwner()
    {
    }
    /**
     * The name of the service
     */
    public final String getNome()
    {
        return "ReadGrantOwner";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentGrantOwner();
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return Cloner.copyIGrantOwner2InfoGrantOwner((IGrantOwner) domainObject);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return GrantOwner.class;
    }
}
/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.IOldPublication;
import Dominio.teacher.OldPublication;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadOldPublication extends ReadDomainObjectService {
    private static ReadOldPublication service = new ReadOldPublication();

    public static ReadOldPublication getService() {
        return service;
    }

    public ReadOldPublication() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadOldPublication";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return OldPublication.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        return sp.getIPersistentOldPublication();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return Cloner.copyIOldPublication2InfoOldPublication((IOldPublication) domainObject);
    }
}
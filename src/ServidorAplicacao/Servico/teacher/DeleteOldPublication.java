/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.teacher.OldPublication;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class DeleteOldPublication extends DeleteDomainObjectService {

    private static DeleteOldPublication service = new DeleteOldPublication();

    public static DeleteOldPublication getService() {
        return service;
    }

    private DeleteOldPublication() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "DeleteOldPublication";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return OldPublication.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentOldPublication();
    }
}
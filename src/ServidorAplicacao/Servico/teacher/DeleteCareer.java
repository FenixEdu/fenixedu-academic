/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.teacher.Career;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class DeleteCareer extends DeleteDomainObjectService {

    private static DeleteCareer service = new DeleteCareer();

    public static DeleteCareer getService() {
        return service;
    }

    private DeleteCareer() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "DeleteCareer";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return Career.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentCareer();
    }
}
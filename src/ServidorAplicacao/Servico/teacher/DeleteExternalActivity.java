/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.teacher.ExternalActivity;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class DeleteExternalActivity extends DeleteDomainObjectService {

    private static DeleteExternalActivity service = new DeleteExternalActivity();

    public static DeleteExternalActivity getService() {
        return service;
    }

    private DeleteExternalActivity() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "DeleteExternalActivity";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return ExternalActivity.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentExternalActivity();
    }
}
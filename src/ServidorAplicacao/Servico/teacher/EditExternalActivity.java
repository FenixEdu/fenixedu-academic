/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoExternalActivity;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.IExternalActivity;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.IPersistentExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditExternalActivity extends EditDomainObjectService {

    private static EditExternalActivity service = new EditExternalActivity();

    public static EditExternalActivity getService() {
        return service;
    }

    /**
     *  
     */
    private EditExternalActivity() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditExternalActivity";
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        return persistentExternalActivity;
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        IExternalActivity externalActivity = Cloner
                .copyInfoExternalActivity2IExternalActivity((InfoExternalActivity) infoObject);
        return externalActivity;
    }
}
/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoExternalActivity;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.ExternalActivity;
import Dominio.teacher.IExternalActivity;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.IPersistentExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadExternalActivity extends ReadDomainObjectService {
    private static ReadExternalActivity service = new ReadExternalActivity();

    public static ReadExternalActivity getService() {
        return service;
    }

    public ReadExternalActivity() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadExternalActivity";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return ExternalActivity.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        return persistentExternalActivity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        InfoExternalActivity infoExternalActivity = Cloner
                .copyIExternalActivity2InfoExternalActivity((IExternalActivity) domainObject);
        return infoExternalActivity;
    }
}
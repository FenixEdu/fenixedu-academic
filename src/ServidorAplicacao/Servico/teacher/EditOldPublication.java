/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoOldPublication;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.IOldPublication;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.IPersistentOldPublication;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditOldPublication extends EditDomainObjectService
{

    private static EditOldPublication service = new EditOldPublication();

    public static EditOldPublication getService()
    {
        return service;
    }

    /**
	 *  
	 */
    private EditOldPublication()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditOldPublication";
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
        return persistentOldPublication;
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        IOldPublication oldPublication = Cloner.copyInfoOldPublication2IOldPublication((InfoOldPublication) infoObject);
        return oldPublication;
    }
}

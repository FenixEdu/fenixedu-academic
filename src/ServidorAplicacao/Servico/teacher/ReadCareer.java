/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoCareer;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.Career;
import Dominio.teacher.ICareer;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.IPersistentCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCareer extends ReadDomainObjectService
{
    private static ReadCareer service = new ReadCareer();

    public static ReadCareer getService()
    {
        return service;
    }

    public ReadCareer()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadCareer";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
	 */
    protected Class getDomainObjectClass()
    {
        return Career.class;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        IPersistentCareer persistentCareer = sp.getIPersistentCareer();
        return persistentCareer;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
	 */
    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        InfoCareer infoCareer = Cloner.copyICareer2InfoCareer((ICareer) domainObject);
        return infoCareer;
    }
}

/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoCareer;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.ICareer;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.IPersistentCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCareer extends EditDomainObjectService
{

    private static EditCareer service = new EditCareer();

    public static EditCareer getService()
    {
        return service;
    }

    /**
	 *  
	 */
    private EditCareer()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditCareer";
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        IPersistentCareer persistentCareer = sp.getIPersistentCareer();
        return persistentCareer;
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        ICareer career = Cloner.copyInfoCareer2ICareer((InfoCareer) infoObject);
        return career;
    }
}

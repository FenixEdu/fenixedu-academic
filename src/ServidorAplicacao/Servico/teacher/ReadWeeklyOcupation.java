/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.IWeeklyOcupation;
import Dominio.teacher.WeeklyOcupation;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadWeeklyOcupation extends ReadDomainObjectService
{
    private static ReadWeeklyOcupation service = new ReadWeeklyOcupation();

    public static ReadWeeklyOcupation getService()
    {
        return service;
    }

    public ReadWeeklyOcupation()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadWeeklyOcupation";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
	 */
    protected Class getDomainObjectClass()
    {
        return WeeklyOcupation.class;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
        return persistentWeeklyOcupation;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
	 */
    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        InfoWeeklyOcupation infoWeeklyOcupation = Cloner.copyIWeeklyOcupation2InfoWeeklyOcupation((IWeeklyOcupation) domainObject);
        return infoWeeklyOcupation;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getISiteComponent(DataBeans.InfoObject)
	 */
    protected ISiteComponent getISiteComponent(InfoObject infoObject)
    {
        InfoWeeklyOcupation infoWeeklyOcupation = (InfoWeeklyOcupation) infoObject;
        return infoWeeklyOcupation;
    }
}

/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.SiteView;
import DataBeans.teacher.InfoCareer;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import Dominio.teacher.Career;
import Dominio.teacher.ICareer;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
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

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getISiteComponent(DataBeans.InfoObject)
	 */
    protected ISiteComponent getISiteComponent(InfoObject infoObject)
    {
        InfoCareer infoCareer = (InfoCareer) infoObject;
        return infoCareer;
    }

    //	public SiteView run(Integer careerId, UserView userView) throws FenixServiceException
    //	{
    //
    //		try
    //		{
    //			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
    //
    //			IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
    //			ITeacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());
    //
    //			IPersistentCareer persistentCareer = persistentSuport.getIPersistentCareer();
    //
    //			ICareer career = (ICareer) persistentCareer.readByOID(Career.class, careerId);
    //
    //			InfoCareer bodyComponent = Cloner.copyICareer2InfoCareer(career);
    //			SiteView siteView = new SiteView(bodyComponent);
    //			
    //			return siteView;
    //		} catch (ExcepcaoPersistencia e)
    //		{
    //			throw new FenixServiceException(e);
    //		}
    //	}
}

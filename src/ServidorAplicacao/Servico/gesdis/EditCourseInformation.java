/*
 * Created on 12/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.gesdis;

import DataBeans.InfoObject;
import DataBeans.gesdis.InfoCourseReport;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.gesdis.IPersistentCourseReport;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCourseInformation extends EditDomainObjectService
{
    /**
	 * The constructor of this class.
	 */
    public EditCourseInformation()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        IPersistentCourseReport persistentCourseReport = sp.getIPersistentCourseReport();
        return persistentCourseReport;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        ICourseReport courseReport =
            Cloner.copyInfoCourseReport2ICourseReport((InfoCourseReport) infoObject);
        return courseReport;
    }
}

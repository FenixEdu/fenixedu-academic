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
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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

    private static EditCourseInformation service = new EditCourseInformation();

    /**
	 * The singleton access method of this class.
	 */
    public static EditCourseInformation getService()
    {
        return service;
    }

    /**
	 * The constructor of this class.
	 */
    private EditCourseInformation()
    {
    }

    /**
	 * The name of the service
	 */
    public final String getNome()
    {
        return "EditCourseInformation";
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

    protected boolean canCreate(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentCourseReport persistentCourseReport = sp.getIPersistentCourseReport();
        IDisciplinaExecucaoPersistente persistentExecutionCourse =
            sp.getIDisciplinaExecucaoPersistente();

        ICourseReport oldCourseReport = (ICourseReport) domainObject;
        ICourseReport newCourseReport =
            persistentCourseReport.readCourseReportByExecutionCourse(oldCourseReport.getExecutionCourse());

        if ((newCourseReport != null)
            && !(newCourseReport.getIdInternal().equals(oldCourseReport.getIdInternal())))
            return false;
        return true;
    }
}

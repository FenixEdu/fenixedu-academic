/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import DataBeans.InfoObject;
import DataBeans.teacher.professorship.InfoSupportLesson;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ISupportLesson;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;

/**
 * @author jpvl
 */
public class EditSupportLessonByOID extends EditDomainObjectService
{
	private static EditSupportLessonByOID service = new EditSupportLessonByOID();

	/**
	 * The singleton access method of this class.
	 */
	public static EditSupportLessonByOID getService()
	{
		return service;
	}


    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentSupportLesson();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoSupportLesson2ISupportLesson((InfoSupportLesson) infoObject);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditSupportLessonByOID";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
	 *          ServidorPersistente.ISuportePersistente)
	 */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        ISupportLesson supportLesson = supportLessonDAO.readByUnique((ISupportLesson) domainObject);
        return supportLesson;
    }

}

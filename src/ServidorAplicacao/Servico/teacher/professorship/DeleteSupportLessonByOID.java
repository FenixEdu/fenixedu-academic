/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import Dominio.SupportLesson;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class DeleteSupportLessonByOID extends DeleteDomainObjectService
{
    private static DeleteSupportLessonByOID service = new DeleteSupportLessonByOID();

    /**
	 * The singleton access method of this class.
	 */
    public static DeleteSupportLessonByOID getService()
    {
        return service;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
	 */
    protected Class getDomainObjectClass()
    {
        // TODO Auto-generated method stub
        return SupportLesson.class;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentSupportLesson();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "DeleteSupportLessonByOID";
    }

}

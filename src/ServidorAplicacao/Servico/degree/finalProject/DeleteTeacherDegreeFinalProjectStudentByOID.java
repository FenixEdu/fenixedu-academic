/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.finalProject;

import Dominio.degree.finalProject.TeacherDegreeFinalProjectStudent;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class DeleteTeacherDegreeFinalProjectStudentByOID extends DeleteDomainObjectService
{
    private static DeleteTeacherDegreeFinalProjectStudentByOID service =
        new DeleteTeacherDegreeFinalProjectStudentByOID();

    /**
	 * The singleton access method of this class.
	 */
    public static DeleteTeacherDegreeFinalProjectStudentByOID getService()
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
        return TeacherDegreeFinalProjectStudent.class;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentTeacherDegreeFinalProjectStudent();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "DeleteTeacherDegreeFinalProjectStudentByOID";
    }

}

/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.workingTime;

import Dominio.teacher.workTime.TeacherInstitutionWorkTime;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class DeleteTeacherInstitutionWorkingTimeByOID extends DeleteDomainObjectService
{
    private static DeleteTeacherInstitutionWorkingTimeByOID service =
        new DeleteTeacherInstitutionWorkingTimeByOID();

    /**
	 * The singleton access method of this class.
	 */
    public static DeleteTeacherInstitutionWorkingTimeByOID getService()
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
        return TeacherInstitutionWorkTime.class;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentTeacherInstitutionWorkingTime();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "DeleteTeacherInstitutionWorkingTimeByOID";
    }

}

/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.workingTime;

import DataBeans.InfoObject;
import DataBeans.teacher.workTime.InfoTeacherInstitutionWorkTime;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

/**
 * @author jpvl
 */
public class EditTeacherInstitutionWorkingTimeByOID extends EditDomainObjectService
{
    private static EditTeacherInstitutionWorkingTimeByOID service =
        new EditTeacherInstitutionWorkingTimeByOID();

    /**
	 * The singleton access method of this class.
	 */
    public static EditTeacherInstitutionWorkingTimeByOID getService()
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
        return sp.getIPersistentTeacherInstitutionWorkingTime();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoTeacherInstitutionWorkingTime2ITeacherInstitutionWorkTime(
            (InfoTeacherInstitutionWorkTime) infoObject);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditTeacherInstitutionWorkingTimeByOID";
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
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO =
            sp.getIPersistentTeacherInstitutionWorkingTime();
        ITeacherInstitutionWorkTime teacherInstitutionWorkTime =
            (ITeacherInstitutionWorkTime) domainObject;
        teacherInstitutionWorkTime = teacherInstitutionWorkingTimeDAO.readByUnique(teacherInstitutionWorkTime);
        return teacherInstitutionWorkTime;
    }

}

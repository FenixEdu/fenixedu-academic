/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import DataBeans.InfoObject;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IProfessorship;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditShiftProfessorshipByOID extends EditDomainObjectService
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentShiftProfessorship();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoShiftProfessorship2IShiftProfessorship((InfoShiftProfessorship) infoObject);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditShiftProfessorshipByOID";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject, ServidorPersistente.ISuportePersistente)
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IProfessorship professorship = (IProfessorship) domainObject;
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        professorship = professorshipDAO.readByTeacherAndExecutionCourse(professorship.getTeacher(), professorship.getExecutionCourse());
        return professorship;
    }

}

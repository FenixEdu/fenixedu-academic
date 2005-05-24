/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditShiftProfessorshipByOID extends EditDomainObjectService {

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentShiftProfessorship();
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return Cloner
                .copyInfoShiftProfessorship2IShiftProfessorship((InfoShiftProfessorship) infoObject);
    }

    public String getNome() {
        return "EditShiftProfessorshipByOID";
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IProfessorship professorship = (IProfessorship) domainObject;
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        professorship = professorshipDAO.readByTeacherAndExecutionCourse(professorship.getTeacher()
                .getIdInternal(), professorship.getExecutionCourse().getIdInternal());
        return professorship;
    }

}
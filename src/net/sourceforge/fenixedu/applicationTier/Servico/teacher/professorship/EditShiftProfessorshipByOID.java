/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditShiftProfessorshipByOID extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject)
            throws ExcepcaoPersistencia {
        InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) infoObject;
        ShiftProfessorship shiftProfessorship = (ShiftProfessorship) domainObject;

        shiftProfessorship.setPercentage(infoShiftProfessorship.getPercentage());

        Professorship professorship = (Professorship) persistentObject.readByOID(Professorship.class,
                infoShiftProfessorship.getInfoProfessorship().getIdInternal());
        shiftProfessorship.setProfessorship(professorship);

        Shift shift = (Shift) persistentObject.readByOID(Shift.class, infoShiftProfessorship.getInfoShift()
                .getIdInternal());
        shiftProfessorship.setShift(shift);
    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeShiftProfessorship();
    }

    @Override
    protected Class getDomainObjectClass() {
        return ShiftProfessorship.class;
    }

    @Override
    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentShiftProfessorship();
    }

    protected DomainObject readObjectByUnique(DomainObject domainObject, ISuportePersistente persistentSupport)
            throws ExcepcaoPersistencia {
        Professorship professorship = (Professorship) domainObject;
        IPersistentProfessorship professorshipDAO = persistentSupport.getIPersistentProfessorship();
        professorship = professorshipDAO.readByTeacherAndExecutionCourse(professorship.getTeacher()
                .getIdInternal(), professorship.getExecutionCourse().getIdInternal());
        return professorship;
    }

}

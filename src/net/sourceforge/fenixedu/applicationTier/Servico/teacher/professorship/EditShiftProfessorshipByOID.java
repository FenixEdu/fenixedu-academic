/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditShiftProfessorshipByOID extends EditDomainObjectService {

    @Override
    protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject,
            IDomainObject domainObject) throws ExcepcaoPersistencia {
        InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) infoObject;
        IShiftProfessorship shiftProfessorship = (ShiftProfessorship) domainObject;

        shiftProfessorship.setPercentage(infoShiftProfessorship.getPercentage());
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        IPersistentShiftProfessorship persistentShift = sp.getIPersistentShiftProfessorship();
        IProfessorship professorship = new Professorship();
        IShift shift = new Shift();

        professorship = (IProfessorship) persistentProfessorship.readByOID(Professorship.class,
                infoShiftProfessorship.getInfoProfessorship().getIdInternal());
        shiftProfessorship.setProfessorship(professorship);
        shiftProfessorship.setKeyProfessorship(professorship.getIdInternal());

        shift = (IShift) persistentShift.readByOID(Shift.class, infoShiftProfessorship.getInfoShift()
                .getIdInternal());
        shiftProfessorship.setShift(shift);
        shiftProfessorship.setKeyShift(shift.getIdInternal());
    }

    @Override
    protected IDomainObject createNewDomainObject(InfoObject infoObject) {
        return new ShiftProfessorship();
    }

    @Override
    protected Class getDomainObjectClass() {
        return ShiftProfessorship.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentShiftProfessorship();
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

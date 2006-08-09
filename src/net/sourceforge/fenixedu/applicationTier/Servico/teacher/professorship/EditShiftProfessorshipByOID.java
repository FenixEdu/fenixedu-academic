package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditShiftProfessorshipByOID extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject)
            throws ExcepcaoPersistencia {
        InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) infoObject;
        ShiftProfessorship shiftProfessorship = (ShiftProfessorship) domainObject;

        shiftProfessorship.setPercentage(infoShiftProfessorship.getPercentage());

        Professorship professorship = rootDomainObject.readProfessorshipByOID(infoShiftProfessorship.getInfoProfessorship().getIdInternal());
        shiftProfessorship.setProfessorship(professorship);

        Shift shift = rootDomainObject.readShiftByOID(infoShiftProfessorship.getInfoShift().getIdInternal());
        shiftProfessorship.setShift(shift);
    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return new ShiftProfessorship();
    }

	@Override
	protected DomainObject readDomainObject(Integer idInternal) {
		return rootDomainObject.readShiftProfessorshipByOID(idInternal);
	}

}

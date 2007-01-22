package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalUnitBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateExternalUnit extends Service {
    
    public Unit run(final CreateExternalUnitBean externalUnitBean) {
	return Unit.createNewAcademicInstitution(
		externalUnitBean.getUnitName(),
		externalUnitBean.getUnitCode(),
		externalUnitBean.getUnitType(),
		externalUnitBean.getParentUnit());
    }
}

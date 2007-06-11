package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalUnitBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class EditExternalUnit extends Service {
    
    public void run(final EditExternalUnitBean externalUnitBean) {
	final Unit externalUnit = externalUnitBean.getExternalUnit();
	externalUnit.edit(externalUnitBean.getUnitName(), externalUnitBean.getUnitCode());
    }
}

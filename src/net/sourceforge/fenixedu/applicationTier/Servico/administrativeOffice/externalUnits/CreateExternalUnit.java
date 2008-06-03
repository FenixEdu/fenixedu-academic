package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.CreateUnit;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalUnitBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class CreateExternalUnit extends Service {
    
    public Unit run(final CreateExternalUnitBean externalUnitBean) throws FenixServiceException {
	
	if (externalUnitBean.getUnitType() == PartyTypeEnum.DEPARTMENT) {
	    return DepartmentUnit.createNewOfficialExternalDepartmentUnit(
		    externalUnitBean.getUnitName(), externalUnitBean.getUnitCode(), externalUnitBean.getParentUnit());
	} else {
	    return new CreateUnit().run(externalUnitBean.getParentUnit(),
		    new MultiLanguageString(Language.getDefaultLanguage(), externalUnitBean.getUnitName()),
		    null, externalUnitBean.getUnitCode(), new YearMonthDay(), null, externalUnitBean.getUnitType(),
		    null, null, null, null, null, null, null, null);
	}
    }
}

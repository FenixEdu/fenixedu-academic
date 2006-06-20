package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertInstitution extends Service {

    public Unit run(String institutionName) throws ExcepcaoPersistencia, FenixServiceException {

        Unit institution = UnitUtils.readExternalInstitutionUnitByName(institutionName);

        if (institution != null) {
            throw new ExistingServiceException(
                    "error.exception.commons.institution.institutionAlreadyExists");
        }

        Unit externalIntitutionsUnit = UnitUtils
                .readUnitWithoutParentstByName(UnitUtils.EXTERNAL_INSTITUTION_UNIT_NAME);
        if (externalIntitutionsUnit == null) {
            throw new FenixServiceException(
                    "error.exception.commons.institution.rootInstitutionNotFound");
        }

        Unit newInstitution = makeNewUnit(institutionName, externalIntitutionsUnit);
        return newInstitution;
    }

    private Unit makeNewUnit(String unitName, Unit externalIntitutionsUnit) throws ExcepcaoPersistencia {
        Unit institutionUnit = new Unit();
        institutionUnit.setName(unitName);
        institutionUnit.setBeginDate(Calendar.getInstance().getTime());
        institutionUnit.setType(PartyTypeEnum.EXTERNAL_INSTITUTION);
        institutionUnit.addParent(externalIntitutionsUnit, AccountabilityType
                .readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
        return institutionUnit;
    }
}

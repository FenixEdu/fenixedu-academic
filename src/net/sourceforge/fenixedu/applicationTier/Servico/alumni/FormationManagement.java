package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FormationManagement extends Service {

    protected AcademicalInstitutionUnit getFormationInstitution(final AlumniFormation formation) {
	AcademicalInstitutionUnit institutionUnit = formation.getInstitution();

	if (institutionUnit == null) {

	    institutionUnit = UniversityUnit.createNewUniversityUnit(new MultiLanguageString(formation.getForeignUnit()), null,
		    null, new YearMonthDay(), null, getParentUnit(formation), null, null, false, null);
	    institutionUnit.setInstitutionType(formation.getInstitutionType());
	}
	return institutionUnit;
    }

    protected Unit getParentUnit(AlumniFormation formation) {
	if (formation.isNationalInstitution() || formation.getCountryUnit() == null) {
	    return CountryUnit.getDefault();
	}

	return formation.getCountryUnit();
    }

}

package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FormationManagement extends FenixService {

    protected AcademicalInstitutionUnit getFormationInstitution(final AlumniFormation formation) {
	AcademicalInstitutionUnit institutionUnit = formation.getInstitution();

	if (institutionUnit == null
		&& formation.getInstitutionType() != null
		&& (formation.getInstitutionType().equals(AcademicalInstitutionType.FOREIGN_INSTITUTION) || formation
			.getInstitutionType().equals(AcademicalInstitutionType.OTHER_INSTITUTION))) {

	    if (!StringUtils.isEmpty(formation.getForeignUnit())) {
		institutionUnit = UniversityUnit.createNewUniversityUnit(new MultiLanguageString(formation.getForeignUnit()),
			null, null, null, new YearMonthDay(), null, getParentUnit(formation), null, null, false, null);
		institutionUnit.setInstitutionType(formation.getInstitutionType());
	    }
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

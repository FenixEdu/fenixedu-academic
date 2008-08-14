package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.EducationArea;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.FormationType;
import net.sourceforge.fenixedu.domain.QualificationType;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.lang.StringUtils;

public class AlumniFormation implements Serializable {

    private final int FIRST_YEAR = 1933;

    private String typeSchema;
    private String institutionSchema;

    private FormationType formationType;
    private QualificationType formationDegree;

    private List<AlumniEducationArea> allAreas;
    private DomainReference<EducationArea> educationArea;

    private AcademicalInstitutionType institutionType;
    private DomainReference<AcademicalInstitutionUnit> parentUnit;
    private DomainReference<AcademicalInstitutionUnit> childUnit;
    private String foreignUnit;
    private DomainReference<CountryUnit> countryUnit;

    private String formationBeginYear;
    private String formationEndYear;

    private BigDecimal formationCredits;
    private Integer formationHours;

    private DomainReference<Formation> formation;

    public AlumniFormation() {
	setTypeSchema("alumni.formation.degree");
	setInstitutionSchema("alumni.formation.no.institution.type");
	initEducationAreas();
    }

    private void initEducationAreas() {
	allAreas = new ArrayList<AlumniEducationArea>();
	for (EducationArea area : RootDomainObject.getInstance().getEducationAreas()) {
	    allAreas.add(new AlumniEducationArea(area));
	}
    }

    public static AlumniFormation buildFrom(Formation dbFormation) {

	AlumniFormation formation = new AlumniFormation();

	formation.setTypeSchema("alumni.formation.degree.both");
	formation.setFormationType(dbFormation.getFormationType());
	formation.setFormationDegree(dbFormation.getType());

	setInstitution(formation, dbFormation);

	formation.setEducationArea(dbFormation.getEducationArea());

	formation.setFormationBeginYear(dbFormation.getBeginYear());
	formation.setFormationEndYear(dbFormation.getYear());
	formation.setFormationCredits(dbFormation.getEctsCredits());
	formation.setFormationHours(dbFormation.getFormationHours());

	formation.setAssociatedFormation(dbFormation);
	return formation;
    }

    private static void setInstitution(AlumniFormation bean, Formation dbFormation) {

	Unit institution = dbFormation.getInstitution();
	if (institution instanceof AcademicalInstitutionUnit) {

	    AcademicalInstitutionType academicalInstitutionType = ((AcademicalInstitutionUnit) institution).getInstitutionType();
	    bean.setInstitutionType(academicalInstitutionType);

	    switch (academicalInstitutionType) {

	    case NATIONAL_PUBLIC_INSTITUTION:
	    case NATIONAL_PRIVATE_INSTITUTION:
		if (AcademicalInstitutionUnit.readOfficialParentUnitsByType(academicalInstitutionType).contains(institution)) {
		    bean.setParentInstitution((AcademicalInstitutionUnit) institution);
		    bean.setInstitutionSchema("alumni.formation.national.institution.parent");

		} else {
		    for (AcademicalInstitutionUnit parent : AcademicalInstitutionUnit.readOfficialParentUnitsByType(bean
			    .getInstitutionType())) {
			for (Accountability unitRelation : parent.getChilds()) {
			    if (unitRelation.getChildParty().equals(institution)) {
				bean.setParentInstitution((AcademicalInstitutionUnit) unitRelation.getParentParty());
			    }
			}
		    }
		    bean.setChildInstitution((AcademicalInstitutionUnit) institution);
		    bean.setInstitutionSchema("alumni.formation.national.institution.both");
		}
		break;
	    case FOREIGN_INSTITUTION:
		setNonNationalInstitution(bean, institution, "alumni.formation.foreign.institution");
		break;
	    case OTHER_INSTITUTION:
		setNonNationalInstitution(bean, institution, "alumni.formation.other.institution");
		break;
	    }

	} else {
	    bean.setForeignUnit(institution.getName());
	    if (institution.hasParentUnit(CountryUnit.getDefault())) {
		bean.setInstitutionType(AcademicalInstitutionType.OTHER_INSTITUTION);
		bean.setInstitutionSchema("alumni.formation.other.institution");
	    } else {
		bean.setInstitutionType(AcademicalInstitutionType.FOREIGN_INSTITUTION);
		bean.setInstitutionSchema("alumni.formation.foreign.institution");
	    }
	}
    }

    private static void setNonNationalInstitution(AlumniFormation bean, Unit institution, String schema) {
	bean.setInstitutionSchema(schema);
	bean.setForeignUnit(institution.getName());
	bean.setChildInstitution((AcademicalInstitutionUnit) institution);
	bean.setCountryUnit((CountryUnit) institution.getParentParties(CountryUnit.class).iterator().next());
    }

    public int getFirstYear() {
	return FIRST_YEAR;
    }

    public int getLastYear() {
	return Integer.valueOf(ExecutionYear.readLastExecutionYear().getYear()).intValue();
    }

    public String getTypeSchema() {
	return this.typeSchema;
    }

    public void setTypeSchema(String schema) {
	this.typeSchema = schema;
    }

    public String getInstitutionSchema() {
	return this.institutionSchema;
    }

    public void setInstitutionSchema(String schema) {
	this.institutionSchema = schema;
    }

    public void updateTypeSchema() {
	if (getFormationType() == null) {
	    setTypeSchema("alumni.formation.degree");
	} else {
	    setTypeSchema("alumni.formation.degree.both");
	}
    }

    // refactor
    public void updateInstitutionSchema() {
	if (getInstitutionType() == null) {
	    setInstitutionSchema("alumni.formation.no.institution.type");
	} else {
	    switch (getInstitutionType()) {

	    case NATIONAL_PUBLIC_INSTITUTION:
	    case NATIONAL_PRIVATE_INSTITUTION:
		if (getParentInstitution() == null) {
		    setInstitutionSchema("alumni.formation.national.institution.parent");
		} else {
		    if (getParentInstitution().hasAnyOfficialChilds()) {
			setInstitutionSchema("alumni.formation.national.institution.both");
		    } else {
			setInstitutionSchema("alumni.formation.national.institution.parent");
		    }
		}
		break;
	    case FOREIGN_INSTITUTION:
		setInstitutionSchema("alumni.formation.foreign.institution");
		break;
	    case OTHER_INSTITUTION:
		setInstitutionSchema("alumni.formation.other.institution");
		break;
	    }
	}
    }

    public FormationType getFormationType() {
	return formationType;
    }

    public void setFormationType(FormationType formationType) {
	this.formationType = formationType;
    }

    public QualificationType getFormationDegree() {
	return formationDegree;
    }

    public void setFormationDegree(QualificationType formationDegree) {
	this.formationDegree = formationDegree;
    }

    public String getFormationBeginYear() {
	return formationBeginYear;
    }

    public void setFormationBeginYear(String formationBeginYear) {
	this.formationBeginYear = formationBeginYear;
    }

    public String getFormationEndYear() {
	return formationEndYear;
    }

    public void setFormationEndYear(String formationEndYear) {
	this.formationEndYear = formationEndYear;
    }

    public BigDecimal getFormationCredits() {
	return formationCredits;
    }

    public void setFormationCredits(BigDecimal formationCredits) {
	this.formationCredits = formationCredits;
    }

    public Integer getFormationHours() {
	return formationHours;
    }

    public void setFormationHours(Integer formationHours) {
	this.formationHours = formationHours;
    }

    public EducationArea getEducationArea() {
	return (this.educationArea != null) ? this.educationArea.getObject() : null;
    }

    public void setEducationArea(EducationArea educationArea) {
	this.educationArea = (educationArea != null) ? new DomainReference<EducationArea>(educationArea) : null;
    }

    public void setEducationArea(Integer educationAreaId) {
	this.educationArea = (educationAreaId != null) ? new DomainReference<EducationArea>(RootDomainObject.getInstance()
		.readEducationAreaByOID(educationAreaId)) : null;
    }

    public AcademicalInstitutionUnit getInstitution() {
	if (isNationalInstitution()) {
	    if (getChildInstitution() != null) {
		return getChildInstitution();
	    } else if (getParentInstitution() != null) {
		if (!getParentInstitution().hasAnyChilds()) {
		    return getParentInstitution();
		}
	    }
	    return null;
	} else {
	    return getChildInstitution();
	}
    }

    public AcademicalInstitutionUnit getParentInstitution() {
	return (this.parentUnit != null) ? this.parentUnit.getObject() : null;
    }

    public void setParentInstitution(AcademicalInstitutionUnit parentUnit) {
	this.parentUnit = (parentUnit != null) ? new DomainReference<AcademicalInstitutionUnit>(parentUnit) : null;
    }

    public AcademicalInstitutionUnit getChildInstitution() {
	return (this.childUnit != null) ? this.childUnit.getObject() : null;
    }

    public void setChildInstitution(AcademicalInstitutionUnit childUnit) {
	this.childUnit = (childUnit != null) ? new DomainReference<AcademicalInstitutionUnit>(childUnit) : null;
    }

    public AcademicalInstitutionType getInstitutionType() {
	return institutionType;
    }

    public void setInstitutionType(AcademicalInstitutionType institutionType) {
	this.institutionType = institutionType;
    }

    public Formation getAssociatedFormation() {
	return (this.formation != null) ? this.formation.getObject() : null;
    }

    public void setAssociatedFormation(Formation formation) {
	this.formation = (formation != null) ? new DomainReference<Formation>(formation) : null;
    }

    public boolean hasAssociatedFormation() {
	return getAssociatedFormation() != null;
    }

    public List<AlumniEducationArea> getAllAreas() {
	Collections.sort(allAreas, AlumniEducationArea.COMPARATOR_BY_CODE);
	return allAreas;
    }

    public Boolean hasFullInformation() {

	if (getFormationDegree() == null) {
	    return Boolean.FALSE;
	}

	if (getEducationArea() == null) {
	    return Boolean.FALSE;
	}

	if (getInstitution() == null && StringUtils.isEmpty(getForeignUnit())) {
	    return Boolean.FALSE;
	}

	if (StringUtils.isEmpty(getFormationBeginYear())) {
	    return Boolean.FALSE;
	}

	return Boolean.TRUE;
    }

    public String getForeignUnit() {
	return foreignUnit;
    }

    public void setForeignUnit(String foreignUnit) {
	setParentInstitution(null);
	setChildInstitution(null);
	this.foreignUnit = foreignUnit;
    }

    public void setCountryUnit(CountryUnit countryUnit) {
	this.countryUnit = (countryUnit != null) ? new DomainReference<CountryUnit>(countryUnit) : null;
    }

    public CountryUnit getCountryUnit() {
	return (this.countryUnit != null) ? this.countryUnit.getObject() : null;
    }

    public boolean isNationalInstitution() {
	return !(getInstitutionType().equals(AcademicalInstitutionType.FOREIGN_INSTITUTION));
    }
}

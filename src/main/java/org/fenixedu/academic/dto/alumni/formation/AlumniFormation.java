/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.EducationArea;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.FormationType;
import net.sourceforge.fenixedu.domain.QualificationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.SchoolUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

public class AlumniFormation implements Serializable, IFormation {

    private final int FIRST_YEAR = 1933;

    private String typeSchema;
    private String institutionSchema;

    private FormationType formationType;
    private QualificationType formationDegree;

    private List<AlumniEducationArea> allAreas;
    private EducationArea educationArea;

    private AcademicalInstitutionType institutionType;
    private AcademicalInstitutionUnit parentUnit;
    private AcademicalInstitutionUnit childUnit;
    private String foreignUnit;
    private CountryUnit countryUnit;

    private String formationBeginYear;
    private String formationEndYear;

    private BigDecimal formationCredits;
    private Integer formationHours;

    private Formation formation;

    public AlumniFormation() {
        setTypeSchema("alumni.formation.degree");
        setInstitutionSchema("alumni.formation.no.institution.type");
        initEducationAreas();
    }

    private void initEducationAreas() {
        allAreas = new ArrayList<AlumniEducationArea>();
        for (EducationArea area : Bennu.getInstance().getEducationAreasSet()) {
            allAreas.add(new AlumniEducationArea(area));
        }
    }

    public static AlumniFormation buildFrom(Formation dbFormation) {

        AlumniFormation formation = new AlumniFormation();

        if (dbFormation.getFormationType() == null) {
            formation.setTypeSchema("alumni.formation.degree");
        } else {
            formation.setTypeSchema("alumni.formation.degree.both");
        }
        formation.setFormationType(dbFormation.getFormationType());
        formation.setFormationDegree(dbFormation.getType());

        formation.setInstitutionType(dbFormation.getInstitutionType());
        if (dbFormation.getInstitutionType() != null) {
            formation.setInstitutionSchema("alumni.formation.national.institution.parent");
        }
        //if the institution is not null the correspondent schema will be resetted in the next method
        setInstitution(formation, dbFormation);
        if (dbFormation.getBaseInstitution() != null) {
            formation.setParentInstitution((AcademicalInstitutionUnit) dbFormation.getBaseInstitution());
            if (((AcademicalInstitutionUnit) dbFormation.getBaseInstitution()).hasAnyOfficialChilds()) {
                formation.setInstitutionSchema("alumni.formation.national.institution.both");
            } else {
                formation.setInstitutionSchema("alumni.formation.national.institution.parent");
            }
        }

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

            AcademicalInstitutionType academicalInstitutionType = dbFormation.getInstitutionType();

            switch (academicalInstitutionType) {

            case NATIONAL_PUBLIC_INSTITUTION:
            case NATIONAL_PRIVATE_INSTITUTION:
                if (AcademicalInstitutionUnit.readOfficialParentUnitsByType(academicalInstitutionType).contains(institution)) {
                    bean.setParentInstitution((AcademicalInstitutionUnit) institution);
                    bean.setInstitutionSchema("alumni.formation.national.institution.parent");

                } else {
                    bean.setChildInstitution((AcademicalInstitutionUnit) institution);
                    bean.setInstitutionSchema("alumni.formation.national.institution.both");
                }
                break;
            case FOREIGN_INSTITUTION:
                setNonNationalInstitution(bean, institution, "alumni.formation.foreign.institution", dbFormation.getCountryUnit());
                break;
            case OTHER_INSTITUTION:
                setNonNationalInstitution(bean, institution, "alumni.formation.other.institution", dbFormation.getCountryUnit());
                break;
            }

        } else {
            if (institution != null) {
                bean.setForeignUnit(institution.getName());
                if (institution.hasParentUnit(CountryUnit.getDefault())) {
                    bean.setInstitutionSchema("alumni.formation.other.institution");
                } else {
                    bean.setInstitutionSchema("alumni.formation.foreign.institution");
                }
            } else {
                if (bean.getInstitutionType() == AcademicalInstitutionType.FOREIGN_INSTITUTION) {
                    bean.setInstitutionSchema("alumni.formation.foreign.institution");
                } else if (bean.getInstitutionType() == AcademicalInstitutionType.OTHER_INSTITUTION) {
                    bean.setInstitutionSchema("alumni.formation.other.institution");
                }
            }
        }
    }

    private static void setNonNationalInstitution(AlumniFormation bean, Unit institution, String schema, CountryUnit countryUnit) {
        bean.setInstitutionSchema(schema);
        bean.setForeignUnit(institution.getName());
        bean.setChildInstitution((AcademicalInstitutionUnit) institution);
        bean.setCountryUnit(countryUnit);
    }

    @Override
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
        if (formationType == null) {
            setFormationDegree(null);
        }
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
        return this.educationArea;
    }

    public void setEducationArea(EducationArea educationArea) {
        this.educationArea = educationArea;
    }

    public AcademicalInstitutionUnit getInstitution() {
        if (getInstitutionType() != null) {
            if (isNationalInstitution()) {
                if (getChildInstitution() != null) {
                    return getChildInstitution();
                } else if (getParentInstitution() != null) {
                    if ((getParentInstitution() instanceof UniversityUnit)
                            && (getParentInstitution().getChildParties(SchoolUnit.class).isEmpty())) {
                        return getParentInstitution();
                    }
                }
                return null;
            } else {
                return getChildInstitution();
            }
        }
        return null;
    }

    public AcademicalInstitutionUnit getParentInstitution() {
        return this.parentUnit;
    }

    public void setParentInstitution(AcademicalInstitutionUnit parentUnit) {
        this.parentUnit = parentUnit;
        if (parentUnit == null) {
            setChildInstitution(null);
        }
    }

    public AcademicalInstitutionUnit getChildInstitution() {
        return this.childUnit;
    }

    public void setChildInstitution(AcademicalInstitutionUnit childUnit) {
        this.childUnit = childUnit;
    }

    public AcademicalInstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(AcademicalInstitutionType institutionType) {
        this.institutionType = institutionType;
    }

    public Formation getAssociatedFormation() {
        return this.formation;
    }

    public void setAssociatedFormation(Formation formation) {
        this.formation = formation;
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
        this.countryUnit = countryUnit;
    }

    public CountryUnit getCountryUnit() {
        return this.countryUnit;
    }

    public boolean isNationalInstitution() {
        return !(getInstitutionType().equals(AcademicalInstitutionType.FOREIGN_INSTITUTION));
    }
}

/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PrecedentDegreeInformationBean implements Serializable {

    private DomainReference<PrecedentDegreeInformation> precedentDegreeInformation;

    private DomainReference<Unit> institution;

    private String newInstitutionName;

    private String currentInstitutionName;

    private String degreeDesignation;

    private String conclusionGrade;

    private Integer conclusionYear;

    private DomainReference<Country> country;

    public PrecedentDegreeInformationBean(PrecedentDegreeInformation information) {
        precedentDegreeInformation = new DomainReference<PrecedentDegreeInformation>(information);
        degreeDesignation = information.getDegreeDesignation();
        conclusionGrade = information.getConclusionGrade();
        conclusionYear = information.getConclusionYear();
        country = (information.getCountry() == null) ? null : new DomainReference<Country>(information
                .getCountry());
        currentInstitutionName = (information.getInstitution() == null) ? null : information
                .getInstitution().getName();
    }

    public PrecedentDegreeInformation getPrecedentDegreeInformation() {
        return precedentDegreeInformation.getObject();
    }

    public String getCurrentInstitutionName() {
        return currentInstitutionName;
    }

    public String getNewInstitutionName() {
        return newInstitutionName;
    }

    public void setNewInstitutionName(String newInstitutionName) {
        this.newInstitutionName = newInstitutionName;
    }

    public Unit getInstitution() {
        return (institution == null) ? null : institution.getObject();
    }

    public void setInstitution(Unit institution) {
        this.institution = (institution == null) ? null : new DomainReference<Unit>(institution);
    }

    public String getConclusionGrade() {
        return conclusionGrade;
    }

    public void setConclusionGrade(String conclusionGrade) {
        this.conclusionGrade = conclusionGrade;
    }

    public Integer getConclusionYear() {
        return conclusionYear;
    }

    public void setConclusionYear(Integer conclusionYear) {
        this.conclusionYear = conclusionYear;
    }

    public Country getCountry() {
        return (country == null) ? null : country.getObject();
    }

    public void setCountry(Country country) {
        this.country = (country == null) ? null : new DomainReference<Country>(country);
    }

    public String getDegreeDesignation() {
        return degreeDesignation;
    }

    public void setDegreeDesignation(String degreeDesignation) {
        this.degreeDesignation = degreeDesignation;
    }

    public boolean isInstitutionFilled() {
        return (getInstitution() != null
                || (getNewInstitutionName() != null && getNewInstitutionName().length() > 0) || getCurrentInstitutionName() != null);
    }

}

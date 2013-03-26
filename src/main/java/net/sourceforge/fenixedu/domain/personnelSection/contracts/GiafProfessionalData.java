package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class GiafProfessionalData extends GiafProfessionalData_Base {

    public GiafProfessionalData() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public GiafProfessionalData(final PersonProfessionalData personProfessionalData, final String personGiafIdentification,
            final LocalDate institutionEntryDate, final ContractSituation contractSituation,
            final String contractSituationGiafId, final LocalDate contractSituationDate,
            final LocalDate terminationSituationDate, final ProfessionalRelation professionalRelation,
            final String professionalRelationGiafId, final LocalDate professionalRelationDate,
            final ProfessionalContractType professionalContractType, final String professionalContractTypeGiafId,
            final ProfessionalCategory professionalCategory, final String professionalCategoryGiafId,
            final LocalDate professionalCategoryDate, final ProfessionalRegime professionalRegime,
            final String professionalRegimeGiafId, final LocalDate professionalRegimeDate, final Campus campus,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPersonProfessionalData(personProfessionalData);
        setGiafPersonIdentification(personGiafIdentification);
        setInstitutionEntryDate(institutionEntryDate);
        setContractSituation(contractSituation);
        setContractSituationGiafId(contractSituationGiafId);
        setContractSituationDate(contractSituationDate);
        setTerminationSituationDate(terminationSituationDate);
        setProfessionalRelation(professionalRelation);
        setProfessionalRelationGiafId(professionalRelationGiafId);
        setProfessionalRelationDate(professionalRelationDate);
        setProfessionalContractType(professionalContractType);
        setProfessionalContractTypeGiafId(professionalContractTypeGiafId);
        setProfessionalCategory(professionalCategory);
        setProfessionalCategoryGiafId(professionalCategoryGiafId);
        setProfessionalCategoryDate(professionalCategoryDate);
        setProfessionalRegime(professionalRegime);
        setProfessionalRegimeGiafId(professionalRegimeGiafId);
        setProfessionalRegimeDate(professionalRegimeDate);
        setCampus(campus);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    public Set<PersonContractSituation> getValidPersonContractSituations() {
        Set<PersonContractSituation> personContractSituations = new HashSet<PersonContractSituation>();
        for (PersonContractSituation personContractSituation : getPersonContractSituations()) {
            if (personContractSituation.isValid() && personContractSituation.getAnulationDate() == null
                    && personContractSituation.getContractSituation().getInExercise()
                    && !personContractSituation.getContractSituation().getEndSituation()) {
                personContractSituations.add(personContractSituation);
            }
        }
        return personContractSituations;
    }

    public Set<PersonProfessionalExemption> getValidPersonProfessionalExemption() {
        Set<PersonProfessionalExemption> personProfessionalExemptions = new HashSet<PersonProfessionalExemption>();
        for (PersonProfessionalExemption personProfessionalExemption : getPersonProfessionalExemptions()) {
            if (personProfessionalExemption.isValid() && personProfessionalExemption.getAnulationDate() == null) {
                personProfessionalExemptions.add(personProfessionalExemption);
            }
        }
        return personProfessionalExemptions;
    }

    public Set<PersonProfessionalRegime> getValidPersonProfessionalRegimes() {
        Set<PersonProfessionalRegime> personProfessionalRegimes = new HashSet<PersonProfessionalRegime>();
        for (PersonProfessionalRegime personProfessionalRegime : getPersonProfessionalRegimes()) {
            if (personProfessionalRegime.isValid() && personProfessionalRegime.getAnulationDate() == null) {
                personProfessionalRegimes.add(personProfessionalRegime);
            }
        }
        return personProfessionalRegimes;
    }

    public Set<PersonProfessionalCategory> getValidPersonProfessionalCategories() {
        Set<PersonProfessionalCategory> personProfessionalCategories = new HashSet<PersonProfessionalCategory>();
        for (PersonProfessionalCategory personProfessionalCategory : getPersonProfessionalCategories()) {
            if (personProfessionalCategory.isValid() && personProfessionalCategory.getAnulationDate() == null) {
                personProfessionalCategories.add(personProfessionalCategory);
            }
        }
        return personProfessionalCategories;
    }

    public Set<PersonProfessionalRelation> getValidPersonProfessionalRelations() {
        Set<PersonProfessionalRelation> personProfessionalRelations = new HashSet<PersonProfessionalRelation>();
        for (PersonProfessionalRelation personProfessionalRelation : getPersonProfessionalRelations()) {
            if (personProfessionalRelation.isValid() && personProfessionalRelation.getAnulationDate() == null) {
                personProfessionalRelations.add(personProfessionalRelation);
            }
        }
        return personProfessionalRelations;
    }

    public boolean isISTID() {
        final String typeGiafId = getProfessionalContractTypeGiafId();
        return typeGiafId != null && typeGiafId.equals("ID");
    }

    public boolean isADIST() {
        final String typeGiafId = getProfessionalContractTypeGiafId();
        return typeGiafId != null && typeGiafId.equals("AD");
    }

    public boolean isIST() {
        return !isISTID() && !isADIST();
    }

    public String getEmployer() {
        return isADIST() ? "ADIST" : (isISTID() ? "IST-ID" : "IST");
    }

    public boolean isActive() {
        return !(getContractSituation().getEndSituation() || !getContractSituation().getInExercise() || (getTerminationSituationDate() != null
                && getTerminationSituationDate().isBefore(new LocalDate()) && getTerminationSituationDate().isAfter(
                getContractSituationDate())));
    }

}

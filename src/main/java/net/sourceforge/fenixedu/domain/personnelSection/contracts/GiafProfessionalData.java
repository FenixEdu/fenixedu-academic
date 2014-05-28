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
package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class GiafProfessionalData extends GiafProfessionalData_Base {

    public GiafProfessionalData() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public GiafProfessionalData(final PersonProfessionalData personProfessionalData, final String personGiafIdentification,
            final LocalDate institutionEntryDate, final ContractSituation contractSituation,
            final String contractSituationGiafId, final LocalDate contractSituationDate,
            final LocalDate terminationSituationDate, final ProfessionalRelation professionalRelation,
            final String professionalRelationGiafId, final LocalDate professionalRelationDate,
            final ProfessionalContractType professionalContractType, final String professionalContractTypeGiafId,
            final ProfessionalCategory professionalCategory, final String professionalCategoryGiafId,
            final LocalDate professionalCategoryDate, final ProfessionalRegime professionalRegime,
            final String professionalRegimeGiafId, final LocalDate professionalRegimeDate, final Space campus,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
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
        return isADIST() ? "ADIST" : (isISTID() ? "IST-ID" : Unit.getInstitutionAcronym());
    }

    public boolean isActive() {
        return !(getContractSituation().getEndSituation() || !getContractSituation().getInExercise() || (getTerminationSituationDate() != null
                && getTerminationSituationDate().isBefore(new LocalDate()) && getTerminationSituationDate().isAfter(
                getContractSituationDate())));
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption> getPersonProfessionalExemptions() {
        return getPersonProfessionalExemptionsSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalExemptions() {
        return !getPersonProfessionalExemptionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRelation> getPersonProfessionalRelations() {
        return getPersonProfessionalRelationsSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalRelations() {
        return !getPersonProfessionalRelationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation> getPersonContractSituations() {
        return getPersonContractSituationsSet();
    }

    @Deprecated
    public boolean hasAnyPersonContractSituations() {
        return !getPersonContractSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalContract> getPersonProfessionalContracts() {
        return getPersonProfessionalContractsSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalContracts() {
        return !getPersonProfessionalContractsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRegime> getPersonProfessionalRegimes() {
        return getPersonProfessionalRegimesSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalRegimes() {
        return !getPersonProfessionalRegimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalCategory> getPersonProfessionalCategories() {
        return getPersonProfessionalCategoriesSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalCategories() {
        return !getPersonProfessionalCategoriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonFunctionsAccumulation> getPersonFunctionsAccumulations() {
        return getPersonFunctionsAccumulationsSet();
    }

    @Deprecated
    public boolean hasAnyPersonFunctionsAccumulations() {
        return !getPersonFunctionsAccumulationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasContractSituation() {
        return getContractSituation() != null;
    }

    @Deprecated
    public boolean hasInstitutionEntryDate() {
        return getInstitutionEntryDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalRegimeGiafId() {
        return getProfessionalRegimeGiafId() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasProfessionalContractTypeGiafId() {
        return getProfessionalContractTypeGiafId() != null;
    }

    @Deprecated
    public boolean hasAnulationDate() {
        return getAnulationDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategoryGiafId() {
        return getProfessionalCategoryGiafId() != null;
    }

    @Deprecated
    public boolean hasProfessionalRelationGiafId() {
        return getProfessionalRelationGiafId() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategory() {
        return getProfessionalCategory() != null;
    }

    @Deprecated
    public boolean hasContractSituationGiafId() {
        return getContractSituationGiafId() != null;
    }

    @Deprecated
    public boolean hasContractSituationDate() {
        return getContractSituationDate() != null;
    }

    @Deprecated
    public boolean hasCampus() {
        return getCampus() != null;
    }

    @Deprecated
    public boolean hasGiafPersonIdentification() {
        return getGiafPersonIdentification() != null;
    }

    @Deprecated
    public boolean hasTerminationSituationDate() {
        return getTerminationSituationDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalRegimeDate() {
        return getProfessionalRegimeDate() != null;
    }

    @Deprecated
    public boolean hasModifiedDate() {
        return getModifiedDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalRelation() {
        return getProfessionalRelation() != null;
    }

    @Deprecated
    public boolean hasProfessionalRegime() {
        return getProfessionalRegime() != null;
    }

    @Deprecated
    public boolean hasImportationDate() {
        return getImportationDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalContractType() {
        return getProfessionalContractType() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategoryDate() {
        return getProfessionalCategoryDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalRelationDate() {
        return getProfessionalRelationDate() != null;
    }

    @Deprecated
    public boolean hasPersonProfessionalData() {
        return getPersonProfessionalData() != null;
    }

}

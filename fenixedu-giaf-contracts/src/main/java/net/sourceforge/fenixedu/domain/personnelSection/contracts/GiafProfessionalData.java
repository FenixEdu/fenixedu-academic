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
package org.fenixedu.academic.domain.personnelSection.contracts;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.organizationalStructure.Unit;

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
        for (PersonContractSituation personContractSituation : getPersonContractSituationsSet()) {
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
        for (PersonProfessionalExemption personProfessionalExemption : getPersonProfessionalExemptionsSet()) {
            if (personProfessionalExemption.isValid() && personProfessionalExemption.getAnulationDate() == null) {
                personProfessionalExemptions.add(personProfessionalExemption);
            }
        }
        return personProfessionalExemptions;
    }

    public Set<PersonProfessionalRegime> getValidPersonProfessionalRegimes() {
        Set<PersonProfessionalRegime> personProfessionalRegimes = new HashSet<PersonProfessionalRegime>();
        for (PersonProfessionalRegime personProfessionalRegime : getPersonProfessionalRegimesSet()) {
            if (personProfessionalRegime.isValid() && personProfessionalRegime.getAnulationDate() == null) {
                personProfessionalRegimes.add(personProfessionalRegime);
            }
        }
        return personProfessionalRegimes;
    }

    public Set<PersonProfessionalCategory> getValidPersonProfessionalCategories() {
        Set<PersonProfessionalCategory> personProfessionalCategories = new HashSet<PersonProfessionalCategory>();
        for (PersonProfessionalCategory personProfessionalCategory : getPersonProfessionalCategoriesSet()) {
            if (personProfessionalCategory.isValid() && personProfessionalCategory.getAnulationDate() == null) {
                personProfessionalCategories.add(personProfessionalCategory);
            }
        }
        return personProfessionalCategories;
    }

    public Set<PersonProfessionalRelation> getValidPersonProfessionalRelations() {
        Set<PersonProfessionalRelation> personProfessionalRelations = new HashSet<PersonProfessionalRelation>();
        for (PersonProfessionalRelation personProfessionalRelation : getPersonProfessionalRelationsSet()) {
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

}

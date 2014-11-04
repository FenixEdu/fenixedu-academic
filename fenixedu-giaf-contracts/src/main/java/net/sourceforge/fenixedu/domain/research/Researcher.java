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
package org.fenixedu.academic.domain.research;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation;
import org.fenixedu.academic.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.domain.Bennu;

public class Researcher extends Researcher_Base {
    public Researcher(Person person) {
        super();
        setPerson(person);
        setAllowsToBeSearched(Boolean.FALSE);
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setPerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private String normalizeKeywords(String keywordList) {
        String[] keys = keywordList.split(",");

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String[] dtd = key.split(" ");

            for (String eee : dtd) {
                if (eee.trim().length() > 0) {
                    sb.append(eee.trim()).append(" ");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(",");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public boolean isActiveContractedResearcher() {
        PersonContractSituation currentResearcherContractSituation = getCurrentContractedResearcherContractSituation();
        return currentResearcherContractSituation != null;
    }

    public PersonContractSituation getCurrentContractedResearcherContractSituation() {
        return getPerson().getPersonProfessionalData() != null ? getPerson().getPersonProfessionalData()
                .getCurrentPersonContractSituationByCategoryType(CategoryType.RESEARCHER) : null;
    }

}

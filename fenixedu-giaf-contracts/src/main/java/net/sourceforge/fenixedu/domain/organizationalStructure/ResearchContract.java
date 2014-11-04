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
package org.fenixedu.academic.domain.organizationalStructure;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public abstract class ResearchContract extends ResearchContract_Base {

    public void initResearchContract(Person person, YearMonthDay beginDate, YearMonthDay endDate, ResearchUnit unit,
            Boolean isExternalContract) {

        for (Accountability accountability : person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT)) {
            ResearchContract contract = (ResearchContract) accountability;
            if (contract.getUnit().equals(unit) && beginDate.equals(contract.getBeginDate())) {
                throw new DomainException("error.contract.already.exists");
            }
        }

        super.init(person, beginDate, endDate, unit);
        setExternalContract(isExternalContract);
        setAccountabilityType(AccountabilityType.readByType(AccountabilityTypeEnum.RESEARCH_CONTRACT));
    }

    public static ResearchContract createResearchContract(ResearchContractType contractType, Person person,
            YearMonthDay beginDate, YearMonthDay endDate, ResearchUnit unit, Boolean isExternalContract) {

        switch (contractType) {
        case RESEARCHER_CONTRACT:
            return new ResearcherContract(person, beginDate, endDate, unit, isExternalContract);
        case TECHNICAL_STAFF_CONTRACT:
            return new ResearchTechnicalStaffContract(person, beginDate, endDate, unit, isExternalContract);
        case SCHOLARSHIP_CONTRACT:
            return new ResearchScholarshipContract(person, beginDate, endDate, unit, isExternalContract);
        case INTERNSHIP_CONTRACT:
            return new ResearchInternshipContract(person, beginDate, endDate, unit, isExternalContract);
        default:
            return null;
        }

    }

    public enum ResearchContractType {
        RESEARCHER_CONTRACT, TECHNICAL_STAFF_CONTRACT, SCHOLARSHIP_CONTRACT, INTERNSHIP_CONTRACT;

        public String getName() {
            return name();
        }
    }

}

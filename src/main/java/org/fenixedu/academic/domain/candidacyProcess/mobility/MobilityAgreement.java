/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.candidacyProcess.mobility;

import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.bennu.core.domain.Bennu;

public class MobilityAgreement extends MobilityAgreement_Base {

    private MobilityAgreement() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MobilityAgreement(MobilityProgram program, UniversityUnit university) {
        this();
        if (university == null) {
            throw new NullPointerException("error.university.cannot.be.null");
        }
        setMobilityProgram(program);
        setUniversityUnit(university);
    }

    public void delete() {
        setMobilityProgram(null);
        setUniversityUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static MobilityAgreement getOrCreateAgreement(MobilityProgram mobilityProgram, UniversityUnit universityUnit) {
        MobilityAgreement mobilityAgreementByUniversityUnit =
                mobilityProgram.getMobilityAgreementByUniversityUnit(universityUnit);

        if (mobilityAgreementByUniversityUnit == null) {
            return new MobilityAgreement(mobilityProgram, universityUnit);
        }

        return mobilityAgreementByUniversityUnit;
    }

}

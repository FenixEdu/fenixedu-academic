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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import pt.ist.fenixframework.Atomic;

public class DegreeOfficePublicCandidacyHashCodeOperations {

    @Atomic
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
            Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
            String email) throws HashCodeForEmailAndProcessAlreadyBounded {

        return DegreeOfficePublicCandidacyHashCode.getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
                individualCandidadyProcessClass, parentProcess, email);
    }

    @Atomic
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCode(
            Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
            String email) throws HashCodeForEmailAndProcessAlreadyBounded {

        return DegreeOfficePublicCandidacyHashCode.getUnusedOrCreateNewHashCode(individualCandidadyProcessClass, parentProcess,
                email);
    }

}

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
package org.fenixedu.academic.domain.accounting.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.accounting.EventType;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType}
 */
@Deprecated
public enum PenaltyExemptionJustificationType {
    SOCIAL_SHARE_GRANT_OWNER,

    ENROLMENT_AFTER_EQUIVALENCE,

    DIRECTIVE_COUNCIL_AUTHORIZATION,

    NUCLEUS_COORDINATOR_AUTHORIZATION,

    SEPARATION_CYCLES_AUTHORIZATION;

    private PenaltyExemptionJustificationType() {

    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return PenaltyExemptionJustificationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return PenaltyExemptionJustificationType.class.getName() + "." + name();
    }

    public static List<PenaltyExemptionJustificationType> getValuesFor(EventType eventType) {
        switch (eventType) {
        case ADMINISTRATIVE_OFFICE_FEE_INSURANCE:
            return Arrays.asList(ENROLMENT_AFTER_EQUIVALENCE, DIRECTIVE_COUNCIL_AUTHORIZATION, NUCLEUS_COORDINATOR_AUTHORIZATION);
        case GRATUITY:
            return Arrays.asList(SOCIAL_SHARE_GRANT_OWNER, ENROLMENT_AFTER_EQUIVALENCE, DIRECTIVE_COUNCIL_AUTHORIZATION,
                    NUCLEUS_COORDINATOR_AUTHORIZATION, SEPARATION_CYCLES_AUTHORIZATION);
        case IMPROVEMENT_OF_APPROVED_ENROLMENT:
            return Collections.singletonList(NUCLEUS_COORDINATOR_AUTHORIZATION);
        case PHD_REGISTRATION_FEE:
            return Arrays.asList(DIRECTIVE_COUNCIL_AUTHORIZATION, NUCLEUS_COORDINATOR_AUTHORIZATION);
        default:
            return Collections.emptyList();
        }

    }
}

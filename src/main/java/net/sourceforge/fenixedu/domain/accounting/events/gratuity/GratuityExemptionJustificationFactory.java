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
package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import org.joda.time.YearMonthDay;

public class GratuityExemptionJustificationFactory {

    public static GratuityExemptionJustification create(final GratuityExemption gratuityExemption,
            final GratuityExemptionJustificationType justificationType, final String reason, final YearMonthDay dispatchDate) {

        switch (justificationType) {
        case INSTITUTION:
        case INSTITUTION_GRANT_OWNER:
        case OTHER_INSTITUTION:
        case PALOP_TEACHER:
        case SON_OF_DECORATED_MILITARY:
        case SOCIAL_SHARE_GRANT_OWNER:
        case STUDENT_TEACH:
        case SEPARATION_CYCLES_AUTHORIZATION:
            return new GratuityExemptionJustification(gratuityExemption, justificationType, reason);
        case DIRECTIVE_COUNCIL_AUTHORIZATION:
            return new GratuityExemptionJustificationByDispatch(gratuityExemption, justificationType, reason, dispatchDate);
        default:
            throw new RuntimeException("Unknown justification type");

        }

    }
}

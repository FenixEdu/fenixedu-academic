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
package org.fenixedu.academic.domain.accounting.events.serviceRequests;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemptionJustification}
 */
@Deprecated
public class AcademicEventExemptionJustification extends AcademicEventExemptionJustification_Base {

    private AcademicEventExemptionJustification() {
        super();
    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), Bundle.ENUMERATION);
    }

}

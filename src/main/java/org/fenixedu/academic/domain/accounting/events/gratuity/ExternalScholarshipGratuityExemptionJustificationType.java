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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.util.Locale;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum ExternalScholarshipGratuityExemptionJustificationType {
    // Third party entity offers to pay the gratuity
    THIRD_PARTY_CONTRIBUTION;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ExternalScholarshipGratuityExemptionJustificationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return ExternalScholarshipGratuityExemptionJustificationType.class.getName() + "." + name();
    }

    public String localizedName(Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

    public String getLocalizedName() {
        return localizedName(I18N.getLocale());
    }

    public ExternalScholarshipGratuityExemptionJustification justification(final ExternalScholarshipGratuityExemption exemption,
            final String reason) {
        return new ExternalScholarshipGratuityExemptionJustification(exemption, this, reason);
    }

}

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
package org.fenixedu.academic.domain.accounting.report.events;

import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.PercentageGratuityExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.ValueGratuityExemption;
import org.fenixedu.academic.domain.phd.debts.PhdEventExemption;

public class ExemptionWrapper {
    Exemption exemption;

    public ExemptionWrapper(final Exemption exemption) {
        this.exemption = exemption;
    }

    public String getExemptionTypeDescription() {
        return exemption.getExemptionJustification().getDescription().toString();
    }

    public String getExemptionValue() {
        if (exemption.isAcademicEventExemption()) {
            return "-";
        } else if (exemption.isGratuityExemption()) {
            GratuityExemption gratuityExemption = (GratuityExemption) exemption;

            if (gratuityExemption.isPercentageExemption()) {
                return "-";
            } else if (gratuityExemption.isValueExemption()) {
                return ((ValueGratuityExemption) gratuityExemption).getValue().toPlainString();
            }
        } else if (exemption.isAdministrativeOfficeFeeAndInsuranceExemption()) {
            return "-";
        } else if (exemption.isInsuranceExemption()) {
            return "-";
        } else if (exemption.isForAdministrativeOfficeFee()) {
            return "-";
        } else if (exemption.isPenaltyExemption()) {
            return "-";
        } else if (exemption.isPhdEventExemption()) {
            return ((PhdEventExemption) exemption).getValue().toPlainString();
        } else if (exemption.isSecondCycleIndividualCandidacyExemption()) {
            return "-";
        }

        throw new RuntimeException("unknown exemption");
    }

    public String getPercentage() {
        if (exemption.isAcademicEventExemption()) {
            return "-";
        } else if (exemption.isGratuityExemption()) {
            GratuityExemption gratuityExemption = (GratuityExemption) exemption;

            if (gratuityExemption.isPercentageExemption()) {
                return ((PercentageGratuityExemption) gratuityExemption).getPercentage().toString();
            } else if (gratuityExemption.isValueExemption()) {
                return "-";

            }
        } else if (exemption.isAdministrativeOfficeFeeAndInsuranceExemption()) {
            return "-";
        } else if (exemption.isInsuranceExemption()) {
            return "-";
        } else if (exemption.isForAdministrativeOfficeFee()) {
            return "-";
        } else if (exemption.isPenaltyExemption()) {
            return "-";
        } else if (exemption.isPhdEventExemption()) {
            return "-";
        } else if (exemption.isSecondCycleIndividualCandidacyExemption()) {
            return "-";
        }

        throw new RuntimeException("unknown exemption");
    }

    public String getJustification() {
        return exemption.getExemptionJustification().getReason();
    }

}

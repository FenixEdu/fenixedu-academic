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
package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.util.Properties;

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemption;
import net.sourceforge.fenixedu.util.Bundle;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class ExemptionWrapper {
    Exemption exemption;

    public ExemptionWrapper(final Exemption exemption) {
        this.exemption = exemption;
    }

    public String getExemptionTypeDescription() {
        Properties formatterProperties = new Properties();

        formatterProperties.put(LabelFormatter.ENUMERATION_RESOURCES, Bundle.ENUMERATION);
        formatterProperties.put(LabelFormatter.APPLICATION_RESOURCES, Bundle.APPLICATION);

        return exemption.getExemptionJustification().getDescription()
                .toString(new DefaultResourceBundleProvider(formatterProperties));
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

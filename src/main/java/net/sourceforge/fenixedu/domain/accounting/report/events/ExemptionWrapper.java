package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.util.Properties;

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemption;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class ExemptionWrapper {
    Exemption exemption;

    public ExemptionWrapper(final Exemption exemption) {
        this.exemption = exemption;
    }

    public String getExemptionTypeDescription() {
        Properties formatterProperties = new Properties();

        formatterProperties.put(LabelFormatter.ENUMERATION_RESOURCES, "resources.EnumerationResources");
        formatterProperties.put(LabelFormatter.APPLICATION_RESOURCES, "resources.ApplicationResources");

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

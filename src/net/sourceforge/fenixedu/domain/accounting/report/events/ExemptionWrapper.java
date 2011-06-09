package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemption;

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

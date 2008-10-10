package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR;

public class CreateStandaloneEnrolmentGratuityPRBean extends CreateGratuityPostingRuleBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal ectsForYear;

    public CreateStandaloneEnrolmentGratuityPRBean() {
	super();
	super.setRule(StandaloneEnrolmentGratuityPR.class);
    }

    public BigDecimal getEctsForYear() {
	return ectsForYear;
    }

    public void setEctsForYear(BigDecimal ectsForYear) {
	this.ectsForYear = ectsForYear;
    }

}

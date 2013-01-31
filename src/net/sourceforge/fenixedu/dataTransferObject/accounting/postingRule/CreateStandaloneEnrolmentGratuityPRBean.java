package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR;

public class CreateStandaloneEnrolmentGratuityPRBean extends CreateGratuityPostingRuleBean {

	static private final long serialVersionUID = 1L;

	private BigDecimal ectsForYear;

	private BigDecimal gratuityFactor;

	private BigDecimal ectsFactor;

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

	public BigDecimal getGratuityFactor() {
		return gratuityFactor;
	}

	public void setGratuityFactor(BigDecimal gratuityFactor) {
		this.gratuityFactor = gratuityFactor;
	}

	public BigDecimal getEctsFactor() {
		return ectsFactor;
	}

	public void setEctsFactor(BigDecimal ectsFactor) {
		this.ectsFactor = ectsFactor;
	}

}

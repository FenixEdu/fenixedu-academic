package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRules.serviceRequests;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;

public class CertificateRequestPRDTO implements Serializable {

    private BigDecimal baseAmount;

    private BigDecimal amountPerUnit;

    private BigDecimal amountPerPage;

    private DomainReference<CertificateRequestPR> postingRule;

    public CertificateRequestPRDTO() {

    }

    public CertificateRequestPRDTO(BigDecimal baseAmount, BigDecimal amountPerUnit,
            BigDecimal amountPerPage) {
        super();

        setBaseAmount(baseAmount);
        setAmountPerUnit(amountPerUnit);
        setAmountPerPage(amountPerPage);

    }

    public CertificateRequestPRDTO(CertificateRequestPR postingRule) {
        setPostingRule(postingRule);
        setBaseAmount(postingRule.getBaseAmount());
        setAmountPerUnit(postingRule.getAmountPerUnit());
        setAmountPerPage(postingRule.getAmountPerPage());
    }

    public BigDecimal getAmountPerPage() {
        return amountPerPage;
    }

    public void setAmountPerPage(BigDecimal amountPerPage) {
        this.amountPerPage = amountPerPage;
    }

    public BigDecimal getAmountPerUnit() {
        return amountPerUnit;
    }

    public void setAmountPerUnit(BigDecimal amountPerUnit) {
        this.amountPerUnit = amountPerUnit;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public CertificateRequestPR getPostingRule() {
        return (this.postingRule != null) ? this.postingRule.getObject() : null;
    }

    public void setPostingRule(CertificateRequestPR postingRule) {
        this.postingRule = (postingRule != null) ? new DomainReference<CertificateRequestPR>(postingRule)
                : null;
    }

}

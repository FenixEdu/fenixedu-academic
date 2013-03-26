package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;

import org.joda.time.LocalDate;

public class CandidacyProcessPaymentCodeBean implements Serializable {

    private PaymentCodeType type;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Integer numberOfPaymentCodes;

    public CandidacyProcessPaymentCodeBean() {

    }

    public CandidacyProcessPaymentCodeBean(PaymentCodeType type) {
        this.type = type;
    }

    public PaymentCodeType getType() {
        return type;
    }

    public void setType(PaymentCodeType type) {
        this.type = type;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfPaymentCodes() {
        return numberOfPaymentCodes;
    }

    public void setNumberOfPaymentCodes(Integer numberOfPaymentCodes) {
        this.numberOfPaymentCodes = numberOfPaymentCodes;
    }

}

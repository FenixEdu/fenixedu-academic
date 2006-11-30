package net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionType;
import net.sourceforge.fenixedu.util.Money;

public class CreateGratuityExemptionBean implements Serializable {

    private DomainReference<GratuityEvent> gratuityEvent;

    private GratuityExemptionType exemptionType;

    private BigDecimal otherPercentage;

    private Money amount;

    private BigDecimal percentage;

    public CreateGratuityExemptionBean(GratuityEvent gratuityEvent) {
	super();
	setGratuityEvent(gratuityEvent);
    }

    public Money getAmount() {
	return amount;
    }

    public void setAmount(Money amount) {
	this.amount = amount;
    }

    public GratuityExemptionType getExemptionType() {
	return exemptionType;
    }

    public void setExemptionType(GratuityExemptionType exemptionType) {
	this.exemptionType = exemptionType;
    }

    public GratuityEvent getGratuityEvent() {
	return (gratuityEvent != null) ? this.gratuityEvent.getObject() : null;
    }

    public void setGratuityEvent(GratuityEvent gratuityEvent) {
	this.gratuityEvent = (gratuityEvent != null) ? new DomainReference<GratuityEvent>(gratuityEvent)
		: null;
    }

    public BigDecimal getPercentage() {
	return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
	this.percentage = percentage;
    }

    public BigDecimal getOtherPercentage() {
	return otherPercentage;
    }

    public void setOtherPercentage(BigDecimal selectedPercentage) {
	this.otherPercentage = selectedPercentage;
    }

    public boolean isPercentageExemption() {
	return getOtherPercentage() != null || getPercentage() != null;
    }

    public BigDecimal getSelectedPercentage() {
	return getOtherPercentage() != null ? getOtherPercentage() : getPercentage();
    }

}

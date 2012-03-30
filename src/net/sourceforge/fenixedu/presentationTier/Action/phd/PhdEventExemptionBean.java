package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemptionJustificationType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.LocalDate;

public class PhdEventExemptionBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdEvent event;

    private PhdEventExemptionJustificationType justificationType;

    private Money value;

    private LocalDate dispatchDate;

    private String reason;
    
    private List<Party> providers;

    private Party provider;
    
    public PhdEventExemptionBean(final PhdEvent event) {
	setEvent(event);
    }

    public PhdEvent getEvent() {
	return event;
    }

    public void setEvent(PhdEvent event) {
	this.event = event;
    }

    public PhdEventExemptionJustificationType getJustificationType() {
	return justificationType;
    }

    public void setJustificationType(PhdEventExemptionJustificationType justificationType) {
	this.justificationType = justificationType;
    }

    public Money getValue() {
	return value;
    }

    public void setValue(Money value) {
	this.value = value;
    }

    public LocalDate getDispatchDate() {
	return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
	this.dispatchDate = dispatchDate;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public List<Party> getProviders() {
	return providers;
    }

    public void setProviders(List<Party> providers) {
	this.providers = providers;
    }

    public Party getProvider() {
	return provider;
    }

    public void setProvider(Party provider) {
	this.provider = provider;
    }

}

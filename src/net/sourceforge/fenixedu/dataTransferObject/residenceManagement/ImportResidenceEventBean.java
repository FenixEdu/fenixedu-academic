package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.SimpleFileBean;

public class ImportResidenceEventBean extends SimpleFileBean {

    private DomainReference<ResidenceYear> residenceYear;
    private DomainReference<ResidenceMonth> residenceMonth;
    private Integer paymentLimitDay;

    public ResidenceYear getResidenceYear() {
	return this.residenceYear != null ? this.residenceYear.getObject() : null;
    }

    public void setResidenceYear(ResidenceYear residenceYear) {
	this.residenceYear = (residenceYear != null) ? new DomainReference<ResidenceYear>(residenceYear) : null;
    }

    public ResidenceMonth getResidenceMonth() {
	return this.residenceMonth != null ? this.residenceMonth.getObject() : null;
    }

    public void setResindenceMonth(ResidenceMonth residenceMonth) {
	this.residenceMonth = (residenceMonth != null) ? new DomainReference<ResidenceMonth>(residenceMonth) : null;
    }

    public Integer getPaymentLimitDay() {
	return paymentLimitDay;
    }

    public void setPaymentLimitDay(Integer paymentLimitDay) {
	this.paymentLimitDay = paymentLimitDay;
    }
}

package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.SimpleFileBean;

public class ImportResidenceEventBean extends SimpleFileBean {

    private DomainReference<ResidenceYear> residenceYear;
    private DomainReference<ResidenceMonth> residenceMonth;
    private Integer paymentLimitDay;

    private String spreadsheetName;
    
    public ImportResidenceEventBean(ResidenceMonth month) {
	setResidenceMonth(month);
	setResidenceYear(month.getYear());
    }
    
    public ImportResidenceEventBean() {
	setResidenceYear(null);
	setResidenceMonth(null);
    }

    public ResidenceYear getResidenceYear() {
	return this.residenceYear.getObject();
    }

    public void setResidenceYear(ResidenceYear residenceYear) {
	this.residenceYear = new DomainReference<ResidenceYear>(residenceYear);
	if (residenceYear != null) {
	    setPaymentLimitDay(residenceYear.getPaymentLimitDay());
	}
    }

    public ResidenceMonth getResidenceMonth() {
	return this.residenceMonth.getObject();
    }

    public void setResidenceMonth(ResidenceMonth residenceMonth) {
	this.residenceMonth = new DomainReference<ResidenceMonth>(residenceMonth);
    }

    public Integer getPaymentLimitDay() {
	return paymentLimitDay;
    }

    public void setPaymentLimitDay(Integer paymentLimitDay) {
	this.paymentLimitDay = paymentLimitDay;
    }

    public String getSpreadsheetName() {
        return spreadsheetName;
    }

    public void setSpreadsheetName(String spreadsheetName) {
        this.spreadsheetName = spreadsheetName;
    }
}

package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.SimpleFileBean;

public class ImportResidenceEventBean extends SimpleFileBean {

    private ResidenceYear residenceYear;
    private ResidenceMonth residenceMonth;
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
        return this.residenceYear;
    }

    public void setResidenceYear(ResidenceYear residenceYear) {
        this.residenceYear = residenceYear;
        if (residenceYear != null) {
            setPaymentLimitDay(residenceYear.getUnit().getCurrentPaymentLimitDay());
        }
    }

    public ResidenceMonth getResidenceMonth() {
        return this.residenceMonth;
    }

    public void setResidenceMonth(ResidenceMonth residenceMonth) {
        this.residenceMonth = residenceMonth;
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

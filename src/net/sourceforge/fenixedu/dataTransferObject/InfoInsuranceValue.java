package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IInsuranceValue;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InfoInsuranceValue extends InfoObject {

    private Double annualValue;

    private InfoExecutionYear infoExecutionYear;

    private Date endDate;

    protected void copyFromDomain(IInsuranceValue insuranceValue) {
        super.copyFromDomain(insuranceValue);
        this.annualValue = insuranceValue.getAnnualValue();
        this.endDate = insuranceValue.getEndDate();
        this.infoExecutionYear = InfoExecutionYear.newInfoFromDomain(insuranceValue.getExecutionYear());

    }

    public static InfoInsuranceValue newInfoFromDomain(IInsuranceValue insuranceValue) {

        InfoInsuranceValue infoInsuranceValue = null;
        if (insuranceValue != null) {
            infoInsuranceValue = new InfoInsuranceValue();
            infoInsuranceValue.copyFromDomain(insuranceValue);
        }
        return infoInsuranceValue;
    }

    /**
     * @return Returns the annualValue.
     */
    public Double getAnnualValue() {
        return annualValue;
    }

    /**
     * @param annualValue
     *            The annualValue to set.
     */
    public void setAnnualValue(Double annualValue) {
        this.annualValue = annualValue;
    }

    /**
     * @return Returns the infoExecutionYear.
     */
    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    /**
     * @param infoExecutionYear
     *            The infoExecutionYear to set.
     */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
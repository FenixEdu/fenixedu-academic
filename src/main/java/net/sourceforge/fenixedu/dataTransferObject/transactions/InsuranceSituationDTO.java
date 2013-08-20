package net.sourceforge.fenixedu.dataTransferObject.transactions;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceSituationDTO extends InfoObject {

    private Double payedValue;

    private Double anualValue;

    private String insuranceTransactionID;

    private InfoExecutionYear infoExecutionYear;

    private String executionYearID;

    /**
     *  
     */
    public InsuranceSituationDTO() {

    }

    /**
     * @param externalId
     */
    public InsuranceSituationDTO(String externalId) {
        super(externalId);

    }

    public Double getAnualValue() {
        return anualValue;
    }

    public void setAnualValue(Double anualValue) {
        this.anualValue = anualValue;
    }

    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    public String getInsuranceTransactionID() {
        return insuranceTransactionID;
    }

    public void setInsuranceTransactionID(String insuranceTransactionID) {
        this.insuranceTransactionID = insuranceTransactionID;
    }

    public Double getPayedValue() {
        return payedValue;
    }

    public void setPayedValue(Double payedValue) {
        this.payedValue = payedValue;
    }

    public String getExecutionYearID() {
        return executionYearID;
    }

    public void setExecutionYearID(String executionYearID) {
        this.executionYearID = executionYearID;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InsuranceSituationDTO) {
            InsuranceSituationDTO insuranceSituationDTO = (InsuranceSituationDTO) obj;

            if ((getInfoExecutionYear() == null && insuranceSituationDTO.getInfoExecutionYear() == null)
                    || (getInfoExecutionYear().equals(insuranceSituationDTO.getInfoExecutionYear()))) {
                result = true;
            }
        }

        return result;
    }

}
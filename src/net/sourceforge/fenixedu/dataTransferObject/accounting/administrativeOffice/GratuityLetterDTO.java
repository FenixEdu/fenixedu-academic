package net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.Money;

public class GratuityLetterDTO implements Serializable {

    private Money administrativeOfficeFeeAmount;

    private Money administrativeOfficeFeePenalty;

    private Money insuranceAmount;

    private Money gratuityTotalAmout;

    private String entityCode;

    private ExecutionYear executionYear;

    private Person person;

    private DebtDTO administrativeOfficeAndInsuranceFeeDebt;

    private DebtDTO totalGratuityDebt;

    private List<DebtDTO> gratuityDebtInstallments;

    public GratuityLetterDTO(final Person person, final ExecutionYear executionYear,
	    final String entityCode) {
	super();
	setPerson(person);
	setExecutionYear(executionYear);
	setEntityCode(entityCode);
	setGratuityDebtInstallments(new ArrayList<DebtDTO>());
	
    }

    public Money getAdministrativeOfficeFeeAmount() {
	return administrativeOfficeFeeAmount;
    }

    public void setAdministrativeOfficeFeeAmount(Money administrativeOfficeFeeAmount) {
	this.administrativeOfficeFeeAmount = administrativeOfficeFeeAmount;
    }

    public String getEntityCode() {
	return entityCode;
    }

    public void setEntityCode(String entityCode) {
	this.entityCode = entityCode;
    }

    public ExecutionYear getExecutionYear() {
	return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public Money getGratuityTotalAmout() {
	return gratuityTotalAmout;
    }

    public void setGratuityTotalAmout(Money gratuityTotalAmout) {
	this.gratuityTotalAmout = gratuityTotalAmout;
    }

    public Money getInsuranceAmount() {
	return insuranceAmount;
    }

    public void setInsuranceAmount(Money insuranceAmount) {
	this.insuranceAmount = insuranceAmount;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public Money getAdministrativeOfficeFeePenalty() {
	return administrativeOfficeFeePenalty;
    }

    public void setAdministrativeOfficeFeePenalty(Money administrativeOfficeFeePenalty) {
	this.administrativeOfficeFeePenalty = administrativeOfficeFeePenalty;
    }

    public DebtDTO getAdministrativeOfficeAndInsuranceFeeDebt() {
	return administrativeOfficeAndInsuranceFeeDebt;
    }

    public void setAdministrativeOfficeAndInsuranceFeeDebt(
	    DebtDTO administrativeOfficeAndInsuranceFeeDebt) {
	this.administrativeOfficeAndInsuranceFeeDebt = administrativeOfficeAndInsuranceFeeDebt;
    }

    public boolean isAdministrativeOfficeAndInsuranceFeeDebtAvailable() {
	return getAdministrativeOfficeAndInsuranceFeeDebt() != null;
    }

    public List<DebtDTO> getGratuityDebtInstallments() {
	return gratuityDebtInstallments;
    }

    public void setGratuityDebtInstallments(List<DebtDTO> gratuityDebtInstallments) {
	this.gratuityDebtInstallments = gratuityDebtInstallments;
    }
    
    public void addGratuityDebtInstallments(DebtDTO installmentDebtDTO) {
	this.gratuityDebtInstallments.add(installmentDebtDTO);
    }

    public boolean isGratuityDebtInstallmentsAvailable() {
	return !this.gratuityDebtInstallments.isEmpty();
    }

    public DebtDTO getTotalGratuityDebt() {
	return totalGratuityDebt;
    }

    public void setTotalGratuityDebt(DebtDTO totalGratuityDebt) {
	this.totalGratuityDebt = totalGratuityDebt;
    }

    public boolean isTotalGratuityDebtAvailable() {
	return this.totalGratuityDebt != null;
    }

    public boolean isAnyGratuityDebtAvailable() {
	return isTotalGratuityDebtAvailable() || isGratuityDebtInstallmentsAvailable();
    }

}

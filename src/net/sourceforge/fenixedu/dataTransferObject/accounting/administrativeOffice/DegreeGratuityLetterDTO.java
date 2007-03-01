package net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.Money;

public class DegreeGratuityLetterDTO extends GratuityLetterDTO {

    private Money administrativeOfficeFeeAmount;

    private Money administrativeOfficeFeePenalty;

    private Money insuranceAmount;

    private Money gratuityTotalAmout;

    private DebtDTO administrativeOfficeAndInsuranceFeeDebt;

    private DebtDTO totalGratuityDebt;

    private List<DebtDTO> gratuityDebtInstallments;

    public DegreeGratuityLetterDTO(final Person person, final ExecutionYear executionYear,
	    final String entityCode) {
	super(person, executionYear, entityCode);
	setGratuityDebtInstallments(new ArrayList<DebtDTO>());
    }

    public Money getAdministrativeOfficeFeeAmount() {
	return administrativeOfficeFeeAmount;
    }

    public void setAdministrativeOfficeFeeAmount(Money administrativeOfficeFeeAmount) {
	this.administrativeOfficeFeeAmount = administrativeOfficeFeeAmount;
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

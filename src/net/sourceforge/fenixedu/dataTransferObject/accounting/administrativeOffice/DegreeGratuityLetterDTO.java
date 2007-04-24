package net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

public class DegreeGratuityLetterDTO extends GratuityLetterDTO {

    private DebtDTO administrativeOfficeAndInsuranceFeeDebt;

    private DebtDTO totalGratuityDebt;

    private List<InstallmentDebtDTO> gratuityDebtInstallments;

    public DegreeGratuityLetterDTO(final Person person, final ExecutionYear executionYear,
	    final String entityCode) {
	super(person, executionYear, entityCode);
	this.gratuityDebtInstallments = new ArrayList<InstallmentDebtDTO>();
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

    public List<InstallmentDebtDTO> getGratuityDebtInstallments() {
	return gratuityDebtInstallments;
    }

    public void addGratuityDebtInstallments(InstallmentDebtDTO installmentDebtDTO) {
	this.gratuityDebtInstallments.add(installmentDebtDTO);
    }

    public boolean isGratuityDebtInstallmentsAvailable() {
	return !this.gratuityDebtInstallments.isEmpty();
    }

    public InstallmentDebtDTO getGratuityDebtInstallmentByOrder(int order) {
	for (final InstallmentDebtDTO installmentDebtDTO : this.gratuityDebtInstallments) {
	    if (installmentDebtDTO.getInstallment().getOrder() == order) {
		return installmentDebtDTO;
	    }
	}

	return null;
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

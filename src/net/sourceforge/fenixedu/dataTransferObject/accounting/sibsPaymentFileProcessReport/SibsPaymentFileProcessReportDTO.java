package net.sourceforge.fenixedu.dataTransferObject.accounting.sibsPaymentFileProcessReport;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class SibsPaymentFileProcessReportDTO {

    private String filename;

    private YearMonthDay whenProcessedBySibs;

    private Integer fileVersion;

    private Money degreeGratuityTotalAmount;

    private Money bolonhaDegreeGratuityTotalAmount;

    private Money integratedMasterDegreeGratuityTotalAmount;

    private Money integratedBolonhaMasterDegreeGratuityTotalAmount;

    private Money administrativeOfficeTaxTotalAmount;

    private Money graduationInsuranceTotalAmount;

    private Money specializationGratuityTotalAmount;

    private Money masterDegreeGratuityTotalAmount;

    private Money bolonhaMasterDegreeGratuityTotalAmount;

    private Money dfaGratuityTotalAmount;

    private Money afterGraduationInsuranceTotalAmount;

    private Money phdGratuityTotalAmout;

    private Money transactionsTotalAmount;

    private Money totalCost;

    public SibsPaymentFileProcessReportDTO() {
	super();
	this.degreeGratuityTotalAmount = Money.ZERO;
	this.bolonhaDegreeGratuityTotalAmount = Money.ZERO;
	this.integratedMasterDegreeGratuityTotalAmount = Money.ZERO;
	this.integratedBolonhaMasterDegreeGratuityTotalAmount = Money.ZERO;
	this.administrativeOfficeTaxTotalAmount = Money.ZERO;
	this.graduationInsuranceTotalAmount = Money.ZERO;
	this.specializationGratuityTotalAmount = Money.ZERO;
	this.masterDegreeGratuityTotalAmount = Money.ZERO;
	this.bolonhaMasterDegreeGratuityTotalAmount = Money.ZERO;
	this.dfaGratuityTotalAmount = Money.ZERO;
	this.afterGraduationInsuranceTotalAmount = Money.ZERO;
	this.phdGratuityTotalAmout = Money.ZERO;
	this.transactionsTotalAmount = Money.ZERO;
	this.totalCost = Money.ZERO;
    }

    public void addAdministrativeOfficeTaxAmount(final Money amount) {
	this.administrativeOfficeTaxTotalAmount = this.administrativeOfficeTaxTotalAmount.add(amount);

    }

    public Money getAdministrativeOfficeTaxTotalAmount() {
	return administrativeOfficeTaxTotalAmount;
    }

    public void addBolonhaDegreeGratuityAmount(final Money amount) {
	this.bolonhaDegreeGratuityTotalAmount = this.bolonhaDegreeGratuityTotalAmount.add(amount);

    }

    public Money getBolonhaDegreeGratuityTotalAmount() {
	return bolonhaDegreeGratuityTotalAmount;
    }

    public void addDegreeGratuityAmount(final Money amount) {
	this.degreeGratuityTotalAmount = this.degreeGratuityTotalAmount.add(amount);

    }

    public Money getDegreeGratuityTotalAmount() {
	return degreeGratuityTotalAmount;
    }

    public Money getGraduationInsuranceTotalAmount() {
	return graduationInsuranceTotalAmount;
    }

    public void addGraduationInsuranceAmount(Money amount) {
	this.graduationInsuranceTotalAmount = this.graduationInsuranceTotalAmount.add(amount);
    }

    public void addDfaGratuityAmount(final Money amount) {
	this.dfaGratuityTotalAmount = this.dfaGratuityTotalAmount.add(amount);
    }

    public Money getDfaGratuityTotalAmount() {
	return dfaGratuityTotalAmount;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public Integer getFileVersion() {
	return fileVersion;
    }

    public void setFileVersion(Integer fileVersion) {
	this.fileVersion = fileVersion;
    }

    public void addAfterGraduationInsuranceAmount(final Money amount) {
	this.afterGraduationInsuranceTotalAmount = this.afterGraduationInsuranceTotalAmount.add(amount);
    }

    public Money getAfterGraduationInsuranceTotalAmount() {
	return afterGraduationInsuranceTotalAmount;
    }

    public void addIntegratedBolonhaMasterDegreeGratuityAmount(final Money amount) {
	this.integratedBolonhaMasterDegreeGratuityTotalAmount = this.integratedBolonhaMasterDegreeGratuityTotalAmount
		.add(amount);
    }

    public Money getIntegratedBolonhaMasterDegreeGratuityTotalAmount() {
	return integratedBolonhaMasterDegreeGratuityTotalAmount;
    }

    public void addIntegratedMasterDegreeGratuityAmount(final Money amount) {
	this.integratedMasterDegreeGratuityTotalAmount = this.integratedMasterDegreeGratuityTotalAmount
		.add(amount);
    }

    public Money getIntegratedMasterDegreeGratuityTotalAmount() {
	return integratedMasterDegreeGratuityTotalAmount;
    }

    public void addMasterDegreeGratuityAmount(final Money amount) {
	this.masterDegreeGratuityTotalAmount = this.masterDegreeGratuityTotalAmount.add(amount);
    }

    public Money getMasterDegreeGratuityTotalAmount() {
	return masterDegreeGratuityTotalAmount;
    }

    public void addBolonhaMasterDegreGratuityTotalAmount(final Money amount) {
	this.bolonhaMasterDegreeGratuityTotalAmount = this.bolonhaMasterDegreeGratuityTotalAmount
		.add(amount);
    }

    public Money getBolonhaMasterDegreeGratuityTotalAmount() {
	return bolonhaMasterDegreeGratuityTotalAmount;
    }

    public void addSpecializationGratuityAmount(final Money amount) {
	this.specializationGratuityTotalAmount = this.specializationGratuityTotalAmount.add(amount);
    }

    public Money getSpecializationGratuityTotalAmount() {
	return specializationGratuityTotalAmount;
    }

    public void addPhdGratuityAmount(final Money amount) {
	this.phdGratuityTotalAmout = this.phdGratuityTotalAmout.add(amount);
    }

    public Money getPhdGratuityTotalAmout() {
	return phdGratuityTotalAmout;
    }

    public Money getTotalCost() {
	return totalCost;
    }

    public void setTotalCost(Money totalCost) {
	this.totalCost = totalCost;
    }

    public Money getTransactionsTotalAmount() {
	return transactionsTotalAmount;
    }

    public void setTransactionsTotalAmount(Money transactionsTotalAmount) {
	this.transactionsTotalAmount = transactionsTotalAmount;
    }

    public YearMonthDay getWhenProcessedBySibs() {
	return whenProcessedBySibs;
    }

    public void setWhenProcessedBySibs(YearMonthDay whenProcessedBySibs) {
	this.whenProcessedBySibs = whenProcessedBySibs;
    }

}

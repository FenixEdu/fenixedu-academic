/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.accounting.sibsPaymentFileProcessReport;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeCandidacyForGraduatedPersonEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeChangeIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeTransferIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.Over23IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.StandaloneEnrolmentGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.GratuitySituationPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.MasterDegreeInsurancePaymentCode;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyEvent;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFile;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;

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

    private Money residenceAmount;

    private Money degreeChangeIndividualCandidacyAmount;

    private Money degreeTransferIndividualCandidacyAmount;

    private Money secondCycleIndividualCandidacyAmount;

    private Money degreeCandidacyForGraduatedPersonAmount;

    private Money totalCost;

    private Money standaloneEnrolmentGratuityEventAmount;

    private Money over23IndividualCandidacyEventAmount;

    private Money institutionAffiliationEventAmount;

    private Money phdProgramCandidacyEventAmount;

    private Money rectorateAmount;

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
        this.residenceAmount = Money.ZERO;
        this.degreeChangeIndividualCandidacyAmount = Money.ZERO;
        this.degreeTransferIndividualCandidacyAmount = Money.ZERO;
        this.secondCycleIndividualCandidacyAmount = Money.ZERO;
        this.degreeCandidacyForGraduatedPersonAmount = Money.ZERO;
        this.standaloneEnrolmentGratuityEventAmount = Money.ZERO;
        this.over23IndividualCandidacyEventAmount = Money.ZERO;
        this.institutionAffiliationEventAmount = Money.ZERO;
        this.phdProgramCandidacyEventAmount = Money.ZERO;
        this.rectorateAmount = Money.ZERO;
    }

    public SibsPaymentFileProcessReportDTO(final SibsIncommingPaymentFile sibsIncomingPaymentFile) {
        this();
        setWhenProcessedBySibs(sibsIncomingPaymentFile.getHeader().getWhenProcessedBySibs());
        setFilename(sibsIncomingPaymentFile.getFilename());
        setTransactionsTotalAmount(sibsIncomingPaymentFile.getFooter().getTransactionsTotalAmount());
        setTotalCost(sibsIncomingPaymentFile.getFooter().getTotalCost());
        setFileVersion(sibsIncomingPaymentFile.getHeader().getVersion());
    }

    private void addAdministrativeOfficeTaxAmount(final Money amount) {
        this.administrativeOfficeTaxTotalAmount = this.administrativeOfficeTaxTotalAmount.add(amount);
    }

    public Money getAdministrativeOfficeTaxTotalAmount() {
        return administrativeOfficeTaxTotalAmount;
    }

    private void addBolonhaDegreeGratuityAmount(final Money amount) {
        this.bolonhaDegreeGratuityTotalAmount = this.bolonhaDegreeGratuityTotalAmount.add(amount);
    }

    public Money getBolonhaDegreeGratuityTotalAmount() {
        return bolonhaDegreeGratuityTotalAmount;
    }

    private void addDegreeGratuityAmount(final Money amount) {
        this.degreeGratuityTotalAmount = this.degreeGratuityTotalAmount.add(amount);
    }

    public Money getDegreeGratuityTotalAmount() {
        return degreeGratuityTotalAmount;
    }

    public Money getGraduationInsuranceTotalAmount() {
        return graduationInsuranceTotalAmount;
    }

    private void addGraduationInsuranceAmount(Money amount) {
        this.graduationInsuranceTotalAmount = this.graduationInsuranceTotalAmount.add(amount);
    }

    private void addDfaGratuityAmount(final Money amount) {
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

    private void addAfterGraduationInsuranceAmount(final Money amount) {
        this.afterGraduationInsuranceTotalAmount = this.afterGraduationInsuranceTotalAmount.add(amount);
    }

    public Money getAfterGraduationInsuranceTotalAmount() {
        return afterGraduationInsuranceTotalAmount;
    }

    private void addIntegratedBolonhaMasterDegreeGratuityAmount(final Money amount) {
        this.integratedBolonhaMasterDegreeGratuityTotalAmount = this.integratedBolonhaMasterDegreeGratuityTotalAmount.add(amount);
    }

    public Money getIntegratedBolonhaMasterDegreeGratuityTotalAmount() {
        return integratedBolonhaMasterDegreeGratuityTotalAmount;
    }

    private void addIntegratedMasterDegreeGratuityAmount(final Money amount) {
        this.integratedMasterDegreeGratuityTotalAmount = this.integratedMasterDegreeGratuityTotalAmount.add(amount);
    }

    public Money getIntegratedMasterDegreeGratuityTotalAmount() {
        return integratedMasterDegreeGratuityTotalAmount;
    }

    private void addMasterDegreeGratuityAmount(final Money amount) {
        this.masterDegreeGratuityTotalAmount = this.masterDegreeGratuityTotalAmount.add(amount);
    }

    public Money getMasterDegreeGratuityTotalAmount() {
        return masterDegreeGratuityTotalAmount;
    }

    private void addBolonhaMasterDegreGratuityTotalAmount(final Money amount) {
        this.bolonhaMasterDegreeGratuityTotalAmount = this.bolonhaMasterDegreeGratuityTotalAmount.add(amount);
    }

    public Money getBolonhaMasterDegreeGratuityTotalAmount() {
        return bolonhaMasterDegreeGratuityTotalAmount;
    }

    private void addSpecializationGratuityAmount(final Money amount) {
        this.specializationGratuityTotalAmount = this.specializationGratuityTotalAmount.add(amount);
    }

    public Money getSpecializationGratuityTotalAmount() {
        return specializationGratuityTotalAmount;
    }

    private void addPhdGratuityAmount(final Money amount) {
        this.phdGratuityTotalAmout = this.phdGratuityTotalAmout.add(amount);
    }

    public Money getPhdGratuityTotalAmout() {
        return phdGratuityTotalAmout;
    }

    public Money getOver23IndividualCandidacyEventAmount() {
        return over23IndividualCandidacyEventAmount;
    }

    public void addOver23IndividualCandidacyEventAmount(final Money amount) {
        this.over23IndividualCandidacyEventAmount = this.over23IndividualCandidacyEventAmount.add(amount);
    }

    public Money getInstitutionAffiliationEventAmount() {
        return institutionAffiliationEventAmount;
    }

    public void addInstitutionAffiliationEventAmount(final Money amount) {
        this.institutionAffiliationEventAmount = this.institutionAffiliationEventAmount.add(amount);
    }

    public Money getPhdProgramCandidacyEventAmount() {
        return phdProgramCandidacyEventAmount;
    }

    public void addPhdProgramCandidacyEventAmount(final Money amount) {
        this.phdProgramCandidacyEventAmount = this.phdProgramCandidacyEventAmount.add(amount);
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

    public Money getResidenceAmount() {
        return residenceAmount;
    }

    public void addResidenceAmount(Money money) {
        this.residenceAmount = this.residenceAmount.add(money);
    }

    public void addAmount(final SibsIncommingPaymentFileDetailLine detailLine, final PaymentCode paymentCode) {
        if (paymentCode.isForRectorate()) {
            addAmountForRectorate(detailLine.getAmount());
        } else if (paymentCode instanceof AccountingEventPaymentCode) {
            addAmountForEvent(detailLine, paymentCode);
        } else if (paymentCode instanceof GratuitySituationPaymentCode) {
            addAmountForGratuitySituation(detailLine, (GratuitySituationPaymentCode) paymentCode);
        } else if (paymentCode instanceof MasterDegreeInsurancePaymentCode) {
            addAfterGraduationInsuranceAmount(detailLine.getAmount());
        } else {
            throw new UnsupportedOperationException("Unknown payment code type");
        }
    }

    private void addAmountForEvent(final SibsIncommingPaymentFileDetailLine detailLine, final PaymentCode paymentCode) {
        final Event event = ((AccountingEventPaymentCode) paymentCode).getAccountingEvent();
        if (event instanceof GratuityEventWithPaymentPlan) {
            addAmountForGratuityEvent(detailLine, (GratuityEventWithPaymentPlan) event);
        } else if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent) {
            addAmountForAdministrativeOfficeAndInsuranceEvent(detailLine, (AdministrativeOfficeFeeAndInsuranceEvent) event);
        } else if (event instanceof DfaGratuityEvent) {
            addDfaGratuityAmount(detailLine.getAmount());
        } else if (event instanceof InsuranceEvent) {
            addAfterGraduationInsuranceAmount(detailLine.getAmount());
        } else if (event instanceof ResidenceEvent) {
            addResidenceAmount(detailLine.getAmount());
        } else if (event instanceof SecondCycleIndividualCandidacyEvent) {
            addSecondCycleIndividualCandidacyAmount(detailLine.getAmount());
        } else if (event instanceof DegreeChangeIndividualCandidacyEvent) {
            addDegreeChangeIndividualCandidacyAmount(detailLine.getAmount());
        } else if (event instanceof DegreeCandidacyForGraduatedPersonEvent) {
            addDegreeCandidacyForGraduatedPersonAmount(detailLine.getAmount());
        } else if (event instanceof DegreeTransferIndividualCandidacyEvent) {
            addDegreeTransferIndividualCandidacyAmount(detailLine.getAmount());
        } else if (event instanceof StandaloneEnrolmentGratuityEvent) {
            addStandaloneEnrolmentGratuityEventAmount(detailLine.getAmount());
        } else if (event instanceof Over23IndividualCandidacyEvent) {
            addOver23IndividualCandidacyEventAmount(detailLine.getAmount());
        } else if (event instanceof PhdProgramCandidacyEvent) {
            addPhdProgramCandidacyEventAmount(detailLine.getAmount());
        } else {
            throw new IllegalArgumentException("Unknown accounting event " + event.getClass().getName());
        }
    }

    private void addAmountForGratuityEvent(final SibsIncommingPaymentFileDetailLine detailLine,
            final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan) {
        switch (gratuityEventWithPaymentPlan.getDegree().getDegreeType()) {
        case DEGREE:
            addDegreeGratuityAmount(detailLine.getAmount());
            break;
        case BOLONHA_DEGREE:
            addBolonhaDegreeGratuityAmount(detailLine.getAmount());
            break;
        case BOLONHA_INTEGRATED_MASTER_DEGREE:
            addIntegratedBolonhaMasterDegreeGratuityAmount(detailLine.getAmount());
            break;
        case BOLONHA_MASTER_DEGREE:
            addBolonhaMasterDegreGratuityTotalAmount(detailLine.getAmount());
            break;
        default:
            throw new IllegalArgumentException("unknown degree type for gratuity event");
        }
    }

    private void addAmountForAdministrativeOfficeAndInsuranceEvent(final SibsIncommingPaymentFileDetailLine detailLine,
            final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent) {
        if (detailLine.getAmount().greaterOrEqualThan(administrativeOfficeFeeAndInsuranceEvent.getAmountToPay())) {
            addGraduationInsuranceAmount(administrativeOfficeFeeAndInsuranceEvent.getInsuranceAmount());
            addAdministrativeOfficeTaxAmount(detailLine.getAmount().subtract(
                    administrativeOfficeFeeAndInsuranceEvent.getInsuranceAmount()));
        } else {
            addAdministrativeOfficeTaxAmount(detailLine.getAmount());
        }
    }

    private void addAmountForGratuitySituation(final SibsIncommingPaymentFileDetailLine detailLine,
            GratuitySituationPaymentCode paymentCode) {
        final GratuitySituation gratuitySituation = paymentCode.getGratuitySituation();
        switch (gratuitySituation.getStudentCurricularPlan().getSpecialization()) {
        case STUDENT_CURRICULAR_PLAN_MASTER_DEGREE:
            addMasterDegreeGratuityAmount(detailLine.getAmount());
            break;
        case STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE:
            addIntegratedMasterDegreeGratuityAmount(detailLine.getAmount());
            break;
        case STUDENT_CURRICULAR_PLAN_SPECIALIZATION:
            addSpecializationGratuityAmount(detailLine.getAmount());
            break;
        default:
            throw new RuntimeException("Unknown specialization "
                    + gratuitySituation.getStudentCurricularPlan().getSpecialization().name());
        }
    }

    public Money getDegreeChangeIndividualCandidacyAmount() {
        return degreeChangeIndividualCandidacyAmount;
    }

    public Money getDegreeTransferIndividualCandidacyAmount() {
        return degreeTransferIndividualCandidacyAmount;
    }

    public Money getSecondCycleIndividualCandidacyAmount() {
        return secondCycleIndividualCandidacyAmount;
    }

    public Money getDegreeCandidacyForGraduatedPersonAmount() {
        return degreeCandidacyForGraduatedPersonAmount;
    }

    public Money getRectorateAmount() {
        return this.rectorateAmount;
    }

    public void addDegreeChangeIndividualCandidacyAmount(Money money) {
        this.degreeChangeIndividualCandidacyAmount = this.degreeChangeIndividualCandidacyAmount.add(money);
    }

    public void addDegreeTransferIndividualCandidacyAmount(Money money) {
        this.degreeTransferIndividualCandidacyAmount = this.degreeTransferIndividualCandidacyAmount.add(money);
    }

    public void addSecondCycleIndividualCandidacyAmount(Money money) {
        this.secondCycleIndividualCandidacyAmount = this.secondCycleIndividualCandidacyAmount.add(money);
    }

    public void addDegreeCandidacyForGraduatedPersonAmount(Money money) {
        this.degreeCandidacyForGraduatedPersonAmount = this.degreeCandidacyForGraduatedPersonAmount.add(money);
    }

    public Money getStandaloneEnrolmentGratuityEventAmount() {
        return this.standaloneEnrolmentGratuityEventAmount;
    }

    public void addStandaloneEnrolmentGratuityEventAmount(Money amount) {
        this.standaloneEnrolmentGratuityEventAmount = this.standaloneEnrolmentGratuityEventAmount.add(amount);
    }

    public void addAmountForRectorate(Money amount) {
        this.rectorateAmount = this.rectorateAmount.add(amount);
    }
}

package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice.DebtDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice.GratuityLetterDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class BuildInformationForDegreeGratuityLetters extends
	BuildInformationForDegreeAdministrativeOfficeDebts {

    public BuildInformationForDegreeGratuityLetters() {
	super();
    }

    public List<GratuityLetterDTO> run(final ExecutionYear executionYear) throws FenixServiceException {
	final List<GratuityLetterDTO> result = new ArrayList<GratuityLetterDTO>();
	for (final Entry<Person, List<Event>> entry : getNotPayedEventsGroupedByPerson(executionYear)
		.entrySet()) {
	    result.add(buildDTO(entry.getKey(), entry.getValue(), executionYear));
	}

	return result;

    }

    protected GratuityLetterDTO buildDTO(final Person person, final List<Event> eventsForPerson,
	    final ExecutionYear executionYear) throws FenixServiceException {
	final GratuityLetterDTO gratuityLetterDTO = new GratuityLetterDTO(person, executionYear,
		ENTITY_CODE);
	final AdministrativeOffice administrativeOffice = AdministrativeOffice
		.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);

	fillInsuranceAndAdminOfficeFeePriceInformation(administrativeOffice, gratuityLetterDTO,
		executionYear);

	for (final Event event : eventsForPerson) {
	    if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent) {
		fillInsuranceAndAdminOfficeFeeDebtInformation(gratuityLetterDTO,
			(AdministrativeOfficeFeeAndInsuranceEvent) event);
	    } else if (event instanceof GratuityEventWithPaymentPlan) {
		fillGratuityDebtInformation(gratuityLetterDTO, (GratuityEventWithPaymentPlan) event);
	    }
	}

	return gratuityLetterDTO;
    }

    private void fillGratuityDebtInformation(GratuityLetterDTO gratuityLetterDTO,
	    GratuityEventWithPaymentPlan event) {
	Money totalAmount = Money.ZERO;
	for (final Installment installment : event.getGratuityPaymentPlan()
		.getInstallmentsSortedByEndDate()) {
	    totalAmount = totalAmount.add(installment.getAmount());
	    final InstallmentPaymentCode installmentPaymentCode = event
		    .getInstallmentPaymentCodeFor(installment);
	    if (installmentPaymentCode != null) {
		gratuityLetterDTO.addGratuityDebtInstallments(new DebtDTO(installment.getEndDate(),
			installmentPaymentCode.getFormattedCode(), installment.getAmount()));
	    }
	}

	final AccountingEventPaymentCode totalPaymentCode = event.getTotalPaymentCode();

	if (totalPaymentCode != null) {
	    gratuityLetterDTO
		    .setTotalGratuityDebt(new DebtDTO(event.getGratuityPaymentPlan()
			    .getLastInstallment().getEndDate(), totalPaymentCode.getFormattedCode(),
			    totalAmount));
	}

	gratuityLetterDTO.setGratuityTotalAmout(totalAmount);

    }

    private void fillInsuranceAndAdminOfficeFeeDebtInformation(GratuityLetterDTO gratuityLetterDTO,
	    AdministrativeOfficeFeeAndInsuranceEvent event) {
	final Money totalAmount = event.getInsuranceAmount().add(
		event.getAdministrativeOfficeFeeAmount());
	gratuityLetterDTO.setAdministrativeOfficeAndInsuranceFeeDebt(new DebtDTO(event
		.getAdministrativeOfficeFeePaymentLimitDate(), event.calculatePaymentCode()
		.getFormattedCode(), totalAmount));

    }

    private void fillInsuranceAndAdminOfficeFeePriceInformation(
	    AdministrativeOffice administrativeOffice, GratuityLetterDTO debtDTO,
	    final ExecutionYear executionYear) {
	final AdministrativeOfficeServiceAgreementTemplate serviceAgreementTemplate = administrativeOffice
		.getServiceAgreementTemplate();
	final AdministrativeOfficeFeeAndInsurancePR postingRule = (AdministrativeOfficeFeeAndInsurancePR) serviceAgreementTemplate
		.findPostingRuleByEventType(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE);
	final DateTime executionYearStartDate = executionYear.getBeginDateYearMonthDay()
		.toDateTimeAtMidnight();
	final DateTime executionYearEndDate = executionYear.getEndDateYearMonthDay()
		.toDateTimeAtMidnight();
	debtDTO.setInsuranceAmount(postingRule.getInsuranceAmount(executionYearStartDate,
		executionYearEndDate));
	debtDTO.setAdministrativeOfficeFeeAmount(postingRule.getAdministrativeOfficeFeeAmount(
		executionYearStartDate, executionYearEndDate));
	debtDTO.setAdministrativeOfficeFeePenalty(postingRule.getAdministrativeOfficeFeePenaltyAmount(
		executionYearStartDate, executionYearEndDate));
    }

}

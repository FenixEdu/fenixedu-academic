package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice.DebtDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice.DegreeGratuityLetterDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice.InstallmentDebtDTO;
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

    public List<DegreeGratuityLetterDTO> run(final ExecutionYear executionYear)
	    throws FenixServiceException {
	throw new RuntimeException(
		"Rewrite service to consider event.isLetterSent and to set whenSentLetter and support more than one gratuity events");

	// final List<DegreeGratuityLetterDTO> result = new
	// ArrayList<DegreeGratuityLetterDTO>();
	// for (final Entry<Person, List<Event>> entry :
	// getNotPayedEventsGroupedByPerson(executionYear)
	// .entrySet()) {
	// result.add(buildDTO(entry.getKey(), entry.getValue(),
	// executionYear));
	// }
	//
	// return result;

    }

    protected DegreeGratuityLetterDTO buildDTO(final Person person, final List<Event> eventsForPerson,
	    final ExecutionYear executionYear) throws FenixServiceException {
	final DegreeGratuityLetterDTO gratuityLetterDTO = new DegreeGratuityLetterDTO(person,
		executionYear, ENTITY_CODE);

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

    private void fillGratuityDebtInformation(DegreeGratuityLetterDTO gratuityLetterDTO,
	    GratuityEventWithPaymentPlan event) {
	Money totalAmount = Money.ZERO;
	for (final Installment installment : event.getGratuityPaymentPlan()
		.getInstallmentsSortedByEndDate()) {
	    totalAmount = totalAmount.add(installment.getAmount());
	    final InstallmentPaymentCode installmentPaymentCode = event
		    .getInstallmentPaymentCodeFor(installment);
	    if (installmentPaymentCode != null) {
		gratuityLetterDTO.addGratuityDebtInstallments(new InstallmentDebtDTO(installment
			.getEndDate(), installmentPaymentCode.getFormattedCode(), installment
			.getAmount(), installment));
	    }
	}

	final AccountingEventPaymentCode totalPaymentCode = event.getTotalPaymentCode();

	if (totalPaymentCode != null) {
	    gratuityLetterDTO
		    .setTotalGratuityDebt(new DebtDTO(event.getGratuityPaymentPlan()
			    .getLastInstallment().getEndDate(), totalPaymentCode.getFormattedCode(),
			    totalAmount));
	}

    }

    private void fillInsuranceAndAdminOfficeFeeDebtInformation(
	    DegreeGratuityLetterDTO gratuityLetterDTO, AdministrativeOfficeFeeAndInsuranceEvent event) {
	final Money totalAmount = event.getInsuranceAmount().add(
		event.getAdministrativeOfficeFeeAmount());
	gratuityLetterDTO.setAdministrativeOfficeAndInsuranceFeeDebt(new DebtDTO(event
		.getAdministrativeOfficeFeePaymentLimitDate(), event.calculatePaymentCode()
		.getFormattedCode(), totalAmount));

    }

}

package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.List;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.util.sibs.SibsOutgoingPaymentFile;

public class GenerateDegreeAdministrativeOfficeSibsOutgoingPaymentFile extends
	BuildInformationForDegreeAdministrativeOfficeDebts {

    public GenerateDegreeAdministrativeOfficeSibsOutgoingPaymentFile() {
	super();
    }

    public SibsOutgoingPaymentFile run(final ExecutionYear executionYear) {
	final SibsOutgoingPaymentFile sibsOutgoingPaymentFile = new SibsOutgoingPaymentFile(
		SOURCE_INSTITUTION_ID, DESTINATION_INSTITUTION_ID, ENTITY_CODE);

	for (final Entry<Person, List<Event>> entry : getNotPayedEventsGroupedByPerson(executionYear)
		.entrySet()) {
	    for (final Event event : entry.getValue()) {
		for (final AccountingEventPaymentCode paymentCode : event.calculatePaymentCodes()) {
		    sibsOutgoingPaymentFile.addLine(paymentCode.getCode(), paymentCode
			    .getMinAmount(), paymentCode.getMaxAmount(), paymentCode.getStartDate(),
			    paymentCode.getEndDate());
		}

	    }
	}

	return sibsOutgoingPaymentFile;
    }

}

package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PastAdministrativeOfficeFeeAndInsuranceEvent extends PastAdministrativeOfficeFeeAndInsuranceEvent_Base {

    protected PastAdministrativeOfficeFeeAndInsuranceEvent() {
	super();
    }

    public PastAdministrativeOfficeFeeAndInsuranceEvent(AdministrativeOffice administrativeOffice, Person person,
	    ExecutionYear executionYear, final Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
	this();
	init(administrativeOffice, person, executionYear, pastAdministrativeOfficeFeeAndInsuranceAmount);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, ExecutionYear executionYear,
	    Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
	super.init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, person, executionYear);
	checkParameters(pastAdministrativeOfficeFeeAndInsuranceAmount);
	super.setPastAdministrativeOfficeFeeAndInsuranceAmount(pastAdministrativeOfficeFeeAndInsuranceAmount);

    }

    private void checkParameters(Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
	if (pastAdministrativeOfficeFeeAndInsuranceAmount == null || pastAdministrativeOfficeFeeAndInsuranceAmount.isZero()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent.pastAdministrativeOfficeFeeAndInsuranceAmount.cannot.be.null.and.must.be.greather.than.zero");
	}

    }

    @Override
    public void setPastAdministrativeOfficeFeeAndInsuranceAmount(Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent.cannot.modify.pastAdministrativeOfficeFeeAndInsuranceAmount");
    }

    @Override
    protected ServiceAgreementTemplate getServiceAgreementTemplate() {
	return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(getExecutionYear().getYear());

	return labelFormatter;
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getExternalAccount();
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	labelFormatter.appendLabel(" ").appendLabel(getExecutionYear().getYear());
	return labelFormatter;
    }

}

package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;
import pt.ist.fenixWebFramework.security.accessControl.Checked;

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

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Override
    public void setPastAdministrativeOfficeFeeAndInsuranceAmount(Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
        super.setPastAdministrativeOfficeFeeAndInsuranceAmount(pastAdministrativeOfficeFeeAndInsuranceAmount);
        // throw new DomainException(
        // "error.net.sourceforge.fenixedu.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent.cannot.modify.pastAdministrativeOfficeFeeAndInsuranceAmount"
        // );
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE);
    }

    @Override
    public boolean isInDebt() {
        return isOpen();
    }

    @Deprecated
    public boolean hasPastAdministrativeOfficeFeeAndInsuranceAmount() {
        return getPastAdministrativeOfficeFeeAndInsuranceAmount() != null;
    }

}

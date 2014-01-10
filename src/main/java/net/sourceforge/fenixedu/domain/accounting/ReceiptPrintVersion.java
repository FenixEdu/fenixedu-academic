package net.sourceforge.fenixedu.domain.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ReceiptPrintVersion extends ReceiptPrintVersion_Base {

    private ReceiptPrintVersion() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    ReceiptPrintVersion(Receipt receipt, Person person) {
        this();
        init(receipt, person);
    }

    private void init(Receipt receipt, Person person) {
        checkParameters(receipt, person);
        super.setWhenCreated(new DateTime());
        super.setReceipt(receipt);
        super.setPerson(person);
    }

    private void checkParameters(Receipt receipt, Person person) {
        if (receipt == null) {
            throw new DomainException("error.accounting.receiptVersion.receipt.cannot.be.null");
        }

        if (person == null) {
            throw new DomainException("error.accounting.receiptVersion.person.cannot.be.null");
        }

    }

    @Override
    public void setPerson(Person person) {
        throw new DomainException("error.accounting.receiptVersion.cannot.modify.person");
    }

    @Override
    public void setReceipt(Receipt receipt) {
        throw new DomainException("error.accounting.receiptVersion.cannot.modify.receipt");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.accounting.receiptVersion.cannot.modify.whenCreated");
    }

    void delete() {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setPerson(null);
        super.setReceipt(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasReceipt() {
        return getReceipt() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}

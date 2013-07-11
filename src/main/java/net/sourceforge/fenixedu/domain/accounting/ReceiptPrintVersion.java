package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class ReceiptPrintVersion extends ReceiptPrintVersion_Base {

    private ReceiptPrintVersion() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

    @Checked("RolePredicates.MANAGER_PREDICATE")
    void delete() {
        super.setPerson(null);
        super.setReceipt(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public boolean hasRootDomainObject() {
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

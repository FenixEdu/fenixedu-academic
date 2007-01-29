package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.joda.time.DateTime;

public class ReceiptPrintVersion extends ReceiptPrintVersion_Base {

    private ReceiptPrintVersion() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    ReceiptPrintVersion(Receipt receipt, Employee employee) {
        this();
        init(receipt, employee);
    }

    private void init(Receipt receipt, Employee employee) {
        checkParameters(receipt, employee);
        super.setWhenCreated(new DateTime());
        super.setReceipt(receipt);
        super.setEmployee(employee);

    }

    private void checkParameters(Receipt receipt, Employee employee) {
        if (receipt == null) {
            throw new DomainException("error.accounting.receiptVersion.receipt.cannot.be.null");
        }

        if (employee == null) {
            throw new DomainException("error.accounting.receiptVersion.employee.cannot.be.null");
        }

    }

    @Override
    public void setEmployee(Employee employee) {
        throw new DomainException("error.accounting.receiptVersion.cannot.modify.employee");
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
	super.setEmployee(null);
	super.setReceipt(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}

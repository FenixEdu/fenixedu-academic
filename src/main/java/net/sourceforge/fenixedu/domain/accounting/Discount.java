package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class Discount extends Discount_Base {

    private Discount() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWhenCreated(new DateTime());
    }

    Discount(final Person person, final Money amount) {
        this();
        checkAmount(amount);
        setAmount(amount);
        if (person != null) {
            setUsername(person.getIstUsername());
        }
    }

    private void checkAmount(Money amount) {
        if (amount == null || !amount.isPositive()) {
            throw new DomainException("error.Discount.invalid.amount");
        }
    }

    @Atomic
    public void delete() {
        setEvent(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasAmount() {
        return getAmount() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEvent() {
        return getEvent() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasUsername() {
        return getUsername() != null;
    }

}

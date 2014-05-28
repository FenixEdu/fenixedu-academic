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

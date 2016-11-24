/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
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
        super.setPerson(null);
        super.setReceipt(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}

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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class Discount extends Discount_Base {

    private Discount() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
    }

    Discount(final Event event, final Person person, final Money amount) {
        this();
        checkEvent(event);
        checkAmount(amount);
        setAmount(amount);
        setEvent(event);
        if (person != null) {
            setUsername(person.getUsername());
        }
    }

    public void checkEvent(Event event) {
        final List<String> operationsAfter = event.getOperationsAfter(getWhenCreated());
        if (!operationsAfter.isEmpty()) {
            throw new DomainException("error.accounting.Discount.cannot.create.operations.after", operationsAfter.stream()
                    .collect(Collectors.joining(",")));
        }
    }

    private void checkAmount(Money amount) {
        if (amount == null || !amount.isPositive()) {
            throw new DomainException("error.Discount.invalid.amount");
        }
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        blockers.addAll(getEvent().getOperationsAfter(getWhenCreated()));
    }

    @Atomic
    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        setEvent(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}

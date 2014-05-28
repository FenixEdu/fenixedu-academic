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
package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;

import org.fenixedu.bennu.core.domain.Bennu;

public class ThesisSubjectOrder extends ThesisSubjectOrder_Base {

    public static Comparator<ThesisSubjectOrder> COMPARATOR_BY_ORDER = new Comparator<ThesisSubjectOrder>() {
        @Override
        public int compare(ThesisSubjectOrder order1, ThesisSubjectOrder order2) {
            return order1.getSubjectOrder() - order2.getSubjectOrder();
        }
    };

    public ThesisSubjectOrder() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ThesisSubjectOrder(ThesisSubject subject, PhdIndividualProgramProcess phdProcess, int order) {
        this();
        setThesisSubject(subject);
        setPhdIndividualProgramProcess(phdProcess);
        setSubjectOrder(order);
    }

    public void delete() {
        if (hasPhdIndividualProgramProcess()) {
            for (ThesisSubjectOrder followingSubjectOrder : getPhdIndividualProgramProcess().getThesisSubjectOrdersSorted()) {
                if (followingSubjectOrder.getSubjectOrder() > getSubjectOrder()) {
                    followingSubjectOrder.decreaseSubjectOrder();
                }
            }
            setPhdIndividualProgramProcess(null);
        }

        setThesisSubject(null);

        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void decreaseSubjectOrder() {
        if (getSubjectOrder() > 1) {
            setSubjectOrder(getSubjectOrder() - 1);
        }
    }

    @ConsistencyPredicate
    public boolean checkHasThesisSubject() {
        return hasThesisSubject();
    }

    @ConsistencyPredicate
    public boolean checkHasPhdIndividualProgramProcess() {
        return hasPhdIndividualProgramProcess();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasThesisSubject() {
        return getThesisSubject() != null;
    }

    @Deprecated
    public boolean hasPhdIndividualProgramProcess() {
        return getPhdIndividualProgramProcess() != null;
    }

    @Deprecated
    public boolean hasSubjectOrder() {
        return getSubjectOrder() != null;
    }

}

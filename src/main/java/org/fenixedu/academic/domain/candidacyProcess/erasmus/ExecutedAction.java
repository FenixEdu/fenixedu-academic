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
package org.fenixedu.academic.domain.candidacyProcess.erasmus;

import java.util.Comparator;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ExecutedAction extends ExecutedAction_Base {

    public static final Comparator<ExecutedAction> WHEN_OCCURED_COMPARATOR = new Comparator<ExecutedAction>() {

        @Override
        public int compare(ExecutedAction o1, ExecutedAction o2) {
            return o1.getWhenOccured().compareTo(o2.getWhenOccured());
        }
    };

    protected ExecutedAction() {
        super();
        setRootDomainObject(Bennu.getInstance());

        setWhoMade(AccessControl.getPerson());
        setWhenOccured(new DateTime());
    }

    public ExecutedAction(ExecutedActionType type) {
        this();
        init(type);
    }

    protected void init(ExecutedActionType type) {
        if (type == null) {
            throw new DomainException("error.erasmus.executed.action.type.is.null");
        }

        setType(type);
    }

}

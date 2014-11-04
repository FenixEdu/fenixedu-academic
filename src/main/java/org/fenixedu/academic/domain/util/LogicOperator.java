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
/*
 * Created on Feb 2, 2006
 */
package org.fenixedu.academic.domain.util;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum LogicOperator {

    AND,

    OR,

    NOT;

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, name());
    }

    public boolean isAND() {
        return this.equals(LogicOperator.AND);
    }

    public boolean isOR() {
        return this.equals(LogicOperator.OR);
    }

    public boolean doLogic(final boolean first, final boolean other) {
        switch (this) {
        case AND:
            return first && other;
        case OR:
            return first || other;
        default:
            throw new DomainException("error.LogicOperator.invalid.operator");
        }
    }
}
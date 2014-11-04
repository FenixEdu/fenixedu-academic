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
package org.fenixedu.academic.service.services.manager.organizationalStructureManagement;

import static org.fenixedu.academic.predicate.AccessControl.check;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.predicate.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateFunction {

    @Atomic
    public static void run(MultiLanguageString functionName, YearMonthDay begin, YearMonthDay end, FunctionType type,
            String unitID) throws FenixServiceException, DomainException {
        check(RolePredicates.MANAGER_PREDICATE);

        Unit unit = (Unit) FenixFramework.getDomainObject(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.function.no.unit");
        }

        new Function(functionName, begin, end, type, unit);
    }
}
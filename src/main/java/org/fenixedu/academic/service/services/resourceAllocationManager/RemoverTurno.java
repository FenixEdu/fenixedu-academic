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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.predicate.RolePredicates;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoverTurno {

    @Atomic
    public static Object run(final InfoShift infoShift, final InfoClass infoClass) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());
        if (shift == null) {
            return Boolean.FALSE;
        }
        final SchoolClass schoolClass = (SchoolClass) CollectionUtils.find(shift.getAssociatedClassesSet(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final SchoolClass schoolClass = (SchoolClass) arg0;
                return schoolClass.getExternalId().equals(infoClass.getExternalId());
            }
        });
        if (schoolClass == null) {
            return Boolean.FALSE;
        }
        shift.getAssociatedClassesSet().remove(schoolClass);
        return Boolean.TRUE;
    }

}
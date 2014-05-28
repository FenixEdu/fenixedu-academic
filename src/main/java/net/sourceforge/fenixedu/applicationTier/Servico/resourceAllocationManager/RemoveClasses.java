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
/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveClasses {

    @Atomic
    public static Boolean run(InfoShift infoShift, List<String> classOIDs) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());

        for (int i = 0; i < classOIDs.size(); i++) {
            final SchoolClass schoolClass = FenixFramework.getDomainObject(classOIDs.get(i));
            shift.getAssociatedClasses().remove(schoolClass);
            schoolClass.getAssociatedShifts().remove(shift);
        }
        return Boolean.TRUE;
    }

}
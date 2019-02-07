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
 *
 * Created on 2003/08/15
 */

package org.fenixedu.academic.service.services.resourceAllocationManager;

import java.util.List;

import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.InfoShift;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveClasses {

    @Atomic
    public static Boolean run(InfoShift infoShift, List<String> classOIDs) {
        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());

        for (int i = 0; i < classOIDs.size(); i++) {
            final SchoolClass schoolClass = FenixFramework.getDomainObject(classOIDs.get(i));
            shift.getAssociatedClassesSet().remove(schoolClass);
            schoolClass.getAssociatedShiftsSet().remove(shift);
        }
        return Boolean.TRUE;
    }

}
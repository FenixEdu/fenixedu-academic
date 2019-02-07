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
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package org.fenixedu.academic.service.services.resourceAllocationManager;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoShift;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadShiftsByClass {

    @Atomic
    public static Object run(InfoClass infoClass) {
        SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());

        Collection<Shift> shifts = schoolClass.getAssociatedShiftsSet();

        return CollectionUtils.collect(shifts, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                Shift shift = (Shift) arg0;
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                return infoShift;
            }
        });
    }

}
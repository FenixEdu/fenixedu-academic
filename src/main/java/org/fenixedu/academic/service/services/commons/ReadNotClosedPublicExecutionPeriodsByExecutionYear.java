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
package org.fenixedu.academic.service.services.commons;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadNotClosedPublicExecutionPeriodsByExecutionYear {

    @Atomic
    public static List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        final ExecutionYear executionYear = FenixFramework.getDomainObject(infoExecutionYear.getExternalId());

        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : executionYear.readNotClosedPublicExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return result;
    }
}
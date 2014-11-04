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
 * Created on 14/Mar/2003
 *  
 */
package org.fenixedu.academic.service.services.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import pt.ist.fenixframework.Atomic;

public class ReadMasterDegrees {

    @Atomic
    public static List run(String executionYearString) throws FenixServiceException {
        final ExecutionYear executionYear;
        if (executionYearString != null) {
            executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        } else {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }

        // Read the degrees
        final List result = ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear.getYear(), DegreeType.MASTER_DEGREE);
        if (result == null || result.size() == 0) {
            throw new NonExistingServiceException();
        }

        final List degrees = new ArrayList(result.size());
        for (final Iterator iterator = result.iterator(); iterator.hasNext();) {
            final ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            degrees.add(infoExecutionDegree);
        }

        return degrees;
    }
}
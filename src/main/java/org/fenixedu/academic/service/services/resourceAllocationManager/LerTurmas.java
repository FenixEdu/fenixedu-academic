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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerTurmas {

    @Atomic
    public static List<InfoClass> run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());

        final Set<SchoolClass> classes;
        if (curricularYear != null) {
            classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionSemester, curricularYear);
        } else {
            classes = executionDegree.findSchoolClassesByExecutionPeriod(executionSemester);
        }

        final List<InfoClass> infoClassesList = new ArrayList<InfoClass>();

        for (final SchoolClass schoolClass : classes) {
            InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
            infoClassesList.add(infoClass);
        }

        return infoClassesList;
    }

}
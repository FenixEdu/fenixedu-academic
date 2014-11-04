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
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package org.fenixedu.academic.service.services.resourceAllocationManager;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CriarTurma {

    @Atomic
    public static Object run(final String className, final Integer curricularYear, final InfoExecutionDegree infoExecutionDegree,
            final AcademicInterval academicInterval) throws ExistingServiceException {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());

        final SchoolClass schoolClass = new SchoolClass(executionDegree, academicInterval, className, curricularYear);
        return InfoClass.newInfoFromDomain(schoolClass);
    }

}
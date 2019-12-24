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
package org.fenixedu.academic.service.services.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControlPredicate;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadExecutionDegreesByExecutionPeriodId {

    @Atomic
    public static List<InfoExecutionDegree> run(String executionPeriodId) throws FenixServiceException {
        return getExecutionDegreesByExecutionPeriodId(executionPeriodId, null);
    }

    @Atomic
    public static List<InfoExecutionDegree> runForAcademicAdmin(String executionPeriodId) throws FenixServiceException {
        return getExecutionDegreesByExecutionPeriodId(executionPeriodId, AcademicPredicates.MANAGE_EXECUTION_COURSES);
    }

    @Atomic
    public static List<InfoExecutionDegree> runForAcademicAdminAdv(String executionPeriodId) throws FenixServiceException {
        return getExecutionDegreesByExecutionPeriodId(executionPeriodId, AcademicPredicates.MANAGE_EXECUTION_COURSES_ADV);
    }

    private static List<InfoExecutionDegree> getExecutionDegreesByExecutionPeriodId(String executionPeriodId,
            AccessControlPredicate<Object> permission) throws FenixServiceException {
        if (executionPeriodId == null) {
            throw new FenixServiceException("executionPeriodId.should.not.be.null");
        }
        ExecutionInterval executionInterval = FenixFramework.getDomainObject(executionPeriodId);

        List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionInterval.getExecutionYear().getExecutionDegreesSet()) {
            if (permission != null) {
                if (!permission.evaluate(executionDegree.getDegree())) {
                    continue;
                }
            }
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            infoExecutionDegreeList.add(infoExecutionDegree);
        }
        return infoExecutionDegreeList;
    }
}
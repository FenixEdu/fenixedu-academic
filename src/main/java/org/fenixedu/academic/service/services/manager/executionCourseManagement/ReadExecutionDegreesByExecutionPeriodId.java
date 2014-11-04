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
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
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
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);

        List<ExecutionDegree> executionDegrees =
                ExecutionDegree.getAllByExecutionYear(executionSemester.getExecutionYear().getYear());

        List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
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
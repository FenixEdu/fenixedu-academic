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
 * Created on 21/Jul/2003
 *
 * 
 */
package org.fenixedu.academic.service.services.teacher;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.filter.SummaryManagementToTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 *         21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 * 
 */
public class DeleteSummary {

    protected Boolean run(ExecutionCourse executionCourse, Summary summary, Professorship professorship)
            throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), executionCourse, summary, professorship);

        if (summary == null) {
            throw new InvalidArgumentsServiceException();
        }

        summary.delete();
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteSummary serviceInstance = new DeleteSummary();

    @Atomic
    public static Boolean runDeleteSummary(ExecutionCourse executionCourse, Summary summary, Professorship professorship)
            throws FenixServiceException, NotAuthorizedException {
        SummaryManagementToTeacherAuthorizationFilter.instance.execute(summary, professorship);
        return serviceInstance.run(executionCourse, summary, professorship);
    }

}
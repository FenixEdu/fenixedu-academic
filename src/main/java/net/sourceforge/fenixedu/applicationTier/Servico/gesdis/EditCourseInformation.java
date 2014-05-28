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
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Filtro.gesdis.EditCourseInformationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author jdnf and mrsp
 */
public class EditCourseInformation {

    protected void run(String courseReportID, InfoCourseReport infoCourseReport, String newReport) throws FenixServiceException {
        final CourseReport courseReport;
        if (courseReportID != null) {
            courseReport = FenixFramework.getDomainObject(courseReportID);
        } else {
            final ExecutionCourse executionCourse =
                    FenixFramework.getDomainObject(infoCourseReport.getInfoExecutionCourse().getExternalId());

            courseReport = executionCourse.createCourseReport(newReport);
        }

        if (courseReport == null) {
            throw new FenixServiceException();
        }

        courseReport.edit(newReport);
    }

    // Service Invokers migrated from Berserk

    private static final EditCourseInformation serviceInstance = new EditCourseInformation();

    @Atomic
    public static void runEditCourseInformation(String courseReportID, InfoCourseReport infoCourseReport, String newReport)
            throws FenixServiceException, NotAuthorizedException {
        EditCourseInformationAuthorizationFilter.instance.execute(courseReportID, infoCourseReport, newReport);
        serviceInstance.run(courseReportID, infoCourseReport, newReport);
    }

}
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
/**
 * Project Sop
 *
 * Package presentationTier.Action.sop.utils
 *
 * Created on 16/Dez/2002
 *
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.service.services.commons.ReadExecutionYear;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.publico.ReadExecutionDegreesByExecutionYearAndDegreeInitials;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
public abstract class RequestUtils {

    public static final InfoExecutionCourse getExecutionCourseBySigla(HttpServletRequest request,
            String infoExecutionCourseInitials) throws Exception {

        AcademicInterval academicInterval = AcademicInterval
                .getAcademicIntervalFromResumedString((String) request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL));

        final ExecutionCourse executionCourse = readBySiglaAndExecutionPeriod(infoExecutionCourseInitials,
                ExecutionInterval.getExecutionInterval(academicInterval));
        if (executionCourse != null) {
            return InfoExecutionCourse.newInfoFromDomain(executionCourse);
        }

        throw new IllegalArgumentException("Not find executionCourse!");
    }

    private static ExecutionCourse readBySiglaAndExecutionPeriod(final String sigla, ExecutionInterval executionInterval) {
        for (ExecutionCourse executionCourse : executionInterval.getAssociatedExecutionCoursesSet()) {
            if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                return executionCourse;
            }
        }
        return null;
    }

    public static final InfoExecutionYear getExecutionYearFromRequest(HttpServletRequest request)
            throws FenixActionException, FenixServiceException {
        InfoExecutionYear infoExecutionYear = null;
        String year = (String) request.getAttribute("eYName");
        if (year == null) {
            year = request.getParameter("eYName");
        }

        if (year != null) {

            infoExecutionYear = ReadExecutionYear.run(year);
        }
        return infoExecutionYear;
    }

    public static final InfoExecutionDegree getExecutionDegreeFromRequest(HttpServletRequest request,
            InfoExecutionYear infoExecutionYear) throws FenixActionException {

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute("exeDegree");
        if (infoExecutionDegree != null) {
            return infoExecutionDegree;
        }

        String degreeInitials = (String) request.getAttribute("degreeInitials");
        String nameDegreeCurricularPlan = (String) request.getAttribute("nameDegreeCurricularPlan");

        if (degreeInitials == null) {
            degreeInitials = request.getParameter("degreeInitials");
        }
        if (nameDegreeCurricularPlan == null) {
            nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");
        }

        infoExecutionDegree = ReadExecutionDegreesByExecutionYearAndDegreeInitials.run(infoExecutionYear, degreeInitials,
                nameDegreeCurricularPlan);

        return infoExecutionDegree;
    }

    public static final void setExecutionPeriodToRequest(HttpServletRequest request, InfoExecutionPeriod infoExecutionPeriod) {
        if (infoExecutionPeriod != null) {

            request.setAttribute("ePName", infoExecutionPeriod.getName());
            request.setAttribute("eYName", infoExecutionPeriod.getInfoExecutionYear().getYear());

        }
    }

    public static final void setExecutionDegreeToRequest(HttpServletRequest request, InfoExecutionDegree executionDegree) {
        if (executionDegree != null) {
            request.setAttribute("exeDegree", executionDegree);
            request.setAttribute("nameDegreeCurricularPlan", executionDegree.getInfoDegreeCurricularPlan().getName());
            if (executionDegree.getInfoDegreeCurricularPlan().getInfoDegree() != null) {
                request.setAttribute("degreeInitials", executionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
            }
            request.setAttribute("degreeCurricularPlanID", executionDegree.getInfoDegreeCurricularPlan().getExternalId());
            request.setAttribute("executionDegree", FenixFramework.getDomainObject(executionDegree.getExternalId()));
        }

    }

}
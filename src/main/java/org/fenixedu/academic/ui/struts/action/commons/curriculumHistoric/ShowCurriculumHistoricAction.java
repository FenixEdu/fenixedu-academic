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
 * Created on Oct 11, 2004
 */
package org.fenixedu.academic.ui.struts.action.commons.curriculumHistoric;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * @author nmgo
 * @author lmre
 */
@Mapping(path = "/showCurriculumHistoric", module = "academicAdministration",
        functionality = DegreeCurricularPlanExecutionYearDispacthAction.class)
@Forwards({ @Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp") })
public class ShowCurriculumHistoricAction extends FenixDispatchAction {

    public ActionForward showCurriculumHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Integer semester = getIntegerFromRequest(request, "semester");
        final AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString(request.getParameter("academicInterval"));

        final CurricularCourse curricularCourse = getDomainObject(request, "curricularCourseCode");

        final AcademicInterval interval =
                curricularCourse.isAnual() ? academicInterval : academicInterval.getChildAcademicInterval(
                        AcademicPeriod.SEMESTER, semester);

        request.setAttribute("infoCurriculumHistoricReport", new InfoCurriculumHistoricReport(interval, curricularCourse));

        return mapping.findForward("show-report");
    }

}

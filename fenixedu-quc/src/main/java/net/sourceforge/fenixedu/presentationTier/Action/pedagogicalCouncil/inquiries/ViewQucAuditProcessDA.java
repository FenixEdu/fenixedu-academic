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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.inquiries;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.inquiries.CompetenceCourseResultsResume;
import org.fenixedu.academic.dto.inquiries.CurricularCourseResumeResult;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.inquiries.ExecutionCourseAudit;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/auditResult", module = "pedagogicalCouncil", functionality = QucAuditPedagogicalCouncilDA.class)
@Forwards({ @Forward(name = "viewProcessDetails", path = "/pedagogicalCouncil/inquiries/viewProcessDetailsNoAction.jsp") })
public class ViewQucAuditProcessDA extends FenixDispatchAction {

    public ActionForward viewProcessDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
        ExecutionCourseAudit executionCourseAudit = FenixFramework.getDomainObject(executionCourseAuditOID);

        List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

        request.setAttribute("executionCourseAudit", executionCourseAudit);
        request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
        return mapping.findForward("viewProcessDetails");
    }

    protected List<CompetenceCourseResultsResume> getCompetenceCourseResultsBeans(ExecutionCourseAudit executionCourseAudit) {
        List<CompetenceCourseResultsResume> competenceCoursesToAudit = new ArrayList<CompetenceCourseResultsResume>();
        for (CompetenceCourse competenceCourse : executionCourseAudit.getExecutionCourse().getCompetenceCourses()) {
            CompetenceCourseResultsResume competenceCourseResultsResume = null;
            for (ExecutionDegree executionDegree : executionCourseAudit.getExecutionCourse().getExecutionDegrees()) {
                CurricularCourseResumeResult courseResumeResult =
                        new CurricularCourseResumeResult(executionCourseAudit.getExecutionCourse(), executionDegree,
                                "label.inquiry.execution", executionDegree.getDegree().getSigla(), AccessControl.getPerson(),
                                ResultPersonCategory.DEPARTMENT_PRESIDENT, false, true, true, true, true);
                if (courseResumeResult.getResultBlocks().size() > 1) {
                    if (competenceCourseResultsResume == null) {
                        competenceCourseResultsResume = new CompetenceCourseResultsResume(competenceCourse);
                        competenceCoursesToAudit.add(competenceCourseResultsResume);
                    }
                    competenceCourseResultsResume.addCurricularCourseResumeResult(courseResumeResult);
                }
            }
        }
        return competenceCoursesToAudit;
    }
}

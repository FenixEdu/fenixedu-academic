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
package org.fenixedu.academic.ui.struts.action.publicRelationsOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.PublicRelationsStudentListQueueJob;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.service.services.student.reports.GenerateStudentReport.StudentReportPredicate;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = PublicRelationsApplication.class, path = "student-listings", titleKey = "title.student.reports")
@Mapping(path = "/studentReports", module = "publicRelations")
@Forwards({ @Forward(name = "search", path = "/publicRelations/reports/studentReports.jsp"),
        @Forward(name = "viewPublicRelationsReports", path = "/publicRelations/reports/viewPublicRelationsReports.jsp") })
public class StudentReportsDA extends FenixDispatchAction {

    public List<QueueJob> getLatestJobs() {
        return QueueJob.getLastJobsForClassOrSubClass(PublicRelationsStudentListQueueJob.class, 5);
    }

    @EntryPoint
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        StudentReportPredicate studentReportPredicate = getRenderedObject();
        if (studentReportPredicate == null) {
            if (request.getParameter("executionYearID") != null) {
                studentReportPredicate = setBean(request);
                request.setAttribute("showLink", true);
            } else {
                studentReportPredicate = new StudentReportPredicate();
            }
        } else {
            if (studentReportPredicate.getExecutionYear() != null
                    & (studentReportPredicate.getConcluded() == true || studentReportPredicate.getActive() == true)) {
                request.setAttribute("showLink", true);
            }
        }

        request.setAttribute("queueJobList", getLatestJobs());
        request.setAttribute("studentReportPredicate", studentReportPredicate);
        return mapping.findForward("search");
    }

    public StudentReportPredicate setBean(HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearID");
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearIDString);

        final String degreeTypeString = request.getParameter("degreeType");
        final DegreeType degreeType = degreeTypeString == null ? null : DegreeType.valueOf(degreeTypeString);

        final String concludedString = request.getParameter("concluded");
        final boolean concluded = Boolean.parseBoolean(concludedString);

        final String activeString = request.getParameter("active");
        final boolean active = Boolean.parseBoolean(activeString);

        StudentReportPredicate studentReportPredicate = new StudentReportPredicate();
        studentReportPredicate.setExecutionYear(executionYear);
        studentReportPredicate.setDegreeType(degreeType);
        studentReportPredicate.setConcluded(concluded);
        studentReportPredicate.setActive(active);

        return studentReportPredicate;

    }

    public static class FindSelectedPublicRelationsStudentList implements Predicate {

        ExecutionYear executionYear;
        DegreeType degreeType;
        boolean active;
        boolean concluded;

        int elements = 0;

        public FindSelectedPublicRelationsStudentList(ExecutionYear executionYear, DegreeType degreeType, boolean active,
                boolean concluded) {
            this.executionYear = executionYear;
            this.degreeType = degreeType;
            this.active = active;
            this.concluded = concluded;
        }

        @Override
        public boolean evaluate(Object object) {
            QueueJob queueJob = (QueueJob) object;
            try {
                PublicRelationsStudentListQueueJob reportJob = (PublicRelationsStudentListQueueJob) queueJob;
                if (this.executionYear == reportJob.getExecutionYear() && this.degreeType == reportJob.getDegreeType()
                        && reportJob.getActive() == active && reportJob.getConcluded() == concluded && elements < 5) {
                    elements++;
                    return true;
                } else {
                    return false;
                }
            } catch (ClassCastException E) {
                return false;
            }

        }
    }

    public ActionForward viewJobs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        StudentReportPredicate studentReportPredicate = setBean(request);
        Predicate predicate =
                new FindSelectedPublicRelationsStudentList(studentReportPredicate.getExecutionYear(),
                        studentReportPredicate.getDegreeType(), studentReportPredicate.getActive(),
                        studentReportPredicate.getConcluded());
        List<FindSelectedPublicRelationsStudentList> selectedJobs =
                (List<FindSelectedPublicRelationsStudentList>) org.apache.commons.collections.CollectionUtils.select(
                        rootDomainObject.getQueueJobSet(), predicate);

        request.setAttribute("studentReportPredicate", studentReportPredicate);
        request.setAttribute("queueJobList", selectedJobs);
        return mapping.findForward("viewPublicRelationsReports");
    }

    public ActionForward requestJob(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StudentReportPredicate studentReportPredicate = setBean(request);

        PublicRelationsStudentListQueueJob job =
                new PublicRelationsStudentListQueueJob(studentReportPredicate.getExecutionYear(),
                        studentReportPredicate.getDegreeType(), studentReportPredicate.getConcluded(),
                        studentReportPredicate.getActive());

        List<QueueJob> queueJobList = getLatestJobs();

        request.setAttribute("queueJobList", queueJobList);
        request.setAttribute("job", job);
        request.setAttribute("showLink", true);
        request.setAttribute("studentReportPredicate", studentReportPredicate);
        return mapping.findForward("search");
    }

}

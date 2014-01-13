package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport.StudentReportPredicate;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PublicRelationsStudentListQueueJob;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.ReportFileFactory;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/studentReports", module = "publicRelations")
@Forwards({ @Forward(name = "search", path = "/publicRelations/reports/studentReports.jsp"),
        @Forward(name = "viewPublicRelationsReports", path = "/publicRelations/reports/viewPublicRelationsReports.jsp") })
public class StudentReportsDA extends FenixDispatchAction {

    public List<QueueJob> getLatestJobs() {
        return QueueJob.getAllJobsForClassOrSubClass(PublicRelationsStudentListQueueJob.class, 5);
    }

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

        List<QueueJob> queueJobList = getLatestJobs();

        request.setAttribute("queueJobList", queueJobList);
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
                ReportFileFactory.createPublicRelationsStudentListQueueJob(studentReportPredicate.getExecutionYear(),
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

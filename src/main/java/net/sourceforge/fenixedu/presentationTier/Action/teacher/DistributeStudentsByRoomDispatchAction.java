package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.TeacherAdministrationSiteComponentService;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.WrittenEvaluationRoomDistribution;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteExamExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "teacher", path = "/distributeStudentsByRoom",
        input = "/examEnrollmentManager.do?method=prepareEnrolmentManagement", attribute = "roomDistributionForm",
        formBean = "roomDistributionForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-rooms", path = "distribute.students.by.room"),
        @Forward(name = "show-distribution", path = "/showStudentsEnrolledInExam.do?method=prepare") })
public class DistributeStudentsByRoomDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final DynaActionForm distributionExam = (DynaActionForm) form;
        final Integer evaluationCode = Integer.valueOf((String) distributionExam.get("evaluationCode"));
        final Integer executionCourseCode = Integer.valueOf((String) distributionExam.get("objectCode"));

        final InfoSiteExamExecutionCourses infoSiteExamExecutionCourses = new InfoSiteExamExecutionCourses();

        final TeacherAdministrationSiteView siteView =
                TeacherAdministrationSiteComponentService.runTeacherAdministrationSiteComponentService(executionCourseCode,
                        new InfoSiteCommon(), infoSiteExamExecutionCourses, null, evaluationCode, null);

        final InfoExam infoExam = infoSiteExamExecutionCourses.getInfoExam();
        final List<InfoExecutionCourse> infoExecutionCourses = infoSiteExamExecutionCourses.getInfoExecutionCourses();

        int totalAttendStudents = 0;
        for (final InfoExecutionCourse infoExecutionCourse : infoExecutionCourses) {
            totalAttendStudents += infoExecutionCourse.getNumberOfAttendingStudents().intValue();
        }

        Collections.sort(infoExam.getAssociatedRooms(), new ReverseComparator(new BeanComparator("capacidadeExame")));

        request.setAttribute("infoExam", infoExam);
        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);
        request.setAttribute("attendStudents", Integer.valueOf(totalAttendStudents));

        return mapping.findForward("show-rooms");
    }

    public ActionForward distribute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm distributionExam = (DynaActionForm) form;
        final Integer evaluationCode = Integer.valueOf((String) distributionExam.get("evaluationCode"));
        final Integer executionCourseCode = Integer.valueOf((String) distributionExam.get("objectCode"));
        final Boolean distributeOnlyEnroledStudents = Boolean.valueOf((String) distributionExam.get("enroll"));
        final List rooms = Arrays.asList((Integer[]) distributionExam.get("rooms"));

        try {
            WrittenEvaluationRoomDistribution.runWrittenEvaluationRoomDistribution(executionCourseCode, evaluationCode, rooms,
                    Boolean.FALSE, distributeOnlyEnroledStudents);
            request.setAttribute("objectCode", executionCourseCode);
            request.setAttribute("evaluationCode", evaluationCode);
            return mapping.findForward("show-distribution");

        } catch (DomainException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError(e.getKey()));
            saveErrors(request, actionErrors);
            return prepare(mapping, form, request, response);
        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return prepare(mapping, form, request, response);
        }
    }

}
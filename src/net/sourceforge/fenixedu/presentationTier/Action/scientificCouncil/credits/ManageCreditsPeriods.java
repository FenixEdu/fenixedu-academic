package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits.CreateTeacherCreditsFillingPeriod;
import net.sourceforge.fenixedu.dataTransferObject.teacherCredits.TeacherCreditsPeriodBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.TeacherCredits;
import net.sourceforge.fenixedu.domain.TeacherCreditsQueueJob;
import net.sourceforge.fenixedu.domain.TeacherCreditsState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "scientificCouncil", path = "/defineCreditsPeriods", attribute = "creditsPeriodForm",
        formBean = "creditsPeriodForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "edit-teacher-credits-periods", path = "/scientificCouncil/credits/periods/editTeacherCreditsPeriod.jsp",
                tileProperties = @Tile(title = "private.scientificcouncil.credits.defineperiods")),
        @Forward(name = "show-credits-periods", path = "/scientificCouncil/credits/periods/showPeriods.jsp",
                tileProperties = @Tile(title = "private.scientificcouncil.credits.defineperiods")) })
public class ManageCreditsPeriods extends FenixDispatchAction {

    public ActionForward showPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TeacherCreditsPeriodBean bean = createBeanTeacherCreditsPeriodBean(mapping, actionForm, request);

        if (isDoneAllTeacherCreditsQueueJob(bean.getExecutionPeriod())) {
            if (isCloseAllTeacherCreditsState(bean.getExecutionPeriod())) {
                request.setAttribute("closePeriodTeacherCredits", true);
            } else {
                request.setAttribute("closePeriodTeacherCredits", false);
            }
        }
        request.setAttribute("teacherCreditsBean", bean);
        return mapping.findForward("show-credits-periods");
    }

    public ActionForward prepareEditTeacherCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
        request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionSemester, true));
        return mapping.findForward("edit-teacher-credits-periods");
    }

    public ActionForward prepareEditDepartmentAdmOfficeCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
        request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionSemester, false));
        return mapping.findForward("edit-teacher-credits-periods");
    }

    public ActionForward editPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TeacherCreditsPeriodBean bean = getRenderedObject("teacherCreditsBeanID");

        try {
            CreateTeacherCreditsFillingPeriod.run(bean);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("teacherCreditsBean", bean);
            return mapping.findForward("edit-teacher-credits-periods");
        }

        request.setAttribute("teacherCreditsBean", bean);
        RenderUtils.invalidateViewState("teacherCreditsBeanID");
        return mapping.findForward("show-credits-periods");
    }

    public ActionForward closeAllPeriodsByExecutionSemester(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        TeacherCreditsPeriodBean bean = createBeanTeacherCreditsPeriodBean(mapping, actionForm, request);
        TeacherCreditsQueueJob.createTeacherCreditsQueueJob(bean.getExecutionPeriod());
        request.setAttribute("teacherCreditsBean", bean);
        return mapping.findForward("show-credits-periods");
    }

    public ActionForward openAllPeriodsByExecutionSemester(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        TeacherCreditsPeriodBean bean = createBeanTeacherCreditsPeriodBean(mapping, actionForm, request);
        TeacherCredits.openAllTeacherCredits(bean.getExecutionPeriod());
        request.setAttribute("teacherCreditsBean", bean);
        request.setAttribute("closePeriodTeacherCredits", false);
        return mapping.findForward("show-credits-periods");
    }

    private boolean isDoneAllTeacherCreditsQueueJob(ExecutionSemester executionSemester) {
        for (QueueJob queueJob : getTeacherCreditsJobs()) {
            TeacherCreditsQueueJob job = (TeacherCreditsQueueJob) queueJob;
            if (job.getExecutionSemester().equals(executionSemester)) {
                if (job.getIsNotDoneAndNotCancelled()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isCloseAllTeacherCreditsState(ExecutionSemester executionSemester) {
        TeacherCreditsState teacherCreditsState = TeacherCreditsState.getTeacherCreditsState(executionSemester);
        if (teacherCreditsState == null || teacherCreditsState.isOpenState()) {
            return false;
        }
        return true;
    }

    public List<QueueJob> getTeacherCreditsJobs() {
        return (QueueJob.getAllJobsForClassOrSubClass(TeacherCreditsQueueJob.class, rootDomainObject.getQueueJobCount()));
    }

    private TeacherCreditsPeriodBean createBeanTeacherCreditsPeriodBean(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request) throws Exception {
        TeacherCreditsPeriodBean bean = getRenderedObject("teacherCreditsBeanID");
        if (bean == null) {
            ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
            if (executionSemester == null) {
                bean = new TeacherCreditsPeriodBean(ExecutionSemester.readActualExecutionSemester());
            } else {
                bean = new TeacherCreditsPeriodBean(executionSemester);
            }
        } else {
            bean.refreshDates();
        }
        return bean;
    }

    private ExecutionSemester getExecutionPeriodToEditPeriod(HttpServletRequest request) {
        String parameter = request.getParameter("executionPeriodId");
        Integer executionPeriodId = parameter != null ? Integer.valueOf(parameter) : null;
        return rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
    }

}
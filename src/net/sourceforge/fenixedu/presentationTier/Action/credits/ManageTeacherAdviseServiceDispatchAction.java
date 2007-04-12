/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.Advise.AdvisePercentageException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherAdviseServiceDispatchAction extends FenixDispatchAction {

    protected void getAdviseServices(HttpServletRequest request, DynaActionForm dynaForm,
            final ExecutionPeriod executionPeriod, Teacher teacher) {

        dynaForm.set("executionPeriodId", executionPeriod.getIdInternal());
        dynaForm.set("teacherId", teacher.getIdInternal());

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null && !teacherService.getTeacherAdviseServices().isEmpty()) {
            BeanComparator comparator = new BeanComparator("advise.student.number");
            Iterator orderedAdviseServicesIter = new OrderedIterator(teacherService
                    .getTeacherAdviseServices().iterator(), comparator);
            request.setAttribute("adviseServices", orderedAdviseServicesIter);
        }

        request.setAttribute("executionPeriod", executionPeriod);
        request.setAttribute("teacher", teacher);
    }

    protected ActionForward editAdviseService(ActionForm form, HttpServletRequest request,
            ActionMapping mapping, RoleType roleType) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm adviseServiceForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf(adviseServiceForm.getString("studentNumber"));
        Double percentage = Double.valueOf(adviseServiceForm.getString("percentage"));
        Integer teacherID = (Integer) adviseServiceForm.get("teacherId");
        Integer executionPeriodID = (Integer) adviseServiceForm.get("executionPeriodId");
        Object[] args = { teacherID, executionPeriodID, studentNumber, percentage,
                AdviseType.FINAL_WORK_DEGREE, roleType };
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "EditTeacherAdviseService",
                    args);

        } catch (AdvisePercentageException ape) {
            ActionMessages actionMessages = new ActionMessages();
            addMessages(ape, actionMessages, AdviseType.FINAL_WORK_DEGREE);
            saveMessages(request, actionMessages);
            return mapping.getInputForward();

        } catch (DomainException e) {
            saveMessages(request, e);
        }

        return mapping.findForward("successfull-edit");
    }

    protected void deleteAdviseService(HttpServletRequest request, RoleType roleType)
            throws NumberFormatException, FenixFilterException, FenixServiceException {

        Integer adviseServiceID = Integer.valueOf(request.getParameter("teacherAdviseServiceID"));
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "DeleteTeacherAdviseServiceByOID", new Object[] { adviseServiceID, roleType });
        } catch (DomainException e) {
            saveMessages(request, e);
        }
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }

    private void addMessages(AdvisePercentageException ape, ActionMessages actionMessages, AdviseType adviseType) {
        ExecutionPeriod executionPeriod = ape.getExecutionPeriod();
        ActionMessage initialActionMessage = new ActionMessage("message.teacherAdvise.percentageExceed");
        actionMessages.add("", initialActionMessage);
        for (Advise advise : ape.getAdvises()) {
            TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionPeriod);
            if (adviseType.equals(ape.getAdviseType()) && teacherAdviseService != null) {
                Integer teacherNumber = advise.getTeacher().getTeacherNumber();
                String teacherName = advise.getTeacher().getPerson().getName();
                Double percentage = teacherAdviseService.getPercentage();
                ActionMessage actionMessage = new ActionMessage("message.teacherAdvise.teacher.percentageExceed", teacherNumber.toString(), teacherName, percentage.toString(), "%");
                actionMessages.add("message.teacherAdvise.teacher.percentageExceed", actionMessage);
            }
        }
    }
}

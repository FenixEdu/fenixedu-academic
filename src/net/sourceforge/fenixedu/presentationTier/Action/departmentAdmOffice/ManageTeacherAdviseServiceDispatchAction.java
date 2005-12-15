/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.IAdvise;
import net.sourceforge.fenixedu.domain.teacher.ITeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
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

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherAdvises");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherAdvises(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        ITeacher teacher = null;
        Integer teacherID = (Integer) dynaForm.get("teacherId");
        if (teacherID != null) {
            teacher = (ITeacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadDomainTeacherByOID", new Object[] { teacherID });
        } else {
            Integer teacherNumber = Integer.valueOf(dynaForm.get("teacherNumber").toString());
            teacher = (ITeacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadDomainTeacherByNumber", new Object[] { teacherNumber });
        }
        dynaForm.set("teacherId", teacher.getIdInternal());

        final Integer executionPeriodID = (Integer) dynaForm.get("executionPeriodId");
        final IExecutionPeriod executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(
                SessionUtils.getUserView(request), "ReadDomainExecutionPeriodByOID",
                new Object[] { executionPeriodID });
        dynaForm.set("executionPeriodId", executionPeriod.getIdInternal());

        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null && !teacherService.getTeacherAdviseServices().isEmpty()) {
            BeanComparator comparator = new BeanComparator("advise.student.number");
            Iterator orderedAdviseServicesIter = new OrderedIterator(teacherService.getTeacherAdviseServices().iterator(),
                    comparator);
            request.setAttribute("adviseServices", orderedAdviseServicesIter);
        }

        request.setAttribute("executionPeriod", executionPeriod);
        request.setAttribute("teacher", teacher);
        return mapping.findForward("list-teacher-advise-services");
    }

    public ActionForward editAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm adviseServiceForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf(adviseServiceForm.getString("studentNumber"));
        Double percentage = Double.valueOf(adviseServiceForm.getString("percentage"));
        Integer teacherID = (Integer) adviseServiceForm.get("teacherId");
        Integer executionPeriodID = (Integer) adviseServiceForm.get("executionPeriodId");
        Object[] args = { teacherID, executionPeriodID, studentNumber, percentage,
                AdviseType.FINAL_WORK_DEGREE };
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "EditTeacherAdviseService",
                    args);
        } catch (AdvisePercentageException ape) {
            ActionMessages actionMessages = new ActionMessages();
            addMessages(ape, actionMessages, AdviseType.FINAL_WORK_DEGREE);
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        Integer adviseServiceID = Integer.valueOf(request.getParameter("teacherAdviseServiceID"));
        ServiceUtils.executeService(SessionUtils.getUserView(request),
                "DeleteTeacherAdviseServiceByOID", new Object[] { adviseServiceID });
        return mapping.findForward("successfull-delete");
    }

    private void addMessages(AdvisePercentageException ape, ActionMessages actionMessages,
            AdviseType adviseType) {
        IExecutionPeriod executionPeriod = ape.getExecutionPeriod();
        for (IAdvise advise : ape.getAdvises()) {
            ITeacherAdviseService teacherAdviseService = advise
                    .getTeacherAdviseServiceByExecutionPeriod(executionPeriod);
            if (adviseType.equals(ape.getAdviseType()) && teacherAdviseService != null) {
                Integer teacherNumber = advise.getTeacher().getTeacherNumber();
                String teacherName = advise.getTeacher().getPerson().getNome();
                Double percentage = teacherAdviseService.getPercentage();
                ActionMessage actionMessage = new ActionMessage(
                        "message.teacherAdvise.teacher.percentageExceed", teacherNumber.toString(),
                        teacherName, percentage.toString(), "%");
                actionMessages.add("message.teacherAdvise.teacher.percentageExceed", actionMessage);
            }
        }
    }
}

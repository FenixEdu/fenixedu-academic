/**
 * Nov 29, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IOtherService;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageOtherServiceDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showOtherServices");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showOtherServices(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm otherServiceForm = (DynaActionForm) form;
        Integer teacherID = (Integer) otherServiceForm.get("teacherId");
        ITeacher teacher = null;
        if (teacherID == null || teacherID == 0) {
            teacher = (ITeacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadDomainTeacherByNumber", new Object[] { Integer.valueOf(otherServiceForm.get(
                            "teacherNumber").toString()) });
        } else {
            teacher = (ITeacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadDomainTeacherByOID", new Object[] { teacherID });
        }
        request.setAttribute("teacher", teacher);

        IExecutionPeriod executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(SessionUtils
                .getUserView(request), "ReadDomainExecutionPeriodByOID",
                new Object[] { (Integer) otherServiceForm.get("executionPeriodId") });
        request.setAttribute("executionPeriod", executionPeriod);

        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null && !teacherService.getOtherServices().isEmpty()) {
            request.setAttribute("otherServices", teacherService.getOtherServices());
        }

        return mapping.findForward("show-other-services");
    }

    public ActionForward prepareEditOtherService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm otherServiceForm = (DynaActionForm) form;

        IExecutionPeriod executionPeriod = null;
        ITeacher teacher = null;
        Integer otherServiceID = (Integer) otherServiceForm.get("otherServiceID");
        if (otherServiceID == null || otherServiceID == 0) {
            Integer teacherID = (Integer) otherServiceForm.get("teacherId");
            Integer executionPeriodID = (Integer) otherServiceForm.get("executionPeriodId");

            teacher = (ITeacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadDomainTeacherByOID", new Object[] { teacherID });
            executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(SessionUtils
                    .getUserView(request), "ReadDomainExecutionPeriodByOID",
                    new Object[] { executionPeriodID });
        } else {
            IOtherService otherService = (IOtherService) ServiceUtils.executeService(SessionUtils
                    .getUserView(request), "ReadOtherServiceByOID", new Object[] { otherServiceID });
            otherServiceForm.set("credits", String.valueOf(otherService.getCredits()));
            otherServiceForm.set("reason", otherService.getReason());

            teacher = otherService.getTeacherService().getTeacher();
            executionPeriod = otherService.getTeacherService().getExecutionPeriod();
        }

        request.setAttribute("teacher", teacher);
        request.setAttribute("executionPeriod", executionPeriod);
        return mapping.findForward("edit-other-service");
    }

    public ActionForward editOtherService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm otherServiceForm = (DynaActionForm) form;
        Double credits = Double.valueOf(otherServiceForm.getString("credits"));
        String reason = otherServiceForm.getString("reason");

        Integer otherServiceID = (Integer) otherServiceForm.get("otherServiceID");
        if (otherServiceID == null || otherServiceID == 0) {
            Integer teacherID = (Integer) otherServiceForm.get("teacherId");
            Integer executionPeriodID = (Integer) otherServiceForm.get("executionPeriodId");
            Object[] args = { teacherID, executionPeriodID, credits, reason };
            ServiceUtils.executeService(SessionUtils.getUserView(request), "CreateOtherService", args);
        } else {
            Object[] args = { otherServiceID, credits, reason };
            ServiceUtils.executeService(SessionUtils.getUserView(request), "EditOtherService", args);
        }

        return mapping.findForward("successful-edit");
    }

    public ActionForward deleteOtherService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm otherServiceForm = (DynaActionForm) form;
        Integer otherServiceID = (Integer) otherServiceForm.get("otherServiceID");
        ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteOtherServiceByOID",
                new Object[] { otherServiceID });
        return mapping.findForward("successful-delete");
    }

}

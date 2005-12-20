/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherInstitutionWorkingTimeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showWorkingTimePeriods");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showWorkingTimePeriods(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        final Integer executionPeriodID = Integer.valueOf((String) institutionWorkingTimeForm
                .get("executionPeriodId"));
        final IExecutionPeriod executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(
                userView, "ReadDomainExecutionPeriodByOID", new Object[] { executionPeriodID });

        Integer teacherNumber = Integer.valueOf(institutionWorkingTimeForm.getString("teacherNumber"));
        List<IDepartment> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        ITeacher teacher = null;
        for (IDepartment department : manageableDepartments) {
            teacher = department.getTeacherByPeriod(teacherNumber, executionPeriod.getBeginDate(),
                    executionPeriod.getEndDate());
            if (teacher != null) {
                break;
            }
        }
        if (teacher == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }

        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null && !teacherService.getInstitutionWorkTimes().isEmpty()) {

            ComparatorChain comparatorChain = new ComparatorChain();
            BeanComparator weekDayComparator = new BeanComparator("weekDay");
            BeanComparator startTimeComparator = new BeanComparator("startTime");

            comparatorChain.addComparator(weekDayComparator);
            comparatorChain.addComparator(startTimeComparator);

            Iterator institutionWorkTimeIterator = new OrderedIterator(teacherService
                    .getInstitutionWorkTimes().iterator(), comparatorChain);

            request.setAttribute("institutionWorkTimeList", institutionWorkTimeIterator);
        }

        institutionWorkingTimeForm.set("teacherNumber", String.valueOf(teacher.getTeacherNumber()));
        request.setAttribute("teacher", teacher);
        request.setAttribute("executionPeriod", executionPeriod);
        return mapping.findForward("list-teacher-institution-working-time");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
        Integer institutionWorkingTimeID = (Integer) institutionWorkingTimeForm
                .get("institutionWorkTimeID");

        IExecutionPeriod executionPeriod = null;
        ITeacher teacher = null;
        if (institutionWorkingTimeID == null || institutionWorkingTimeID == 0) {
            Integer teacherID = Integer.valueOf(institutionWorkingTimeForm.getString("teacherId"));
            teacher = (ITeacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadDomainTeacherByOID", new Object[] { teacherID });
            Integer executionPeriodID = Integer.valueOf(institutionWorkingTimeForm
                    .getString("executionPeriodId"));
            executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(SessionUtils
                    .getUserView(request), "ReadDomainExecutionPeriodByOID",
                    new Object[] { executionPeriodID });
            request.setAttribute("toCreate", "toCreate");
        } else {

            IInstitutionWorkTime institutionWorkTime = (IInstitutionWorkTime) ServiceUtils
                    .executeService(SessionUtils.getUserView(request), "ReadInstitutionWorkTimeByOID",
                            new Object[] { institutionWorkingTimeID });

            Date startTime = institutionWorkTime.getStartTime();
            Date endTime = institutionWorkTime.getEndTime();

            Calendar time = Calendar.getInstance();
            time.setTime(startTime);
            institutionWorkingTimeForm.set("startTimeHour", String.valueOf(time
                    .get(Calendar.HOUR_OF_DAY)));
            institutionWorkingTimeForm
                    .set("startTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));

            time.setTime(endTime);
            institutionWorkingTimeForm
                    .set("endTimeHour", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
            institutionWorkingTimeForm.set("endTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));

            institutionWorkingTimeForm.set("weekDay", institutionWorkTime.getWeekDay().getName());
            ITeacherService teacherService = institutionWorkTime.getTeacherService();
            teacher = teacherService.getTeacher();
            executionPeriod = teacherService.getExecutionPeriod();
        }

        institutionWorkingTimeForm.set("teacherNumber", String.valueOf(teacher.getTeacherNumber()));
        request.setAttribute("teacher", teacher);
        request.setAttribute("executionPeriod", executionPeriod);

        return mapping.findForward("edit-institution-work-time");
    }

    public ActionForward editInstitutionWorkingTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException, InvalidPeriodException {

        InstitutionWorkTimeDTO institutionWorkTimeDTO = getInstitutionWorkTimeDTO(form);
        Calendar begin = Calendar.getInstance();
        begin.setTime(institutionWorkTimeDTO.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTime(institutionWorkTimeDTO.getEndTime());

        if (end.before(begin)) {
            throw new InvalidPeriodException();
        }

        if (institutionWorkTimeDTO.getIdInternal() != null
                && institutionWorkTimeDTO.getIdInternal() != 0) {
            ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "EditTeacherInstitutionWorkTime", new Object[] { institutionWorkTimeDTO });
        } else {
            DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
            Integer teacherID = Integer.valueOf(institutionWorkingTimeForm.getString("teacherId"));
            Integer executionPeriodID = Integer.valueOf(institutionWorkingTimeForm
                    .getString("executionPeriodId"));

            Object[] args = { teacherID, executionPeriodID, institutionWorkTimeDTO };
            ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "CreateTeacherInstitutionWorkTime", args);
        }

        return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteInstitutionWorkingTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm institutionWorkTimeForm = (DynaActionForm) form;
        Integer institutionWorkTimeID = (Integer) institutionWorkTimeForm.get("institutionWorkTimeID");
        ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteInstitutionWorkTimeByOID",
                new Object[] { institutionWorkTimeID });

        return mapping.findForward("successfull-delete");
    }

    protected InstitutionWorkTimeDTO getInstitutionWorkTimeDTO(ActionForm form) {
        DynaActionForm institutionWorkTimeForm = (DynaActionForm) form;
        InstitutionWorkTimeDTO institutionWorkTimeDTO = new InstitutionWorkTimeDTO();

        institutionWorkTimeDTO.setIdInternal((Integer) institutionWorkTimeForm
                .get("institutionWorkTimeID"));

        WeekDay weekDay = WeekDay.valueOf((String) institutionWorkTimeForm.get("weekDay"));
        institutionWorkTimeDTO.setWeekDay(weekDay);

        Calendar calendar = Calendar.getInstance();

        setHoursAndMinutes(calendar, Integer.valueOf((String) institutionWorkTimeForm
                .get("startTimeHour")), Integer.valueOf((String) institutionWorkTimeForm
                .get("startTimeMinutes")));
        institutionWorkTimeDTO.setStartTime(new Date(calendar.getTimeInMillis()));

        setHoursAndMinutes(calendar, Integer
                .valueOf((String) institutionWorkTimeForm.get("endTimeHour")), Integer
                .valueOf((String) institutionWorkTimeForm.get("endTimeMinutes")));
        institutionWorkTimeDTO.setEndTime(new Date(calendar.getTimeInMillis()));

        return institutionWorkTimeDTO;
    }

    private void setHoursAndMinutes(Calendar calendar, Integer hour, Integer minutes) {
        calendar.set(Calendar.HOUR_OF_DAY, hour != null ? hour.intValue() : 0);
        calendar.set(Calendar.MINUTE, minutes != null ? minutes.intValue() : 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public class InvalidPeriodException extends FenixActionException {
    }
}

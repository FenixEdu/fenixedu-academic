/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherInstitutionWorkingTimeDispatchAction extends FenixDispatchAction {

    protected void getInstitutionWokTimeList(HttpServletRequest request,
            DynaActionForm institutionWorkingTimeForm, final ExecutionPeriod executionPeriod,
            Teacher teacher) {

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
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
    }

    protected void prepareToEdit(InstitutionWorkTime institutionWorkTime, Teacher teacher,
            ExecutionPeriod executionPeriod, HttpServletRequest request,
            DynaActionForm institutionWorkingTimeForm) {

        if (institutionWorkTime == null) {
            request.setAttribute("toCreate", "toCreate");
        } else {
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
            institutionWorkingTimeForm.set("institutionWorkTimeID", institutionWorkTime.getIdInternal());
        }

        institutionWorkingTimeForm.set("teacherNumber", String.valueOf(teacher.getTeacherNumber()));
        request.setAttribute("teacher", teacher);
        request.setAttribute("executionPeriod", executionPeriod);
    }

    protected void editInstitutionWorkingTime(ActionForm form, HttpServletRequest request,
            RoleType roleType) throws NumberFormatException, FenixFilterException,
            FenixServiceException, InvalidPeriodException {

        InstitutionWorkTimeDTO institutionWorkTimeDTO = getInstitutionWorkTimeDTO(form);
        Calendar begin = Calendar.getInstance();
        begin.setTime(institutionWorkTimeDTO.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTime(institutionWorkTimeDTO.getEndTime());

        if (end.before(begin)) {
            throw new InvalidPeriodException();
        }

        try {
            if (institutionWorkTimeDTO.getIdInternal() != null
                    && institutionWorkTimeDTO.getIdInternal() != 0) {
                ServiceUtils.executeService(SessionUtils.getUserView(request),
                        "EditTeacherInstitutionWorkTime", new Object[] { institutionWorkTimeDTO,
                                roleType });
            } else {
                DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
                Integer teacherID = Integer.valueOf(institutionWorkingTimeForm.getString("teacherId"));
                Integer executionPeriodID = Integer.valueOf(institutionWorkingTimeForm
                        .getString("executionPeriodId"));

                Object[] args = { teacherID, executionPeriodID, institutionWorkTimeDTO, roleType };
                ServiceUtils.executeService(SessionUtils.getUserView(request),
                        "CreateTeacherInstitutionWorkTime", args);
            }
        } catch (DomainException e) {
            saveMessages(request, e);                       
        }        
    }

    protected void deleteInstitutionWorkingTime(ActionForm actionForm, HttpServletRequest request,
            RoleType roleType) throws NumberFormatException, FenixFilterException, FenixServiceException {

        DynaActionForm institutionWorkTimeForm = (DynaActionForm) actionForm;
        Integer institutionWorkTimeID = (Integer) institutionWorkTimeForm.get("institutionWorkTimeID");
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "DeleteInstitutionWorkTimeByOID", new Object[] { institutionWorkTimeID, roleType });
        } catch (DomainException e) {
            saveMessages(request, e);
        }
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

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;

        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(Integer
                .valueOf((String) institutionWorkingTimeForm.get("executionPeriodId")));

        Integer teacherId = Integer.valueOf(institutionWorkingTimeForm.getString("teacherId"));
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);

        getInstitutionWokTimeList(request, institutionWorkingTimeForm, executionPeriod, teacher);
        return mapping.findForward("list-teacher-institution-working-time");
    }

    private void setHoursAndMinutes(Calendar calendar, Integer hour, Integer minutes) {
        calendar.set(Calendar.HOUR_OF_DAY, hour != null ? hour.intValue() : 0);
        calendar.set(Calendar.MINUTE, minutes != null ? minutes.intValue() : 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public class InvalidPeriodException extends FenixActionException {
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }
}

package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Contract;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SchedulesPrintAction extends ShowTeacherCreditsDispatchAction {
    
    public ActionForward showSchedulesPrint(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer executionPeriodId = Integer.valueOf(request.getParameter("executionPeriodId"));
        Integer teacherId = Integer.valueOf(request.getParameter("teacherId"));

        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        
        getAllTeacherCredits(request, executionPeriod, teacher);
        
        setLegalRegimen(request, executionPeriod, teacher);
        setWorkingUnit(request, executionPeriod, teacher);                        
        setWeekDays(request);
        
        request.setAttribute("teacherService", teacher.getTeacherServiceByExecutionPeriod(executionPeriod));
        
        return mapping.findForward("show-schedules-resume-print");
    }

    private void setWeekDays(HttpServletRequest request) {
        List<WeekDay> weekDays = new ArrayList<WeekDay>();
        for (WeekDay weekDay : WeekDay.values()) {
            if(!weekDay.equals(WeekDay.SUNDAY)) {
                weekDays.add(weekDay);
            }
        }
        request.setAttribute("weekDays", weekDays);
    }

    private void setWorkingUnit(HttpServletRequest request, ExecutionPeriod executionPeriod, Teacher teacher) {
        List<Contract> contracts = teacher.getPerson().getEmployee().getContractsByPeriod(
                executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay());

        if (!contracts.isEmpty()) {
            Collections.sort(contracts, new BeanComparator("beginDate"));
            request.setAttribute("workingUnit", contracts.get(0).getWorkingUnit());
        }
    }

    private void setLegalRegimen(HttpServletRequest request, ExecutionPeriod executionPeriod, Teacher teacher) {
        List<TeacherLegalRegimen> teacherLegalRegimens = teacher
                .getAllLegalRegimensWithoutEndSituations(executionPeriod.getBeginDateYearMonthDay(), executionPeriod
                        .getEndDateYearMonthDay());

        if (!teacherLegalRegimens.isEmpty()) {
            Collections.sort(teacherLegalRegimens, new BeanComparator("beginDate"));
            request.setAttribute("legalRegimen", teacherLegalRegimens.get(0));
        }
    }    
}

package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.util.WeekDay;

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
        
        OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
        setLegalRegimen(request, occupationPeriod, teacher);
        setWorkingUnit(request, occupationPeriod, teacher);                        
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

    private void setWorkingUnit(HttpServletRequest request, OccupationPeriod occupationPeriod, Teacher teacher) {       
        if(occupationPeriod != null) {
            Unit lastWorkingUnit = teacher.getLastWorkingUnit(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());           
            if(lastWorkingUnit != null) {
                request.setAttribute("workingUnit", lastWorkingUnit);
                Unit departmentUnit = lastWorkingUnit.getDepartmentUnit();
                if(departmentUnit != null && departmentUnit.getDepartment() != null ) {
                    request.setAttribute("departmentRealName", lastWorkingUnit.getDepartmentUnit().getDepartment().getRealName());
                }
            }
        }
    }

    private void setLegalRegimen(HttpServletRequest request, OccupationPeriod occupationPeriod, Teacher teacher) {        
        if(occupationPeriod != null) {
            TeacherLegalRegimen lastLegalRegimen = teacher.getLastLegalRegimenWithoutEndSituations(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());
            if (lastLegalRegimen != null) {            
                request.setAttribute("legalRegimen", lastLegalRegimen);
            }
        }
    }        
}

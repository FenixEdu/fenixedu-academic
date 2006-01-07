/**
 * Nov 21, 2005
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
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherSupportLessonsDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showProfessorships");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showProfessorships(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);
        
        final Integer executionPeriodID = (Integer) dynaForm.get("executionPeriodId");
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) ServiceUtils.executeService(
                userView, "ReadDomainExecutionPeriodByOID",
                new Object[] { executionPeriodID });
        
        Integer teacherNumber = Integer.valueOf(dynaForm.getString("teacherNumber"));
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        Teacher teacher = null;
        for (Department department : manageableDepartments) {
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
        request.setAttribute("teacher", teacher);

        List<Professorship> professorships = teacher
                .getDegreeProfessorshipsByExecutionPeriod(executionPeriod);
        List<ProfessorshipDTO> professorshipDTOs = (List<ProfessorshipDTO>) CollectionUtils.collect(
                professorships, new Transformer() {
                    public Object transform(Object arg0) {
                        Professorship professorship = (Professorship) arg0;
                        return new ProfessorshipDTO(professorship);
                    }
                });
        if (!professorshipDTOs.isEmpty()) {
            Iterator professorshipDTOsIterator = new OrderedIterator(professorshipDTOs.iterator(),
                    new BeanComparator("professorship.executionCourse.nome"));
            request.setAttribute("professorshipDTOs", professorshipDTOsIterator);
        }
        return mapping.findForward("list-professorships");
    }

    public ActionForward showSupportLessons(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");

        Professorship professorship = (Professorship) ServiceUtils.executeService(SessionUtils
                .getUserView(request), "ReadDomainProfessorshipByOID", new Object[] { professorshipID });

        ComparatorChain comparatorChain = new ComparatorChain();
        BeanComparator weekDayComparator = new BeanComparator("weekDay.diaSemana");
        BeanComparator startTimeComparator = new BeanComparator("startTime");

        comparatorChain.addComparator(weekDayComparator);
        comparatorChain.addComparator(startTimeComparator);
        if (!professorship.getSupportLessons().isEmpty()) {
            Iterator supportLessonsOrderedIter = new OrderedIterator(professorship
                    .getSupportLessonsIterator(), comparatorChain);
            request.setAttribute("supportLessonList", supportLessonsOrderedIter);
        }
        request.setAttribute("professorship", professorship);
        return mapping.findForward("list-support-lessons");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        Integer supportLesssonID = (Integer) supportLessonForm.get("supportLessonID");

        Professorship professorship = null;
        if (supportLesssonID != null && supportLesssonID != 0) {
            SupportLesson supportLesson = (SupportLesson) ServiceUtils.executeService(SessionUtils
                    .getUserView(request), "ReadDomainSupportLessonByOID",
                    new Object[] { supportLesssonID });

            Date startTime = supportLesson.getStartTime();
            Date endTime = supportLesson.getEndTime();

            if (startTime != null || endTime != null) {
                Calendar time = Calendar.getInstance();
                if (startTime != null) {
                    time.setTime(startTime);
                    supportLessonForm.set("startTimeHour", String
                            .valueOf(time.get(Calendar.HOUR_OF_DAY)));
                    supportLessonForm.set("startTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
                }
                if (endTime != null) {
                    time.setTime(endTime);
                    supportLessonForm.set("endTimeHour", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
                    supportLessonForm.set("endTimeMinutes", String.valueOf(time.get(Calendar.MINUTE)));
                }
            }
            supportLessonForm.set("weekDay", getWeekDayString(supportLesson.getWeekDay()));
            supportLessonForm.set("place", supportLesson.getPlace());
            supportLessonForm.set("supportLessonID", supportLesson.getIdInternal());
            supportLessonForm.set("professorshipID", supportLesson.getProfessorship().getIdInternal());

            request.setAttribute("supportLesson", supportLesson);
            professorship = supportLesson.getProfessorship();
        } else {
            Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");
            professorship = (Professorship) ServiceUtils.executeService(SessionUtils
                    .getUserView(request), "ReadDomainProfessorshipByOID",
                    new Object[] { professorshipID });
        }

        request.setAttribute("professorship", professorship);

        return mapping.findForward("edit-support-lesson");
    }

    public ActionForward editSupportLesson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException, InvalidPeriodException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        SupportLessonDTO supportLessonDTO = new SupportLessonDTO();

        supportLessonDTO.setIdInternal((Integer) supportLessonForm.get("supportLessonID"));
        supportLessonDTO.setProfessorshipID((Integer) supportLessonForm.get("professorshipID"));
        supportLessonDTO.setWeekDay(getWeekDay((String) supportLessonForm.get("weekDay")));
        Calendar calendar = Calendar.getInstance();

        setHoursAndMinutes(calendar, Integer.valueOf((String) supportLessonForm.get("startTimeHour")),
                Integer.valueOf((String) supportLessonForm.get("startTimeMinutes")));
        supportLessonDTO.setStartTime(new Date(calendar.getTimeInMillis()));

        setHoursAndMinutes(calendar, Integer.valueOf((String) supportLessonForm.get("endTimeHour")),
                Integer.valueOf((String) supportLessonForm.get("endTimeMinutes")));
        supportLessonDTO.setEndTime(new Date(calendar.getTimeInMillis()));
        supportLessonDTO.setPlace((String) supportLessonForm.get("place"));

        Calendar begin = Calendar.getInstance();
        begin.setTime(supportLessonDTO.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTime(supportLessonDTO.getEndTime());

        if (end.before(begin)) {
            throw new InvalidPeriodException();
        }
        ServiceUtils.executeService(SessionUtils.getUserView(request), "EditSupportLesson",
                new Object[] { supportLessonDTO });

        return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        Integer supportLessonID = (Integer) supportLessonForm.get("supportLessonID");

        ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteSupportLesson",
                new Object[] { supportLessonID });

        return mapping.findForward("successfull-delete");
    }

    /**
     * @param semana
     * @return
     */
    private String getWeekDayString(DiaSemana weekday) {
        switch (weekday.getDiaSemana().intValue()) {
        case DiaSemana.DOMINGO:
            return "D";
        case DiaSemana.SEGUNDA_FEIRA:
            return "2";
        case DiaSemana.TERCA_FEIRA:
            return "3";
        case DiaSemana.QUARTA_FEIRA:
            return "4";
        case DiaSemana.QUINTA_FEIRA:
            return "5";
        case DiaSemana.SEXTA_FEIRA:
            return "6";
        case DiaSemana.SABADO:
            return "S";
        default:
            return "";
        }
    }

    private DiaSemana getWeekDay(String weekday) {
        int weekDayInt = 0;
        try {
            weekDayInt = Integer.parseInt(weekday);
        } catch (NumberFormatException e) {
            if (weekday.equalsIgnoreCase("S")) {
                weekDayInt = DiaSemana.SABADO;
            } else if (weekday.equalsIgnoreCase("D")) {
                weekDayInt = DiaSemana.DOMINGO;
            }
        }
        return new DiaSemana(weekDayInt);
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

/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department.supportLessons;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ReadProfessorshipSupportLessons;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipSupportLessonsDTO;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author jpvl
 */
public class ReadTeacherSupportLessonsAction extends Action {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IUserView userView = UserView.getUser();
        DynaActionForm professorShipForm = (DynaActionForm) form;
        Integer teacherId = (Integer) professorShipForm.get("teacherId");
        Integer executionCourseId = (Integer) professorShipForm.get("executionCourseId");

        ProfessorshipSupportLessonsDTO professorshipSupportLessonsDTO =
                ReadProfessorshipSupportLessons.runReadProfessorshipSupportLessons(teacherId, executionCourseId);

        ComparatorChain comparatorChain = new ComparatorChain();

        BeanComparator weekDayComparator = new BeanComparator("weekDay.diaSemana");
        BeanComparator startTimeComparator = new BeanComparator("startTime");

        comparatorChain.addComparator(weekDayComparator);
        comparatorChain.addComparator(startTimeComparator);

        Collections.sort(professorshipSupportLessonsDTO.getInfoSupportLessonList(), comparatorChain);

        request.setAttribute("professorshipSupportLessons", professorshipSupportLessonsDTO);

        return mapping.findForward("list-support-lessons");
    }

}
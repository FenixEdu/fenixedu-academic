/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.department.supportLessons;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.teacher.professorship.ProfessorshipSupportLessonsDTO;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ReadTeacherSupportLessonsAction extends Action
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *          org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *          javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm professorShipForm = (DynaActionForm) form;
        Integer teacherId = (Integer) professorShipForm.get("teacherId");
        Integer executionCourseId = (Integer) professorShipForm.get("executionCourseId");
        
        Object args[] = { teacherId, executionCourseId };
        ProfessorshipSupportLessonsDTO professorshipSupportLessonsDTO =
            (ProfessorshipSupportLessonsDTO) ServiceUtils.executeService(
                userView,
                "ReadProfessorshipSupportLessons",
                args);
        
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

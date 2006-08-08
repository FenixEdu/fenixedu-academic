package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Pedro Santos e Rita Carvalho 22/Out/2004
 * 
 */
public class ViewStudentScheduleAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		final Object[] args = { Student.readByUsername(request.getParameter("userName")) };
		request.setAttribute("infoLessons", ServiceUtils.executeService(getUserView(request),
				"ReadStudentTimeTable", args));

		return mapping.findForward("sucess");

	}

}
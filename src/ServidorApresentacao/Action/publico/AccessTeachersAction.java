package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AccessTeachersAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,  
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = getSession(request);
		session.removeAttribute(SessionConstants.INFO_SECTION);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		Object args[] = {infoSite.getInfoExecutionCourse()};
		GestorServicos serviceManager = GestorServicos.manager();
		
		List teachers = (List) serviceManager.executar(userView, "ReadTeachersByExecutionCourseProfessorship", args);

		List responsibleTeachers = (List) serviceManager.executar(userView, "ReadTeachersByExecutionCourseResponsibility", args);

		if (responsibleTeachers != null && !responsibleTeachers.isEmpty()) {
			session.removeAttribute(SessionConstants.RESPONSIBLE_TEACHERS_LIST);			
			session.setAttribute(SessionConstants.RESPONSIBLE_TEACHERS_LIST, responsibleTeachers);

			if (teachers != null && !teachers.isEmpty()) {
				teachers.removeAll(responsibleTeachers);
				session.removeAttribute(SessionConstants.TEACHERS_LIST);			
				session.setAttribute(SessionConstants.TEACHERS_LIST, teachers);
			}
		}
		else {
			session.removeAttribute(SessionConstants.RESPONSIBLE_TEACHERS_LIST);	
			session.removeAttribute(SessionConstants.TEACHERS_LIST);	
		}

		return mapping.findForward("Teachers");
	}
}
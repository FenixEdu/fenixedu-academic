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
		
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		Object args[] = { infoSite.getInfoExecutionCourse()};
		GestorServicos serviceManager = GestorServicos.manager();
		boolean result = false;
		List teachers = (List) serviceManager.executar(userView, "ReadTeachersByExecutionCourseProfessorship", args);

		if (teachers != null && !teachers.isEmpty()) {
			session.removeAttribute(SessionConstants.TEACHERS_LIST);			
			session.setAttribute(SessionConstants.TEACHERS_LIST, teachers);
		}

		return mapping.findForward("Teachers");
	}
}
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoCurriculum;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brandão
 */
public class AccessObjectivesAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,  
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = getSession(request);
		
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		Object args[] = { infoSite.getInfoExecutionCourse()};
		GestorServicos serviceManager = GestorServicos.manager();
		InfoCurriculum curriculumView = (InfoCurriculum) serviceManager.executar(userView, "ReadCurriculum", args);

		//remove old alternative site
		session.removeAttribute(SessionConstants.EXECUTION_COURSE_CURRICULUM);
		
		//put new alternative site
		session.setAttribute(SessionConstants.EXECUTION_COURSE_CURRICULUM, curriculumView);

		return mapping.findForward("Objectives");
	}
}
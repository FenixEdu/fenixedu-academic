package ServidorApresentacao.Action.teacher;

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
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author ep15
 */

public class CurriculumManagerDispatchAction extends FenixAction {

	public ActionForward acessObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = getSession(request);

		
		session.removeAttribute(SessionConstants.INFO_SITE_SECTION);
		session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT);
		session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
		session.removeAttribute("ReferenciaBibliografica");
		session.removeAttribute(SessionConstants.EXECUTION_COURSE_CURRICULUM);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		//        userView.setSeccaoNome(null);
		//
		//        userView.setAnunciosNome(null);
		//
		//        userView.setCurriculumNome(null);

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		Object args[] = { infoSite.getInfoExecutionCourse()};
		GestorServicos serviceManager = GestorServicos.manager();

		InfoCurriculum curriculumView =
			(InfoCurriculum) serviceManager.executar(
				userView,
				"ReadCurriculum",
				args);

		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_CURRICULUM,
			curriculumView);

		// userView.setCurriculumNome(curriculumView.getPrograma());

		return mapping.findForward("GestaoDeCurriculum");

	}

	public ActionForward acessProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = getSession(request);

		session.removeAttribute(SessionConstants.INFO_SITE_SECTION);
		session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT);
		session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
		session.removeAttribute("ReferenciaBibliografica");
		session.removeAttribute(SessionConstants.EXECUTION_COURSE_CURRICULUM);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		//		  userView.setSeccaoNome(null);
		//
		//		  userView.setAnunciosNome(null);
		//
		//		  userView.setCurriculumNome(null);

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		Object args[] = { infoSite.getInfoExecutionCourse()};
		GestorServicos serviceManager = GestorServicos.manager();
		InfoCurriculum curriculumView =
			(InfoCurriculum) serviceManager.executar(
				userView,
				"ReadCurriculum",
				args);
		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_CURRICULUM,
			curriculumView);
		// userView.setCurriculumNome(curriculumView.getPrograma());

		return mapping.findForward("GestaoDeCurriculum");
	}

}

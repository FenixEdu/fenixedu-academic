package ServidorApresentacao.Action.teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoCurriculum;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author jmota
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
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		Object args[] = { infoSite.getInfoExecutionCourse()};
		try {
			GestorServicos serviceManager = GestorServicos.manager();
			InfoCurriculum curriculumView =
				(InfoCurriculum) serviceManager.executar(
					userView,
					"ReadCurriculum",
					args);
			session.setAttribute(
				SessionConstants.EXECUTION_COURSE_CURRICULUM,
				curriculumView);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
			}
		return mapping.findForward("viewObjectives");
	}
	public ActionForward editObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
			
		return mapping.findForward("viewObjectives");
	}
	public ActionForward prepareEditObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
			
		return mapping.findForward("editObjectives");
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
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		InfoCurriculum curriculumView;
		try {
			Object args[] = { infoSite.getInfoExecutionCourse()};
			GestorServicos serviceManager = GestorServicos.manager();
			curriculumView =
				(InfoCurriculum) serviceManager.executar(
					userView,
					"ReadCurriculum",
					args);
			session.setAttribute(
				SessionConstants.EXECUTION_COURSE_CURRICULUM,
				curriculumView);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return mapping.findForward("viewProgram");
	}
	public ActionForward editProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			return mapping.findForward("viewProgram");
	}
	public ActionForward prepareEditProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			return mapping.findForward("editProgram");
	}
}

package ServidorApresentacao.Action.teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.gesdis.InfoCurriculum;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
/**
 * @author jmota
 */
public class CurriculumManagerDispatchAction extends FenixDispatchAction {
	
	public ActionForward acessObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		SessionUtils.validSessionVerification(request, mapping);	
		
		HttpSession session = getSession(request);
		session.removeAttribute(SessionConstants.INFO_SITE_SECTION);
		session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT);
		session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
//		session.removeAttribute("ReferenciaBibliografica");
		session.removeAttribute(SessionConstants.EXECUTION_COURSE_CURRICULUM);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		if (infoSite!=null){
		
		try {
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
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
			}
		return mapping.findForward("viewObjectives");
		}
		else 
		{throw new FenixActionException();
		}
	}
	public ActionForward editObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		SessionUtils.validSessionVerification(request, mapping);	
		HttpSession session = request.getSession();
		DynaActionForm objectivesForm = (DynaActionForm) form;	
		InfoCurriculum oldCurriculum = (InfoCurriculum) session.getAttribute(SessionConstants.EXECUTION_COURSE_CURRICULUM);
		
		
		InfoCurriculum newCurriculum = new InfoCurriculum();
		BeanUtils.copyProperties(oldCurriculum,newCurriculum);	
		newCurriculum.setGeneralObjectives((String) objectivesForm.get("generalObjectives"));
		newCurriculum.setOperacionalObjectives((String) objectivesForm.get("operacionalObjectives"));
		Object args[] = {oldCurriculum,newCurriculum};
		UserView userView =
					(UserView) session.getAttribute(SessionConstants.U_VIEW);
		try {
			GestorServicos serviceManager = GestorServicos.manager();
			InfoCurriculum curriculumView =
						(InfoCurriculum) serviceManager.executar(
							userView,
							"EditCurriculum",
							args);
					session.setAttribute(
						SessionConstants.EXECUTION_COURSE_CURRICULUM,
						curriculumView);
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
					}
		return mapping.findForward("viewObjectives");
	}
	public ActionForward prepareEditObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);	
			
		return mapping.findForward("editObjectives");
	}
	public ActionForward acessProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);	
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
			SessionUtils.validSessionVerification(request, mapping);
			return mapping.findForward("viewProgram");
	}
	public ActionForward prepareEditProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);
			return mapping.findForward("editProgram");
	}
}

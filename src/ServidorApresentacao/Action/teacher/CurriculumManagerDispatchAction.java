package ServidorApresentacao.Action.teacher;
import java.lang.reflect.InvocationTargetException;

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
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author jmota
 */
public class CurriculumManagerDispatchAction extends FenixDispatchAction {
	public ActionForward acessObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		try {
			
			HttpSession session = request.getSession(false);
			session.removeAttribute(SessionConstants.INFO_SECTION);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
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
			return mapping.findForward("viewObjectives");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		} 
	}
	public ActionForward prepareEditObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		return mapping.findForward("editObjectives");
	}
	public ActionForward editObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		try {
			
			HttpSession session = request.getSession(false);
			session.removeAttribute(SessionConstants.INFO_SECTION);
			DynaActionForm objectivesForm = (DynaActionForm) form;
			InfoCurriculum oldCurriculum =
				(InfoCurriculum) session.getAttribute(
					SessionConstants.EXECUTION_COURSE_CURRICULUM);
			InfoCurriculum newCurriculum = new InfoCurriculum();
			BeanUtils.copyProperties(newCurriculum, oldCurriculum);
			newCurriculum.setGeneralObjectives(
				(String) objectivesForm.get("generalObjectives"));
			newCurriculum.setOperacionalObjectives(
				(String) objectivesForm.get("operacionalObjectives"));
			newCurriculum.setInfoExecutionCourse(
				oldCurriculum.getInfoExecutionCourse());
			Object args[] = { oldCurriculum, newCurriculum };
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			Boolean result =
				(Boolean) serviceManager.executar(
					userView,
					"EditCurriculum",
					args);
			if (result.booleanValue()) {
				session.setAttribute(
					SessionConstants.EXECUTION_COURSE_CURRICULUM,
					newCurriculum);
			} else {
				mapping.getInputForward();
				//TODO: error message required. verify which  error
			}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		} catch (IllegalAccessException e) {
			throw new FenixActionException(e);
		} catch (InvocationTargetException e) {
			throw new FenixActionException(e);
		}
		return mapping.findForward("viewObjectives");
	}
	public ActionForward acessProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		try {
			
			HttpSession session = request.getSession(false);
			session.removeAttribute(SessionConstants.INFO_SECTION);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			InfoSite infoSite =
				(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
			InfoCurriculum curriculumView;
			System.out.println(infoSite);
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
		throws FenixActionException {
		try {
			
			HttpSession session = request.getSession(false);
			session.removeAttribute(SessionConstants.INFO_SECTION);
			DynaActionForm objectivesForm = (DynaActionForm) form;
			InfoCurriculum oldCurriculum =
				(InfoCurriculum) session.getAttribute(
					SessionConstants.EXECUTION_COURSE_CURRICULUM);
			InfoCurriculum newCurriculum = new InfoCurriculum();
			BeanUtils.copyProperties(newCurriculum, oldCurriculum);
			newCurriculum.setProgram((String) objectivesForm.get("program"));
			newCurriculum.setInfoExecutionCourse(
				oldCurriculum.getInfoExecutionCourse());
			Object args[] = { oldCurriculum, newCurriculum };
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			Boolean result =
				(Boolean) serviceManager.executar(
					userView,
					"EditCurriculum",
					args);
			if (result.booleanValue()) {
				session.setAttribute(
					SessionConstants.EXECUTION_COURSE_CURRICULUM,
					newCurriculum);
			} else {
				//TODO verify errors
				return mapping.getInputForward();
			}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		} catch (IllegalAccessException e) {
			throw new FenixActionException(e);
		} catch (InvocationTargetException e) {
			throw new FenixActionException(e);
		}
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
	public ActionForward insertCurriculum(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
			
		
		HttpSession session = request.getSession();
		session.removeAttribute(SessionConstants.INFO_SECTION);
		DynaActionForm curriculumForm = (DynaActionForm) form;
		InfoCurriculum infoCurriculum = new InfoCurriculum();
		infoCurriculum.setGeneralObjectives((String) curriculumForm.get("generalObjectives"));
		infoCurriculum.setOperacionalObjectives((String) curriculumForm.get("operacionalObjectives"));
		infoCurriculum.setProgram((String) curriculumForm.get("program"));
		infoCurriculum.setInfoExecutionCourse(((InfoSite)session.getAttribute(SessionConstants.INFO_SITE)).getInfoExecutionCourse());
		
		
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		try {
			Object args[] = { infoCurriculum };

			GestorServicos serviceManager = GestorServicos.manager();
			Boolean result =
				(Boolean) serviceManager.executar(
					userView,
					"InsertCurriculum",
					args);
			if (result.booleanValue()) {
				session.setAttribute(
					SessionConstants.EXECUTION_COURSE_CURRICULUM,
					infoCurriculum);
			}

		} catch (FenixServiceException e) {

		}
		return mapping.findForward("viewCurriculum");
	}
}

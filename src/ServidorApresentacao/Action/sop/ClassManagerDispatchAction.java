/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 4/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.NotAuthorizedException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 *
 * 
 */
public class ClassManagerDispatchAction extends DispatchAction {
	private static final String CLASS_NAME_PARAM = "className";
	/**
	 * Create class
	 * */
	public ActionForward createClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		HttpSession session = request.getSession(false);

		session.removeAttribute(SessionConstants.EXECUTION_COURSE_KEY);
		session.removeAttribute(SessionConstants.CLASS_VIEW);
		session.removeAttribute(SessionConstants.LESSON_LIST_ATT);
		String className = getClassName(form);

		IUserView userView = SessionUtils.getUserView(request);

		if (className != null && !className.equals("")) {
			InfoClass classView =
				getInfoTurma(userView, className, request.getSession(false));

			if (classView == null) {
				Integer curricularYear =
					(Integer) session.getAttribute(
						SessionConstants.CURRICULAR_YEAR_KEY);
				InfoExecutionDegree infoExecutionDegree =
					(InfoExecutionDegree) session.getAttribute(
						SessionConstants.INFO_EXECUTION_DEGREE_KEY);
				InfoExecutionPeriod infoExecutionPeriod =
					(InfoExecutionPeriod) session.getAttribute(
						SessionConstants.INFO_EXECUTION_PERIOD_KEY);

				//				CurricularYearAndSemesterAndInfoExecutionDegree context =
				//					SessionUtils.getContext(request);

				InfoClass infoClass = new InfoClass();
				infoClass.setNome(className);
				infoClass.setAnoCurricular(curricularYear);
				infoClass.setInfoExecutionDegree(infoExecutionDegree);
				infoClass.setInfoExecutionPeriod(infoExecutionPeriod);

				Object argsCriarTurma[] = { infoClass };

				try {
					ServiceUtils.executeService(
						userView,
						"CriarTurma",
						argsCriarTurma);
				} catch (ExistingServiceException e) {
					throw new ExistingActionException("A Turma",e);
				}
				return viewClass(mapping, form, request, response);
			} else {
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"existingClass",
					new ActionError("errors.existClass", className));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
		} else {
			return mapping.getInputForward();
		}

	}

	/**
	 * Change class name
	 * 
	 * */
	public ActionForward editClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		String className = getClassName(form);

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
		boolean change = request.getParameter("change") != null;

		if (change) {

			InfoClass oldClassView =
				(InfoClass) session.getAttribute(SessionConstants.CLASS_VIEW);
			

			if (oldClassView == null) {
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"errors.unknownClass",
					new ActionError("errors.unknownClass", className));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();

			}
			InfoClass newClassView =
				(InfoClass) BeanUtils.cloneBean(oldClassView);

			newClassView.setNome(className);

			Object[] argsEditarTurma = {  oldClassView,newClassView };
			try {

				ServiceUtils.executeService(
					userView,
					"EditarTurma",
					argsEditarTurma);
			} catch (ExistingServiceException ex) {
				throw new ExistingActionException("A Turma", ex);
			} catch (NotAuthorizedException e) {
				throw e; 
			} catch (FenixServiceException e) {
				e.printStackTrace(System.out);
				oldClassView.setNome(newClassView.getNome());
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"existingClass",
					new ActionError("errors.existClass", className));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

			session.setAttribute(SessionConstants.CLASS_VIEW, newClassView);

		} else { /** starting editing */
			//InfoClass classView = getInfoTurma(userView, className, session);
			session.setAttribute(
				SessionConstants.CLASS_VIEW,
				getInfoTurma(
					SessionUtils.getUserView(request),
					className,
					session));
		}

		setLessonListToSession(session, userView, className);

		return mapping.getInputForward();
	}

	/**
	 * View class time table
	 */
	public ActionForward viewClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		String className = getClassName(form);
		IUserView userView = SessionUtils.getUserView(request);
		InfoClass classView =
			getInfoTurma(userView, className, request.getSession(false));
		setLessonListToSession(request.getSession(false), userView, className);
		HttpSession session = request.getSession(false);
		session.setAttribute(SessionConstants.CLASS_VIEW, classView);

		return mapping.getInputForward();
	}

	/**
	 * Delete class.
	 * */
	public ActionForward deleteClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		DynaValidatorForm classForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession(false);
		String className = getClassName(classForm);
		IUserView userView = SessionUtils.getUserView(request);

		InfoClass infoClass = getInfoTurma(userView, className, session);

//		ClassKey keyClass = new ClassKey(className);
		Object argsApagarTurma[] = { infoClass };
		ServiceUtils.executeService(userView, "ApagarTurma", argsApagarTurma);

		return mapping.findForward("listClasses");
	}

	/**
	 * 
	 *  Private methods
	 * 
	 * */

	private String getClassName(ActionForm form) {
		DynaValidatorForm classForm = (DynaValidatorForm) form;

		return (String) classForm.get(CLASS_NAME_PARAM);

	}

	private InfoClass getInfoTurma(
		IUserView userView,
		String className,
		HttpSession session)
		throws Exception {
		/* :FIXME: put this 2 variables into parameters */
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY);

		Object argsLerTurma[] =
			{ className, infoExecutionDegree, infoExecutionPeriod };
		InfoClass classView =
			(InfoClass) ServiceUtils.executeService(
				userView,
				"LerTurma",
				argsLerTurma);
		return classView;
	}

	private void setLessonListToSession(
		HttpSession session,
		IUserView userView,
		String className)
		throws Exception {

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY);

		InfoClass infoClass = new InfoClass();

		infoClass.setInfoExecutionDegree(infoExecutionDegree);
		infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
		infoClass.setNome(className);

		Object argsApagarTurma[] = { infoClass };

		/** InfoLesson ArrayList */
		ArrayList lessonList =
			(ArrayList) ServiceUtils.executeService(
				userView,
				"LerAulasDeTurma",
				argsApagarTurma);

		session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessonList);

	}

}

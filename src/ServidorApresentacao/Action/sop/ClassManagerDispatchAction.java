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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.ClassKey;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoClass;
import ServidorAplicacao.IUserView;
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
		HttpSession session = request.getSession();

		session.removeAttribute(SessionConstants.CLASS_VIEW);
		session.removeAttribute(SessionConstants.LESSON_LIST_ATT);
		String className = getClassName(form);

		IUserView userView = SessionUtils.getUserView(request);

		if (className != null && !className.equals("")) {
			InfoClass classView = getInfoTurma(userView, className);

			if (classView == null) {
				CurricularYearAndSemesterAndInfoExecutionDegree context =
					SessionUtils.getContext(request);

				Object argsCriarTurma[] =
					{
						 new InfoClass(
							className,
							context.getSemestre(),
							context.getAnoCurricular(),
							context
								.getInfoLicenciaturaExecucao()
								.getInfoLicenciatura())};

				ServiceUtils.executeService(
					userView,
					"CriarTurma",
					argsCriarTurma);
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

		HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);
		boolean change = request.getParameter("change") != null;
		InfoClass infoTurma = getInfoTurma(userView, className);

		if (change) {

			infoTurma =
				(InfoClass) session.getAttribute(SessionConstants.CLASS_VIEW);

			ClassKey keyTurma = new ClassKey(infoTurma.getNome());
			//isto não está bem
			infoTurma.setNome(className);

			Object[] argsEditarTurma = { keyTurma, infoTurma };
			try {

				ServiceUtils.executeService(
					userView,
					"EditarTurma",
					argsEditarTurma);

			} catch (Exception e) {
				infoTurma.setNome(keyTurma.getNomeTurma());
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"existingClass",
					new ActionError("errors.existClass", className));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

		} else { /** starting editing */
			session.setAttribute(
				SessionConstants.CLASS_VIEW,
				getInfoTurma(SessionUtils.getUserView(request), className));
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
		InfoClass classView = getInfoTurma(userView, className);
		setLessonListToSession(request.getSession(), userView, className);
		HttpSession session = request.getSession();
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
		String className = getClassName(classForm);
		IUserView userView = SessionUtils.getUserView(request);

		ClassKey keyClass = new ClassKey(className);
		Object argsApagarTurma[] = { keyClass };
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

	private InfoClass getInfoTurma(IUserView userView, String className)
		throws Exception {

		ClassKey keyClass = new ClassKey(className);
		Object argsLerTurma[] = { keyClass };
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
		ClassKey keyClass = new ClassKey(className);
		Object argsApagarTurma[] = { keyClass };

		/** InfoLesson ArrayList */
		ArrayList lessonList =
			(ArrayList) ServiceUtils.executeService(
				userView,
				"LerAulasDeTurma",
				argsApagarTurma);

		session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessonList);

	}

}

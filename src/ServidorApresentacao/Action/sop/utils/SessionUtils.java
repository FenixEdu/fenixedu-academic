/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 4/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;

/**
 * @author jpvl
 */
public final class SessionUtils {

	public static CurricularYearAndSemesterAndInfoExecutionDegree getContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		return (
			CurricularYearAndSemesterAndInfoExecutionDegree) session
				.getAttribute(
			SessionConstants.CONTEXT_KEY);
	}

	public static IUserView getUserView(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		return (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	}

	public static List getExecutionCourses(HttpServletRequest request)
		throws Exception {

		HttpSession session = request.getSession(false);
		ArrayList infoCourseList = new ArrayList();

		// Nao verifica se ja existem em sessao porque podem 
		// ser de um periodo execucao diferente 

		IUserView userView = SessionUtils.getUserView(request);
		// Ler Disciplinas em Execucao
		Integer curricularYear =
			(Integer) session.getAttribute(
				SessionConstants.CURRICULAR_YEAR_KEY);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		Object[] args =
			{ infoExecutionDegree, infoExecutionPeriod, curricularYear };

		infoCourseList =
			(ArrayList) ServiceUtils.executeService(
				userView,
				"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
				args);

		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_LIST_KEY,
			infoCourseList);

		return infoCourseList;

	}

	/**
	 * Removes all attributes that start with prefix parameter. 
	 * It uses case-sensitive search.
	 */
	public static void removeAttributtes(HttpSession session, String prefix) {
		Enumeration attNames = session.getAttributeNames();
		Vector toRemoveAtts = new Vector();
		while (attNames.hasMoreElements()) {
			String attName = (String) attNames.nextElement();
			if (attName.startsWith(prefix)) {
				toRemoveAtts.add(attName);
			}
		}
		for (int i = 0; i < toRemoveAtts.size(); i++) {
			session.removeAttribute((String) toRemoveAtts.elementAt(i));
		}
	}
	/**
	 * @deprecated
	 * 
	 * @param request
	 * @param mapping
	 * @throws FenixActionException
	 */
	public static void validSessionVerification(
		HttpServletRequest request,
		ActionMapping mapping)
		throws FenixActionException {

		HttpSession session = request.getSession(false);

		if (session == null
			|| request.getSession(false).getAttribute(
				SessionConstants.SESSION_IS_VALID)
				== null) {

			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("error.invalid.session");
			errors.add("error.invalid.session", error);
			request.setAttribute(Globals.ERROR_KEY, errors);
			throw new InvalidSessionActionException();
		}

	}

}

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

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import ServidorAplicacao.IUserView;

/**
 * @author jpvl
 */
public final class SessionUtils {

	public static CurricularYearAndSemesterAndInfoExecutionDegree getContext(HttpServletRequest request) {
		HttpSession session = request.getSession();

		return (
			CurricularYearAndSemesterAndInfoExecutionDegree) session
				.getAttribute(
			SessionConstants.CONTEXT_KEY);
	}

	public static IUserView getUserView(HttpServletRequest request) {
		HttpSession session = request.getSession();

		return (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	}

	public static List getExecutionCourses(
		HttpServletRequest request,
		CurricularYearAndSemesterAndInfoExecutionDegree context)
		throws Exception {

		HttpSession session = request.getSession();
		ArrayList infoCourseList =
			(ArrayList) session.getAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY);
		if (infoCourseList == null) {

			IUserView userView = SessionUtils.getUserView(request);
			// Ler Disciplinas em Execucao
			Object[] args = { context };

			infoCourseList =
				(ArrayList) ServiceUtils.executeService(
					userView,
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					args);
			session.setAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY,
				infoCourseList);
		}
		return infoCourseList;

	}
	/**
	 * Removes all attributes that start with prefix parameter. 
	 * It uses case-sensitive search.
	 */
	public static void removeAttributtes(HttpSession session, String prefix){
		Enumeration attNames = session.getAttributeNames();
		Vector toRemoveAtts = new Vector();
		while (attNames.hasMoreElements()){
			String attName = (String) attNames.nextElement();
			if (attName.startsWith(prefix)){
				toRemoveAtts.add(attName);
			}
		}
		for (int i = 0; i < toRemoveAtts.size(); i++) {
			session.removeAttribute((String)toRemoveAtts.elementAt(i));		
		}
	}

}

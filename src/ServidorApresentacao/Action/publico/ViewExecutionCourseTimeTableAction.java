/*
 * Created on 24/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ViewExecutionCourseTimeTableAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		InfoExecutionCourse infoExecCourse =
			(InfoExecutionCourse) RequestUtils.getExecutionCourseFromRequest(
				request);
		
		HttpSession session = request.getSession(false);
		GestorServicos gestor = GestorServicos.manager();
		// Read list of Lessons to show execution course schedule.
		Object argsReadLessonsOfExecutionCours[] = { infoExecCourse };
		List infoLessons =
			(List) gestor.executar(
				null,
				"LerAulasDeDisciplinaExecucao",
				argsReadLessonsOfExecutionCours);

		if (infoLessons != null) {
			session.setAttribute(SessionConstants.LESSON_LIST_ATT, infoLessons);
		}
		System.out.println(infoLessons);
		return mapping.findForward("Sucess");
	}

}

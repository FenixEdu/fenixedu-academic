/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author João Mota
 *
 */
public class ViewShiftTimeTableAction extends Action {

	/**
	 * Constructor for ViewClassTimeTableAction.
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		String shiftName = request.getParameter("shiftName");

		if (shiftName == null)
			return mapping.getInputForward();
		HttpSession session = request.getSession(true);
		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) RequestUtils.getExecutionCourseFromRequest(
				request);

		Object[] args = { new ShiftKey(shiftName, infoExecutionCourse)};
		List lessons =
			(List) ServiceUtils.executeService(null, "LerAulasDeTurno", args);

		GestorServicos manager = GestorServicos.manager();

		Object argsReadCurricularCourseListOfExecutionCourse[] =
			{ infoExecutionCourse };
		List infoCurricularCourses =
			(List) manager.executar(
				null,
				"ReadCurricularCourseListOfExecutionCourse",
				argsReadCurricularCourseListOfExecutionCourse);

		if (infoCurricularCourses != null
			&& !infoCurricularCourses.isEmpty()) {
			request.setAttribute(
				"publico.infoCurricularCourses",
				infoCurricularCourses);
		}

		InfoShift shiftView = new InfoShift();
		shiftView.setNome(shiftName);
		request.setAttribute("shift", shiftView);
		request.setAttribute("lessonList", lessons);
		InfoSite site = RequestUtils.getSiteFromRequest(request);
		RequestUtils.setExecutionCourseToRequest(request, infoExecutionCourse);
		RequestUtils.setSectionsToRequest(request, site);
		RequestUtils.setSectionToRequest(request);

		return mapping.findForward("Sucess");
	}
}

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
import DataBeans.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

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

		//get the required data from session/request
		HttpSession session = request.getSession(true);

		InfoSite infoSite = RequestUtils.getSiteFromAnyScope(request);

		InfoExecutionCourse infoExecCourse = infoSite.getInfoExecutionCourse();

		//execute action main service(s)	
		GestorServicos gestor = GestorServicos.manager();
		// Read list of Lessons to show execution course schedule.
		Object argsReadLessonsOfExecutionCours[] = { infoExecCourse };
		List infoLessons =
			(List) gestor.executar(
				null,
				"LerAulasDeDisciplinaExecucao",
				argsReadLessonsOfExecutionCours);

		//place in session/request data required for jsp display
		if (infoLessons != null) {
			request.setAttribute("lessonList", infoLessons);
		}

		Object argsReadCurricularCourseListOfExecutionCourse[] =
			{ infoSite.getInfoExecutionCourse()};
		List infoCurricularCourses =
			(List) gestor.executar(
				null,
				"ReadCurricularCourseListOfExecutionCourse",
				argsReadCurricularCourseListOfExecutionCourse);

		if (infoCurricularCourses != null
			&& !infoCurricularCourses.isEmpty()) {
			request.setAttribute(
				"publico.infoCurricularCourses",
				infoCurricularCourses);
		}

		request.setAttribute(
			"exeCourse.theo",
			infoExecCourse.getTheoreticalHours());
		request.setAttribute(
			"exeCourse.prat",
			infoExecCourse.getPraticalHours());
		request.setAttribute(
			"exeCourse.theoPrat",
			infoExecCourse.getTheoPratHours());
		request.setAttribute("exeCourse.lab", infoExecCourse.getLabHours());

		RequestUtils.setExecutionCourseToRequest(request, infoExecCourse);
		RequestUtils.setSiteFirstPageToRequest(request, infoSite);
		RequestUtils.setSectionsToRequest(request, infoSite);
		RequestUtils.setSectionToRequest(request);

		return mapping.findForward("Sucess");
	}

}

package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

/**
 * @author ep15
 * @author Ivo Brandão
 */
public class AccessAnnouncementManagementAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(true);

		InfoSite infoSite = RequestUtils.getSiteFromRequest(request);

		Object args[] = new Object[1];
		args[0] = infoSite;

		GestorServicos manager = GestorServicos.manager();

		List announcements =
			(List) manager.executar(null, "ReadAnnouncements", args);

		//put new announcement list
		Collections.sort(announcements);
		request.setAttribute("announcementList", announcements);
		Object argsReadCurricularCourseListOfExecutionCourse[] =
			{ infoSite.getInfoExecutionCourse()};
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
		RequestUtils.setExecutionCourseToRequest(
			request,
			infoSite.getInfoExecutionCourse());
		RequestUtils.setSiteFirstPageToRequest(request, infoSite);
		RequestUtils.setSectionsToRequest(request, infoSite);
		RequestUtils.setSectionToRequest(request);
		return mapping.findForward("Sucess");
	}
}
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

import DataBeans.gesdis.InfoCurriculum;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ViewProgramAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(true);
		try {

			InfoSite infoSite = RequestUtils.getSiteFromAnyScope(request);

			Object args[] = { infoSite.getInfoExecutionCourse()};
			GestorServicos serviceManager = GestorServicos.manager();
			InfoCurriculum curriculumView =
				(InfoCurriculum) serviceManager.executar(
					null,
					"ReadCurriculum",
					args);

			GestorServicos manager = GestorServicos.manager();

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

			if (curriculumView != null) {

				request.setAttribute("program", curriculumView.getProgram());

			}
			RequestUtils.setExecutionCourseToRequest(
				request,
				infoSite.getInfoExecutionCourse());
			RequestUtils.setSectionsToRequest(request, infoSite);
			RequestUtils.setSectionToRequest(request);
			return mapping.findForward("viewProgram");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

	}

}

/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSection;
import DataBeans.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author lmac2
 */

public class ViewSectionAction extends FenixAction {
	/*
	 * Created on 7/Abr/2003
	 *
	 * To change this generated comment go to 
	 * Window>Preferences>Java>Code Generation>Code Template
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(true);

		String indexString = (String) request.getParameter("index");
		Integer index = new Integer(indexString);

		InfoSite infoSite = RequestUtils.getSiteFromRequest(request);

		List sections = null;
		try {

			Object[] args = { infoSite };
			sections =
				(List) ServiceUtils.executeService(null, "ReadSections", args);

			Collections.sort(sections);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
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

		InfoSection infoSection = (InfoSection) sections.get(index.intValue());

		request.setAttribute("infoSection", infoSection);
		request.setAttribute("sections", sections);

		GestorServicos gestor = GestorServicos.manager();

		Object argsViewSection[] = { infoSection };

		List infoItems =
			(List) gestor.executar(null, "ReadItems", argsViewSection);

		request.setAttribute("itemList", infoItems);

		RequestUtils.setExecutionCourseToRequest(
			request,
			infoSite.getInfoExecutionCourse());
		return mapping.findForward("Sucess");

	}

}

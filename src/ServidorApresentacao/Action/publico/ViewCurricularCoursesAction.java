/*
 * Created on 9/Abr/2003
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

import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ViewCurricularCoursesAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,  
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			HttpSession session = request.getSession(true);
			
			InfoSite site =RequestUtils.getSiteFromRequest(request);
			GestorServicos gestor = GestorServicos.manager();

//			Read associated curricular courses to display curricular course information.
					 Object argsReadCurricularCourseListOfExecutionCourse[] =
						 { site.getInfoExecutionCourse() };
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
			
					RequestUtils.setExecutionCourseToRequest(request,site.getInfoExecutionCourse());
					RequestUtils.setSectionsToRequest(request,site);
					RequestUtils.setSectionToRequest(request);

			return mapping.findForward("sucess");
		}

}

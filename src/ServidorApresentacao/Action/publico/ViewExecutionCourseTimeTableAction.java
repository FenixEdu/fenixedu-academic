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
import DataBeans.gesdis.InfoSite;
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

//get the required data from session/request
		HttpSession session = request.getSession();

		InfoSite infoSite = RequestUtils.getSiteFromAnyScope(request);
		
		InfoExecutionCourse infoExecCourse =infoSite.getInfoExecutionCourse();
		
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
			session.setAttribute(SessionConstants.LESSON_LIST_ATT, infoLessons);
		}
		
		
//		exeCourse.theo -> executionCourse theoretical hours
//		exeCourse.prat -> executionCourse pratical hours
//		exeCourse.theoPrat -> executionCourse theoretical-pratical hours
//		exeCourse.lab -> executionCourse lab hours
		request.setAttribute("exeCourse.theo",infoExecCourse.getTheoreticalHours());
		request.setAttribute("exeCourse.prat",infoExecCourse.getPraticalHours());
		request.setAttribute("exeCourse.theoPrat",infoExecCourse.getTheoPratHours());
		request.setAttribute("exeCourse.lab",infoExecCourse.getLabHours());

//TODO: change this to request 
		session.setAttribute(SessionConstants.INFO_SITE,infoSite);		
		
		return mapping.findForward("Sucess");
	}

}

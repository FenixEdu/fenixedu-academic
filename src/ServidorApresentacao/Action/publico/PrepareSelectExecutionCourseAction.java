package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(true);

		GestorServicos gestor = GestorServicos.manager();

		InfoExecutionCourse executionCourse = new InfoExecutionCourse();

		InfoExecutionPeriod infoExecutionPeriod =
			RequestUtils.getExecutionPeriodFromRequest(request);

		InfoExecutionDegree infoExecutionDegree =
			RequestUtils.getExecutionDegreeFromRequest(
				request,
				infoExecutionPeriod.getInfoExecutionYear());
				
		executionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
		
		Integer curricularYear =
			(Integer) request.getAttribute("curYear");
		
		
		Object argsSelectExecutionCourse[] =
			{ infoExecutionDegree, infoExecutionPeriod, curricularYear };

		List infoExecutionCourses;
		try {
			infoExecutionCourses =
				(List) gestor.executar(
					null,
					"SelectExecutionCourse",
					argsSelectExecutionCourse);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		RequestUtils.setExecutionPeriodToRequest(request,infoExecutionPeriod);
		request.setAttribute("exeCourseList", infoExecutionCourses);
		return mapping.findForward("sucess");
	}

}

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
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		

		HttpSession sessao = request.getSession(false);
		sessao.removeAttribute(SessionConstants.INFO_SECTION);
		if (sessao != null) {
			
			GestorServicos gestor = GestorServicos.manager();

			InfoExecutionCourse executionCourse = new InfoExecutionCourse();
			
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) sessao.getAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY);
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) sessao.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);							
			executionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
			Integer curricularYear = (Integer) sessao.getAttribute(SessionConstants.CURRICULAR_YEAR_KEY);			
			Object argsSelectExecutionCourse[] = { infoExecutionDegree, infoExecutionPeriod, curricularYear};
			
			List infoExecutionCourses =
				(List) gestor.executar(null, "SelectExecutionCourse", argsSelectExecutionCourse);
		
			request.getSession(false).setAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY,
				infoExecutionCourses);
			return mapping.findForward("sucess");
		}
		else {
			throw new Exception();
					// nao ocorre... pedido passa pelo filtro Autorizacao
		}
	}

}

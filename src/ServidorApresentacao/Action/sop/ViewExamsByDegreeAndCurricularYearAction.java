package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewExamsByDegreeAndCurricularYearAction extends Action {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) request.getAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_KEY);

			Integer curricularYear =
				(Integer) request.getAttribute(
					SessionConstants.CURRICULAR_YEAR_KEY);

			Object[] args = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
			List infoExecutionCourseAndExamsList =
				(List) gestor.executar(
					userView, "ReadExamsByExecutionDegreeAndCurricularYear", args);


			if (infoExecutionCourseAndExamsList != null
				&& infoExecutionCourseAndExamsList.isEmpty()) {
				request.removeAttribute(SessionConstants.INFO_EXAMS_KEY);
			} else {
				request.setAttribute(
					SessionConstants.INFO_EXAMS_KEY,
					infoExecutionCourseAndExamsList);
			}

			return mapping.findForward("Sucess");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}

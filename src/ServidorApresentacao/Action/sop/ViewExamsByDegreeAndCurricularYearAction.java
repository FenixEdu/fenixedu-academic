package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewExamsByDegreeAndCurricularYearAction extends FenixExecutionDegreeAndCurricularYearContextAction {
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
					SessionConstants.EXECUTION_PERIOD);
			System.out.println("infoExecutionPeriod=" + infoExecutionPeriod);
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) request.getAttribute(
					SessionConstants.EXECUTION_DEGREE);
			System.out.println("infoExecutionDegree=" + infoExecutionDegree);

			Integer curricularYear =
				(Integer) request.getAttribute(
					SessionConstants.CURRICULAR_YEAR_KEY);
			request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
			ContextUtils.setCurricularYearContext(request);

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

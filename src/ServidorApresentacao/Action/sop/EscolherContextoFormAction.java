package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 */
public class EscolherContextoFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);
		DynaActionForm escolherContextoForm = (DynaActionForm) form;

		HttpSession session = request.getSession(false);

		SessionUtils.removeAttributtes(
			session,
			SessionConstants.CONTEXT_PREFIX);

		if (session != null) {
			Integer semestre = null;
			/* TODO: add to the executionPeriod functions to return the period in terms of integers */
			/* TODO: Clean semestre from session */
			if (((InfoExecutionPeriod)session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY)).getName().equals("1º Semestre")) {
				 semestre=new Integer(1);
			} else {
				semestre=new Integer(2);
			}
			
			
			Integer anoCurricular =
				(Integer) escolherContextoForm.get("anoCurricular");

			int index = Integer.parseInt((String)escolherContextoForm.get("index"));

			IUserView userView = (IUserView) session.getAttribute("UserView");
			

			session.setAttribute("anoCurricular", anoCurricular);
			session.setAttribute("semestre", semestre);

			
			List infoExecutionDegreeList = (List) session.getAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);
			
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(index);
			
			if (infoExecutionDegree != null) {
				CurricularYearAndSemesterAndInfoExecutionDegree cYSiED =
					new CurricularYearAndSemesterAndInfoExecutionDegree(
						anoCurricular,
						semestre,
						infoExecutionDegree);
				session.setAttribute(SessionConstants.CONTEXT_KEY, cYSiED);
				
				session.setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, anoCurricular);
				session.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);

			} else {
				session.removeAttribute(SessionConstants.CONTEXT_KEY);
				session.removeAttribute(SessionConstants.CURRICULAR_YEAR_KEY);
				session.removeAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY);
				return mapping.findForward("Licenciatura execucao inexistente");
			}
			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}

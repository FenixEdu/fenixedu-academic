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
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.FenixAction;
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
		DynaActionForm escolherContextoForm = (DynaActionForm) form;

		HttpSession session = request.getSession(false);

		SessionUtils.removeAttributtes(
			session,
			SessionConstants.CONTEXT_PREFIX);

		if (session != null) {
			/* :FIXME: get semestre with executionPeriod */
			Integer semestre = new Integer(2); 
			
			//(Integer) escolherContextoForm.get("semestre");
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
				return mapping.findForward("Licenciatura execucao inexistente");
			}

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}

package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoClass;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class ViewClassesAction extends FenixAction {

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			InfoClass infoClass = new InfoClass();
			
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY); 
			
			
			CurricularYearAndSemesterAndInfoExecutionDegree ctx =
				(CurricularYearAndSemesterAndInfoExecutionDegree) request
					.getSession()
					.getAttribute(SessionConstants.CONTEXT_KEY);
			infoClass.setAnoCurricular(ctx.getAnoCurricular());
			
			infoClass.setInfoExecutionDegree(infoExecutionDegree);
			infoClass.setInfoExecutionPeriod(infoExecutionPeriod);

			Object argsSelectClasses[] = { infoClass };
			List infoClasses =
				(List) gestor.executar(
					null,
					"SelectClasses",
					argsSelectClasses);

			if (infoClasses != null && infoClasses.isEmpty()) {
				session.setAttribute("publico.infoClasses", null);
			} else {
				session.setAttribute("publico.infoClasses", infoClasses);
			}
			return mapping.findForward("Sucess");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}

}

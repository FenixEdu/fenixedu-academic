package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class ViewShiftsFormAction extends FenixAction {

	/**
		 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			GestorServicos gestor = GestorServicos.manager();
			InfoShift infoShift = new InfoShift();
		
			DynaActionForm courseForm = (DynaActionForm) form;
			
			
			
			InfoExecutionCourse executionCourse =new InfoExecutionCourse();
			
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) sessao.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			
			executionCourse.setSigla((String) courseForm.get("courseInitials"));
			executionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
			
			infoShift.setInfoDisciplinaExecucao(executionCourse);
			
			sessao.setAttribute(SessionConstants.EXECUTION_COURSE_KEY, executionCourse);
			
								
			Object argsSelectShifts[] = { infoShift	};
			List infoShifts =
				(List) gestor.executar(null, "SelectShifts", argsSelectShifts);

			if (infoShifts != null && !infoShifts.isEmpty())
				request.setAttribute("publico.infoShifts", infoShifts);
			return mapping.findForward("Sucess");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}

}
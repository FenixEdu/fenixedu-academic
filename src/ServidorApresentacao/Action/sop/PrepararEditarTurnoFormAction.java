package ServidorApresentacao.Action.sop;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.comparators.InfoShiftComparatorByLessonType;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao
	.Action
	.sop
	.base
	.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 * 
*/

public class PrepararEditarTurnoFormAction
	extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		super.execute(mapping, form, request, response);

		DynaActionForm editarTurnoForm = (DynaActionForm) form;
		HttpSession sessao = request.getSession(false);
		if (sessao != null) {

			DynaActionForm manipularTurnosForm =
				(DynaActionForm) request.getAttribute("manipularTurnosForm");
			Integer indexTurno =
				(Integer) manipularTurnosForm.get("indexTurno");

			IUserView userView = (IUserView) sessao.getAttribute("UserView");
			GestorServicos gestor = GestorServicos.manager();

			//ArrayList infoTurnos = (ArrayList) request.getAttribute("infoTurnosDeDisciplinaExecucao");
			InfoExecutionCourse iDE =
				(InfoExecutionCourse) request.getAttribute(
					SessionConstants.EXECUTION_COURSE);
			Object argsLerTurnosDeDisciplinaExecucao[] = { iDE };
			List infoTurnos =
				(List) gestor.executar(
					userView,
					"LerTurnosDeDisciplinaExecucao",
					argsLerTurnosDeDisciplinaExecucao);
			Collections.sort(infoTurnos, new InfoShiftComparatorByLessonType());

			InfoShift infoTurno =
				(InfoShift) infoTurnos.get(indexTurno.intValue());
			request.setAttribute("infoTurno", infoTurno);
			request.setAttribute(SessionConstants.SHIFT, infoTurno);
			editarTurnoForm.set("nome", infoTurno.getNome());
			editarTurnoForm.set("lotacao", infoTurno.getLotacao());
			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
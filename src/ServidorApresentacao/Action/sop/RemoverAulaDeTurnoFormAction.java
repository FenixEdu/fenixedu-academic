package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao
	.Action
	.sop
	.base
	.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & SaraRibeiro
 * 
 */
public class RemoverAulaDeTurnoFormAction
	extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		super.execute(mapping, form, request, response);

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {

			//DynaActionForm manipularTurnosForm =
			//	(DynaActionForm) request.getAttribute("manipularTurnosForm");
			DynaActionForm editarAulasDeTurnoForm =
				(DynaActionForm) request.getAttribute("editarAulasDeTurnoForm");

			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
			GestorServicos gestor = GestorServicos.manager();

			//Integer indexTurno =
			//	(Integer) manipularTurnosForm.get("indexTurno");
			//ArrayList infoTurnos =
			//	(ArrayList) request.getAttribute(
			//		"infoTurnosDeDisciplinaExecucao");
			//InfoShift infoTurno = (InfoShift) request.getAttribute(SessionConstants.SHIFT);
			Integer shiftOID =
				new Integer(request.getParameter(SessionConstants.SHIFT_OID));
			Object args[] = { shiftOID };
			InfoShift infoTurno =
				(InfoShift) gestor.executar(userView, "ReadShiftByOID", args);


			InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);

			System.out.println("infoTurno: " + infoTurno);
			System.out.println("infoExecutionCourse: " + infoExecutionCourse);

			//	(InfoShift) infoTurnos.get(indexTurno.intValue());

			Integer indexAula =
				(Integer) editarAulasDeTurnoForm.get("indexAula");

			//ArrayList infoAulas =
			//	(ArrayList) request.getAttribute("infoAulasDeTurno");
			Object argsLerAulasDeTurno[] =
				{
					 new ShiftKey(
						infoTurno.getNome(),
						infoExecutionCourse)};
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					userView,
					"LerAulasDeTurno",
					argsLerAulasDeTurno);

			System.out.println("infoAulas.size=" + infoAulas.size());

			InfoLesson infoLesson =
				(InfoLesson) infoAulas.get(indexAula.intValue());

			System.out.println("infoLesson=" + infoLesson);

			//request.removeAttribute("indexAula");
			Object argsRemoverAula[] = { infoLesson, infoTurno };
			//Boolean result =
			//	(Boolean)
			gestor.executar(
					userView,
					"RemoverAula",
					argsRemoverAula);

			//if (result != null && result.booleanValue()) {
			//	infoAulas.remove(indexAula.intValue());
			//	request.removeAttribute("infoAulasDeTurno");
			//	if (!infoAulas.isEmpty())
			//		request.setAttribute("infoAulasDeTurno", infoAulas);
			//}

			//request.removeAttribute("editarAulasDeTurnoForm");
			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}

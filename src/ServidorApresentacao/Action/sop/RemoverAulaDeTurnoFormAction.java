package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
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
				(InfoShift) ServiceManagerServiceFactory.executeService(userView, "ReadShiftByOID", args);


			InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);

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
				(ArrayList) ServiceManagerServiceFactory.executeService(
					userView,
					"LerAulasDeTurno",
					argsLerAulasDeTurno);

			InfoLesson infoLesson =
				(InfoLesson) infoAulas.get(indexAula.intValue());

			//request.removeAttribute("indexAula");
			Object argsRemoverAula[] = { infoLesson, infoTurno };
			//Boolean result =
			//	(Boolean)
			ServiceManagerServiceFactory.executeService(
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

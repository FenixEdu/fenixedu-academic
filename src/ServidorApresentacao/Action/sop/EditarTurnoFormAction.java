package ServidorApresentacao.Action.sop;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author tfc130
 */
public class EditarTurnoFormAction extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		super.execute(mapping, form, request, response);
			
		DynaActionForm editarTurnoForm = (DynaActionForm) form;
		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos gestor = GestorServicos.manager();

//			ArrayList infoTurnos =
//				(ArrayList) session.getAttribute(
//					"infoTurnosDeDisciplinaExecucao");

			Integer shiftOID = new Integer(request.getParameter("old_shift_oid"));

			//InfoShift infoTurnoAntigo =
			//	(InfoShift) request.getAttribute("infoTurno");

			Object args[] = { shiftOID };
			InfoShift infoTurnoAntigo = (InfoShift) gestor.executar(
					userView,
					"ReadShiftByOID",
					args);


			InfoShift infoTurnoNovo =
				new InfoShift(
					(String) editarTurnoForm.get("nome"),
					infoTurnoAntigo.getTipo(),
					(Integer) editarTurnoForm.get("lotacao"),
					infoTurnoAntigo.getInfoDisciplinaExecucao());

			Object argsEditarTurno[] = { infoTurnoAntigo, infoTurnoNovo };

			ActionErrors actionErrors = null;
			try {
					gestor.executar(
						userView,
						"EditarTurno",
						argsEditarTurno);
			} catch (ExistingServiceException e) {
//NOTE: Agora com as excepções este erro não é utilizado
//				actionErrors = new ActionErrors();
//				actionErrors.add(
//					"error.shift.duplicate",
//					new ActionError(
//						"error.shift.duplicate",
//						infoTurnoNovo.getNome(),
//						infoTurnoAntigo.getInfoDisciplinaExecucao().getNome()));
//				editarTurnoForm.set("nome", infoTurnoAntigo.getNome());
//				saveErrors(request, actionErrors);
				throw new ExistingActionException("O Turno", e);

			}

			if (actionErrors == null) {
//				infoTurnos.set(
//					infoTurnos.indexOf(infoTurnoAntigo),
//					infoTurnoNovo);
				//session.removeAttribute("indexTurno");
				return (mapping.findForward("Guardar"));
			} else {
				return mapping.getInputForward();
			}
		} else
			throw new FenixActionException();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}

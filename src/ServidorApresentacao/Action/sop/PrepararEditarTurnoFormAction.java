package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoShift;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
/**
@author tfc130
*/
public class PrepararEditarTurnoFormAction extends Action {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);
		DynaActionForm editarTurnoForm = (DynaActionForm) form;
		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			DynaActionForm manipularTurnosForm = (DynaActionForm) sessao.getAttribute("manipularTurnosForm");

			Integer indexTurno =(Integer) manipularTurnosForm.get("indexTurno");
			ArrayList infoTurnos = (ArrayList) sessao.getAttribute("infoTurnosDeDisciplinaExecucao");
			InfoShift infoTurno = (InfoShift) infoTurnos.get(indexTurno.intValue());
			sessao.setAttribute("infoTurno", infoTurno);

			editarTurnoForm.set("nome", infoTurno.getNome());
			editarTurnoForm.set("lotacao", infoTurno.getLotacao());

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
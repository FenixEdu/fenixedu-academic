package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.TipoAula;

/**
 * @author tfc130
 */
public class PrepararAulaFormAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			//        diasSemana.add(new LabelValueBean("escolher", ""));
			ArrayList diasSemana = Util.getDaysOfWeek();
			sessao.setAttribute("diasSemana", diasSemana);

			ArrayList tiposAula = new ArrayList();
			//        tiposAula.add(new LabelValueBean("escolher", ""));
			tiposAula.add(
				new LabelValueBean(
					"Teorica",
					(new Integer(TipoAula.TEORICA)).toString()));
			tiposAula.add(
				new LabelValueBean(
					"Pratica",
					(new Integer(TipoAula.PRATICA)).toString()));
			tiposAula.add(
				new LabelValueBean(
					"Teorico-Pratica",
					(new Integer(TipoAula.TEORICO_PRATICA)).toString()));
			tiposAula.add(
				new LabelValueBean(
					"Laboratorial",
					(new Integer(TipoAula.LABORATORIAL)).toString()));
			tiposAula.add(
				new LabelValueBean(
					"Dúvidas",
					(new Integer(TipoAula.DUVIDAS)).toString()));
			tiposAula.add(
				new LabelValueBean(
					"Reserva",
					(new Integer(TipoAula.RESERVA)).toString()));
			sessao.setAttribute("tiposAula", tiposAula);


			ArrayList horas = Util.getHours();
			sessao.setAttribute("horas", horas);

			ArrayList minutos = Util.getMinutes();
			sessao.setAttribute("minutos", minutos);

			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
			GestorServicos gestor = GestorServicos.manager();

			// Ler as Salas
			Object argsLerSalas[] = new Object[0];
			ArrayList infoSalas =
				(ArrayList) gestor.executar(userView, "LerSalas", argsLerSalas);

			//Collections.sort(infoSalas);

			ArrayList listaSalas = new ArrayList();
			listaSalas.add(new LabelValueBean("escolher", ""));
			for (int i = 0; i < infoSalas.size(); i++) {
				InfoRoom elem = (InfoRoom) infoSalas.get(i);
				listaSalas.add(
					new LabelValueBean(elem.getNome(), elem.getNome()));
			}
			sessao.setAttribute("listaSalas", listaSalas);
			sessao.setAttribute("listaInfoSalas", infoSalas);

			// Fim ler salas.

			// Ler Disciplinas em Execucao
			SessionUtils.getExecutionCourses(
				request,
				SessionUtils.getContext(request));

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}

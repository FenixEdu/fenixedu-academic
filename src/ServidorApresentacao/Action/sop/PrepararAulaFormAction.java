package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author tfc130d
 */
public class PrepararAulaFormAction
	extends FenixExecutionDegreeAndCurricularYearContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		super.execute(mapping, form, request, response);

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			//        diasSemana.add(new LabelValueBean("escolher", ""));
			ArrayList diasSemana = Util.getDaysOfWeek();
			request.setAttribute("diasSemana", diasSemana);

			RequestUtils.setLessonTypes(request);

			ArrayList horas = Util.getHours();
			request.setAttribute("horas", horas);

			ArrayList minutos = Util.getMinutes();
			request.setAttribute("minutos", minutos);

			//			// Ler as Salas
			//			Object argsLerSalas[] = new Object[0];
			//			ArrayList infoSalas =
			//				(ArrayList) gestor.executar(userView, "LerSalas", argsLerSalas);
			//
			//			//Collections.sort(infoSalas);
			//
			//			ArrayList listaSalas = new ArrayList();
			//			listaSalas.add(new LabelValueBean("escolher", ""));
			//			for (int i = 0; i < infoSalas.size(); i++) {
			//				InfoRoom elem = (InfoRoom) infoSalas.get(i);
			//				listaSalas.add(
			//					new LabelValueBean(elem.getNome(), elem.getNome()));
			//			}
			//			sessao.setAttribute("listaSalas", listaSalas);
			//			sessao.setAttribute("listaInfoSalas", infoSalas);

			// Fim ler salas.d

			// Ler Disciplinas em Execucao
			SessionUtils.getExecutionCourses(request);

			//sessao.removeAttribute(SessionConstants.EXECUTION_COURSE_KEY);
			//sessao.removeAttribute(SessionConstants.CLASS_VIEW);

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}

package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.ExecutionCourseKeyAndLessonType;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorApresentacao
	.Action
	.sop
	.base
	.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class PrepararAdicionarAulasDeTurnoFormAction
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
			IUserView userView = (IUserView) sessao.getAttribute("UserView");
			ArrayList infoAulasDeTurno =
				(ArrayList) request.getAttribute("infoAulasDeTurno");

			//InfoShift infoTurno = (InfoShift) request.getAttribute("infoTurno");
			InfoShift infoTurno =
				(InfoShift) request.getAttribute(SessionConstants.SHIFT);

			// Ler Aulas de Disciplina em Execucao e Tipo
			InfoExecutionCourse infoDisciplina =
				(InfoExecutionCourse) request.getAttribute(
					SessionConstants.EXECUTION_COURSE);
			ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao =
				new ExecutionCourseKeyAndLessonType(
					infoTurno.getTipo(),
					infoDisciplina.getSigla());

			Object argsLerAulasDeDisciplinaETipo[] =
				{ tipoAulaAndKeyDisciplinaExecucao, infoDisciplina };
			ArrayList infoAulasDeDisciplina =
				(ArrayList) ServiceManagerServiceFactory.executeService(
					userView,
					"LerAulasDeDisciplinaExecucaoETipo",
					argsLerAulasDeDisciplinaETipo);

			if (infoAulasDeTurno != null)
				infoAulasDeDisciplina.removeAll(infoAulasDeTurno);
			request.removeAttribute("infoAulasDeDisciplinaExecucao");
			if (!infoAulasDeDisciplina.isEmpty())
				request.setAttribute(
					"infoAulasDeDisciplinaExecucao",
					infoAulasDeDisciplina);

			return mapping.findForward("Sucesso");
		} 
			throw new Exception();
		
	}
}
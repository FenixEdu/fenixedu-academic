package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ExecutionCourseKeyAndLessonType;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;

/**
 * @author tfc130
 */
public class PrepararAdicionarAulasDeTurnoFormAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
        IUserView userView = (IUserView) sessao.getAttribute("UserView");
        GestorServicos gestor = GestorServicos.manager();
        ArrayList infoAulasDeTurno = (ArrayList) sessao.getAttribute("infoAulasDeTurno");
		InfoShift infoTurno = (InfoShift) sessao.getAttribute("infoTurno");

		// Ler Aulas de Disciplina em Execucao e Tipo
		InfoExecutionCourse infoDisciplina = (InfoExecutionCourse) sessao.getAttribute("infoDisciplinaExecucao");
		ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao = new ExecutionCourseKeyAndLessonType(infoTurno.getTipo(),infoDisciplina.getSigla());
		
	    Object argsLerAulasDeDisciplinaETipo[] = { tipoAulaAndKeyDisciplinaExecucao, infoDisciplina };
        ArrayList infoAulasDeDisciplina = (ArrayList) gestor.executar(userView, "LerAulasDeDisciplinaExecucaoETipo", argsLerAulasDeDisciplinaETipo);

		if (infoAulasDeTurno != null)
			infoAulasDeDisciplina.removeAll(infoAulasDeTurno);
		sessao.removeAttribute("infoAulasDeDisciplinaExecucao");
		if (!infoAulasDeDisciplina.isEmpty())
	     	sessao.setAttribute("infoAulasDeDisciplinaExecucao", infoAulasDeDisciplina);

      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}
package ServidorApresentacao.Action.sop;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.comparators.InfoShiftComparatorByLessonType;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
@author tfc130
*/

public class PrepararManipularTurnosFormAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
        IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
        GestorServicos gestor = GestorServicos.manager();
        
		// Ler Turnos de Disciplinas em Execucao
        InfoExecutionCourse iDE = (InfoExecutionCourse) sessao.getAttribute(SessionConstants.EXECUTION_COURSE_KEY);
        Object argsLerTurnosDeDisciplinaExecucao[] = { iDE };
		
		List infoTurnosDeDisciplinaExecucao = (List) gestor.executar(userView, "LerTurnosDeDisciplinaExecucao", argsLerTurnosDeDisciplinaExecucao);
        
		Collections.sort(infoTurnosDeDisciplinaExecucao, new InfoShiftComparatorByLessonType());
		
		sessao.removeAttribute(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY);
		if (infoTurnosDeDisciplinaExecucao.size() > 0)
			sessao.setAttribute(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY, infoTurnosDeDisciplinaExecucao);
			
		sessao.removeAttribute(SessionConstants.CLASS_VIEW);
      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}
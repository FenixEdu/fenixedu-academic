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
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author tfc130
 */
public class ApagarTurnoFormAction extends FenixAction {
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
    
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
    	
      DynaActionForm manipularTurnosForm = (DynaActionForm) sessao.getAttribute("manipularTurnosForm");
    
      IUserView userView = (IUserView) sessao.getAttribute("UserView");
      GestorServicos gestor = GestorServicos.manager();
      Integer indexTurno = (Integer) manipularTurnosForm.get("indexTurno");
      ArrayList infoTurnos = (ArrayList) sessao.getAttribute("infoTurnosDeDisciplinaExecucao");
      InfoShift infoTurno = (InfoShift) infoTurnos.get(indexTurno.intValue());

	  sessao.removeAttribute("indexTurno");
	  InfoExecutionCourse IEC = (InfoExecutionCourse) sessao.getAttribute(SessionConstants.EXECUTION_COURSE_KEY);
	  
	  
	  Object argsApagarTurno[] = { new ShiftKey(infoTurno.getNome(),IEC),IEC};
      Boolean result = (Boolean) gestor.executar(userView, "ApagarTurno", argsApagarTurno);
	  
	  if (result != null && result.booleanValue()) {
	  	infoTurnos.remove(indexTurno.intValue());
	  	sessao.removeAttribute("infoTurnosDeDisciplinaExecucao");
	  	if (!infoTurnos.isEmpty())
	  		sessao.setAttribute("infoTurnosDeDisciplinaExecucao",infoTurnos);
	  }
	  
      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}

package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author tfc130
 */
public class RemoverAulaDeTurnoFormAction extends FenixAction {
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
    	
      DynaActionForm manipularTurnosForm = (DynaActionForm) request.getAttribute("manipularTurnosForm");
      DynaActionForm editarAulasDeTurnoForm = (DynaActionForm) request.getAttribute("editarAulasDeTurnoForm");
    
      IUserView userView = (IUserView) request.getAttribute(SessionConstants.U_VIEW);
      GestorServicos gestor = GestorServicos.manager();
      
      Integer indexTurno = (Integer) manipularTurnosForm.get("indexTurno");
      ArrayList infoTurnos = (ArrayList) request.getAttribute("infoTurnosDeDisciplinaExecucao");
      InfoShift infoTurno = (InfoShift) infoTurnos.get(indexTurno.intValue());

      Integer indexAula = (Integer) editarAulasDeTurnoForm.get("indexAula");
      ArrayList infoAulas = (ArrayList) request.getAttribute("infoAulasDeTurno");
      
      InfoLesson infoLesson = (InfoLesson) infoAulas.get(indexAula.intValue());      
      
	  request.removeAttribute("indexAula");

	  Object argsRemoverAula[] = { infoLesson, infoTurno};
      Boolean result = (Boolean) gestor.executar(userView, "RemoverAula", argsRemoverAula);
	  
	  if (result != null && result.booleanValue()) {
	  	infoAulas.remove(indexAula.intValue());
	  	request.removeAttribute("infoAulasDeTurno");
	  	if (!infoAulas.isEmpty())
	  		request.setAttribute("infoAulasDeTurno",infoAulas);
	  }
	  
	  request.removeAttribute("editarAulasDeTurnoForm");
      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}

package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoLesson;
import ServidorApresentacao.Action.base.FenixAction;


/**
 * @author tfc130
 */
public class PrepararEditarAulaFormAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		
   	DynaActionForm editarAulaForm = (DynaActionForm) form;
    
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
    	
      DynaActionForm manipularAulasForm = (DynaActionForm) request.getAttribute("manipularAulasForm");
    
      Integer indexAula = (Integer) manipularAulasForm.get("indexAula");
      ArrayList infoAulas = (ArrayList) request.getAttribute("listaAulas");
      InfoLesson infoAula = (InfoLesson) infoAulas.get(indexAula.intValue());
      
	  //sessao.removeAttribute("indexAula");
	  request.setAttribute("infoAula", infoAula);
	
	  editarAulaForm.set("diaSemana", String.valueOf(infoAula.getWeekDay()));
	  editarAulaForm.set("horaInicio", String.valueOf(infoAula.getInicio().get(Calendar.HOUR_OF_DAY)));
	  editarAulaForm.set("minutosInicio", String.valueOf(infoAula.getInicio().get(Calendar.MINUTE)));
	  editarAulaForm.set("horaFim", String.valueOf(infoAula.getFim().get(Calendar.HOUR_OF_DAY)));
	  editarAulaForm.set("minutosFim", String.valueOf(infoAula.getFim().get(Calendar.MINUTE)));
	  editarAulaForm.set("tipoAula", String.valueOf(infoAula.getTipo().getTipo().intValue()));
	  editarAulaForm.set("nomeSala", infoAula.getInfoSala().getNome());

      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}

package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.TipoSala;

/**
 * @author tfc130
 */
public class PrepareConsultRoomsFormAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
      	
	
    
		HttpSession session = request.getSession(true);
    if (session != null) {
    	//TODO: No futuro, os edificios devem ser lidos da BD 
        List buildings = Util.readExistingBuldings("*",null);
        request.setAttribute("publico.buildings", buildings);

        //TODO: No futuro, os tipos de salas devem ser lidos da BD 
        ArrayList types = new ArrayList();
        types.add(new LabelValueBean("*", null));
        types.add(new LabelValueBean("Anfiteatro", (new Integer(TipoSala.ANFITEATRO)).toString()));
        types.add(new LabelValueBean("Laboratório", (new Integer(TipoSala.LABORATORIO)).toString()));
        types.add(new LabelValueBean("Plana", (new Integer(TipoSala.PLANA)).toString()));
        request.setAttribute("publico.types", types);
        
        // keep selected executionPeriod in request
        InfoExecutionPeriod infoExecutionPeriod = RequestUtils.setExecutionContext(request);
        RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
        
      return mapping.findForward("Sucess");
    } else
      throw new Exception(); 
  }
}

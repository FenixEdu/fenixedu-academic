package ServidorApresentacao.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;

import ServidorApresentacao.Action.ExcepcaoSessaoInexistente;

/**
 * @author jorge
 */
public abstract class FenixAction extends Action {
	

/*  public abstract ActionForward execute(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response)
      throws Exception;*/
      
  protected HttpSession getSession(HttpServletRequest request) 
      throws ExcepcaoSessaoInexistente {
    HttpSession result = request.getSession(false);
    if (result == null)
      throw new ExcepcaoSessaoInexistente();
    
    return result;
  }
}

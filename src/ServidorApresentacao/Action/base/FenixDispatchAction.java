package ServidorApresentacao.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.actions.DispatchAction;

import ServidorApresentacao.Action.ExcepcaoSessaoInexistente;

/**
 * @author joao
 */
public abstract class FenixDispatchAction extends DispatchAction {

  protected HttpSession getSession(HttpServletRequest request) 
      throws ExcepcaoSessaoInexistente {
    HttpSession result = request.getSession(false);
    if (result == null)
      throw new ExcepcaoSessaoInexistente();
    
    return result;
  }
}

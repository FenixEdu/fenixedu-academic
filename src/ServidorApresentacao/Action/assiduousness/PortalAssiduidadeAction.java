package ServidorApresentacao.Action.assiduousness;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoPortalAssiduidade;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroPortalAssiduidade;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import constants.assiduousness.Constants;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public final class PortalAssiduidadeAction extends Action {
  
  public ActionForward execute(ActionMapping mapping,
  ActionForm form,
  HttpServletRequest request,
  HttpServletResponse response)
  throws IOException, ServletException {
    HttpSession session = request.getSession();
    ActionErrors errors = new ActionErrors();
     
    Pessoa pessoa = (Pessoa)session.getAttribute(Constants.USER_KEY);

    
    ServicoAutorizacaoPortalAssiduidade servicoAutorizacaoPortalAssiduidade = 
    new ServicoAutorizacaoPortalAssiduidade(pessoa);
    ServicoSeguroPortalAssiduidade servicoSeguroPortalAssiduidade =
    new ServicoSeguroPortalAssiduidade(servicoAutorizacaoPortalAssiduidade);
    
    try {
      Executor.getInstance().doIt(servicoSeguroPortalAssiduidade);
      
    } catch (NotExecuteException nee) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError(nee.getMessage()));
    } catch (PersistenceException pe) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.server"));
    } finally {
      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        return (new ActionForward(mapping.getInput()));
      }
    }    
    return (mapping.findForward("PortalAssiduidade"));
  }
}
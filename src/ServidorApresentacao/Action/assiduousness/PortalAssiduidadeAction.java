package ServidorApresentacao.Action.assiduousness;

import java.io.IOException;
import java.util.Locale;

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
import org.apache.struts.util.MessageResources;

import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.NotAuthorizeException;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoPortalAssiduidade;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroPortalAssiduidade;
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
		System.out.println("--->No PortalAssiduidadeAction...");
    Locale locale = getLocale(request);
    
    HttpSession session = request.getSession();
    MessageResources messages = getResources(request);
    ActionErrors errors = new ActionErrors();
     
    Pessoa pessoa = (Pessoa)session.getAttribute(Constants.USER_KEY);
    if(pessoa == null){
    	System.out.println("Pessoa da sessao a null");
    }
    
    ServicoAutorizacaoPortalAssiduidade servicoAutorizacaoPortalAssiduidade = 
    new ServicoAutorizacaoPortalAssiduidade(pessoa);
    ServicoSeguroPortalAssiduidade servicoSeguroPortalAssiduidade =
    new ServicoSeguroPortalAssiduidade(servicoAutorizacaoPortalAssiduidade);
    
    try {
      Executor.getInstance().doIt(servicoSeguroPortalAssiduidade);
      
    } catch (NotAuthorizeException nae) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError(nae.getMessage()));
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
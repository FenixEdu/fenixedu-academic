package ServidorApresentacao.Action.person;

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

import constants.assiduousness.Constants;

import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.NotAuthorizeException;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.person.ServicoAutorizacaoAlterarPessoa;
import ServidorAplicacao.Servico.person.ServicoAutorizacaoLerPessoa;
import ServidorAplicacao.Servico.person.ServicoSeguroAlterarPessoa;
import ServidorAplicacao.Servico.person.ServicoSeguroLerPessoa;
import ServidorApresentacao.formbeans.person.AlterarPasswordForm;

/**
 *
 * @author  Fernanda Quiterio 6 Tania Pousao
 */
public class AlterarPasswordAction extends Action {
  
  public ActionForward execute(ActionMapping mapping,
  ActionForm form,
  HttpServletRequest request,
  HttpServletResponse response)
  throws IOException, ServletException {
    
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);
    HttpSession session = request.getSession();
    ActionErrors errors = new ActionErrors();
        
    if (isCancelled(request)) {
      if (mapping.getAttribute() != null)
        session.removeAttribute(mapping.getAttribute());
      return (mapping.findForward("PortalAssiduidadeAction"));
    }
    
    String username = (String)session.getAttribute(Constants.USER_KEY);
    
    ServicoAutorizacaoLerPessoa servicoAutorizacaoLerPessoa =
    new ServicoAutorizacaoLerPessoa();
    ServicoSeguroLerPessoa servicoSeguroLerPessoa =
    new ServicoSeguroLerPessoa(servicoAutorizacaoLerPessoa, username);
    
    try {
      
      Executor.getInstance().doIt(servicoSeguroLerPessoa);
      
    } catch (NotAuthorizeException nae) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.authorization"));
    } catch (NotExecuteException nee) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.execution"));
    } catch (PersistenceException pe) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.server"));
    } finally {
      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        return (new ActionForward(mapping.getInput()));
      }
    }
    Pessoa pessoa = servicoSeguroLerPessoa.getPessoa();
    
    AlterarPasswordForm passwordForm = (AlterarPasswordForm) form;
    if (!passwordForm.getPasswordAntiga().equals(pessoa.getPassword())) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.password.invalida"));
      
      saveErrors(request, errors);
      return (new ActionForward(mapping.getInput()));
    }
    
    //alteracao da password
    pessoa.setPassword(passwordForm.getPasswordNova());
    
    ServicoAutorizacaoAlterarPessoa servicoAutorizacaoAlterarPessoa =
    new ServicoAutorizacaoAlterarPessoa();
    ServicoSeguroAlterarPessoa servicoSeguroAlterarPessoa =
    new ServicoSeguroAlterarPessoa(servicoAutorizacaoAlterarPessoa, pessoa);
    try {
      
      Executor.getInstance().doIt(servicoSeguroAlterarPessoa);
      
    } catch (NotAuthorizeException nae) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.authorization"));
    } catch (NotExecuteException nee) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.server"));
    } catch (PersistenceException pe) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.server"));
    } finally {
      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        saveToken(request);
        return (new ActionForward(mapping.getInput()));
      }
    }
    
    return (mapping.findForward("AlterarPasswordConfirmar"));
  }
}

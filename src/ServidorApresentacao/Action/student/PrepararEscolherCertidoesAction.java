package ServidorApresentacao.Action.student;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;


/**
 * @author Ricardo Nortadas
 */
public class PrepararEscolherCertidoesAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	  throws Exception {

	HttpSession sessao = request.getSession(false);
	if (sessao != null) {

		
		
		ArrayList certidoes = new ArrayList();

		certidoes.add(new LabelValueBean("Exemplo 1","1"));
		certidoes.add(new LabelValueBean("Exemplo 2","2"));
		certidoes.add(new LabelValueBean("Exemplo 3","3"));
		certidoes.add(new LabelValueBean("Exemplo 4","4"));
		certidoes.add(new LabelValueBean("Exemplo 5","5"));
		sessao.setAttribute("certidoes", certidoes);

		
	  return mapping.findForward("sucesso");
	} 
	  throw new Exception();  
		
  }
}
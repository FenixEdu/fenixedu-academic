package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorApresentacao.Action.FenixAction;

/**
 * @author Ricardo Nortadas
 */
public class EscolherCertidoesAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	  throws Exception {
	DynaActionForm escolherCertidaoForm = (DynaActionForm) form;

	HttpSession session = request.getSession(false);

	System.out.println("entrei no escolher certidoes");
	String result="Inexistente";
	String tipo="";
	
	if (session != null) {
		
		tipo =  (String) escolherCertidaoForm.get("certidao");

		int intTipo = Integer.parseInt(tipo);
		
		
		System.out.println("vou escrever o tipo");
		System.out.println(tipo);
		

			switch(intTipo){
				case 1:result="Tipo1";break;	
				case 2:result="Tipo2";break;
				case 3:result="Tipo3";break;
				case 4:result="Tipo4";break;
				case 5:result="Tipo5";break;
					
			}

		}
		
		session.setAttribute("Tipo", tipo);
		
		System.out.println("O tipo pedido é:"+result);
		return mapping.findForward(result);

	  }
}

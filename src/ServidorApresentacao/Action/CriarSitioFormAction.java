package ServidorApresentacao.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;

/**
 * @author jorge
 */
public class CriarSitioFormAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
    DynaActionForm criarSitioForm = (DynaActionForm) form;
    
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
      IUserView userView = (IUserView) sessao.getAttribute("UserView");
      GestorServicos gestor = GestorServicos.manager();
      Object argsCriarSitio[] = {criarSitioForm.get("nome"), 
                                 criarSitioForm.get("anoCurricular"),
                                 criarSitioForm.get("semestre"),
                                 criarSitioForm.get("curso"),
                                 criarSitioForm.get("departamento")};

      gestor.executar(userView, "CriarSitio", argsCriarSitio);
      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}

package ServidorApresentacao.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.SeccaoView;
import ServidorAplicacao.Servico.SitioView;

/**
 * @author jorge
 */
public class ObterSeccaoAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

    DynaActionForm dynamicForm = (DynaActionForm) form;

    HttpSession session = getSession(request);
    String nomeSeccao = (String) dynamicForm.get("nomeSeccao");
    if (nomeSeccao == null) {
      nomeSeccao = (String)request.getAttribute("nomeSeccao");
      if (nomeSeccao == null) {
        request.setAttribute("Seccao", null);
        return mapping.findForward("MostrarSeccao");
      }
    }
    
    Object argumentos[] = {((SitioView) session.getAttribute("Sitio")).getNome(),
                           nomeSeccao};

    GestorServicos gestor = GestorServicos.manager();
    SeccaoView seccao = (SeccaoView) gestor.executar(null, "ObterSeccao", argumentos);
    request.setAttribute("Seccao", seccao);
    return mapping.findForward("MostrarSeccao");
  }
}

package ServidorApresentacao.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.SitioView;

/**
 * @author jorge
 */
public class MostrarSitioAction extends FenixAction {
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

    DynaActionForm dynamicForm = (DynaActionForm) form;

    Object argumentos[] = {dynamicForm.get("nomeSitio")};

    GestorServicos gestor = GestorServicos.manager();
    SitioView sitio = (SitioView) gestor.executar(null, "ObterSitio", argumentos);
    request.setAttribute("Sitio", sitio);
    return mapping.findForward("MostrarSitio");
  }
}
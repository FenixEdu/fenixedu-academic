package ServidorApresentacao.Action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.GestorServicos;

/**
 * @author jorge
 */
public class ObterSitiosAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

    GestorServicos gestor = GestorServicos.manager();
    List sitios = (List) gestor.executar(null, "ObterSitios", null);
    request.setAttribute("Sitios", sitios);
    return mapping.findForward("MostrarSitios");
  }
}
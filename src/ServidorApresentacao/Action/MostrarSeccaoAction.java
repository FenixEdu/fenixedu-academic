package ServidorApresentacao.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.SeccaoView;

/**
 * @author jorge
 */
public class MostrarSeccaoAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

    DynaActionForm dynamicForm = (DynaActionForm) form;

    Object argumentos[] = {dynamicForm.get("nomeSitio"),
                           dynamicForm.get("nomeSeccao")};

    GestorServicos gestor = GestorServicos.manager();
    SeccaoView seccao = (SeccaoView) gestor.executar(null, "ObterSeccao", argumentos);
    request.setAttribute("Seccao", seccao);
    return mapping.findForward("MostrarSeccao");
  }
}

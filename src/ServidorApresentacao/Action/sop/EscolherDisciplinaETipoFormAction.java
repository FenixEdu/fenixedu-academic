package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorApresentacao.Action.base.FenixAction;
/**
 * @author tfc130
 */
public class EscolherDisciplinaETipoFormAction extends FenixAction
{
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        DynaActionForm escolherDisciplinaETipoForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(false);
        if (sessao != null)
        {
            ArrayList infoDisciplinasExecucao =
                (ArrayList) sessao.getAttribute("infoDisciplinasExecucao");
            int i =
                ((Integer) escolherDisciplinaETipoForm.get("indexDisciplinaExecucao")).intValue() - 1;
            sessao.setAttribute("infoDisciplinaExecucao", infoDisciplinasExecucao.get(i));
            return mapping.findForward("Sucesso");
        } else
            throw new Exception(); // nao ocorre... pedido passa pelo filtro Autorizacao 
    }

}

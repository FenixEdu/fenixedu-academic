package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;

/**
 * @author tfc130
 */
public class ManipularAulasFormAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            /* Obtem o parametro do submit que indica a operacao a realizar */

            if (request.getParameter("operation").equals("Editar Aula"))
                return (mapping.findForward("Editar"));

            else if (request.getParameter("operation").equals("Apagar Aula"))
                return (mapping.findForward("Apagar"));

            else if (request.getParameter("operation").equals("Voltar"))
                return (mapping.findForward("Voltar"));

            return (mapping.findForward("voltar"));

        }
        throw new Exception();

    }
}
package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class EscolherDisciplinaETipoFormAction extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm escolherDisciplinaETipoForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            List infoDisciplinasExecucao = (ArrayList) sessao.getAttribute("infoDisciplinasExecucao");
            int i = ((Integer) escolherDisciplinaETipoForm.get("indexDisciplinaExecucao")).intValue() - 1;
            sessao.setAttribute("infoDisciplinaExecucao", infoDisciplinasExecucao.get(i));
            return mapping.findForward("Sucesso");
        }
        throw new Exception();
    }

}
package net.sourceforge.fenixedu.presentationTier.Action.assiduousness;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Executor;
import net.sourceforge.fenixedu.applicationTier.PersistenceException;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoAutorizacaoPortalAssiduidade;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroPortalAssiduidade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public final class PortalAssiduidadeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        ActionErrors errors = new ActionErrors();

        Person pessoa = (Person) session.getAttribute(Constants.USER_KEY);

        ServicoAutorizacaoPortalAssiduidade servicoAutorizacaoPortalAssiduidade = new ServicoAutorizacaoPortalAssiduidade(
                pessoa);
        ServicoSeguroPortalAssiduidade servicoSeguroPortalAssiduidade = new ServicoSeguroPortalAssiduidade(
                servicoAutorizacaoPortalAssiduidade);

        try {
            Executor.getInstance().doIt(servicoSeguroPortalAssiduidade);

        } catch (NotExecuteException nee) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee.getMessage()));
        } catch (PersistenceException pe) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
        } finally {
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        }
        return (mapping.findForward("PortalAssiduidade"));
    }
}
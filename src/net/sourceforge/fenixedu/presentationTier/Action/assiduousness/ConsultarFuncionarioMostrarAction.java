package net.sourceforge.fenixedu.presentationTier.Action.assiduousness;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Executor;
import net.sourceforge.fenixedu.applicationTier.PersistenceException;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoAutorizacaoLer;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroConsultarFuncionario;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroLerFuncionario;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.ConsultarFuncionarioMostrarForm;

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
public final class ConsultarFuncionarioMostrarAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();

        IPerson pessoa = (IPerson) session.getAttribute(Constants.USER_KEY);

        ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
        ServicoSeguroLerFuncionario servicoSeguroLerFuncionario = new ServicoSeguroLerFuncionario(
                servicoAutorizacaoLer, pessoa.getIdInternal().intValue());

        try {
            Executor.getInstance().doIt(servicoSeguroLerFuncionario);

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

        Funcionario funcionario = servicoSeguroLerFuncionario.getFuncionario();

        ServicoSeguroConsultarFuncionario servicoSeguroConsultarFuncionario = new ServicoSeguroConsultarFuncionario(
                servicoAutorizacaoLer, funcionario.getNumeroMecanografico());

        try {

            /* funcionario a consultar */
            Executor.getInstance().doIt(servicoSeguroConsultarFuncionario);

        } catch (NotExecuteException nee) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee.getMessage()));
        } catch (PersistenceException pe) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
        } finally {
            if (!errors.isEmpty()) {
                saveErrors(request, errors);

                return mapping.getInputForward();
            }
        }

        session.setAttribute("numMecanografico", new Integer(funcionario.getNumeroMecanografico()));
        session.setAttribute("pessoa", servicoSeguroConsultarFuncionario.getPessoa());
        session.setAttribute("centroCusto", servicoSeguroConsultarFuncionario.getCentroCusto());

        ConsultarFuncionarioMostrarForm funcNaoDocenteForm = (ConsultarFuncionarioMostrarForm) form;
        funcNaoDocenteForm.setForm((Date) session.getAttribute(Constants.INICIO_CONSULTA),
                (Date) session.getAttribute(Constants.FIM_CONSULTA), servicoSeguroConsultarFuncionario
                        .getPessoa(), servicoSeguroConsultarFuncionario.getFuncionario(),
                servicoSeguroConsultarFuncionario.getStatusAssiduidade(),
                servicoSeguroConsultarFuncionario.getCentroCusto(), servicoSeguroConsultarFuncionario
                        .getFuncNaoDocente(), servicoSeguroConsultarFuncionario.getRotacaoHorario(),
                servicoSeguroConsultarFuncionario.getListaRegimesRotacao());

        return (mapping.findForward("ConsultarFuncionarioMostrar"));
    }
}
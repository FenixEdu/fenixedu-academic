package ServidorApresentacao.Action.assiduousness;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import Dominio.Funcionario;
import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConsultarFuncionario;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerFuncionario;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorApresentacao.formbeans.assiduousness.ConsultarFuncionarioMostrarForm;
import constants.assiduousness.Constants;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public final class ConsultarFuncionarioMostrarAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();

        Pessoa pessoa = (Pessoa) session.getAttribute(Constants.USER_KEY);

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
package net.sourceforge.fenixedu.presentationTier.Action.assiduousness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Executor;
import net.sourceforge.fenixedu.applicationTier.PersistenceException;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoAutorizacaoLer;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroBuscarParamJustificacoes;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroConstruirEscolhasMarcacoesPonto;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroConsultarJustificacoes;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroConsultarMarcacaoPonto;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroConsultarVerbete;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.IStrategyJustificacoes;
import net.sourceforge.fenixedu.domain.Justificacao;
import net.sourceforge.fenixedu.domain.MarcacaoPonto;
import net.sourceforge.fenixedu.domain.ParamJustificacao;
import net.sourceforge.fenixedu.domain.SuporteStrategyJustificacoes;
import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.ConsultarFuncionarioMostrarForm;
import net.sourceforge.fenixedu.util.Comparador;
import net.sourceforge.fenixedu.util.FormataCalendar;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public final class ConsultarFuncionarioEscolhaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();

        if (isCancelled(request)) {
            if (mapping.getAttribute() != null)
                session.removeAttribute(mapping.getAttribute());
            return (mapping.findForward("PortalGestaoAssiduidadeAction"));
        }

        ConsultarFuncionarioMostrarForm formEscolha = (ConsultarFuncionarioMostrarForm) form;
        Integer numMecanografico = (Integer) session.getAttribute("numMecanografico");

        if (formEscolha.getEscolha().equals("consultar.marcacao")) {
            //»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
            // Marcacoes de Ponto
            // «««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««

            List listaFuncionarios = new ArrayList();
            listaFuncionarios.add(numMecanografico);

            List listaEstados = new ArrayList();
            listaEstados.add(new String("valida"));
            listaEstados.add(new String("regularizada"));
            listaEstados.add(new String("cartaoFuncionarioInvalido"));
            listaEstados.add(new String("cartaoSubstitutoInvalido"));

            ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
            ServicoSeguroConstruirEscolhasMarcacoesPonto servicoSeguroConstruirEscolhasMarcacoesPonto = new ServicoSeguroConstruirEscolhasMarcacoesPonto(
                    servicoAutorizacaoLer, listaFuncionarios, null
                    /* listaCartoes */
                    , formEscolha.getDataInicioEscolha(), formEscolha.getDataFimEscolha());
            try {
                Executor.getInstance().doIt(servicoSeguroConstruirEscolhasMarcacoesPonto);
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

            ServicoSeguroConsultarMarcacaoPonto servicoSeguroConsultarMarcacaoPonto = new ServicoSeguroConsultarMarcacaoPonto(
                    servicoAutorizacaoLer, servicoSeguroConstruirEscolhasMarcacoesPonto
                            .getListaFuncionarios(), servicoSeguroConstruirEscolhasMarcacoesPonto
                            .getListaCartoes(), listaEstados, formEscolha.getDataInicioEscolha(),
                    formEscolha.getDataFimEscolha(), Boolean.TRUE);
            try {
                Executor.getInstance().doIt(servicoSeguroConsultarMarcacaoPonto);
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
            List listaMarcacoesPonto = servicoSeguroConsultarMarcacaoPonto.getListaMarcacoesPonto();

            // ordena as marcacoes de ponto por ordem decrescente porque a
            // tabela e apresentada ao contrario no jsp
            Comparador comparador = new Comparador(new String("MarcacaoPonto"),
                    new String("decrescente"));
            Object[] arrayMarcacoesPonto = listaMarcacoesPonto.toArray();
            Arrays.sort(arrayMarcacoesPonto, comparador);

            // lista ordenada das marcacoes de ponto
            listaMarcacoesPonto = new ArrayList();
            for (int i = 0; i < arrayMarcacoesPonto.length; i++)
                listaMarcacoesPonto.add(arrayMarcacoesPonto[i]);

            // criacao da tabela de marcacoes para mostrar no jsp
            List listaMarcacoesPontoBody = new ArrayList();
            ListIterator iterListaMarcacoesPonto = listaMarcacoesPonto.listIterator();
            MarcacaoPonto marcacaoPonto = null;
            Calendar calendario = Calendar.getInstance();
            calendario.setLenient(false);

            while (iterListaMarcacoesPonto.hasNext()) {
                marcacaoPonto = (MarcacaoPonto) iterListaMarcacoesPonto.next();

                if (marcacaoPonto.getSiglaUnidade() == null
                        || marcacaoPonto.getSiglaUnidade().length() == 0) {
                    listaMarcacoesPontoBody.add(0, "&nbsp;");
                } else {
                    listaMarcacoesPontoBody.add(0, marcacaoPonto.getSiglaUnidade());
                }
                listaMarcacoesPontoBody.add(1, String.valueOf(marcacaoPonto.getNumFuncionario()));
                listaMarcacoesPontoBody.add(2, String.valueOf(marcacaoPonto.getNumCartao()));

                calendario.setTimeInMillis(marcacaoPonto.getData().getTime());
                if (marcacaoPonto.getEstado().equals("regularizada")) {
                    listaMarcacoesPontoBody.add(3, "<b>" + FormataCalendar.dataHoras(calendario)
                            + "</b>");
                } else {
                    listaMarcacoesPontoBody.add(3, FormataCalendar.dataHoras(calendario));
                }
                //listaMarcacoesPontoBody.add(4,
                // messages.getMessage(marcacaoPonto.getEstado()));
            }
            List listagem = new ArrayList();
            List listaHeaders = new ArrayList();

            listaHeaders.add(messages.getMessage("prompt.unidade"));
            listaHeaders.add(messages.getMessage("prompt.funcionario"));
            listaHeaders.add(messages.getMessage("prompt.numCartao"));
            listaHeaders.add(messages.getMessage("prompt.data"));
            //listaHeaders.add(messages.getMessage("prompt.estado"));

            listagem.add(listaHeaders);
            listagem.add(listaMarcacoesPontoBody);

            request.setAttribute("listagem", listagem);
            request.setAttribute("forward", mapping.findForward("ConsultarMarcacaoPontoMostrar"));
            session.setAttribute("linkBotao", "ConsultarFuncionarioMostrar");

            return (mapping.findForward("MostrarListaAction"));

        } else if (formEscolha.getEscolha().equals("consultar.justificacoes")) {
            //»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
            // Justificacoes
            // ««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««

            ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
            ServicoSeguroConsultarJustificacoes servicoSeguroConsultarJustificacoes = new ServicoSeguroConsultarJustificacoes(
                    servicoAutorizacaoLer, numMecanografico.intValue(), formEscolha
                            .getDataInicioEscolha(), formEscolha.getDataFimEscolha());
            try {
                Executor.getInstance().doIt(servicoSeguroConsultarJustificacoes);
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
            List listaJustificacoes = servicoSeguroConsultarJustificacoes.getListaJustificacoes();

            // ordena as justificacoes por decrescente porque a tabela e
            // apresentada ao contrario no jsp
            Comparador comparador = new Comparador(new String("Justificacao"), new String("crescente"));
            Collections.sort(listaJustificacoes, comparador);

            ServicoSeguroBuscarParamJustificacoes servicoSeguroBuscarParamJustificacoes = new ServicoSeguroBuscarParamJustificacoes(
                    servicoAutorizacaoLer, listaJustificacoes);
            try {
                Executor.getInstance().doIt(servicoSeguroBuscarParamJustificacoes);
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
            List listaParamJustificacoes = servicoSeguroBuscarParamJustificacoes.getListaJustificacoes();

            // criacao da tabela de justificacoes para mostrar no jsp
            List listaJustificacoesBody = new ArrayList();
            List listaBody = new ArrayList();
            ListIterator iterListaParamJustificacoes = listaParamJustificacoes.listIterator();
            Justificacao justificacao = null;
            ParamJustificacao paramJustificacao = null;

            while (iterListaParamJustificacoes.hasNext()) {
                paramJustificacao = (ParamJustificacao) iterListaParamJustificacoes.next();
                justificacao = (Justificacao) listaJustificacoes.get(iterListaParamJustificacoes
                        .previousIndex());

                listaJustificacoesBody.add(0, numMecanografico.toString());
                IStrategyJustificacoes strategyJustificacoes = SuporteStrategyJustificacoes
                        .getInstance().callStrategy(paramJustificacao.getTipo());
                strategyJustificacoes.setListaJustificacoesBody(paramJustificacao, justificacao,
                        listaJustificacoesBody);

                listaBody.addAll(listaJustificacoesBody);
                listaJustificacoesBody.clear();
            }
            List listagem = new ArrayList();
            List listaHeaders = new ArrayList();

            listaHeaders.add(messages.getMessage("prompt.funcionario"));
            listaHeaders.add(messages.getMessage("prompt.sigla"));
            listaHeaders.add(messages.getMessage("prompt.descricaoJustificacao"));
            listaHeaders.add(messages.getMessage("prompt.tipo"));
            listaHeaders.add(messages.getMessage("prompt.diaInicio"));
            listaHeaders.add(messages.getMessage("prompt.diaFim"));
            listaHeaders.add(messages.getMessage("prompt.horaInicio"));
            listaHeaders.add(messages.getMessage("prompt.horaFim"));

            listagem.add(listaHeaders);
            listagem.add(listaBody);

            request.setAttribute("listagem", listagem);
            request.setAttribute("forward", mapping.findForward("ConsultarJustificacoesMostrar"));
            session.setAttribute("linkBotao", "ConsultarFuncionarioMostrar");

            return (mapping.findForward("MostrarListaAction"));

        } else if (formEscolha.getEscolha().equals("consultar.verbete")) {
            //»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
            // Verbete
            // «««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««
            //			//ATENCAO: Servico acede à BD Oracle para ler as marcacoes ponto
            ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
            ServicoSeguroConsultarVerbete servicoSeguroConsultarVerbete = new ServicoSeguroConsultarVerbete(
                    servicoAutorizacaoLer, numMecanografico, formEscolha.getDataInicioEscolha(),
                    formEscolha.getDataFimEscolha(), Boolean.TRUE, locale);

            //			try {
            //				SuportePersistenteOracle.getInstance().iniciarTransaccao();

            try {
                Executor.getInstance().doIt(servicoSeguroConsultarVerbete);
            } catch (NotExecuteException nee) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee.getMessage()));
            } catch (PersistenceException pe) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
            } finally {
                //					SuportePersistenteOracle.getInstance().cancelarTransaccao();
                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
            }
            //				
            //				SuportePersistenteOracle.getInstance().confirmarTransaccao();
            //			} catch (Exception e) {
            //				if (!errors.empty()) {
            //					saveErrors(request, errors);
            //					return (new ActionForward(mapping.getInput()));
            //				}
            //			}

            List listaHeaders = new ArrayList();
            listaHeaders.add(messages.getMessage("prompt.data"));
            listaHeaders.add(messages.getMessage("prompt.sigla"));
            listaHeaders.add(messages.getMessage("prompt.saldoHN"));
            listaHeaders.add(messages.getMessage("prompt.saldoPF"));
            listaHeaders.add(messages.getMessage("prompt.justificacao"));
            listaHeaders.add(messages.getMessage("consultar.marcacao"));

            List listaHeadersResumo = new ArrayList();
            listaHeadersResumo.add(messages.getMessage("prompt.saldoHN"));
            listaHeadersResumo.add(messages.getMessage("prompt.saldoPF"));
            listaHeadersResumo.add(messages.getMessage("prompt.saldoNocturno"));
            listaHeadersResumo.add(messages.getMessage("DSC"));
            listaHeadersResumo.add(messages.getMessage("DS"));

            List listaHeadersTrabExtra = new ArrayList();
            listaHeadersTrabExtra.add(messages.getMessage("prompt.primeiroEscalao"));
            listaHeadersTrabExtra.add(messages.getMessage("prompt.segundoEscalao"));
            listaHeadersTrabExtra.add(messages.getMessage("prompt.depoisSegundoEscalao"));
            listaHeadersTrabExtra.add(messages.getMessage("prompt.primeiroEscalao"));
            listaHeadersTrabExtra.add(messages.getMessage("prompt.segundoEscalao"));
            listaHeadersTrabExtra.add(messages.getMessage("prompt.depoisSegundoEscalao"));

            List listaSaldos = servicoSeguroConsultarVerbete.getListaSaldos();
            List listaResumo = new ArrayList();
            Calendar calendario = Calendar.getInstance();
            calendario.setLenient(false);

            // saldoHN
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(0)).longValue());
            listaResumo.add(0, FormataCalendar.horasSaldo(calendario));
            // saldoPF
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(1)).longValue());
            listaResumo.add(1, FormataCalendar.horasSaldo(calendario));
            // saldo nocturno normal
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(7)).longValue());
            listaResumo.add(2, FormataCalendar.horasSaldo(calendario));
            // saldo DSC ou Feriado
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(5)).longValue());
            listaResumo.add(3, FormataCalendar.horasSaldo(calendario));
            // saldo DS
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(6)).longValue());
            listaResumo.add(4, FormataCalendar.horasSaldo(calendario));

            List listaTrabExtra = new ArrayList();

            // saldo primeiro escalao diurno
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(2)).longValue());
            listaTrabExtra.add(0, FormataCalendar.horasSaldo(calendario));
            // saldo segundo escalao diurno
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(3)).longValue());
            listaTrabExtra.add(1, FormataCalendar.horasSaldo(calendario));
            // saldo para alem do segundo escalao diurno
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(4)).longValue());
            listaTrabExtra.add(2, FormataCalendar.horasSaldo(calendario));
            // saldo primeiro escalao nocturno
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(8)).longValue());
            listaTrabExtra.add(3, FormataCalendar.horasSaldo(calendario));
            // saldo segundo escalao nocturno
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(9)).longValue());
            listaTrabExtra.add(4, FormataCalendar.horasSaldo(calendario));
            // saldo para alem do segundo escalao nocturno
            calendario.clear();
            calendario.setTimeInMillis(((Long) listaSaldos.get(10)).longValue());
            listaTrabExtra.add(5, FormataCalendar.horasSaldo(calendario));

            List listagem = new ArrayList();
            listagem.add(listaHeaders);
            listagem.add(servicoSeguroConsultarVerbete.getVerbete());
            listagem.add(listaHeadersResumo);
            listagem.add(listaResumo);
            listagem.add(listaHeadersTrabExtra);
            listagem.add(listaTrabExtra);

            request.setAttribute("listagem", listagem);
            request.setAttribute("forward", mapping.findForward("ConsultarVerbeteMostrar"));
            session.setAttribute("linkBotao", "ConsultarFuncionarioMostrar");

            // para o caso de querer imprimir o verbete do funcionario
            session.setAttribute("impressaoVerbete", listagem);

            return (mapping.findForward("MostrarListaAction"));
        }
        return (mapping.findForward("PortalGestaoAssiduidadeAction"));
    }
}
package ServidorApresentacao.Action.assiduousness;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Locale;

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
import org.apache.struts.util.MessageResources;

import Dominio.MarcacaoPonto;
import ServidorAplicacao.Executor;
import ServidorAplicacao.NotAuthorizeException;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConstruirEscolhasMarcacoesPonto;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConsultarMarcacaoPonto;
import ServidorApresentacao.formbeans.assiduousness.ConsultarMarcacaoPontoForm;
import Util.Comparador;
import Util.FormataCalendar;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public final class ConsultarMarcacaoPontoAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		System.out.println("--->No ConsultarMarcacaoPontoAction...");

		Locale locale = getLocale(request);
		MessageResources messages = getResources(request);
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		if (isCancelled(request)) {
			if (mapping.getAttribute() != null)
				session.removeAttribute(mapping.getAttribute());
			return (mapping.findForward("PortalAssiduidadeAction"));
		}

		ConsultarMarcacaoPontoForm formConsultar = (ConsultarMarcacaoPontoForm) form;

		ArrayList listaFuncionarios = null;
		ArrayList listaCartoes = null;
		ArrayList listaEstados = null;
		Timestamp dataInicio = null;
		Timestamp dataFim = null;

		if (formConsultar.getFuncionariosEscolhidos() != null) {
			if (formConsultar.getFuncionariosEscolhidos().length > 0) {
				listaFuncionarios = new ArrayList();
				for (int i = 0; i < (formConsultar.getFuncionariosEscolhidos()).length; i++) {
					listaFuncionarios.add(new Integer((formConsultar.getFuncionariosEscolhidos())[i]));
				}
			}
		}

		if (formConsultar.getCartoesEscolhidos() != null) {
			if (formConsultar.getCartoesEscolhidos().length > 0) {
				listaCartoes = new ArrayList();
				for (int i = 0; i < (formConsultar.getCartoesEscolhidos()).length; i++) {
					listaCartoes.add(new Integer((formConsultar.getCartoesEscolhidos())[i]));
				}
			}
		}

		if (formConsultar.getEstadosEscolhidos() != null) {
			if (formConsultar.getEstadosEscolhidos().length > 0) {
				listaEstados = new ArrayList();
				for (int i = 0; i < (formConsultar.getEstadosEscolhidos()).length; i++) {
					listaEstados.add(new String((formConsultar.getEstadosEscolhidos())[i]));
				}
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setLenient(false);

		calendar.set(
			(new Integer(formConsultar.getAnoInicio())).intValue(),
			(new Integer(formConsultar.getMesInicio())).intValue() - 1,
			(new Integer(formConsultar.getDiaInicio())).intValue(),
			00,
			00,
			00);
		dataInicio = new Timestamp(calendar.getTimeInMillis());

		calendar.clear();
		calendar.set(
			(new Integer(formConsultar.getAnoFim())).intValue(),
			(new Integer(formConsultar.getMesFim())).intValue() - 1,
			(new Integer(formConsultar.getDiaFim())).intValue(),
			23,
			59,
			59);
		dataFim = new Timestamp(calendar.getTimeInMillis());

		// procura cartoes que os funcionarios escolhidos tenham utilizado nas marcacoes de ponto
		ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
		ServicoSeguroConstruirEscolhasMarcacoesPonto servicoSeguroConstruirEscolhasMarcacoesPonto =
			new ServicoSeguroConstruirEscolhasMarcacoesPonto(servicoAutorizacaoLer, listaFuncionarios, listaCartoes, dataInicio, dataFim);
		try {
			Executor.getInstance().doIt(servicoSeguroConstruirEscolhasMarcacoesPonto);
		} catch (NotAuthorizeException nae) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nae.getMessage()));
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

		ServicoSeguroConsultarMarcacaoPonto servicoSeguroConsultarMarcacaoPonto =
			new ServicoSeguroConsultarMarcacaoPonto(
				servicoAutorizacaoLer,
				servicoSeguroConstruirEscolhasMarcacoesPonto.getListaFuncionarios(),
				servicoSeguroConstruirEscolhasMarcacoesPonto.getListaCartoes(),
				listaEstados,
				dataInicio,
				dataFim);
		try {
			Executor.getInstance().doIt(servicoSeguroConsultarMarcacaoPonto);
		} catch (NotAuthorizeException nae) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nae.getMessage()));
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

		ArrayList listaMarcacoesPonto = servicoSeguroConsultarMarcacaoPonto.getListaMarcacoesPonto();

		// ordena as marcacoes de ponto por ordem decrescente porque a tabela e apresentada ao contrario no jsp
		Comparador comparador = new Comparador(new String("MarcacaoPonto"), new String("decrescente"));
		Collections.sort(listaMarcacoesPonto,comparador);

		/*
		    java.util.Date dataTesteInicial = new java.util.Date();
		    java.util.Date dataTesteFinal = new java.util.Date();
		 
		    dataTesteInicial = (java.util.Date)Date.valueOf(new String("valor introduzido no jsp"));
		    dataTesteFinal = (java.util.Date)Date.valueOf(new String("valor introduzido no jsp"));
		    while(dataTesteInicial.before(dataTesteFinal)){
		      listaMarcacoesDiarias.add(0, dataTesteInicial);
		      obter marcacoes de ponto deste dia (servico que acede a base de dados
		      para buscar as marcacoes que vem numa lista de strings)
		      while(marcacoes daquele dia){
		        acrescentar na lista de marcacoes diarias todas as marcacoes desse dia
		      }
		    }
		 */

		// criacao da tabela de marcacoes para mostrar no jsp 
		ArrayList listaMarcacoesPontoBody = new ArrayList();
		ListIterator iterListaMarcacoesPonto = listaMarcacoesPonto.listIterator();
		MarcacaoPonto marcacaoPonto = null;
		Calendar calendario = Calendar.getInstance();
		calendario.setLenient(false);

		while (iterListaMarcacoesPonto.hasNext()) {
			marcacaoPonto = (MarcacaoPonto) iterListaMarcacoesPonto.next();

			if (marcacaoPonto.getSiglaUnidade().length() == 0) {
				listaMarcacoesPontoBody.add(0, "&nbsp;");
			} else {
				listaMarcacoesPontoBody.add(0, marcacaoPonto.getSiglaUnidade());
			}
			if (marcacaoPonto.getNumFuncionario() == 0) {
				listaMarcacoesPontoBody.add(1, new String("&nbsp;"));
			} else {
				listaMarcacoesPontoBody.add(
					1,
					new String(
						"<a href=\"../lerDadosFuncionario.do?numero="
							+ String.valueOf(marcacaoPonto.getNumFuncionario())
							+ "\">"
							+ String.valueOf(marcacaoPonto.getNumFuncionario())
							+ "</a>"));
			}
			listaMarcacoesPontoBody.add(2, String.valueOf(marcacaoPonto.getNumCartao()));
			
			calendario.clear();
			calendario.setTimeInMillis(marcacaoPonto.getData().getTime());
			if (marcacaoPonto.getEstado().equals("regularizada")) {
				listaMarcacoesPontoBody.add(3, "<b>" + FormataCalendar.dataHoras(calendario) + "</b>");
			} else {
				listaMarcacoesPontoBody.add(3, FormataCalendar.dataHoras(calendario));
			}
			//listaMarcacoesPontoBody.add(5, messages.getMessage(marcacaoPonto.getEstado()));
		}

		ArrayList listagem = new ArrayList();
		ArrayList listaHeaders = new ArrayList();

		listaHeaders.add(messages.getMessage("prompt.unidade"));
		listaHeaders.add(messages.getMessage("prompt.funcionario"));
		listaHeaders.add(messages.getMessage("prompt.numCartao"));
		listaHeaders.add(messages.getMessage("prompt.data"));
		//listaHeaders.add(messages.getMessage("prompt.estado"));

		listagem.add(listaHeaders);
		listagem.add(listaMarcacoesPontoBody);

		request.setAttribute("listagem", listagem);
		request.setAttribute("forward", mapping.findForward("ConsultarMarcacaoPontoMostrar"));
		session.setAttribute("linkBotao", "PrepararConsultarMarcacaoPontoAction");

		return (mapping.findForward("MostrarListaAction"));
	}
}
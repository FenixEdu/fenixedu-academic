package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import Dominio.CentroCusto;
import Dominio.Funcionario;
import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.NotAuthorizeException;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConsultarFuncionario;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConsultarVerbete;
import constants.assiduousness.Constants;

/**
 * @author Fernanda Quitério & Tânia Pousão
 *
 */
public class ThreadImprimirNVerbetes extends Thread {
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private HttpSession session = null;

	private PrintWriter stream = null;
	private ArrayList listaFuncionarios = null;
	private Timestamp dataInicio = null;
	private Timestamp dataFim = null;
	private Locale locale = null;

	public ThreadImprimirNVerbetes(
		HttpServletResponse response,
		ArrayList listaFuncionarios,
		Timestamp dataInicio,
		Timestamp dataFim,
		Locale locale) {
		this.request = null;
		this.response = response;
		this.session = null;
		this.listaFuncionarios = listaFuncionarios;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.locale = locale;
	}

	public ThreadImprimirNVerbetes(
		HttpServletRequest request,
		HttpServletResponse response,
		HttpSession session,
		ArrayList listaFuncionarios,
		Timestamp dataInicio,
		Timestamp dataFim,
		Locale locale) {
		this.request = request;
		this.response = response;
		this.session = session;
		this.listaFuncionarios = listaFuncionarios;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.locale = locale;
	}

	public void run() {
		System.out.println("TTTTTTTTT - " + "inicio de thread com nome: " + this.getName());

		ActionErrors errors = new ActionErrors();
		ResourceBundle bundle = ResourceBundle.getBundle(Constants.APPLICATION_RESOURCES, locale);

		int numeroMecanografico = 0;
		String verbete = new String();
		boolean temVerbetes = false;
		ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
		ServicoSeguroConsultarVerbete servicoSeguroConsultarVerbete = null;
		ListIterator iteradorLista = listaFuncionarios.listIterator();

		ArrayList listaHeaders = new ArrayList();
		listaHeaders.add(bundle.getString("prompt.data"));
		listaHeaders.add(bundle.getString("prompt.sigla"));
		listaHeaders.add(bundle.getString("prompt.saldoHN"));
		listaHeaders.add(bundle.getString("prompt.saldoPF"));
		listaHeaders.add(bundle.getString("prompt.justificacao"));
		listaHeaders.add(bundle.getString("consultar.marcacao"));

		ArrayList listaHeadersResumo = new ArrayList();
		listaHeadersResumo.add(bundle.getString("prompt.saldoHN"));
		listaHeadersResumo.add(bundle.getString("prompt.saldoPF"));
		listaHeadersResumo.add(bundle.getString("prompt.saldoNocturno"));
		listaHeadersResumo.add(bundle.getString("DSC"));
		listaHeadersResumo.add(bundle.getString("DS"));

		ArrayList listaHeadersTrabExtra = new ArrayList();
		listaHeadersTrabExtra.add(bundle.getString("prompt.primeiroEscalao"));
		listaHeadersTrabExtra.add(bundle.getString("prompt.segundoEscalao"));
		listaHeadersTrabExtra.add(bundle.getString("prompt.depoisSegundoEscalao"));
		listaHeadersTrabExtra.add(bundle.getString("prompt.primeiroEscalao"));
		listaHeadersTrabExtra.add(bundle.getString("prompt.segundoEscalao"));
		listaHeadersTrabExtra.add(bundle.getString("prompt.depoisSegundoEscalao"));

		verbete = verbete.concat("<html>\n<head>\n<title></title>\n</head>\n<body>\n");
		verbete = verbete.concat("<table>\n");

		while (iteradorLista.hasNext()) {
			numeroMecanografico = ((Integer) iteradorLista.next()).intValue();

			ServicoSeguroConsultarFuncionario servicoSeguroConsultarFuncionario =
				new ServicoSeguroConsultarFuncionario(servicoAutorizacaoLer, numeroMecanografico);
			try {
				// Funcionario do verbete
				Executor.getInstance().doIt(servicoSeguroConsultarFuncionario);

			} catch (NotAuthorizeException nae) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nae.getMessage()));
			} catch (NotExecuteException nee) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee.getMessage()));
			} catch (PersistenceException pe) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
			} finally {
				if (!errors.isEmpty()) {
					continue;
				}
			}
			ArrayList listagem = null;
			if (session != null) {
				listagem = (ArrayList) session.getAttribute("impressaoVerbete");
			}
			if ((request == null) || (request != null && request.getParameter("impressaoFuncionario") == null) || (listagem == null)) {

				servicoSeguroConsultarVerbete =
					new ServicoSeguroConsultarVerbete(
						servicoAutorizacaoLer,
						new Integer(numeroMecanografico),
						this.dataInicio,
						this.dataFim,
						locale);
				try {
					Executor.getInstance().doIt(servicoSeguroConsultarVerbete);
				} catch (NotAuthorizeException nae) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nae.getMessage()));
				} catch (NotExecuteException nee) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee.getMessage()));
				} catch (PersistenceException pe) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
				} finally {
					if (!errors.isEmpty()) {
						System.out.println(
							"TTTTTTTTT - " + this.getName() + ": erro no servico consultar verbete do funcionario numero " + numeroMecanografico);
						continue;
					}
				}

				ArrayList listaResumo = calculaTrabExtra(servicoSeguroConsultarVerbete.getListaSaldos());

				verbete =
					verbete.concat(
						criarInicioFicheiro(
							servicoSeguroConsultarFuncionario.getPessoa(),
							servicoSeguroConsultarFuncionario.getFuncionario(),
							servicoSeguroConsultarFuncionario.getCentroCusto()));
				verbete =
					verbete.concat(criarListagem(locale, listaHeaders, servicoSeguroConsultarVerbete.getVerbete(), "consultar.verbete", 5));
				verbete =
					verbete.concat(
						criarListagem(
							locale,
							listaHeadersResumo,
							calculaResultado(servicoSeguroConsultarVerbete.getListaSaldos()),
							"resumo.valores",
							3));
				/*verbete =
					verbete.concat(
						criarListagem(
							locale,
							listaHeadersTrabExtra,
							calculaTrabExtra(servicoSeguroConsultarVerbete.getListaSaldos()),
							"trabalho.extra",
							2));*/
			} else {
				verbete =
					verbete.concat(
						criarInicioFicheiro(
							servicoSeguroConsultarFuncionario.getPessoa(),
							servicoSeguroConsultarFuncionario.getFuncionario(),
							servicoSeguroConsultarFuncionario.getCentroCusto()));
				verbete =
					verbete.concat(criarListagem(locale, (ArrayList) listagem.get(0), (ArrayList) listagem.get(1), "consultar.verbete", 5));
				verbete = verbete.concat(criarListagem(locale, (ArrayList) listagem.get(2), (ArrayList) listagem.get(3), "resumo.valores", 3));
				/*verbete =
					verbete.concat(
						criarListagem(locale, (ArrayList) listagem.get(4), (ArrayList) listagem.get(5), "trabalho.extra", 2));*/
			}
			verbete = verbete.concat(criarFimFicheiro());
			temVerbetes = true;
		}
		verbete = verbete.concat("</table>\n</body>\n</html>");

		if (temVerbetes) {
			// no caso de estarmos a imprimir o verbete de um funcionario apenas, apresenta-se o verbete na página
			if (listaFuncionarios.size() == 1 && (request != null && request.getParameter("impressaoFuncionario") != null)) {
				this.response.setContentType("text/html; charset=ISO-8859-1");

				java.io.PrintWriter stream = null;
				try {
					stream = this.response.getWriter();

				} catch (Exception e) {
					e.printStackTrace();
				}
				stream.write(verbete);

			} else {

				File verbetes = null;
				BufferedWriter escritor = null;
				try {
					verbetes = new File(this.getName() + ".html");
					escritor = new BufferedWriter(new FileWriter(verbetes));
				} catch (Exception exception) {
					System.out.println("TTTTTTTTT - " + this.getName() + ": erro ao criar o ficheiro de verbetes");
					return;
				}
				try {
					escritor.write(verbete);
				} catch (Exception exception) {
					System.out.println("TTTTTTTTT - " + this.getName() + ": erro ao escrever no ficheiro de verbetes");
					return;
				}
				try {
					escritor.close();
				} catch (Exception e) {
					System.out.println("TTTTTTTTT - " + this.getName() + ": erro ao fechar o ficheiro de verbetes");
					return;
				}
			}
		}
		System.out.println("TTTTTTTTT - " + "fim de thread com nome: " + this.getName());

		return;
	}

	public ArrayList calculaResultado(ArrayList listaSaldos) {
		ArrayList listaResumo = new ArrayList();
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
		return listaResumo;
	} /* calculaResultado */

	public ArrayList calculaTrabExtra(ArrayList listaSaldos) {
		ArrayList listaTrabExtra = new ArrayList();
		Calendar calendario = Calendar.getInstance();
		calendario.setLenient(false);

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
		return listaTrabExtra;
	} /* calculaTrabExtra */

	public String criarInicioFicheiro(Pessoa pessoa, Funcionario funcionario, CentroCusto centroCusto) {
		StringBuffer results = new StringBuffer();
		results.append("<tr>\n<td>\n");
		results.append("<div align='left'>\n");
		results.append("<table width='700' border='0' align='left' cellpadding='0' cellspacing='0' nowrap=\"nowrap\">\n");
		results.append("<tr>\n");
		results.append("<td width='350'>&nbsp;&nbsp;</td>\n");
		results.append("<td width='350' align='left'>\n");
		results.append("<font size='2' face='Arial, Helvetica, sans-serif'><br>\n");

		results.append(String.valueOf(funcionario.getNumeroMecanografico()) + "<p style='{margin-top: -5; margin-bottom:-5}'>&nbsp;</p>");
		results.append(pessoa.getNome() + "<p style='{margin-top: -5; margin-bottom:-5}'>&nbsp;</p>");
		results.append(
			centroCusto.getSigla()
				+ "-"
				+ centroCusto.getDepartamento()
				+ "<br>"
				+ centroCusto.getSeccao1()
				+ "<br>"
				+ centroCusto.getSeccao2());
		results.append("\n<br></font>\n");
		results.append("</td>\n");
		results.append("</tr>\n");

		results.append("<tr>\n");
		results.append("<td colspan='2' align='center'>&nbsp;&nbsp;&nbsp; <p></p><p></p><p></p><br></td>\n");
		results.append("</tr>\n");

		return results.toString();
	} /* criarInicioFicheiro */

	public String criarListagem(Locale locale, ArrayList listaHeaders, ArrayList listaBody, String titulo, int tamanho) {
		ResourceBundle bundle = ResourceBundle.getBundle(Constants.APPLICATION_RESOURCES, locale);
		StringBuffer results = new StringBuffer();

		if (titulo != null) {
			results.append("<tr align='left'>\n");
			results.append("<td colspan='2' align='center'>\n");
			results.append("<font size='" + tamanho + "' face='Arial, Helvetica, sans-serif'><b>\n");
			results.append(bundle.getString(titulo));
			results.append("\n</b></font><p style='{margin-top: -5; margin-bottom:-5}'>&nbsp;</p>\n");
			results.append("</td>\n");
			results.append("</tr>\n");
		}
		results.append("<tr align='left'>\n");
		results.append("<td colspan='2'>\n");
		// lista de assiduidade 
		results.append(constroiListagem(listaHeaders, listaBody));
		results.append("</td>\n");
		results.append("</tr>\n");

		results.append("<tr>\n");
		results.append("<td colspan='2' align='center'><br></td>\n");
		results.append("</tr>\n");

		return results.toString();
	} /* criarListagem */
	/*
			results.append("<tr align='left'>\n");
			results.append("<td colspan='2' align='center'>\n");
			results.append("<font size='3' face='Arial, Helvetica, sans-serif'><b>\n");
			results.append(bundle.getString("resumo.valores"));
			results.append("\n</b></font><p style='{margin-top: -5; margin-bottom:-5}'>&nbsp;</p>\n");
			results.append("</td>\n");
			results.append("</tr>\n");
	
			results.append("<tr align='left'>\n");
			results.append("<td colspan='2'>\n");
			// lista de resumo 
			results.append(constroiListagem(listaHeadersResumo, listaResumo));
			results.append("</td>\n");
			results.append("</tr>\n");
	*/
	public String criarFimFicheiro() {
		StringBuffer results = new StringBuffer();

		results.append("</table>\n");
		results.append("</div>");
		results.append("<br style='page-break-after:always;' />\n");
		results.append("</td>\n</tr>\n");

		return results.toString();
	} /* criarFimFicheiro */

	public String constroiListagem(ArrayList listaHeaders, ArrayList listaBody) {
		StringBuffer results =
			new StringBuffer("<table cellspacing=\"0\" style=\"{border-style:solid ; border-color: DarkBlue; border-width:2px}\" cellpadding=\"0\" align='center' width='600'>\n");

		results.append("<tr>\n");
		int i = 0;
		while (i < listaHeaders.size() - 1) {
			results.append(
				"<th nowrap bgcolor='DarkBlue' align='center' style='{border-bottom-style:solid; border-width: 1px; border-right-style: solid}'>\n");
			results.append("<font color='#ffffff' size='2.5'>");
			results.append((String) listaHeaders.get(i));
			results.append("</font>\n");
			results.append("</th>\n");
			i++;
		}
		results.append("<th nowrap bgcolor='DarkBlue' style='{border-bottom-style:solid; border-width: 1px} align='center'>\n");
		results.append("<font color='#ffffff' size='2.5'>");
		results.append((String) listaHeaders.get(i));
		results.append("</font>\n");
		results.append("</th>\n");
		results.append("</tr>\n");

		i = 0;
		int j = 0;
		String contents;

		String bodyBGColor1 = "#ffffff";
		String bodyBGColor2 = "#ffffff"; //"#B1BBD6";
		String corFundo = bodyBGColor2;

		while (i < listaBody.size()) {
			results.append("<tr");
			results.append(" bgcolor='");
			if (corFundo == bodyBGColor2)
				corFundo = bodyBGColor1;
			else
				corFundo = bodyBGColor2;

			results.append(corFundo);
			results.append("'>\n");

			while (j < listaHeaders.size()) {
				contents = (String) listaBody.get(i);

				if ((j == listaHeaders.size() - 1)
					&& (i <= (listaBody.size() - listaHeaders.size() - 1))) { // é a última coluna e não é a última linha
					results.append("<td nowrap style='{border-bottom-style:dotted; border-width: 1px}' align='left'>\n");
				} else if (
					(j != listaHeaders.size() - 1)
						&& (i <= (listaBody.size() - listaHeaders.size() - 1))) { // não é a última coluna e não é a última linha
					results.append(
						"<td nowrap style='{border-bottom-style:dotted; border-width: 1px; border-right-style: solid}' align='left'>\n");
				} else if (
					(j == listaHeaders.size() - 1)
						&& ((i >= (listaBody.size() - listaHeaders.size() - 1))
							&& (i <= listaBody.size() - 1))) { // é a última coluna e é a última linha
					results.append("<td nowrap style='{border-width: 1px}' align='left'>\n");
				} else if (
					(j != listaHeaders.size() - 1)
						&& ((i >= (listaBody.size() - listaHeaders.size() - 1))
							&& (i <= listaBody.size() - 1))) { // não é a última coluna e é a última linha
					results.append("<td nowrap style='{border-width: 1px; border-right-style: solid}' align='left'>\n");
				}
				results.append("<font color='DarkBlue' size='2.5'>\n");
				results.append(contents);
				results.append("\n</font>\n");
				results.append("</td>\n");
				i++;
				j++;
			}
			results.append("</tr>\n");
			//i=j;
			j = 0;
		}

		results.append("</table>\n");
		return results.toString();
	} /* constroiListagem */

}

package ServidorApresentacao.Action.assiduousness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorApresentacao.formbeans.assiduousness.ImprimirVerbetesForm;
import Util.ThreadImprimirNVerbetes;
import Util.ThreadImprimirVerbetes;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ImprimirVerbetesAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		System.out.println("--->No ImprimirVerbetesAction...");

		Locale locale = getLocale(request);
		MessageResources messages = getResources(request);
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		if (isCancelled(request)) {
			if (mapping.getAttribute() != null)
				session.removeAttribute(mapping.getAttribute());
			return (mapping.findForward("PortalGestaoAssiduidadeAction"));
		}

		ImprimirVerbetesForm formImpressao = (ImprimirVerbetesForm) form;

		ArrayList listaFuncionarios = null;
		ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();

		if (request.getParameter("impressaoFuncionario") != null) {
			listaFuncionarios = new ArrayList();
			listaFuncionarios.add(Integer.valueOf(request.getParameter("impressaoFuncionario")));
			// lanca uma thread que vai construir o verbete para imprimir
			try {

				ThreadImprimirNVerbetes nVerbetes =
					new ThreadImprimirNVerbetes(
						request,
						response,
						session,
						listaFuncionarios,
						formImpressao.getDataInicioImpressao(),
						formImpressao.getDataFimImpressao(),
						locale);
				nVerbetes.setName("verbeteFuncionario");
				nVerbetes.start();

				try {
					if (nVerbetes.isAlive()) {
						nVerbetes.join();
					}
				} catch (InterruptedException ie) {
					return (mapping.getInputForward());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			session.removeAttribute("impressaoVerbete");
			return (mapping.findForward(""));

		} else {
			/*ServicoSeguroLerNumMecanograficosAssiduidade servicoSeguroLerNumMecanograficosAssiduidade =
				new ServicoSeguroLerNumMecanograficosAssiduidade(servicoAutorizacaoLer, formImpressao.getDataInicioImpressao());
			try {
				Executor.getInstance().doIt(servicoSeguroLerNumMecanograficosAssiduidade);
			} catch (NotAuthorizeException nae) {
				System.out.println(nae.getMessage());
			} catch (NotExecuteException nee) {
				System.out.println(nee.getMessage());
			} catch (PersistenceException pe) {
				System.out.println(pe.getMessage());
			}
			listaFuncionarios = servicoSeguroLerNumMecanograficosAssiduidade.getListaNumerosMecanograficos();*/

			listaFuncionarios = new ArrayList();
//			listaFuncionarios.add(new Integer(3482));
//			listaFuncionarios.add(new Integer(2997));
//			listaFuncionarios.add(new Integer(3647));
//			listaFuncionarios.add(new Integer(3482));
			listaFuncionarios.add(new Integer(2997));
			listaFuncionarios.add(new Integer(3647));
			listaFuncionarios.add(new Integer(3482));
			listaFuncionarios.add(new Integer(2997));
			listaFuncionarios.add(new Integer(2350));

			ThreadImprimirVerbetes threadImpressaoVerbetes =
				new ThreadImprimirVerbetes(
					response,
					session,
					listaFuncionarios,
					formImpressao.getDataInicioImpressao(),
					formImpressao.getDataFimImpressao(),
					locale);
			threadImpressaoVerbetes.setName("threadPrincipal");
			threadImpressaoVerbetes.start();

			return (mapping.findForward("PortalGestaoAssiduidadeAction"));
		}

		/*
				response.setContentType("text/html; charset=ISO-8859-1");
		
				java.io.PrintWriter stream = null;
				try {
					stream = response.getWriter();
		
				} catch (Exception e) {
					e.printStackTrace();
				}
				stream.write("<html>\n<head>\n<title></title>\n</head>\n<body>\n");
				stream.write("<table>\n");
		
				//}*/
		/*
				while (iteradorFuncionario.hasNext()) {
					numeroMecanografico = ((Integer) iteradorFuncionario.next()).intValue();
		
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
							saveErrors(request, errors);
							return (new ActionForward(mapping.getInput()));
						}
					}
		
					ArrayList listagem = (ArrayList) session.getAttribute("impressaoVerbete");
					if ((request.getParameter("impressaoFuncionario") == null) || (listagem == null)) {
						servicoSeguroConsultarVerbete =
							new ServicoSeguroConsultarVerbete(
								servicoAutorizacaoLer,
								new Integer(numeroMecanografico),
								formImpressao.getDataInicioImpressao(),
								formImpressao.getDataFimImpressao(),
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
								saveErrors(request, errors);
								return (new ActionForward(mapping.getInput()));
							}
						}
		
						ArrayList listaResumo = calculaTrabExtra(servicoSeguroConsultarVerbete.getListaSaldos());
		
						stream.write(
							criarInicioFicheiro(
								servicoSeguroConsultarFuncionario.getPessoa(),
								servicoSeguroConsultarFuncionario.getFuncionario(),
								servicoSeguroConsultarFuncionario.getCentroCusto()));
						stream.write(criarListagem(locale, listaHeaders, servicoSeguroConsultarVerbete.getVerbete(), "consultar.verbete", 5));
						stream.write(
							criarListagem(
								locale,
								listaHeadersResumo,
								calculaResultado(servicoSeguroConsultarVerbete.getListaSaldos()),
								"resumo.valores",
								3));
						stream.write(
							criarListagem(
								locale,
								listaHeadersTrabExtra,
								calculaTrabExtra(servicoSeguroConsultarVerbete.getListaSaldos()),
								"trabalho.extra",
								2));
						stream.write(criarFimFicheiro());
					} else {
						stream.write(
							criarInicioFicheiro(
								servicoSeguroConsultarFuncionario.getPessoa(),
								servicoSeguroConsultarFuncionario.getFuncionario(),
								servicoSeguroConsultarFuncionario.getCentroCusto()));
						stream.write(criarListagem(locale, (ArrayList) listagem.get(0), (ArrayList) listagem.get(1), "consultar.verbete", 5));
						stream.write(criarListagem(locale, (ArrayList) listagem.get(2), (ArrayList) listagem.get(3), "resumo.valores", 3));
						stream.write(criarListagem(locale, (ArrayList) listagem.get(4), (ArrayList) listagem.get(5), "trabalho.extra", 2));
						stream.write(criarFimFicheiro());
		
						session.removeAttribute("impressaoVerbete");
					}
				}*/
		/*
				try {
		
					verbetePrimMetade.join();
					//if (indice != 1) {
						verbeteSegMetade.join();
					//}
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("ImprimirVerbetesAction.execute: erro ao esperar pelas excepcoes.");
				}
				stream.write("</table>\n</body>\n</html>");
		*/
		//return (mapping.findForward("PortalGestaoAssiduidadeAction"));
	}
	/*
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
		}*/ /* calculaResultado */
	/*
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
		}*/ /* calculaTrabExtra */
	/*
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
		}*/ /* criarInicioFicheiro */
	/*
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
		}*/ /* criarListagem */
	/*
		public String criarFimFicheiro() {
			StringBuffer results = new StringBuffer();
	
			results.append("</table>\n");
			results.append("</div>");
			results.append("<br style='page-break-after:always;' />\n");
			results.append("</td>\n</tr>\n");
	
			return results.toString();
		}*/ /* criarFimFicheiro */
	/*
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
		}*/ /* constroiListagem */
}
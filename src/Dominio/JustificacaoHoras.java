package Dominio;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import ServidorApresentacao.formbeans.assiduousness.IntroduzirJustificacaoForm;
import Util.FormataCalendar;
import constants.assiduousness.Constants;
/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public class JustificacaoHoras implements IStrategyJustificacoes {
	public JustificacaoHoras() {
	}

	public void completaListaMarcacoes(Justificacao justificacao, ArrayList listaMarcacoesPonto) {
		MarcacaoPonto entrada = new MarcacaoPonto(0, new Timestamp(justificacao.getHoraInicio().getTime()), 0);
		MarcacaoPonto saida = new MarcacaoPonto(0, new Timestamp(justificacao.getHoraFim().getTime()), 0);
		listaMarcacoesPonto.add(entrada);
		listaMarcacoesPonto.add(saida);
	} /* completaListaMarcacoes */

	public void setJustificacao(Justificacao justificacao, ActionForm form) {
		IntroduzirJustificacaoForm formJustificacao = (IntroduzirJustificacaoForm) form;
		Calendar calendario = Calendar.getInstance();
		calendario.setLenient(false);

		calendario.clear();
		calendario.set(
			(new Integer(formJustificacao.getAnoInicio())).intValue(),
			(new Integer(formJustificacao.getMesInicio())).intValue() - 1,
			(new Integer(formJustificacao.getDiaInicio())).intValue(),
			00,
			00,
			00);
		justificacao.setDiaInicio(calendario.getTime());

		calendario.clear();
		calendario.set(
			(new Integer(formJustificacao.getAnoFim())).intValue(),
			(new Integer(formJustificacao.getMesFim())).intValue() - 1,
			(new Integer(formJustificacao.getDiaFim())).intValue(),
			00,
			00,
			00);
		justificacao.setDiaFim(calendario.getTime());

		calendario.clear();
		calendario.set(
			1970,
			0,
			1,
			new Integer(formJustificacao.getHoraInicio()).intValue(),
			new Integer(formJustificacao.getMinutosInicio()).intValue(),
			00);
		justificacao.setHoraInicio(new Time(calendario.getTimeInMillis()));

		calendario.clear();
		calendario.set(1970, 0, 1, new Integer(formJustificacao.getHoraFim()).intValue(), new Integer(formJustificacao.getMinutosFim()).intValue(), 00);
		justificacao.setHoraFim(new Time(calendario.getTimeInMillis()));

		if (formJustificacao.getObservacao().length() < 1) {
			justificacao.setObservacao(String.valueOf(""));
		} else {
			justificacao.setObservacao(formJustificacao.getObservacao());
		}
	} /* setJustificacao */

	public void setListaJustificacoesBody(ParamJustificacao paramJustificacao, Justificacao justificacao, ArrayList listaJustificacoesBody) {
		Calendar calendario = Calendar.getInstance();

		listaJustificacoesBody.add(0, paramJustificacao.getSigla());
		listaJustificacoesBody.add(1, paramJustificacao.getTipo());

		calendario.clear();
		calendario.setTime(justificacao.getDiaInicio());

		listaJustificacoesBody.add(2, FormataCalendar.data(calendario));

		if (justificacao.getDiaFim() != null) {
			calendario.clear();
			calendario.setTime(justificacao.getDiaFim());
			listaJustificacoesBody.add(3, FormataCalendar.data(calendario));
		} else {
			listaJustificacoesBody.add(3, new String("&nbsp;"));
		}
		calendario.clear();
		calendario.setTimeInMillis(justificacao.getHoraInicio().getTime());
		listaJustificacoesBody.add(4, FormataCalendar.horasMinutos(calendario));

		calendario.clear();
		calendario.setTimeInMillis(justificacao.getHoraFim().getTime());
		listaJustificacoesBody.add(5, FormataCalendar.horasMinutos(calendario));
	} /* setListaJustificacoesBody */

	public void updateSaldosHorarioVerbeteBody(
		Justificacao justificacao,
		ParamJustificacao paramJustificacao,
		Horario horario,
		ArrayList listaRegimes,
		ArrayList listaMarcacoesPonto,
		ArrayList listaSaldos) {
		// saldo do horario normal
		long saldo = ((Long) listaSaldos.get(0)).longValue() + (justificacao.getHoraFim().getTime() - justificacao.getHoraInicio().getTime());
		listaSaldos.set(0, new Long(saldo));
		/*
				// saldo da plataforma fixa e periodo de refeicao caso exista
				IStrategyHorarios horarioStrategy = SuporteStrategyHorarios.getInstance().callStrategy(horario.getModalidade());
				horarioStrategy.updateSaldosHorarioVerbeteBody(justificacao, horario, listaRegimes, listaMarcacoesPonto, listaSaldos);
		*/
	} /* updateSaldosHorarioVerbeteBody */

	public ActionErrors validateIntroduzirJustificacao(ActionForm form) {
		/*
		 * preenche hoea inicio e hora fim
		 * preenche data inicio
		 * permite haver varios dias a justificar algumas horas
		 * isto acontece teoricamente e nao esta testado na exaustao
		 */
		IntroduzirJustificacaoForm formJustificacao = (IntroduzirJustificacaoForm) form;
		ActionErrors errors = new ActionErrors();

		int horaInicio = 0;
		int minutosInicio = 0;
		int diaInicio = 0;
		int mesInicio = 0;
		int anoInicio = 0;
		int horaFim = 0;
		int minutosFim = 0;
		int diaFim = 0;
		int mesFim = 0;
		int anoFim = 0;

		if ((formJustificacao.getHoraInicio().length() < 1)
			|| (formJustificacao.getMinutosInicio().length() < 1)
			|| (formJustificacao.getDiaInicio().length() < 1)
			|| (formJustificacao.getMesInicio().length() < 1)
			|| (formJustificacao.getAnoInicio().length() < 1)
			|| (formJustificacao.getHoraFim().length() < 1)
			|| (formJustificacao.getMinutosFim().length() < 1)) {
			errors.add("dates", new ActionError("error.campos.justificacaoHoras"));
		} else {
			if (!((formJustificacao.getDiaFim().length() > 0)
				&& (formJustificacao.getMesFim().length() > 0)
				&& (formJustificacao.getAnoFim().length() > 0))) {
				errors.add("dates", new ActionError("error.campos.justificacaoHoras"));
			} else {
				try {
					diaFim = (new Integer(formJustificacao.getDiaFim())).intValue();
					mesFim = (new Integer(formJustificacao.getMesFim())).intValue();
					anoFim = (new Integer(formJustificacao.getAnoFim())).intValue();
				} catch (java.lang.NumberFormatException e) {
					errors.add("numero", new ActionError("error.numero.naoInteiro"));
				}
			}
			try {
				horaInicio = (new Integer(formJustificacao.getHoraInicio())).intValue();
				minutosInicio = (new Integer(formJustificacao.getMinutosInicio())).intValue();
				diaInicio = (new Integer(formJustificacao.getDiaInicio())).intValue();
				mesInicio = (new Integer(formJustificacao.getMesInicio())).intValue();
				anoInicio = (new Integer(formJustificacao.getAnoInicio())).intValue();
				horaFim = (new Integer(formJustificacao.getHoraFim())).intValue();
				minutosFim = (new Integer(formJustificacao.getMinutosFim())).intValue();
			} catch (java.lang.NumberFormatException e) {
				errors.add("numero", new ActionError("error.numero.naoInteiro"));
			}
			try {
				Calendar calendarInicio = Calendar.getInstance();
				Calendar calendarFim = Calendar.getInstance();
				calendarInicio.setLenient(false);
				calendarFim.setLenient(false);

				calendarInicio.clear();
				calendarInicio.set(anoInicio, mesInicio - 1, diaInicio, 00, 00, 00);

				if ((formJustificacao.getDiaFim().length() > 0)
					&& (formJustificacao.getMesFim().length() > 0)
					&& (formJustificacao.getAnoFim().length() > 0)) {
					calendarFim.clear();
					calendarFim.set(anoFim, mesFim - 1, diaFim, 00, 00, 00);

					if (!(calendarInicio.getTimeInMillis() <= calendarFim.getTimeInMillis())) {
						errors.add("datas", new ActionError("error.dataValidade.incorrecta"));
					}
				}
				calendarInicio.clear();
				calendarInicio.set(1970, 0, 1, horaInicio, minutosInicio, 00);

				calendarFim.clear();
				calendarFim.set(1970, 0, 1, horaFim, minutosFim, 00);

				if (!(calendarInicio.before((Calendar) calendarFim))) {
					errors.add("horas", new ActionError("error.intervaloJustificacao.incorrecto"));
				}
			} catch (java.lang.IllegalArgumentException ee) {
				errors.add("datas", new ActionError("error.data.horas"));
			}
		}
		return errors;
	} /* validateIntroduzirJustificacao */
	
	public String formataDuracao(Horario horario, Justificacao justificacao) {
		
		Calendar calendario = Calendar.getInstance();
		calendario.setLenient(false);
		calendario.clear();
		calendario.setTimeInMillis(justificacao.getHoraFim().getTime() - justificacao.getHoraInicio().getTime());
		calendario.add(Calendar.HOUR_OF_DAY, -1);
		Time diferenca = new Time(calendario.getTimeInMillis());

		String duracao =
			diferenca.toString().substring(0, diferenca.toString().lastIndexOf(":"));
		
		int indice = duracao.length();
		while(indice < Constants.MAX_DURACAO){
			duracao = "0" + duracao;
		
			indice++;
		} 
				
		return duracao;
	}	/* formataDuracao */
}
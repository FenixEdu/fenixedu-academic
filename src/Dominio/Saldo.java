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
public class Saldo implements IStrategyJustificacoes {
	public Saldo() { }
  
	public void completaListaMarcacoes(Timestamp dataConsulta, Justificacao justificacao, ArrayList listaMarcacoesPonto) {
	} /* completaListaMarcacoes */
	
	public void setJustificacao(Justificacao justificacao, ActionForm form) {
		IntroduzirJustificacaoForm formJustificacao = (IntroduzirJustificacaoForm) form;
		Calendar calendario = Calendar.getInstance();
		calendario.setLenient(false);
    
		calendario.clear();
		calendario.set((new Integer(formJustificacao.getAnoInicio())).intValue(), (new Integer(formJustificacao.getMesInicio())).intValue()-1, (new Integer(formJustificacao.getDiaInicio())).intValue(), 00, 00, 00);
		justificacao.setDiaInicio(calendario.getTime());
		justificacao.setDiaFim(calendario.getTime());

		calendario.clear();
		calendario.set(1970, 0, 1,new Integer(formJustificacao.getHoraInicio()).intValue(), new Integer(formJustificacao.getMinutosInicio()).intValue(), 00);
		justificacao.setHoraInicio(new Time(calendario.getTimeInMillis()));
    
		if(formJustificacao.getObservacao().length() < 1){
			justificacao.setObservacao(String.valueOf(""));
		} else{
			justificacao.setObservacao(formJustificacao.getObservacao());
		}
	} /* setJustificacao */
    
	public void setListaJustificacoesBody(ParamJustificacao paramJustificacao, Justificacao justificacao, ArrayList listaJustificacoesBody) {
		Calendar calendario = Calendar.getInstance();
    
		listaJustificacoesBody.add(1, paramJustificacao.getSigla());
		listaJustificacoesBody.add(2, paramJustificacao.getDescricao());
		listaJustificacoesBody.add(3, paramJustificacao.getTipo());
    
		calendario.clear();
		calendario.setTime(justificacao.getDiaInicio());
		listaJustificacoesBody.add(4, FormataCalendar.data(calendario));
		listaJustificacoesBody.add(5, new String(""));
    
		calendario.clear();
		calendario.setTimeInMillis(justificacao.getHoraInicio().getTime());
		listaJustificacoesBody.add(6, FormataCalendar.horasMinutosDuracao(calendario));
		listaJustificacoesBody.add(7, new String(""));
	} /* setListaJustificacoesBody */
  
	public void updateSaldosHorarioVerbeteBody(Justificacao justificacao, ParamJustificacao paramJustificacao, Horario horario, ArrayList listaRegimes, ArrayList listaMarcacoesPonto, ArrayList listaSaldos) {
		Calendar calendario = Calendar.getInstance();
		calendario.setLenient(false);
    
		if(paramJustificacao.getSigla().equals("COMP")){
			calendario.clear();

			IStrategyHorarios horarioStrategy = SuporteStrategyHorarios.getInstance().callStrategy(horario.getModalidade());
			long duracaoPF = horarioStrategy.duracaoPF(horario, listaRegimes);
			if(justificacao.getHoraInicio().getTime() > duracaoPF){
				justificacao.setHoraInicio(new Time(duracaoPF));
			}
			
			long tempo = ((Long)listaSaldos.get(1)).longValue() + justificacao.getHoraInicio().getTime();
			if(tempo <= 0){
				listaSaldos.set(1, new Long(tempo));
			} else{
				listaSaldos.set(1, new Long(0));      
			}
		}
	} /* updateSaldosHorarioVerbeteBody */
  
	public ActionErrors validateIntroduzirJustificacao(ActionForm form) {
		/*
		 * preenche so a hora inicio
		 * preenche so a data inicio
		 */
		IntroduzirJustificacaoForm formJustificacao = (IntroduzirJustificacaoForm) form;
		ActionErrors errors = new ActionErrors();
    
		int horaInicio = 0;
		int minutosInicio = 0;
		int diaInicio = 0;
		int mesInicio = 0;
		int anoInicio = 0;
    
		if(((formJustificacao.getHoraInicio().length() < 1) || (formJustificacao.getMinutosInicio().length() < 1) ||
		(formJustificacao.getDiaInicio().length() < 1) ||(formJustificacao.getMesInicio().length() < 1) || (formJustificacao.getAnoInicio().length() < 1)) ||
		((formJustificacao.getDiaFim().length() > 0) ||(formJustificacao.getMesFim().length() > 0) || (formJustificacao.getAnoFim().length() > 0) ||
		(formJustificacao.getHoraFim().length() > 0) || (formJustificacao.getMinutosFim().length() > 0))){
			errors.add("campos", new ActionError("error.camposSaldo.preencher"));
		} else {
			try {
				horaInicio = (new Integer(formJustificacao.getHoraInicio())).intValue();
				minutosInicio = (new Integer(formJustificacao.getMinutosInicio())).intValue();
				diaInicio = (new Integer(formJustificacao.getDiaInicio())).intValue();
				mesInicio = (new Integer(formJustificacao.getMesInicio())).intValue();
				anoInicio = (new Integer(formJustificacao.getAnoInicio())).intValue();
			} catch (java.lang.NumberFormatException e) {
				errors.add("numero", new ActionError("error.numero.naoInteiro"));
			}
			try{
				Calendar calendarInicio = Calendar.getInstance();
				calendarInicio.setLenient(false);
        
				calendarInicio.clear();
				calendarInicio.set(anoInicio, mesInicio-1, diaInicio, 00, 00, 00);
        
				calendarInicio.clear();
				calendarInicio.set(1970, 0, 1, horaInicio, minutosInicio, 00);
        
			}catch(java.lang.IllegalArgumentException ee){
				errors.add("datas", new ActionError("error.data.horas"));
			}
		}
		return errors;
	} /* validateIntroduzirJustificacao */
	
	public String formataDuracao(Horario horario, Justificacao justificacao) {	
		Calendar calendario = Calendar.getInstance();
		calendario.setLenient(false);
		calendario.clear();
		calendario.setTimeInMillis(justificacao.getHoraInicio().getTime());
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
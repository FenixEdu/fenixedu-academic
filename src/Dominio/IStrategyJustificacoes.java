package Dominio;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IStrategyJustificacoes {

	public void completaListaMarcacoes(Timestamp dataConsulta, Justificacao justificacao, ArrayList listaMarcacoesPonto);
	public void setJustificacao(Justificacao justificacao, ActionForm form);
	public void setListaJustificacoesBody(ParamJustificacao paramJustificacao, Justificacao justificacao, ArrayList listaJustificacoesBody);
	public void updateSaldosHorarioVerbeteBody(Justificacao justificacao, ParamJustificacao paramJustificacao, Horario horario, ArrayList listaRegimes, ArrayList listaMarcacoesPonto, ArrayList listaSaldos);
	public ActionErrors validateIntroduzirJustificacao(ActionForm form);
	public String formataDuracao(Horario horario, Justificacao justificacao); 
}
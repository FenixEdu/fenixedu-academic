package Dominio;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import ServidorApresentacao.formbeans.assiduousness.IntroduzirJustificacaoForm;
import ServidorApresentacao.formbeans.assiduousness.LerDadosJustificacaoForm;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IStrategyJustificacoes {

	public void completaListaMarcacoes(Timestamp dataConsulta, Justificacao justificacao, ArrayList listaMarcacoesPonto);
	public void setJustificacao(Justificacao justificacao, IntroduzirJustificacaoForm form);
	public void setJustificacao(Justificacao justificacao, LerDadosJustificacaoForm form);
	public void setListaJustificacoesBody(ParamJustificacao paramJustificacao, Justificacao justificacao, ArrayList listaJustificacoesBody);
	public void updateSaldosHorarioVerbeteBody(Justificacao justificacao, ParamJustificacao paramJustificacao, Horario horario, ArrayList listaRegimes, ArrayList listaMarcacoesPonto, ArrayList listaSaldos);
	public ActionErrors validateFormJustificacao(IntroduzirJustificacaoForm form);
	public ActionErrors validateFormJustificacao(LerDadosJustificacaoForm form);
	public String formataDuracao(Horario horario, Justificacao justificacao);
	public void setLerDadosJustificacaoForm(Justificacao justificacao, ActionForm lerDadosJustificacaoForm); 
}
package Dominio;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public class Desfasado implements IStrategyHorarios {
	//TODO: Implementar o horario desfasado
	public Desfasado() {
	}

	public ActionErrors validateAssociarHorario(ActionForm form) {
		ActionErrors errors = new ActionErrors();
		return errors;
	} /* validateAssociarHorario */

	public ActionErrors validateAssociarHorarioTipo(
		ActionForm form,
		HorarioTipo horarioTipo,
		ArrayList listaRegimes) {
		ActionErrors errors = new ActionErrors();
		return errors;
	} /* validateAssociarHorarioTipo */

	public void setHorario(Horario horario, ActionForm form) {
	} /* setHorario */

	public void setHorarioTipoRotativo(Horario horario, ActionForm form) {
	} /* setHorarioRotativo */

	public void setDatasHorario(Horario horario, ActionForm form) {
	} /* setDatasHorario */

	public void setFormAssociarHorarioConfirmar(
		Locale locale,
		ActionForm form,
		Pessoa pessoa,
		Funcionario funcionario,
		Horario horario,
		ArrayList listaRegime,
		boolean isExcepcao) {
	} /* setFormAssociarHorarioConfirmar */

	public void setFormAssociarHorarioTipoConfirmar(
		Locale locale,
		ActionForm form,
		Pessoa pessoa,
		Funcionario funcionario,
		HorarioTipo horarioTipo,
		Horario horario,
		ArrayList listaRegime,
		boolean isExcepcao) {
	} /* setFormAssociarHorarioTipoConfirmar */

	public String descricaoHorario(Locale locale, Horario horario, ArrayList listaRegimes) {
		String descricao = null;

		return descricao;
	} /* descricaoHorario */

	public String descricaoHorarioTipo(
		Locale locale,
		HorarioTipo horarioTipo,
		ArrayList listaRegimes) {
		return null;
	} /* descricaoHorarioTipo */

	public String periodoTrabalhoHorarioTipo(HorarioTipo horarioTipo) {
		return null;
	} /* periodoTrabalhoHorarioTipo */

	public void setHorarioTipoFormConsultarFuncionarioMostrar(
		ActionForm form,
		HorarioTipo horarioTipo,
		Horario horario,
		ArrayList listaRegime) {
	} /* setHorarioTipoFormConsultarFuncionarioMostrar */

	public void setSaldosHorarioVerbeteBody(
		Horario horario,
		ArrayList listaRegimes,
		ArrayList listaParamJustificacoes,
		ArrayList listaMarcacoesPonto,
		ArrayList listaSaldos) {
	} /* setSaldosHorarioVerbeteBody */

	public long calcularTrabalhoNocturno(
		Horario horario,
		MarcacaoPonto entrada,
		MarcacaoPonto saida) {
		return 0;
	} /* calcularTrabalhoNocturno */

	public void calcularHorasExtraordinarias(
		Horario horario,
		ArrayList listaMarcacoesPonto,
		ArrayList listaSaldos) {
	} /* calcularHorasExtraorinarias */

	public long duracaoPF(Horario horario, ArrayList listaRegimes) {
		return 0;
	} /* duracaoPF */

	public long duracaoDiaria(Horario horario) {
		return 0;
	} /* duracaoDiaria */

	public int mapeamentoFechoMes(Horario horario, ArrayList listaRegimes) {
		return 0;
	} /* mapeamentoFechoMes */
}
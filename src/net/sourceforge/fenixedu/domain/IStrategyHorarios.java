package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IStrategyHorarios
{

	public ActionErrors validateAssociarHorario(ActionForm form);
	public ActionErrors validateAssociarHorarioTipo(
		ActionForm form,
		HorarioTipo horarioTipo,
		ArrayList listaRegimes);

	public void setHorario(Horario horario, ActionForm form);
	public void setHorarioTipoRotativo(Horario horario, ActionForm form);
	public void setDatasHorario(Horario horario, ActionForm form);

	public void setFormAssociarHorarioConfirmar(
		Locale locale,
		ActionForm form,
		Person pessoa,
		Funcionario funcionario,
		Horario horario,
		ArrayList listaRegime,
		boolean isExcepcao,
		String alterar);
	public void setFormAssociarHorarioTipoConfirmar(
		Locale locale,
		ActionForm form,
		Person pessoa,
		Funcionario funcionario,
		HorarioTipo horarioTipo,
		Horario horario,
		ArrayList listaRegime,
		boolean isExcepcao);

	public String descricaoHorario(Locale locale, Horario horario, ArrayList listaRegimes);
	public String descricaoHorarioTipo(Locale locale, HorarioTipo horarioTipo, ArrayList listaRegimes);
	public String periodoTrabalhoHorarioTipo(HorarioTipo horarioTipo);

	public void setSaldosHorarioVerbeteBody(
		Horario horario,
		ArrayList listaRegimes,
		ArrayList listaParamJustificacoes,
		ArrayList listaMarcacoesPonto,
		ArrayList listaSaldos);
	public long calcularTrabalhoNocturno(Horario horario, MarcacaoPonto entrada, MarcacaoPonto saida);
	public void calcularHorasExtraordinarias(
		Horario horario,
		ArrayList listaMarcacoesPonto,
		ArrayList listaSaldos);

	public long limitaTrabalhoSeguido(Horario _horario, long entrada, long saida, boolean limita);
	public long duracaoPF(Horario horario, ArrayList listaRegimes);
	public long duracaoDiaria(Horario horario);

	public int mapeamentoFechoMes(Horario horario, ArrayList listaRegimes);
	public boolean isNocturno(Horario horario);
	public int marcacoesObrigatorias(Horario horario);
}
package Dominio;

import java.util.List;
import java.util.Locale;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IStrategyHorarios {

    public ActionErrors validateAssociarHorario(ActionForm form);

    public ActionErrors validateAssociarHorarioTipo(ActionForm form, HorarioTipo horarioTipo,
            List listaRegimes);

    public void setHorario(Horario horario, ActionForm form);

    public void setHorarioTipoRotativo(Horario horario, ActionForm form);

    public void setDatasHorario(Horario horario, ActionForm form);

    public void setFormAssociarHorarioConfirmar(Locale locale, ActionForm form, Pessoa pessoa,
            Funcionario funcionario, Horario horario, List listaRegime, boolean isExcepcao,
            String alterar);

    public void setFormAssociarHorarioTipoConfirmar(Locale locale, ActionForm form, Pessoa pessoa,
            Funcionario funcionario, HorarioTipo horarioTipo, Horario horario, List listaRegime,
            boolean isExcepcao);

    public String descricaoHorario(Locale locale, Horario horario, List listaRegimes);

    public String descricaoHorarioTipo(Locale locale, HorarioTipo horarioTipo, List listaRegimes);

    public String periodoTrabalhoHorarioTipo(HorarioTipo horarioTipo);

    public void setSaldosHorarioVerbeteBody(Horario horario, List listaRegimes,
            List listaParamJustificacoes, List listaMarcacoesPonto, List listaSaldos);

    public long calcularTrabalhoNocturno(Horario horario, MarcacaoPonto entrada, MarcacaoPonto saida);

    public void calcularHorasExtraordinarias(Horario horario, List listaMarcacoesPonto, List listaSaldos);

    public long limitaTrabalhoSeguido(Horario _horario, long entrada, long saida, boolean limita);

    public long duracaoPF(Horario horario, List listaRegimes);

    public long duracaoDiaria(Horario horario);

    public int mapeamentoFechoMes(Horario horario, List listaRegimes);
}
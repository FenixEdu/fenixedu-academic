package Dominio;

import java.util.List;
import java.util.Locale;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class Rigido implements IStrategyHorarios {
    //TODO: Implementar o horario rigido
    public ActionErrors validateAssociarHorario(ActionForm form) {
        return null;
    } /* validateAssociarHorario */

    public ActionErrors validateAssociarHorarioTipo(ActionForm form, HorarioTipo horarioTipo,
            List listaRegimes) {
        return null;
    } /* validateAssociarHorarioTipo */

    public void setHorario(Horario horario, ActionForm form) {

    } /* setHorario */

    public void setHorarioTipoRotativo(Horario horario, ActionForm form) {

    } /* setHorarioTipoRotativo */

    public void setDatasHorario(Horario horario, ActionForm form) {

    } /* setDatasHorario */

    public void setFormAssociarHorarioConfirmar(Locale locale, ActionForm form, Pessoa pessoa,
            Funcionario funcionario, Horario horario, List listaRegime, boolean isExcepcao,
            String alterar) {

    } /* setFormAssociarHorarioConfirmar */

    public void setFormAssociarHorarioTipoConfirmar(Locale locale, ActionForm form, Pessoa pessoa,
            Funcionario funcionario, HorarioTipo horarioTipo, Horario horario, List listaRegime,
            boolean isExcepcao) {

    } /* setFormAssociarHorarioTipoConfirmar */

    public String descricaoHorario(Locale locale, Horario horario, List listaRegimes) {
        return null;
    } /* descricaoHorario */

    public String descricaoHorarioTipo(Locale locale, HorarioTipo horarioTipo, List listaRegimes) {
        return null;
    } /* descricaoHorarioTipo */

    public String periodoTrabalhoHorarioTipo(HorarioTipo horarioTipo) {
        return null;
    } /* periodoTrabalhoHorarioTipo */

    public void setSaldosHorarioVerbeteBody(Horario horario, List listaRegimes,
            List listaParamJustificacoes, List listaMarcacoesPonto, List listaSaldos) {
    } /* setSaldosHorarioVerbeteBody */

    public long calcularTrabalhoNocturno(Horario horario, MarcacaoPonto entrada, MarcacaoPonto saida) {
        return 0;
    } /* calcularTrabalhoNocturno */

    public void calcularHorasExtraordinarias(Horario horario, List listaMarcacoesPonto, List listaSaldos) {
    } /* calcularHorasExtraorinarias */

    public long limitaTrabalhoSeguido(Horario horario, long entrada, long saida, boolean limita) {
        return 0;
    } /* limitaTrabalhoSeguido */

    public long duracaoPF(Horario horario, List listaRegimes) {
        return 0;
    } /* duracaoPF */

    public long duracaoDiaria(Horario horario) {
        return 0;
    } /* duracaoDiaria */

    public int mapeamentoFechoMes(Horario horario, List listaRegimes) {
        return 0;
    } /* mapeamentoFechoMes */
}
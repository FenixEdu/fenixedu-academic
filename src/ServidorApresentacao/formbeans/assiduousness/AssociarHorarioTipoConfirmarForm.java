package ServidorApresentacao.formbeans.assiduousness;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.struts.action.ActionForm;

import Dominio.FuncNaoDocente;
import Dominio.Funcionario;
import Dominio.Horario;
import Dominio.HorarioTipo;
import Dominio.IStrategyHorarios;
import Dominio.Pessoa;
import Dominio.SuporteStrategyHorarios;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class AssociarHorarioTipoConfirmarForm extends ActionForm {
    private String _nome = null;

    private String _numMecanografico = null;

    private String _sigla = null;

    private String _inicioExpedienteHoras = null;

    private String _inicioExpedienteMinutos = null;

    private String _fimExpedienteHoras = null;

    private String _fimExpedienteMinutos = null;

    private String _diaAnteriorExpediente = null;

    private String _diaSeguinteExpediente = null;

    private String _inicioRefeicaoHoras = null;

    private String _inicioRefeicaoMinutos = null;

    private String _fimRefeicaoHoras = null;

    private String _fimRefeicaoMinutos = null;

    private String _diaAnteriorRefeicao = null;

    private String _diaSeguinteRefeicao = null;

    private String _intervaloMinimoHoras = null;

    private String _intervaloMinimoMinutos = null;

    private String _descontoObrigatorioHoras = null;

    private String _descontoObrigatorioMinutos = null;

    private String _inicioHN1Horas = null;

    private String _inicioHN1Minutos = null;

    private String _fimHN1Horas = null;

    private String _fimHN1Minutos = null;

    private String _diaAnteriorHN1 = null;

    private String _diaSeguinteHN1 = null;

    private String _inicioHN2Horas = null;

    private String _inicioHN2Minutos = null;

    private String _fimHN2Horas = null;

    private String _fimHN2Minutos = null;

    private String _diaAnteriorHN2 = null;

    private String _diaSeguinteHN2 = null;

    private String _inicioPF1Horas = null;

    private String _inicioPF1Minutos = null;

    private String _fimPF1Horas = null;

    private String _fimPF1Minutos = null;

    private String _diaAnteriorPF1 = null;

    private String _diaSeguintePF1 = null;

    private String _inicioPF2Horas = null;

    private String _inicioPF2Minutos = null;

    private String _fimPF2Horas = null;

    private String _fimPF2Minutos = null;

    private String _diaAnteriorPF2 = null;

    private String _diaSeguintePF2 = null;

    private String _diaInicio = null;

    private String _mesInicio = null;

    private String _anoInicio = null;

    private String _diaFim = null;

    private String _mesFim = null;

    private String _anoFim = null;

    private String _trabalhoConsecutivoHoras = null;

    private String _trabalhoConsecutivoMinutos = null;

    private String _duracaoSemanal = null;

    private String _modalidade = null;

    private List _listaRegime = new ArrayList();

    private boolean _excepcaoHorario = false;

    /**
     * @return
     */
    public String getTrabalhoConsecutivoHoras() {
        return _trabalhoConsecutivoHoras;
    }

    /**
     * @param consecutivoHoras
     */
    public void setTrabalhoConsecutivoHoras(String consecutivoHoras) {
        _trabalhoConsecutivoHoras = consecutivoHoras;
    }

    /**
     * @return
     */
    public String getTrabalhoConsecutivoMinutos() {
        return _trabalhoConsecutivoMinutos;
    }

    /**
     * @param consecutivoMinutos
     */
    public void setTrabalhoConsecutivoMinutos(String consecutivoMinutos) {
        _trabalhoConsecutivoMinutos = consecutivoMinutos;
    }

    public String getNome() {
        return (_nome);
    }

    public String getNumMecanografico() {
        return (_numMecanografico);
    }

    public String getSigla() {
        return (_sigla);
    }

    public String getInicioExpedienteHoras() {
        return (_inicioExpedienteHoras);
    }

    public String getInicioExpedienteMinutos() {
        return (_inicioExpedienteMinutos);
    }

    public String getFimExpedienteHoras() {
        return (_fimExpedienteHoras);
    }

    public String getFimExpedienteMinutos() {
        return (_fimExpedienteMinutos);
    }

    public String getDiaAnteriorExpediente() {
        return (_diaAnteriorExpediente);
    }

    public String getDiaSeguinteExpediente() {
        return (_diaSeguinteExpediente);
    }

    public String getInicioRefeicaoHoras() {
        return _inicioRefeicaoHoras;
    }

    public String getInicioRefeicaoMinutos() {
        return _inicioRefeicaoMinutos;
    }

    public String getFimRefeicaoHoras() {
        return _fimRefeicaoHoras;
    }

    public String getFimRefeicaoMinutos() {
        return _fimRefeicaoMinutos;
    }

    public String getDiaAnteriorRefeicao() {
        return _diaAnteriorRefeicao;
    }

    public String getDiaSeguinteRefeicao() {
        return _diaSeguinteRefeicao;
    }

    public String getIntervaloMinimoHoras() {
        return _intervaloMinimoHoras;
    }

    public String getIntervaloMinimoMinutos() {
        return _intervaloMinimoMinutos;
    }

    public String getDescontoObrigatorioHoras() {
        return _descontoObrigatorioHoras;
    }

    public String getDescontoObrigatorioMinutos() {
        return _descontoObrigatorioMinutos;
    }

    public String getInicioHN1Horas() {
        return (_inicioHN1Horas);
    }

    public String getInicioHN1Minutos() {
        return (_inicioHN1Minutos);
    }

    public String getFimHN1Horas() {
        return (_fimHN1Horas);
    }

    public String getFimHN1Minutos() {
        return (_fimHN1Minutos);
    }

    public String getInicioHN2Horas() {
        return (_inicioHN2Horas);
    }

    public String getInicioHN2Minutos() {
        return (_inicioHN2Minutos);
    }

    public String getFimHN2Horas() {
        return (_fimHN2Horas);
    }

    public String getFimHN2Minutos() {
        return (_fimHN2Minutos);
    }

    public String getDiaAnteriorHN1() {
        return _diaAnteriorHN1;
    }

    public String getDiaAnteriorHN2() {
        return _diaAnteriorHN2;
    }

    public String getDiaSeguinteHN1() {
        return _diaSeguinteHN1;
    }

    public String getDiaSeguinteHN2() {
        return _diaSeguinteHN2;
    }

    public String getInicioPF1Horas() {
        return (_inicioPF1Horas);
    }

    public String getInicioPF1Minutos() {
        return (_inicioPF1Minutos);
    }

    public String getFimPF1Horas() {
        return (_fimPF1Horas);
    }

    public String getFimPF1Minutos() {
        return (_fimPF1Minutos);
    }

    public String getInicioPF2Horas() {
        return (_inicioPF2Horas);
    }

    public String getInicioPF2Minutos() {
        return (_inicioPF2Minutos);
    }

    public String getFimPF2Horas() {
        return (_fimPF2Horas);
    }

    public String getFimPF2Minutos() {
        return (_fimPF2Minutos);
    }

    public String getDiaAnteriorPF1() {
        return _diaAnteriorPF1;
    }

    public String getDiaAnteriorPF2() {
        return _diaAnteriorPF2;
    }

    public String getDiaSeguintePF1() {
        return _diaSeguintePF1;
    }

    public String getDiaSeguintePF2() {
        return _diaSeguintePF2;
    }

    public String getDiaInicio() {
        return (_diaInicio);
    }

    public String getMesInicio() {
        return (_mesInicio);
    }

    public String getAnoInicio() {
        return (_anoInicio);
    }

    public String getDiaFim() {
        return (_diaFim);
    }

    public String getMesFim() {
        return (_mesFim);
    }

    public String getAnoFim() {
        return (_anoFim);
    }

    public String getDuracaoSemanal() {
        return (_duracaoSemanal);
    }

    public String getModalidade() {
        return (_modalidade);
    }

    public List getListaRegime() {
        return (_listaRegime);
    }

    public boolean isExcepcaoHorario() {
        return _excepcaoHorario;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public void setNumMecanografico(String numMecanografico) {
        _numMecanografico = numMecanografico;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public void setInicioExpedienteHoras(String inicioExpedienteHoras) {
        _inicioExpedienteHoras = inicioExpedienteHoras;
    }

    public void setInicioExpedienteMinutos(String inicioExpedienteMinutos) {
        _inicioExpedienteMinutos = inicioExpedienteMinutos;
    }

    public void setFimExpedienteHoras(String fimExpedienteHoras) {
        _fimExpedienteHoras = fimExpedienteHoras;
    }

    public void setFimExpedienteMinutos(String fimExpedienteMinutos) {
        _fimExpedienteMinutos = fimExpedienteMinutos;
    }

    public void setDiaAnteriorExpediente(String diaAnteriorExpediente) {
        _diaAnteriorExpediente = diaAnteriorExpediente;
    }

    public void setDiaSeguinteExpediente(String diaSeguinteExpediente) {
        _diaSeguinteExpediente = diaSeguinteExpediente;
    }

    public void setInicioRefeicaoHoras(String _inicioRefeicaoHoras) {
        this._inicioRefeicaoHoras = _inicioRefeicaoHoras;
    }

    public void setInicioRefeicaoMinutos(String _inicioRefeicaoMinutos) {
        this._inicioRefeicaoMinutos = _inicioRefeicaoMinutos;
    }

    public void setFimRefeicaoHoras(String _fimRefeicaoHoras) {
        this._fimRefeicaoHoras = _fimRefeicaoHoras;
    }

    public void setFimRefeicaoMinutos(String _fimRefeicaoMinutos) {
        this._fimRefeicaoMinutos = _fimRefeicaoMinutos;
    }

    public void setDiaAnteriorRefeicao(String diaAnteriorRefeicao) {
        _diaAnteriorRefeicao = diaAnteriorRefeicao;
    }

    public void setDiaSeguinteRefeicao(String diaSeguinteRefeicao) {
        _diaSeguinteRefeicao = diaSeguinteRefeicao;
    }

    public void setIntervaloMinimoHoras(String _intervaloMinimoHoras) {
        this._intervaloMinimoHoras = _intervaloMinimoHoras;
    }

    public void setIntervaloMinimoMinutos(String _intervaloMinimoMinutos) {
        this._intervaloMinimoMinutos = _intervaloMinimoMinutos;
    }

    public void setDescontoObrigatorioHoras(String _descontoObrigatorioHoras) {
        this._descontoObrigatorioHoras = _descontoObrigatorioHoras;
    }

    public void setDescontoObrigatorioMinutos(String _descontoObrigatorioMinutos) {
        this._descontoObrigatorioMinutos = _descontoObrigatorioMinutos;
    }

    public void setInicioHN1Horas(String inicioHN1Horas) {
        _inicioHN1Horas = inicioHN1Horas;
    }

    public void setInicioHN1Minutos(String inicioHN1Minutos) {
        _inicioHN1Minutos = inicioHN1Minutos;
    }

    public void setFimHN1Horas(String fimHN1Horas) {
        _fimHN1Horas = fimHN1Horas;
    }

    public void setFimHN1Minutos(String fimHN1Minutos) {
        _fimHN1Minutos = fimHN1Minutos;
    }

    public void setInicioHN2Horas(String inicioHN2Horas) {
        _inicioHN2Horas = inicioHN2Horas;
    }

    public void setInicioHN2Minutos(String inicioHN2Minutos) {
        _inicioHN2Minutos = inicioHN2Minutos;
    }

    public void setFimHN2Horas(String fimHN2Horas) {
        _fimHN2Horas = fimHN2Horas;
    }

    public void setFimHN2Minutos(String fimHN2Minutos) {
        _fimHN2Minutos = fimHN2Minutos;
    }

    public void setDiaAnteriorHN1(String diaAnteriorHN1) {
        _diaAnteriorHN1 = diaAnteriorHN1;
    }

    public void setDiaAnteriorHN2(String diaAnteriorHN2) {
        _diaAnteriorHN2 = diaAnteriorHN2;
    }

    public void setDiaSeguinteHN1(String diaSeguinteHN1) {
        _diaSeguinteHN1 = diaSeguinteHN1;
    }

    public void setDiaSeguinteHN2(String diaSeguinteHN2) {
        _diaSeguinteHN2 = diaSeguinteHN2;
    }

    public void setInicioPF1Horas(String inicioPF1Horas) {
        _inicioPF1Horas = inicioPF1Horas;
    }

    public void setInicioPF1Minutos(String inicioPF1Minutos) {
        _inicioPF1Minutos = inicioPF1Minutos;
    }

    public void setFimPF1Horas(String fimPF1Horas) {
        _fimPF1Horas = fimPF1Horas;
    }

    public void setFimPF1Minutos(String fimPF1Minutos) {
        _fimPF1Minutos = fimPF1Minutos;
    }

    public void setInicioPF2Horas(String inicioPF2Horas) {
        _inicioPF2Horas = inicioPF2Horas;
    }

    public void setInicioPF2Minutos(String inicioPF2Minutos) {
        _inicioPF2Minutos = inicioPF2Minutos;
    }

    public void setFimPF2Horas(String fimPF2Horas) {
        _fimPF2Horas = fimPF2Horas;
    }

    public void setFimPF2Minutos(String fimPF2Minutos) {
        _fimPF2Minutos = fimPF2Minutos;
    }

    public void setDiaAnteriorPF1(String diaAnteriorPF1) {
        _diaAnteriorPF1 = diaAnteriorPF1;
    }

    public void setDiaAnteriorPF2(String diaAnteriorPF2) {
        _diaAnteriorPF2 = diaAnteriorPF2;
    }

    public void setDiaSeguintePF1(String diaSeguintePF1) {
        _diaSeguintePF1 = diaSeguintePF1;
    }

    public void setDiaSeguintePF2(String diaSeguintePF2) {
        _diaSeguintePF2 = diaSeguintePF2;
    }

    public void setDiaInicio(String diaInicio) {
        _diaInicio = diaInicio;
    }

    public void setMesInicio(String mesInicio) {
        _mesInicio = mesInicio;
    }

    public void setAnoInicio(String anoInicio) {
        _anoInicio = anoInicio;
    }

    public void setDiaFim(String diaFim) {
        _diaFim = diaFim;
    }

    public void setMesFim(String mesFim) {
        _mesFim = mesFim;
    }

    public void setAnoFim(String anoFim) {
        _anoFim = anoFim;
    }

    public void setDuracaoSemanal(String duracaoSemanal) {
        _duracaoSemanal = duracaoSemanal;
    }

    public void setModalidade(String modalidade) {
        _modalidade = modalidade;
    }

    public void setListaRegime(List listaRegime) {
        _listaRegime = listaRegime;
    }

    public void setExcepcaoHorario(boolean excepcaoHorario) {
        _excepcaoHorario = excepcaoHorario;
    }

    public void setForm(Locale locale, Pessoa pessoa, Funcionario funcionario,
            FuncNaoDocente funcNaoDocente, HorarioTipo horarioTipo, Horario horario, List listaRegime,
            boolean isExcepcaoHorario) {

        IStrategyHorarios horarioStrategy = SuporteStrategyHorarios.getInstance().callStrategy(
                horarioTipo.getModalidade());
        horarioStrategy.setFormAssociarHorarioTipoConfirmar(locale, this, pessoa, funcionario,
                horarioTipo, horario, (ArrayList) listaRegime, isExcepcaoHorario);
    }
}
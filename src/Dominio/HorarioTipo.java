package Dominio;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class HorarioTipo {
    private int codigoInterno;

    private String sigla;

    private String modalidade;

    private float duracaoSemanal;

    private Timestamp inicioPF1;

    private Timestamp fimPF1;

    private Timestamp inicioPF2;

    private Timestamp fimPF2;

    private Timestamp inicioHN1;

    private Timestamp fimHN1;

    private Timestamp inicioHN2;

    private Timestamp fimHN2;

    private Timestamp inicioRefeicao;

    private Timestamp fimRefeicao;

    private Time descontoObrigatorioRefeicao;

    private Time intervaloMinimoRefeicao;

    private Timestamp inicioExpediente;

    private Timestamp fimExpediente;

    private Time trabalhoConsecutivo;

    /* Construtores */
    public HorarioTipo() {

        this.codigoInterno = 0;
        this.sigla = null;
        this.modalidade = null;
        this.duracaoSemanal = 0;
        this.inicioPF1 = null;
        this.fimPF1 = null;
        this.inicioPF2 = null;
        this.fimPF2 = null;
        this.inicioHN1 = null;
        this.fimHN1 = null;
        this.inicioHN2 = null;
        this.fimHN2 = null;
        this.trabalhoConsecutivo = null;
    }

    public HorarioTipo(int codigoInterno, String sigla, String modalidade, float duracaoSemanal,
            Timestamp inicioPF1, Timestamp fimPF1, Timestamp inicioPF2, Timestamp fimPF2,
            Timestamp inicioHN1, Timestamp fimHN1, Timestamp inicioHN2, Timestamp fimHN2) {

        this.codigoInterno = codigoInterno;
        this.sigla = sigla;
        this.modalidade = modalidade;
        this.duracaoSemanal = duracaoSemanal;
        this.inicioPF1 = inicioPF1;
        this.fimPF1 = fimPF1;
        this.inicioPF2 = inicioPF2;
        this.fimPF2 = fimPF2;
        this.inicioHN1 = inicioHN1;
        this.fimHN1 = fimHN1;
        this.inicioHN2 = inicioHN2;
        this.fimHN2 = fimHN2;
        this.inicioRefeicao = null;
        this.fimRefeicao = null;
        this.descontoObrigatorioRefeicao = null;
        this.intervaloMinimoRefeicao = null;
        this.inicioExpediente = null;
        this.fimExpediente = null;
        this.trabalhoConsecutivo = null;
    }

    public HorarioTipo(int codigoInterno, String sigla, String modalidade, float duracaoSemanal,
            Timestamp inicioPF1, Timestamp fimPF1, Timestamp inicioPF2, Timestamp fimPF2,
            Timestamp inicioHN1, Timestamp fimHN1, Timestamp inicioHN2, Timestamp fimHN2,
            Timestamp inicioRefeicao, Timestamp fimRefeicao, Time descontoObrigatorioRefeicao,
            Time intervaloMinimoRefeicao, Timestamp inicioExpediente, Timestamp fimExpediente,
            Time trabalhoConsecutivo) {

        this.codigoInterno = codigoInterno;
        this.sigla = sigla;
        this.modalidade = modalidade;
        this.duracaoSemanal = duracaoSemanal;
        this.inicioPF1 = inicioPF1;
        this.fimPF1 = fimPF1;
        this.inicioPF2 = inicioPF2;
        this.fimPF2 = fimPF2;
        this.inicioHN1 = inicioHN1;
        this.fimHN1 = fimHN1;
        this.inicioHN2 = inicioHN2;
        this.fimHN2 = fimHN2;
        this.inicioRefeicao = inicioRefeicao;
        this.fimRefeicao = fimRefeicao;
        this.descontoObrigatorioRefeicao = descontoObrigatorioRefeicao;
        this.intervaloMinimoRefeicao = intervaloMinimoRefeicao;
        this.inicioExpediente = inicioExpediente;
        this.fimExpediente = fimExpediente;
        this.trabalhoConsecutivo = trabalhoConsecutivo;
    }

    /**
     * @return
     */
    public Time getTrabalhoConsecutivo() {
        return trabalhoConsecutivo;
    }

    /**
     * @param trabalhoConsecutivo
     */
    public void setTrabalhoConsecutivo(Time trabalhoConsecutivo) {
        this.trabalhoConsecutivo = trabalhoConsecutivo;
    }

    /**
     * Returns the codigoInterno.
     * 
     * @return int
     */
    public int getCodigoInterno() {
        return codigoInterno;
    }

    /**
     * Returns the intervaloMinimoRefeicao.
     * 
     * @return Time
     */
    public Time getIntervaloMinimoRefeicao() {
        return intervaloMinimoRefeicao;
    }

    /**
     * Returns the descontoObrigatorioRefeicao.
     * 
     * @return Time
     */
    public Time getDescontoObrigatorioRefeicao() {
        return descontoObrigatorioRefeicao;
    }

    /**
     * Returns the duracaoSemanal.
     * 
     * @return float
     */
    public float getDuracaoSemanal() {
        return duracaoSemanal;
    }

    /**
     * Returns the fimExpediente.
     * 
     * @return Timestamp
     */
    public Timestamp getFimExpediente() {
        return fimExpediente;
    }

    /**
     * Returns the fimHN1.
     * 
     * @return Timestamp
     */
    public Timestamp getFimHN1() {
        return fimHN1;
    }

    /**
     * Returns the fimHN2.
     * 
     * @return Timestamp
     */
    public Timestamp getFimHN2() {
        return fimHN2;
    }

    /**
     * Returns the fimPF1.
     * 
     * @return Timestamp
     */
    public Timestamp getFimPF1() {
        return fimPF1;
    }

    /**
     * Returns the fimPF2.
     * 
     * @return Timestamp
     */
    public Timestamp getFimPF2() {
        return fimPF2;
    }

    /**
     * Returns the fimRefeicao.
     * 
     * @return Timestamp
     */
    public Timestamp getFimRefeicao() {
        return fimRefeicao;
    }

    /**
     * Returns the inicioExpediente.
     * 
     * @return Timestamp
     */
    public Timestamp getInicioExpediente() {
        return inicioExpediente;
    }

    /**
     * Returns the inicioHN1.
     * 
     * @return Timestamp
     */
    public Timestamp getInicioHN1() {
        return inicioHN1;
    }

    /**
     * Returns the inicioHN2.
     * 
     * @return Timestamp
     */
    public Timestamp getInicioHN2() {
        return inicioHN2;
    }

    /**
     * Returns the inicioPF1.
     * 
     * @return Timestamp
     */
    public Timestamp getInicioPF1() {
        return inicioPF1;
    }

    /**
     * Returns the inicioPF2.
     * 
     * @return Timestamp
     */
    public Timestamp getInicioPF2() {
        return inicioPF2;
    }

    /**
     * Returns the inicioRefeicao.
     * 
     * @return Timestamp
     */
    public Timestamp getInicioRefeicao() {
        return inicioRefeicao;
    }

    /**
     * Returns the modalidade.
     * 
     * @return String
     */
    public String getModalidade() {
        return modalidade;
    }

    /**
     * Returns the sigla.
     * 
     * @return String
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Sets the codigoInterno.
     * 
     * @param codigoInterno
     *            The codigoInterno to set
     */
    public void setCodigoInterno(int codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    /**
     * Sets the intervaloMinimoRefeicao.
     * 
     * @param intervaloMinimoRefeicao
     *            The intervaloMinimoRefeicao to set
     */
    public void setIntervaloMinimoRefeicao(Time intervaloMinimoRefeicao) {
        this.intervaloMinimoRefeicao = intervaloMinimoRefeicao;
    }

    /**
     * Sets the descontoObrigatorioRefeicao.
     * 
     * @param descontoObrigatorioRefeicao
     *            The descontoObrigatorioRefeicao to set
     */
    public void setDescontoObrigatorioRefeicao(Time descontoObrigatorioRefeicao) {
        this.descontoObrigatorioRefeicao = descontoObrigatorioRefeicao;
    }

    /**
     * Sets the duracaoSemanal.
     * 
     * @param duracaoSemanal
     *            The duracaoSemanal to set
     */
    public void setDuracaoSemanal(float duracaoSemanal) {
        this.duracaoSemanal = duracaoSemanal;
    }

    /**
     * Sets the fimExpediente.
     * 
     * @param fimExpediente
     *            The fimExpediente to set
     */
    public void setFimExpediente(Timestamp fimExpediente) {
        this.fimExpediente = fimExpediente;
    }

    /**
     * Sets the fimHN1.
     * 
     * @param fimHN1
     *            The fimHN1 to set
     */
    public void setFimHN1(Timestamp fimHN1) {
        this.fimHN1 = fimHN1;
    }

    /**
     * Sets the fimHN2.
     * 
     * @param fimHN2
     *            The fimHN2 to set
     */
    public void setFimHN2(Timestamp fimHN2) {
        this.fimHN2 = fimHN2;
    }

    /**
     * Sets the fimPF1.
     * 
     * @param fimPF1
     *            The fimPF1 to set
     */
    public void setFimPF1(Timestamp fimPF1) {
        this.fimPF1 = fimPF1;
    }

    /**
     * Sets the fimPF2.
     * 
     * @param fimPF2
     *            The fimPF2 to set
     */
    public void setFimPF2(Timestamp fimPF2) {
        this.fimPF2 = fimPF2;
    }

    /**
     * Sets the fimRefeicao.
     * 
     * @param fimRefeicao
     *            The fimRefeicao to set
     */
    public void setFimRefeicao(Timestamp fimRefeicao) {
        this.fimRefeicao = fimRefeicao;
    }

    /**
     * Sets the inicioExpediente.
     * 
     * @param inicioExpediente
     *            The inicioExpediente to set
     */
    public void setInicioExpediente(Timestamp inicioExpediente) {
        this.inicioExpediente = inicioExpediente;
    }

    /**
     * Sets the inicioHN1.
     * 
     * @param inicioHN1
     *            The inicioHN1 to set
     */
    public void setInicioHN1(Timestamp inicioHN1) {
        this.inicioHN1 = inicioHN1;
    }

    /**
     * Sets the inicioHN2.
     * 
     * @param inicioHN2
     *            The inicioHN2 to set
     */
    public void setInicioHN2(Timestamp inicioHN2) {
        this.inicioHN2 = inicioHN2;
    }

    /**
     * Sets the inicioPF1.
     * 
     * @param inicioPF1
     *            The inicioPF1 to set
     */
    public void setInicioPF1(Timestamp inicioPF1) {
        this.inicioPF1 = inicioPF1;
    }

    /**
     * Sets the inicioPF2.
     * 
     * @param inicioPF2
     *            The inicioPF2 to set
     */
    public void setInicioPF2(Timestamp inicioPF2) {
        this.inicioPF2 = inicioPF2;
    }

    /**
     * Sets the inicioRefeicao.
     * 
     * @param inicioRefeicao
     *            The inicioRefeicao to set
     */
    public void setInicioRefeicao(Timestamp inicioRefeicao) {
        this.inicioRefeicao = inicioRefeicao;
    }

    /**
     * Sets the modalidade.
     * 
     * @param modalidade
     *            The modalidade to set
     */
    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    /**
     * Sets the sigla.
     * 
     * @param sigla
     *            The sigla to set
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * equals
     * 
     * @param obj
     */
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof HorarioTipo) {
            HorarioTipo horario = (HorarioTipo) obj;

            resultado = ((this.getCodigoInterno() == horario.getCodigoInterno())
                    && (this.getSigla() == horario.getSigla())
                    && (this.getModalidade() == horario.getModalidade())
                    && (this.getDuracaoSemanal() == horario.getDuracaoSemanal())
                    && (this.getInicioPF1() == horario.getInicioPF1())
                    && (this.getFimPF1() == horario.getFimPF1())
                    && (this.getInicioPF2() == horario.getInicioPF2())
                    && (this.getFimPF2() == horario.getFimPF2())
                    && (this.getInicioHN1() == horario.getInicioHN1())
                    && (this.getFimHN1() == horario.getFimHN1())
                    && (this.getInicioHN2() == horario.getInicioHN2())
                    && (this.getFimHN2() == horario.getFimHN2())
                    && (this.getInicioRefeicao() == horario.getInicioRefeicao())
                    && (this.getFimRefeicao() == horario.getFimRefeicao())
                    && (this.getDescontoObrigatorioRefeicao() == horario
                            .getDescontoObrigatorioRefeicao())
                    && (this.getIntervaloMinimoRefeicao() == horario.getIntervaloMinimoRefeicao())
                    && (this.getInicioExpediente() == horario.getInicioExpediente())
                    && (this.getFimExpediente() == horario.getFimExpediente()) && (this
                    .getTrabalhoConsecutivo() == horario.getTrabalhoConsecutivo()));
        }
        return resultado;
    }

}
package net.sourceforge.fenixedu.domain;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.constants.assiduousness.Constants;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Horario {
    private int codigoInterno;

    private int chaveHorarioTipo;

    private int chaveFuncionario;

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

    private Date dataCumprir;

    private Date dataInicio;

    private Date dataFim;

    private int numDias;

    private int posicao;

    private Time trabalhoConsecutivo;

    private int quem;

    private Timestamp quando;

    private int codigoMapeamenento = 0;

    /* Construtores */
    public Horario() {

        this.codigoInterno = 0;
        this.chaveHorarioTipo = 0;
        this.chaveFuncionario = 0;
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
        this.inicioRefeicao = null;
        this.fimRefeicao = null;
        this.descontoObrigatorioRefeicao = null;
        this.intervaloMinimoRefeicao = null;
        this.inicioExpediente = null;
        this.fimExpediente = null;
        this.dataCumprir = null;
        this.dataInicio = null;
        this.dataFim = null;
        this.numDias = 0;
        this.posicao = 0;
        this.trabalhoConsecutivo = null;
        this.quem = 0;
        this.quando = null;
    }

    public Horario(String sigla) {

        this.codigoInterno = 0;
        this.chaveHorarioTipo = 0;
        this.chaveFuncionario = 0;
        this.sigla = sigla;
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
        this.inicioRefeicao = null;
        this.fimRefeicao = null;
        this.descontoObrigatorioRefeicao = null;
        this.intervaloMinimoRefeicao = null;
        this.inicioExpediente = null;
        this.fimExpediente = null;
        this.dataCumprir = null;
        this.dataInicio = null;
        this.dataFim = null;
        this.numDias = 0;
        this.posicao = 0;
        this.trabalhoConsecutivo = null;
        this.quem = 0;
        this.quando = null;
    }

    public Horario(int codigoInterno, int chaveHorarioTipo, int chaveFuncionario, String sigla,
            String modalidade, float duracaoSemanal, Timestamp inicioPF1, Timestamp fimPF1,
            Timestamp inicioPF2, Timestamp fimPF2, Timestamp inicioHN1, Timestamp fimHN1,
            Timestamp inicioHN2, Timestamp fimHN2, Timestamp inicioExpediente, Timestamp fimExpediente,
            Date dataCumprir, Date dataInicio, Date dataFim, int numDias, int posicao, int quem,
            Timestamp quando) {

        this.codigoInterno = codigoInterno;
        this.chaveHorarioTipo = chaveHorarioTipo;
        this.chaveFuncionario = chaveFuncionario;
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
        this.inicioExpediente = inicioExpediente;
        this.fimExpediente = fimExpediente;
        this.dataCumprir = dataCumprir;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.numDias = numDias;
        this.posicao = posicao;
        this.trabalhoConsecutivo = null;

        this.quem = quem;
        this.quando = quando;
    }

    public Horario(int codigoInterno, int chaveHorarioTipo, int chaveFuncionario, String sigla,
            String modalidade, float duracaoSemanal, Timestamp inicioPF1, Timestamp fimPF1,
            Timestamp inicioPF2, Timestamp fimPF2, Timestamp inicioHN1, Timestamp fimHN1,
            Timestamp inicioHN2, Timestamp fimHN2, Timestamp inicioRefeicao, Timestamp fimRefeicao,
            Time descontoObrigatorioRefeicao, Time descontoMinimo, Timestamp inicioExpediente,
            Timestamp fimExpediente, Date dataCumprir, Date dataInicio, Date dataFim, int numDias,
            int posicao, Time trabalhoConsecutivo, int quem, Timestamp quando) {

        this.codigoInterno = codigoInterno;
        this.chaveHorarioTipo = chaveHorarioTipo;
        this.chaveFuncionario = chaveFuncionario;
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
        this.intervaloMinimoRefeicao = descontoMinimo;
        this.inicioExpediente = inicioExpediente;
        this.fimExpediente = fimExpediente;
        this.dataCumprir = dataCumprir;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.numDias = numDias;
        this.posicao = posicao;
        this.trabalhoConsecutivo = trabalhoConsecutivo;

        this.quem = quem;
        this.quando = quando;
    }

    /*
     * Construtores auxiliares para a leitura das rotacoes do horario na BD
     * Oracle
     */
    public Horario(int chaveHorarioTipo, String sigla, String modalidade, float duracaoSemanal,
            Date dataInicio, int numDias, int posicao) {

        this.codigoInterno = 0;
        this.chaveHorarioTipo = chaveHorarioTipo;
        this.chaveFuncionario = 0;
        this.sigla = sigla;
        this.modalidade = modalidade;
        this.duracaoSemanal = duracaoSemanal;
        this.inicioPF1 = null;
        this.fimPF1 = null;
        this.inicioPF2 = null;
        this.fimPF2 = null;
        this.inicioHN1 = null;
        this.fimHN1 = null;
        this.inicioHN2 = null;
        this.fimHN2 = null;
        this.inicioRefeicao = null;
        this.fimRefeicao = null;
        this.descontoObrigatorioRefeicao = null;
        this.intervaloMinimoRefeicao = null;
        this.inicioExpediente = null;
        this.fimExpediente = null;
        this.dataCumprir = null;
        this.dataInicio = dataInicio;
        this.dataFim = null;
        this.numDias = numDias;
        this.posicao = posicao;
        this.trabalhoConsecutivo = null;
        this.quem = 0;
        this.quando = null;
    }

    /* Construtor auxiliar para a leitura das rotacoes do horario na BD Oracle */
    public Horario(int chaveHorarioTipo, int chaveFuncionario, Date dataInicio, Date dataFim,
            int numDias, int posicao) {

        this.codigoInterno = 0;
        this.chaveHorarioTipo = chaveHorarioTipo;
        this.chaveFuncionario = chaveFuncionario;
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
        this.inicioRefeicao = null;
        this.fimRefeicao = null;
        this.descontoObrigatorioRefeicao = null;
        this.intervaloMinimoRefeicao = null;
        this.inicioExpediente = null;
        this.fimExpediente = null;
        this.dataCumprir = null;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.numDias = numDias;
        this.posicao = posicao;
        this.trabalhoConsecutivo = null;
        this.quem = 0;
        this.quando = null;
    }

    public Horario(int chaveHorarioTipo, int chaveFuncionario, Date dataInicio, Date dataFim,
            int numDias, int posicao, int quem, Timestamp quando) {

        this.codigoInterno = 0;
        this.chaveHorarioTipo = chaveHorarioTipo;
        this.chaveFuncionario = chaveFuncionario;
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
        this.inicioRefeicao = null;
        this.fimRefeicao = null;
        this.descontoObrigatorioRefeicao = null;
        this.intervaloMinimoRefeicao = null;
        this.inicioExpediente = null;
        this.fimExpediente = null;
        this.dataCumprir = null;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.numDias = numDias;
        this.posicao = posicao;
        this.trabalhoConsecutivo = null;
        this.quem = quem;
        this.quando = quando;
    }

    /**
     * Returns the chaveFuncionario.
     * 
     * @return int
     */
    public int getChaveFuncionario() {
        return chaveFuncionario;
    }

    /**
     * Returns the quem.
     * 
     * @return int
     */
    public int getQuem() {
        return quem;
    }

    /**
     * Returns the chaveHorarioTipo.
     * 
     * @return int
     */
    public int getChaveHorarioTipo() {
        return chaveHorarioTipo;
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
     * Returns the dataCumprir.
     * 
     * @return Date
     */
    public Date getDataCumprir() {
        return dataCumprir;
    }

    /**
     * Returns the dataFim.
     * 
     * @return Date
     */
    public Date getDataFim() {
        return dataFim;
    }

    /**
     * Returns the dataInicio.
     * 
     * @return Date
     */
    public Date getDataInicio() {
        return dataInicio;
    }

    /**
     * Returns the descontoMinimo.
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
     * Returns the numDias.
     * 
     * @return int
     */
    public int getNumDias() {
        return numDias;
    }

    /**
     * Returns the posicao.
     * 
     * @return int
     */
    public int getPosicao() {
        return posicao;
    }

    /**
     * Returns the quando.
     * 
     * @return Timestamp
     */
    public Timestamp getQuando() {
        return quando;
    }

    /**
     * Returns the sigla.
     * 
     * @return String
     */
    public String getSigla() {
        return sigla;
    }

    public int getCodigoMapeamenento() {
        return codigoMapeamenento;
    }

    /**
     * Sets the chaveFuncionario.
     * 
     * @param chaveFuncionario
     *            The chaveFuncionario to set
     */
    public void setChaveFuncionario(int chaveFuncionario) {
        this.chaveFuncionario = chaveFuncionario;
    }

    /**
     * Sets the quem.
     * 
     * @param quem
     *            The quem to set
     */
    public void setQuem(int quem) {
        this.quem = quem;
    }

    /**
     * Sets the chaveHorarioTipo.
     * 
     * @param chaveHorarioTipo
     *            The chaveHorarioTipo to set
     */
    public void setChaveHorarioTipo(int chaveHorarioTipo) {
        this.chaveHorarioTipo = chaveHorarioTipo;
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
     * Sets the dataCumprir.
     * 
     * @param dataCumprir
     *            The dataCumprir to set
     */
    public void setDataCumprir(Date dataCumprir) {
        this.dataCumprir = dataCumprir;
    }

    /**
     * Sets the dataFim.
     * 
     * @param dataFim
     *            The dataFim to set
     */
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * Sets the dataInicio.
     * 
     * @param dataInicio
     *            The dataInicio to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * Sets the descontoMinimo.
     * 
     * @param descontoMinimo
     *            The descontoMinimo to set
     */
    public void setIntervaloMinimoRefeicao(Time descontoMinimo) {
        this.intervaloMinimoRefeicao = descontoMinimo;
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
     * Sets the numDias.
     * 
     * @param numDias
     *            The numDias to set
     */
    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    /**
     * Sets the posicao.
     * 
     * @param posicao
     *            The posicao to set
     */
    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    /**
     * Sets the quando.
     * 
     * @param quando
     *            The quando to set
     */
    public void setQuando(Timestamp quando) {
        this.quando = quando;
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

    public void setCodigoMapeamenento(int codigoMapeamenento) {
        this.codigoMapeamenento = codigoMapeamenento;
    }

    public Time getTrabalhoConsecutivo() {
        return trabalhoConsecutivo;
    }

    public void setTrabalhoConsecutivo(Time trabalhoConsecutivo) {
        this.trabalhoConsecutivo = trabalhoConsecutivo;
    }

    /**
     * equals
     * 
     * @param obj
     */
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof Horario) {
            Horario horario = (Horario) obj;

            resultado = ((this.getCodigoInterno() == horario.getCodigoInterno())
                    && (this.getChaveHorarioTipo() == horario.getChaveHorarioTipo())
                    && (this.getChaveFuncionario() == horario.getChaveFuncionario())
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
                    && (this.getFimExpediente() == horario.getFimExpediente())
                    && (this.getDataInicio() == horario.getDataInicio())
                    && (this.getDataFim() == horario.getDataFim())
                    && (this.getNumDias() == horario.getNumDias())
                    && (this.getPosicao() == horario.getPosicao())
                    && (this.getTrabalhoConsecutivo().equals(horario.getTrabalhoConsecutivo()))
                    && (this.getQuem() == horario.getQuem()) && (this.getQuando() == horario.getQuando()));
        }
        return resultado;
    }

    public Object clone() {
        return new Horario(this.codigoInterno, this.chaveHorarioTipo, this.chaveFuncionario, this
                .getSigla(), this.getModalidade(), this.getDuracaoSemanal(), this.getInicioPF1(), this
                .getFimPF1(), this.getInicioPF2(), this.getFimPF2(), this.getInicioHN1(), this
                .getFimHN1(), this.getInicioHN2(), this.getFimHN2(), this.getInicioRefeicao(), this
                .getFimRefeicao(), this.getDescontoObrigatorioRefeicao(), this
                .getIntervaloMinimoRefeicao(), this.getInicioExpediente(), this.getFimExpediente(), this
                .getDataCumprir(), this.getDataInicio(), this.getDataFim(), this.getNumDias(), this
                .getPosicao(), this.getTrabalhoConsecutivo(), this.getQuem(), this.getQuando());
    }

    public void transforma(HorarioTipo horarioTipo) {
        this.setSigla(horarioTipo.getSigla());
        this.setModalidade(horarioTipo.getModalidade());
        this.setDuracaoSemanal(horarioTipo.getDuracaoSemanal());
        this.setInicioPF1(horarioTipo.getInicioPF1());
        this.setFimPF1(horarioTipo.getFimPF1());
        this.setInicioPF2(horarioTipo.getInicioPF2());
        this.setFimPF2(horarioTipo.getFimPF2());
        this.setInicioHN1(horarioTipo.getInicioHN1());
        this.setFimHN1(horarioTipo.getFimHN1());
        this.setInicioHN2(horarioTipo.getInicioHN2());
        this.setFimHN2(horarioTipo.getFimHN2());
        this.setInicioRefeicao(horarioTipo.getInicioRefeicao());
        this.setFimRefeicao(horarioTipo.getFimRefeicao());
        this.setDescontoObrigatorioRefeicao(horarioTipo.getDescontoObrigatorioRefeicao());
        this.setIntervaloMinimoRefeicao(horarioTipo.getIntervaloMinimoRefeicao());
        this.setInicioExpediente(horarioTipo.getInicioExpediente());
        this.setFimExpediente(horarioTipo.getFimExpediente());
        this.setTrabalhoConsecutivo(horarioTipo.getTrabalhoConsecutivo());
    }

    public void transformaDescanso(HorarioTipo horarioTipo) {
        this.setModalidade(horarioTipo.getModalidade());
        this.setDuracaoSemanal(horarioTipo.getDuracaoSemanal());
    }

    public void transformaFeriado() {
        //é um feriado
        this.setSigla(Constants.FERIADO);
        // Expediente de dias de Descanso
        this.setInicioExpediente(new Timestamp(Constants.EXPEDIENTE_MINIMO));
        this.setFimExpediente(new Timestamp(Constants.EXPEDIENTE_MAXIMO));
    }

    public String preencheSigla(Locale locale) {
        if (this.getChaveHorarioTipo() != 0) {
            //é um horario tipo
            return this.getSigla();
        }
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources", locale);
        if ((this.getSigla() != null)
                && (this.getSigla().equals(Constants.DS) || this.getSigla().equals(Constants.DSC) || this
                        .getSigla().equals(Constants.FERIADO))) {
            //é um horário de descanso
            return bundle.getString(this.getSigla());
        }
        //é um horario personalizado
        return bundle.getString(this.getModalidade());

    }
}
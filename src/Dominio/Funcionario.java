/*
 * Funcionario.java
 *
 */

package Dominio;

import java.sql.Timestamp;
import java.util.Date;

import constants.assiduousness.Constants;

/**
 * 
 * @author Ivo Brandão
 */
public class Funcionario {

    private int codigoInterno = 0;

    private IPerson person = null;

    private int chavePessoa = 0;

    private int numeroMecanografico = 0;

    private int chaveHorarioActual = 0;

    private Date antiguidade = null;

    //historico
    private Integer chaveFuncResponsavel = null;

    private Integer chaveCCLocalTrabalho = null;

    private Integer chaveCCCorrespondencia = null;

    private Integer chaveCCVencimento = null;

    private String calendario = null;

    private Integer chaveStatus = null;

    private Date dataInicio = null;

    private Date dataFim = null;

    private int quem = 0;

    private Timestamp quando = null;

    //private ICostCenter salaryCostCenter = null;
    private ICostCenter workingPlaceCostCenter = null;

    private ICostCenter mailingCostCenter = null;

    private Date assiduidade = null; //atributo auxiliar

    private int chaveHistorico = 0; //identifica o histórico

    /** Construtor por omissao */
    public Funcionario() {
        this.codigoInterno = 0;
        this.chavePessoa = 0;
        this.numeroMecanografico = 0;
        this.chaveHorarioActual = 0;
        this.antiguidade = new Date(0);
        this.chaveFuncResponsavel = null;
        this.chaveCCLocalTrabalho = null;
        this.chaveCCCorrespondencia = null;
        this.chaveCCVencimento = null;
        this.calendario = new String(Constants.CALENDARIO_LISBOA);
        this.chaveStatus = null;
        this.dataInicio = null;
        this.dataFim = null;
        this.quem = 0;
        this.quando = new Timestamp(0);
    }

    /** Construtor */
    public Funcionario(int codigoInterno, int chavePessoa, int numeroMecanografico,
            int chaveHorarioActual) {

        this.codigoInterno = codigoInterno;
        this.chavePessoa = chavePessoa;
        this.numeroMecanografico = numeroMecanografico;
        this.chaveHorarioActual = chaveHorarioActual;
        this.antiguidade = new Date(0);
        this.chaveFuncResponsavel = null;
        this.chaveCCLocalTrabalho = null;
        this.chaveCCCorrespondencia = null;
        this.chaveCCVencimento = null;
        this.calendario = new String(Constants.CALENDARIO_LISBOA);
        this.chaveStatus = null;
        this.dataInicio = null;
        this.dataFim = null;
        this.quem = 0;
        this.quando = new Timestamp(0);

    }

    /** Construtor */
    public Funcionario(int codigoInterno, int chavePessoa, int numeroMecanografico,
            int chaveHorarioActual, Date antiguidade) {

        this.codigoInterno = codigoInterno;
        this.chavePessoa = chavePessoa;
        this.numeroMecanografico = numeroMecanografico;
        this.chaveHorarioActual = chaveHorarioActual;
        this.antiguidade = antiguidade;
        this.chaveFuncResponsavel = null;
        this.chaveCCLocalTrabalho = null;
        this.chaveCCCorrespondencia = null;
        this.chaveCCVencimento = null;
        this.calendario = new String(Constants.CALENDARIO_LISBOA);
        this.chaveStatus = null;
        this.dataInicio = null;
        this.dataFim = null;
        this.quem = 0;
        this.quando = new Timestamp(0);

    }

    public Funcionario(int codigoInterno, int chavePessoa, int numeroMecanografico,
            int chaveHorarioActual, Integer chaveCCLocalTrabalho, Integer chaveCCCorrespondencia,
            Integer chaveCCVencimento) {

        this.codigoInterno = codigoInterno;
        this.chavePessoa = chavePessoa;
        this.numeroMecanografico = numeroMecanografico;
        this.chaveHorarioActual = chaveHorarioActual;
        this.antiguidade = new Date(0);
        this.chaveFuncResponsavel = null;
        this.chaveCCLocalTrabalho = chaveCCLocalTrabalho;
        this.chaveCCCorrespondencia = chaveCCCorrespondencia;
        this.chaveCCVencimento = chaveCCVencimento;
        this.calendario = new String(Constants.CALENDARIO_LISBOA);
        this.chaveStatus = null;
        this.dataInicio = null;
        this.dataFim = null;
        this.quem = 0;
        this.quando = new Timestamp(0);
    }

    public Funcionario(int codigoInterno, int chavePessoa, int numeroMecanografico,
            int chaveHorarioActual, Date antiguidade, Integer chaveFuncResponsavel,
            Integer chaveCCLocalTrabalho, Integer chaveCCCorrespondencia, Integer chaveCCVencimento,
            String calendario, Integer chaveStatus, Date dataInicio, Date dataFim, int quem,
            Timestamp quando) {

        this.codigoInterno = codigoInterno;
        this.chavePessoa = chavePessoa;
        this.numeroMecanografico = numeroMecanografico;
        this.chaveHorarioActual = chaveHorarioActual;
        this.antiguidade = antiguidade;
        this.chaveFuncResponsavel = chaveFuncResponsavel;
        this.chaveCCLocalTrabalho = chaveCCLocalTrabalho;
        this.chaveCCCorrespondencia = chaveCCCorrespondencia;
        this.chaveCCVencimento = chaveCCVencimento;
        this.calendario = calendario;
        this.chaveStatus = chaveStatus;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quem = quem;
        this.quando = quando;

        this.chaveHistorico = 0;
    }

    public Funcionario(int codigoInterno, int chavePessoa, int numeroMecanografico,
            int chaveHorarioActual, Date antiguidade, int chaveHistorico, Integer chaveFuncResponsavel,
            Integer chaveCCLocalTrabalho, Integer chaveCCCorrespondencia, Integer chaveCCVencimento,
            String calendario, Integer chaveStatus, Date dataInicio, Date dataFim, int quem,
            Timestamp quando) {

        this.codigoInterno = codigoInterno;
        this.chavePessoa = chavePessoa;
        this.numeroMecanografico = numeroMecanografico;
        this.chaveHorarioActual = chaveHorarioActual;
        this.antiguidade = antiguidade;
        this.chaveFuncResponsavel = chaveFuncResponsavel;
        this.chaveCCLocalTrabalho = chaveCCLocalTrabalho;
        this.chaveCCCorrespondencia = chaveCCCorrespondencia;
        this.chaveCCVencimento = chaveCCVencimento;
        this.calendario = calendario;
        this.chaveStatus = chaveStatus;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quem = quem;
        this.quando = quando;

        this.chaveHistorico = chaveHistorico;
    }

    /** Verifica se outro objecto e identico a este */
    public boolean equals(Object obj) {

        return ((obj instanceof Funcionario)
                && (codigoInterno == ((Funcionario) obj).getCodigoInterno())
                && (chavePessoa == ((Funcionario) obj).getChavePessoa())
                && (numeroMecanografico == ((Funcionario) obj).getNumeroMecanografico())
                && (chaveHorarioActual == ((Funcionario) obj).getChaveHorarioActual())
                && (antiguidade == ((Funcionario) obj).getAntiguidade())
                && ((chaveFuncResponsavel == null && ((Funcionario) obj).getChaveFuncResponsavel() == null) || (chaveFuncResponsavel != null && chaveFuncResponsavel
                        .equals(((Funcionario) obj).getChaveFuncResponsavel())))
                && ((chaveCCLocalTrabalho == null && ((Funcionario) obj).getChaveCCLocalTrabalho() == null) || (chaveCCLocalTrabalho != null && chaveCCLocalTrabalho
                        .equals(((Funcionario) obj).getChaveCCLocalTrabalho())))
                && ((chaveCCCorrespondencia == null && ((Funcionario) obj).getChaveCCCorrespondencia() == null) || (chaveCCCorrespondencia != null && chaveCCCorrespondencia
                        .equals(((Funcionario) obj).getChaveCCCorrespondencia())))
                && ((chaveCCVencimento == null && ((Funcionario) obj).getChaveCCVencimento() == null) || (chaveCCVencimento != null && chaveCCVencimento
                        .equals(((Funcionario) obj).getChaveCCVencimento())))
                && (calendario == ((Funcionario) obj).getCalendario())
                && ((chaveStatus == null && ((Funcionario) obj).getChaveStatus() == null) || (chaveStatus != null && chaveStatus
                        .equals(((Funcionario) obj).getChaveStatus())))
                && (dataInicio == ((Funcionario) obj).getDataInicio())
                && (dataFim == ((Funcionario) obj).getDataFim())
                && (quem == ((Funcionario) obj).getQuem()) && (quando == ((Funcionario) obj).getQuando()));
    }

    public int getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(int codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public int getChavePessoa() {
        return chavePessoa;
    }

    public IPerson getPerson() {
        return person;
    }

    public void setChavePessoa(int chavePessoa) {
        this.chavePessoa = chavePessoa;
    }

    public void setPerson(IPerson person) {
        this.person = person;
    }

    public int getNumeroMecanografico() {
        return numeroMecanografico;
    }

    public void setNumeroMecanografico(int numeroMecanografico) {
        this.numeroMecanografico = numeroMecanografico;
    }

    public int getChaveHorarioActual() {
        return chaveHorarioActual;
    }

    public void setChaveHorarioActual(int chaveHorarioActual) {
        this.chaveHorarioActual = chaveHorarioActual;
    }

    public Date getAntiguidade() {
        return antiguidade;
    }

    public void setAntiguidade(Date antiguidade) {
        this.antiguidade = antiguidade;
    }

    public Date getAssiduidade() {
        return assiduidade;
    }

    public void setAssiduidade(Date date) {
        assiduidade = date;
    }

    public String getCalendario() {
        return calendario;
    }

    public Integer getChaveCCCorrespondencia() {
        return chaveCCCorrespondencia;
    }

    public Integer getChaveCCLocalTrabalho() {
        return chaveCCLocalTrabalho;
    }

    public Integer getChaveCCVencimento() {
        return chaveCCVencimento;
    }

    public Integer getChaveFuncResponsavel() {
        return chaveFuncResponsavel;
    }

    public Integer getChaveStatus() {
        return chaveStatus;
    }

    public void setChaveStatus(Integer chaveStatus) {
        this.chaveStatus = chaveStatus;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setCalendario(String calendario) {
        this.calendario = calendario;
    }

    public void setChaveCCCorrespondencia(Integer chaveCCCorrespondencia) {
        this.chaveCCCorrespondencia = chaveCCCorrespondencia;
    }

    public void setChaveCCLocalTrabalho(Integer chaveCCLocalTrabalho) {
        this.chaveCCLocalTrabalho = chaveCCLocalTrabalho;
    }

    public void setChaveCCVencimento(Integer chaveCCVencimento) {
        this.chaveCCVencimento = chaveCCVencimento;
    }

    public void setChaveFuncResponsavel(Integer chaveFuncResponsavel) {
        this.chaveFuncResponsavel = chaveFuncResponsavel;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Timestamp getQuando() {
        return quando;
    }

    public int getQuem() {
        return quem;
    }

    public void setQuando(Timestamp quando) {
        this.quando = quando;
    }

    public void setQuem(int quem) {
        this.quem = quem;
    }

    public ICostCenter getMailingCostCenter() {
        return this.mailingCostCenter;
    }

    public void setMailingCostCenter(ICostCenter mailingCostCenter) {
        this.mailingCostCenter = mailingCostCenter;
    }

    public ICostCenter getWorkingPlaceCostCenter() {
        return this.workingPlaceCostCenter;
    }

    public void setWorkingPlaceCostCenter(ICostCenter workingPlaceCostCenter) {
        this.workingPlaceCostCenter = workingPlaceCostCenter;
    }

    public int getChaveHistorico() {
        return chaveHistorico;
    }

    public void setChaveHistorico(int i) {
        chaveHistorico = i;
    }

    public String toString() {
        String string = null;

        string = "[FUNCIONARIO:" + " CodigoInterno: " + getCodigoInterno() + "\n ChavePessoa: "
                + getChavePessoa() + "\n NumeroMecanografico: " + getNumeroMecanografico()
                + "\n ChaveHorarioActual: " + getChaveHorarioActual() + "\n Antiguidade: "
                + getAntiguidade() + "\n ID HISTORICO: " + getChaveHistorico()
                + "\n ChaveFuncionarioResponsavel: " + getChaveFuncResponsavel()
                + "\n CCLocalTrabalho: " + getChaveCCLocalTrabalho() + "\n CCCorrespondencia: "
                + getChaveCCCorrespondencia() + "\n CCVencimento: " + getChaveCCVencimento()
                + "\n Calendario: " + getCalendario() + "\n StatusAssiduidade: " + getChaveStatus()
                + "\n DataInicioValidade: " + getDataInicio() + "\n DataFimValidade: " + getDataFim()
                + "]\n";

        return string;
    }
}
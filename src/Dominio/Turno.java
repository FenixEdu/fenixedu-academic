/*
 * Turno.java
 *
 * Created on 17 de Outubro de 2002, 19:28
 */

package Dominio;

/**
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import Util.TipoAula;

public class Turno extends DomainObject implements ITurno {
    protected String _nome;

    protected TipoAula _tipo;

    protected Integer _lotacao;

    protected Integer ocupation;

    protected Double percentage;

    private Integer availabilityFinal;

    protected IExecutionCourse _disciplinaExecucao;

    private List associatedShiftProfessorship;

    private List associatedLessons;

    private List associatedClasses;

    // c�digos internos da base de dados
    private Integer chaveDisciplinaExecucao;

    /**
     * Construtor sem argumentos p�blico requerido pela moldura de objectos
     * OJB
     */
    public Turno() {
    }

    public Turno(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Turno(String nome, TipoAula tipo, Integer lotacao, IExecutionCourse disciplinaExecucao) {
        setNome(nome);
        setTipo(tipo);
        setLotacao(lotacao);
        setDisciplinaExecucao(disciplinaExecucao);
        setAssociatedLessons(new ArrayList());
        setAssociatedClasses(new ArrayList());
    }

    public String getNome() {
        return _nome;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public Integer getChaveDisciplinaExecucao() {
        return this.chaveDisciplinaExecucao;
    }

    public void setChaveDisciplinaExecucao(Integer chaveDisciplinaExecucao) {
        this.chaveDisciplinaExecucao = chaveDisciplinaExecucao;
    }

    public TipoAula getTipo() {
        return _tipo;
    }

    public void setTipo(TipoAula tipo) {
        _tipo = tipo;
    }

    public Integer getLotacao() {
        return _lotacao;
    }

    public void setLotacao(Integer lotacao) {
        _lotacao = lotacao;
    }

    public IExecutionCourse getDisciplinaExecucao() {
        return _disciplinaExecucao;
    }

    public void setDisciplinaExecucao(IExecutionCourse disciplinaExecucao) {
        _disciplinaExecucao = disciplinaExecucao;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ITurno) {
            ITurno turno = (ITurno) obj;
            resultado =
                (getNome().equals(turno.getNome()))
                    && (getTipo().equals(turno.getTipo()))
                    && (getDisciplinaExecucao().equals(turno.getDisciplinaExecucao()))
                    && (getLotacao().equals(turno.getLotacao()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[TURNO";
        result += ", codigoInterno=" + this.getIdInternal();
        result += ", nome=" + _nome;
        result += ", tipo=" + _tipo;
        result += ", lotacao=" + _lotacao;
        result += ", chaveDisciplinaExecucao=" + this.chaveDisciplinaExecucao;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public List getAssociatedShiftProfessorship() {
        return associatedShiftProfessorship;
    }

    /**
     * @param list
     */
    public void setAssociatedShiftProfessorship(List list) {
        associatedShiftProfessorship = list;
    }

    /**
     * @return List lessons that belong to this shift
     */
    public List getAssociatedLessons() {
        return associatedLessons;
    }

    /**
     * @param lessons
     *            list of lessons that belong to this shift
     */
    public void setAssociatedLessons(List lessons) {
        associatedLessons = lessons;
    }

    /**
     * @return
     */
    public Integer getAvailabilityFinal() {
        return availabilityFinal;
    }

    /**
     * @param integer
     */
    public void setAvailabilityFinal(Integer integer) {
        availabilityFinal = integer;
    }

    /**
     * @return
     */
    public List getAssociatedClasses() {
        return associatedClasses;
    }

    /**
     * @return
     */
    public Integer getOcupation() {
        return ocupation;
    }

    /**
     * @param list
     */
    public void setAssociatedClasses(List list) {
        associatedClasses = list;
    }

    /**
     * @return
     */
    public Double getPercentage() {
        return percentage;
    }

    /**
     * @param integer
     */
    public void setOcupation(Integer integer) {
        ocupation = integer;
    }

    /**
     * @param integer
     */
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.ITurno#hours()
     */
    public double hours() {
        double hours = 0;
        List lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            IAula lesson = (IAula) lessons.get(i);
            hours += lesson.hours();
        }
        return hours;
    }
}
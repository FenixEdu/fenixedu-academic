/*
 * Lesson.java
 *
 * Created on 18 de Outubro de 2002, 00:54
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.Calendar;

import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoAula;
import net.sourceforge.fenixedu.util.date.TimePeriod;

public class Lesson extends DomainObject implements ILesson {
    protected DiaSemana _diaSemana;

    protected Calendar _inicio;

    protected Calendar _fim;

    protected TipoAula _tipo;

    protected IRoom _sala;

    //  protected IExecutionCourse _disciplinaExecucao;
    protected IShift _shift;

    protected IExecutionPeriod executionPeriod;

    protected IRoomOccupation roomOccupation;

    // c�digos internos da base de dados
    private Integer _chaveSala;

    private Integer _keyShift;

    //    private Integer _chaveDisciplinaExecucao;

    private Integer keyExecutionPeriod;

    private Integer keyRoomOccupation;

    /**
     * @return
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @param executionPeriod
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @return
     */
    public Integer getKeyExecutionPeriod() {
        return keyExecutionPeriod;
    }

    /**
     * @param keyExecutionPeriod
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }

    /**
     * Construtor sem argumentos p�blico requerido pela moldura de objectos
     * OJB
     */
    public Lesson() {
    }

    public Lesson(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Lesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, TipoAula tipo, IRoom sala,
            IRoomOccupation roomOccupation, IShift shift /*
                                                          * ,IExecutionCourse
                                                          * disciplinaExecucao
                                                          */
    ) {
        setDiaSemana(diaSemana);
        setInicio(inicio);
        setFim(fim);
        setTipo(tipo);
        setSala(sala);
        setRoomOccupation(roomOccupation);
        setShift(shift);
        //    setDisciplinaExecucao(disciplinaExecucao);
    }

    public DiaSemana getDiaSemana() {
        return _diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        _diaSemana = diaSemana;
    }

    public Calendar getInicio() {
        return _inicio;
    }

    public void setInicio(Calendar inicio) {
        _inicio = inicio;
    }

    public Calendar getFim() {
        return _fim;
    }

    public void setFim(Calendar fim) {
        _fim = fim;
    }

    public TipoAula getTipo() {
        return _tipo;
    }

    public void setTipo(TipoAula tipo) {
        _tipo = tipo;
    }

    public Integer getChaveSala() {
        return _chaveSala;
    }

    public void setChaveSala(Integer chaveSala) {
        _chaveSala = chaveSala;
    }

    /*
     * public Integer getChaveDisciplinaExecucao() { return
     * _chaveDisciplinaExecucao; }
     * 
     * public void setChaveDisciplinaExecucao(Integer chaveDisciplinaExecucao) {
     * _chaveDisciplinaExecucao = chaveDisciplinaExecucao; }
     * 
     * public IExecutionCourse getDisciplinaExecucao() { return
     * _disciplinaExecucao; }
     * 
     * public void setDisciplinaExecucao(IExecutionCourse disciplinaExecucao) {
     * _disciplinaExecucao = disciplinaExecucao; if (disciplinaExecucao != null) {
     * setExecutionPeriod(disciplinaExecucao.getExecutionPeriod()); } }
     */
    public IRoom getSala() {
        return _sala;
    }

    public void setSala(IRoom sala) {
        _sala = sala;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ILesson) {
            ILesson aula = (ILesson) obj;
            resultado = getIdInternal().equals(aula.getIdInternal());
//            resultado = getDiaSemana().equals(aula.getDiaSemana())
//                    && (getInicio().get(Calendar.HOUR_OF_DAY) == aula.getInicio().get(
//                            Calendar.HOUR_OF_DAY))
//                    && (getInicio().get(Calendar.MINUTE) == aula.getInicio().get(Calendar.MINUTE))
//                    && (getFim().get(Calendar.HOUR_OF_DAY) == aula.getFim().get(Calendar.HOUR_OF_DAY))
//                    && (getFim().get(Calendar.MINUTE) == aula.getFim().get(Calendar.MINUTE))
//                    && getSala().getNome().equals(aula.getSala().getNome())
//                    && getRoomOccupation().getRoom().getNome().equals(
//                            aula.getRoomOccupation().getRoom().getNome());
        }

        return resultado;
    }

    public String toString() {
        String result = "[AULA";
        result += ", codInt=" + getIdInternal();
        result += ", diaSemana=" + _diaSemana;
        if (_inicio != null)
            result += ", inicio=" + _inicio.get(Calendar.HOUR_OF_DAY) + ":"
                    + _inicio.get(Calendar.MINUTE);
        if (_fim != null)
            result += ", fim=" + _fim.get(Calendar.HOUR_OF_DAY) + ":" + _fim.get(Calendar.MINUTE);
        result += ", tipo=" + _tipo;
        result += ", chaveSala=" + _chaveSala;
        //    result += ", chaveDisciplinaExecucao=" + _chaveDisciplinaExecucao;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public IShift getShift() {
        return _shift;
    }

    /**
     * @param shift
     */
    public void setShift(IShift shift) {
        this._shift = shift;
    }

    /**
     * @return
     */
    public Integer getKeyShift() {
        return _keyShift;
    }

    /**
     * @param shift
     */
    public void setKeyShift(Integer shift) {
        _keyShift = shift;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.ILesson#hours()
     */
    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getInicio(), this.getFim());
        return timePeriod.hours().doubleValue();
    }

    public Integer getKeyRoomOccupation() {
        return keyRoomOccupation;
    }

    public void setKeyRoomOccupation(Integer keyRoomOccupation) {
        this.keyRoomOccupation = keyRoomOccupation;
    }

    public IRoomOccupation getRoomOccupation() {
        return roomOccupation;
    }

    public void setRoomOccupation(IRoomOccupation roomOccupation) {
        this.roomOccupation = roomOccupation;
    }

}
/*
 * TurmaTurno.java
 *
 * Created on 19 de Outubro de 2002, 14:42
 */

package Dominio;

/**
 * 
 * @author tfc130
 */
public class TurmaTurno extends DomainObject implements ITurmaTurno {
    protected ITurma _turma;

    protected ITurno _turno;

    // c�digos internos da base de dados
    private Integer _chaveTurma;

    private Integer _chaveTurno;

    /**
     * Construtor sem argumentos p�blico requerido pela moldura de objectos
     * OJB
     */
    public TurmaTurno() {
    }

    public TurmaTurno(ITurma turma, ITurno turno) {
        setTurma(turma);
        setTurno(turno);
    }

    public Integer getChaveTurma() {
        return _chaveTurma;
    }

    public void setChaveTurma(Integer chaveTurma) {
        _chaveTurma = chaveTurma;
    }

    public Integer getChaveTurno() {
        return _chaveTurno;
    }

    public void setChaveTurno(Integer chaveTurno) {
        _chaveTurno = chaveTurno;
    }

    public ITurma getTurma() {
        return _turma;
    }

    public void setTurma(ITurma turma) {
        _turma = turma;
    }

    public ITurno getTurno() {
        return _turno;
    }

    public void setTurno(ITurno turno) {
        _turno = turno;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ITurmaTurno) {
            ITurmaTurno turma_turno = (ITurmaTurno) obj;
            resultado = getTurma().equals(turma_turno.getTurma())
                    && getTurno().equals(turma_turno.getTurno());
        }
        return resultado;
    }

    public String toString() {
        String result = "[TURMA_TURNO";
        result += ", turma=" + _turma;
        result += ", turno=" + _turno;
        result += ", chaveTurma=" + _chaveTurma;
        result += ", chaveTurno=" + _chaveTurno;
        result += "]";
        return result;
    }

}
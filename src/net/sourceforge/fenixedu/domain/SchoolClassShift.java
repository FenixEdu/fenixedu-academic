/*
 * SchoolClassShift.java
 *
 * Created on 19 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
public class SchoolClassShift extends DomainObject implements ISchoolClassShift {
    protected ISchoolClass _turma;

    protected IShift _turno;

    // c�digos internos da base de dados
    private Integer _chaveTurma;

    private Integer _chaveTurno;

    /**
     * Construtor sem argumentos p�blico requerido pela moldura de objectos
     * OJB
     */
    public SchoolClassShift() {
    }

    public SchoolClassShift(ISchoolClass turma, IShift turno) {
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

    public ISchoolClass getTurma() {
        return _turma;
    }

    public void setTurma(ISchoolClass turma) {
        _turma = turma;
    }

    public IShift getTurno() {
        return _turno;
    }

    public void setTurno(IShift turno) {
        _turno = turno;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ISchoolClassShift) {
            ISchoolClassShift turma_turno = (ISchoolClassShift) obj;
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
/*
 * TurnoAluno.java
 *
 * Created on 21 de Outubro de 2002, 18:50
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
public class ShiftStudent extends DomainObject implements ITurnoAluno {
    private IShift shift;

    private IStudent student;

    // códigos internos da base de dados
    private Integer keyShift;

    private Integer keyStudent;

    /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
    public ShiftStudent() {
    }

    public ShiftStudent(IShift shift, IStudent student) {
        setShift(shift);
        setStudent(student);
    }

    public void setShift(IShift shift) {
        this.shift = shift;
    }

    public IStudent getStudent() {
        return student;
    }

    public void setStudent(IStudent student) {
        this.student = student;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ITurnoAluno) {
            ITurnoAluno shiftStudent = (ITurnoAluno) obj;
            result = getShift().equals(shiftStudent.getShift())
                    && getStudent().equals(shiftStudent.getStudent());
        }
        return result;
    }

    public String toString() {
        String result = "[SHIFT_STUDENT";
        result += ", shift=" + shift;
        result += ", student=" + student;
        result += ", keyShift=" + keyShift;
        result += ", keyStudent=" + keyStudent;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public Integer getKeyShift() {
        return keyShift;
    }

    /**
     * @return
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }

    /**
     * @return
     */
    public IShift getShift() {
        return shift;
    }

    /**
     * @param integer
     */
    public void setKeyShift(Integer integer) {
        keyShift = integer;
    }

    /**
     * @param integer
     */
    public void setKeyStudent(Integer integer) {
        keyStudent = integer;
    }

}
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
public class SchoolClassShift extends SchoolClassShift_Base {
    protected ISchoolClass schoolClass;

    protected IShift shift;

    /**
     * Construtor sem argumentos p�blico requerido pela moldura de objectos
     * OJB
     */
    public SchoolClassShift() {
    }

    public SchoolClassShift(ISchoolClass schoolClass, IShift shift) {
        setSchoolClass(schoolClass);
        setShift(shift);
    }

    public ISchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(ISchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public IShift getShift() {
        return shift;
    }

    public void setShift(IShift shift) {
        this.shift = shift;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ISchoolClassShift) {
            ISchoolClassShift schoolClass_shift = (ISchoolClassShift) obj;
            result = getSchoolClass().equals(schoolClass_shift.getSchoolClass())
                    && getShift().equals(schoolClass_shift.getShift());
        }
        return result;
    }

    public String toString() {
        String result = "[TURMA_TURNO";
        result += ", turma=" + schoolClass;
        result += ", turno=" + shift;
        result += ", chaveTurma=" + getKeySchoolClass();
        result += ", chaveTurno=" + getKeyShift();
        result += "]";
        return result;
    }

}
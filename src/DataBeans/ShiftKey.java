/*
 * KeysTurno.java
 *
 * Created on 31 de Outubro de 2002, 12:33
 */

package DataBeans;

/**
 * 
 * @author tfc130
 */
public class ShiftKey extends InfoObject {
    protected String shiftName;

    private InfoExecutionCourse infoExecutionCourse;

    public ShiftKey() {
    }

    public ShiftKey(String shiftName, InfoExecutionCourse infoExecutionCourse) {
        setShiftName(shiftName);
        setInfoExecutionCourse(infoExecutionCourse);
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String nomeTurno) {
        shiftName = nomeTurno;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ShiftKey) {
            ShiftKey keyTurno = (ShiftKey) obj;

            resultado = (getShiftName().equals(keyTurno.getShiftName()));
        }

        return resultado;
    }

    public String toString() {
        String result = "[KEYTURNO";
        result += ", turno=" + shiftName;
        result += "]";
        return result;
    }

    /**
     * Returns the infoExecutionCourse.
     * 
     * @return InfoExecutionCourse
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * Sets the infoExecutionCourse.
     * 
     * @param infoExecutionCourse
     *            The infoExecutionCourse to set
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

}
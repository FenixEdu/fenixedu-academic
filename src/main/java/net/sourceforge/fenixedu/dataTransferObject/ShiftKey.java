/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * KeysTurno.java
 *
 * Created on 31 de Outubro de 2002, 12:33
 */

package net.sourceforge.fenixedu.dataTransferObject;

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

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ShiftKey) {
            ShiftKey keyTurno = (ShiftKey) obj;

            resultado = (getShiftName().equals(keyTurno.getShiftName()));
        }

        return resultado;
    }

    @Override
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
/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.servlet.taglib.sop.v3;

/**
 * @author jpvl
 */
public class DayColumn {

    private Integer maxColisionSize;

    private int index;

    private String label;

    public DayColumn(int index, String label) {
        maxColisionSize = new Integer(1);
        this.index = index;
        this.label = label;
    }

    /**
     * Returns the maxColisionSize.
     * 
     * @return Integer
     */
    public Integer getMaxColisionSize() {
        return maxColisionSize;
    }

    /**
     * Sets the maxColisionSize.
     * 
     * @param maxColisionSize
     *            The maxColisionSize to set
     */
    public void setMaxColisionSize(Integer maxColisionSize) {
        this.maxColisionSize = new Integer(Math.max(maxColisionSize.intValue(), this.maxColisionSize.intValue()));
    }

    /**
     * Returns the index.
     * 
     * @return int
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index.
     * 
     * @param index
     *            The index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns the label.
     * 
     * @return String
     */
    public String getLabel() {
        return label;
    }

}
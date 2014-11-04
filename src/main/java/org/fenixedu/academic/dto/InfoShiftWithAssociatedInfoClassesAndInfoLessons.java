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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author tfc130
 * 
 */
public class InfoShiftWithAssociatedInfoClassesAndInfoLessons extends InfoObject {

    private InfoShift infoShift;

    private List infoLessons;

    private List infoClasses;

    /**
     * Constructor for InfoShiftWithAssociatedInfoClassesAndInfoLessons.
     */
    public InfoShiftWithAssociatedInfoClassesAndInfoLessons() {
        super();
    }

    /**
     * Constructor for InfoShiftWithAssociatedInfoClassesAndInfoLessons.
     */
    public InfoShiftWithAssociatedInfoClassesAndInfoLessons(InfoShift infoShift, List infoLessons, List infoClasses) {
        super();
        setInfoClasses(infoClasses);
        setInfoLessons(infoLessons);
        setInfoShift(infoShift);
    }

    /**
     * Returns the infoClasses.
     * 
     * @return List
     */
    public List getInfoClasses() {
        return infoClasses;
    }

    /**
     * Returns the infoLessons.
     * 
     * @return List
     */
    public List getInfoLessons() {
        return infoLessons;
    }

    /**
     * Returns the infoShift.
     * 
     * @return InfoShift
     */
    public InfoShift getInfoShift() {
        return infoShift;
    }

    /**
     * Sets the infoClasses.
     * 
     * @param infoClasses
     *            The infoClasses to set
     */
    public void setInfoClasses(List infoClasses) {
        this.infoClasses = infoClasses;
    }

    /**
     * Sets the infoLessons.
     * 
     * @param infoLessons
     *            The infoLessons to set
     */
    public void setInfoLessons(List infoLessons) {
        this.infoLessons = infoLessons;
    }

    /**
     * Sets the infoShift.
     * 
     * @param infoShift
     *            The infoShift to set
     */
    public void setInfoShift(InfoShift infoShift) {
        this.infoShift = infoShift;
    }

}
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
public class LessonSlot {

    private int endIndex;

    private int startIndex;

    /* if this member is null then LessonSlot is empty */
    private InfoLessonWrapper infoLessonWrapper;

    public LessonSlot(InfoLessonWrapper infoLessonWrapper, int startIndex, int endIndex) {
        this.infoLessonWrapper = infoLessonWrapper;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.infoLessonWrapper.setLessonSlot(this);
    }

    /**
     * Returns the infoLessonWrapper.
     * 
     * @return InfoLessonWrapper
     */
    public InfoLessonWrapper getInfoLessonWrapper() {
        return infoLessonWrapper;
    }

    /**
     * Returns the endIndex.
     * 
     * @return int
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Returns the startIndex.
     * 
     * @return int
     */
    public int getStartIndex() {
        return startIndex;
    }

    public boolean isSinleSlot() {
        return getStartIndex() == getEndIndex();
    }
}
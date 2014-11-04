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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jpvl
 */
public class TimeTableSlot {
    private Integer lineIndex;

    private DayColumn dayColumn;

    private List lessonSlotList;

    public TimeTableSlot(DayColumn dayColumn, Integer lineIndex) {
        this.lineIndex = lineIndex;
        this.dayColumn = dayColumn;
        lessonSlotList = new ArrayList();
    }

    public void addLessonSlot(LessonSlot lessonSlot) {
        Iterator iterator = lessonSlotList.iterator();
        while (iterator.hasNext()) {
            LessonSlot lessonSlotElem = (LessonSlot) iterator.next();
            lessonSlotElem.getInfoLessonWrapper().addCollision();
        }
        lessonSlot.getInfoLessonWrapper().setNumberOfCollisions(new Integer(lessonSlotList.size()));
        lessonSlotList.add(lessonSlot);
        dayColumn.setMaxColisionSize(new Integer(lessonSlotList.size()));
    }

    /**
     * Returns the infoLessonWrapperList.
     * 
     * @return List
     */
    public List getLessonSlotList() {
        return lessonSlotList;
    }

    /**
     * Returns the lineIndex.
     * 
     * @return Integer
     */
    public Integer getLineIndex() {
        return lineIndex;
    }

    /**
     * Sets the lineIndex.
     * 
     * @param lineIndex
     *            The lineIndex to set
     */
    public void setLineIndex(Integer lineIndex) {
        this.lineIndex = lineIndex;
    }

}
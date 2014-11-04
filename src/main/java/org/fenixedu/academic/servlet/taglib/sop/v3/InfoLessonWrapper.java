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

import org.fenixedu.academic.dto.InfoShowOccupation;

/**
 * @author jpvl
 * 
 */
public class InfoLessonWrapper implements Comparable {
    // private InfoLesson infoLesson;
    private InfoShowOccupation infoShowOccupation;

    private boolean locked;

    private int slotIndex;

    private LessonSlot lessonSlot;

    private Integer numberOfCollisions = new Integer(0);

    private boolean firstRowAlreadyAppended = false;
    private boolean secondRowAlreadyAppended = false;

    public boolean isFirstRowAlreadyAppended() {
        return firstRowAlreadyAppended;
    }

    public void setFirstRowAlreadyAppended(boolean firstRowAlreadyAppended) {
        this.firstRowAlreadyAppended = firstRowAlreadyAppended;
    }

    public boolean isSecondRowAlreadyAppended() {
        return secondRowAlreadyAppended;
    }

    public void setSecondRowAlreadyAppended(boolean secondRowAlreadyAppended) {
        this.secondRowAlreadyAppended = secondRowAlreadyAppended;
    }

    /*
     * public InfoLessonWrapper(InfoLesson infoLesson) { this.infoLesson =
     * infoLesson; this.locked = false; }
     */
    public InfoLessonWrapper(InfoShowOccupation infoShowOccupation) {
        this.infoShowOccupation = infoShowOccupation;
        this.locked = false;
    }

    /**
     * Returns the locked.
     * 
     * @return boolean
     */
    public boolean isLocked() {
        return locked;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    /**
     * Sets the locked.
     * 
     * @param locked
     *            The locked to set
     */
    public void setLocked(boolean locked, int slotIndex) {
        this.locked = locked;
        this.slotIndex = slotIndex;
    }

    /**
     * Returns the infoLesson.
     * 
     * @return InfoLesson
     */
    /*
     * public InfoLesson getInfoLesson() { return infoLesson; }
     */
    public InfoShowOccupation getInfoShowOccupation() {
        return infoShowOccupation;
    }

    protected void setLessonSlot(LessonSlot lessonSlot) {
        this.lessonSlot = lessonSlot;
    }

    /**
     * Returns the lessonSlot.
     * 
     * @return LessonSlot
     */
    public LessonSlot getLessonSlot() {
        return lessonSlot;
    }

    public int getSpan() {
        int startIndex = lessonSlot.getStartIndex();
        int endIndex = lessonSlot.getEndIndex();
        return endIndex - startIndex;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object toCompareWith) {
        int compareResult = 0;
        if (toCompareWith instanceof InfoLessonWrapper) {
            InfoLessonWrapper infoLessonWrapper = (InfoLessonWrapper) toCompareWith;
            compareResult = infoLessonWrapper.getSpan() - getSpan();
        }
        return compareResult;
    }

    /**
     * @return Integer
     */
    public Integer getNumberOfCollisions() {
        return numberOfCollisions;
    }

    public void addCollision() {
        this.numberOfCollisions = new Integer(this.numberOfCollisions.intValue() + 1);
    }

    /**
     * Sets the numberOfCollisions.
     * 
     * @param numberOfCollisions
     *            The numberOfCollisions to set
     */
    public void setNumberOfCollisions(Integer numberOfCollisions) {
        if (numberOfCollisions.intValue() > this.numberOfCollisions.intValue()) {
            this.numberOfCollisions = numberOfCollisions;
        }
    }

}
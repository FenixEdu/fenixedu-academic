package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

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
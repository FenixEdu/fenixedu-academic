package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;

/**
 * @author jpvl
 *  
 */
public class InfoLessonWrapper implements Comparable {
    //private InfoLesson infoLesson;
    private InfoShowOccupation infoShowOccupation;

    private boolean locked;

    private int slotIndex;

    private LessonSlot lessonSlot;

    private Integer numberOfCollisions = new Integer(0);

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
        if (numberOfCollisions.intValue() > this.numberOfCollisions.intValue())
            this.numberOfCollisions = numberOfCollisions;
    }

}
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

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

}
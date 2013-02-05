package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

/**
 * @author jpvl
 * 
 */
public interface LessonSlotContentRenderer {
    public StringBuilder render(String context, LessonSlot lessonSlot);
}
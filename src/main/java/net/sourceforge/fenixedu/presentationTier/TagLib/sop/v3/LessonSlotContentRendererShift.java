/**
 * Aug 6, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

/**
 * @author Ricardo Rodrigues
 * 
 */

public abstract class LessonSlotContentRendererShift extends LessonSlotContentRenderer {
    public abstract StringBuilder lastRender(LessonSlot lessonSlot, String context);
}

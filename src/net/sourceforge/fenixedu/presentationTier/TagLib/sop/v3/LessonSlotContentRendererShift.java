/**
* Aug 6, 2005
*/
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;


/**
 * @author Ricardo Rodrigues
 *
 */

public interface LessonSlotContentRendererShift extends LessonSlotContentRenderer {
    public StringBuilder lastRender(LessonSlot lessonSlot, String context);
}



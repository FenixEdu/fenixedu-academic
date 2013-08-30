package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;

/**
 * @author jpvl
 * 
 */
public abstract class LessonSlotContentRenderer {

    public StringBuilder render(String context, LessonSlot lessonSlot) {
        final StringBuilder builder = new StringBuilder();
        lessonSlot.getInfoLessonWrapper().setFirstRowAlreadyAppended(true);
        return builder;
    }

    public String renderTitleText(final LessonSlot lessonSlot) {
        final InfoShowOccupation occupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        final StringBuilder builder = new StringBuilder();
        builder.append(occupation.getBeginHourMinuteSecond().toString("HH:mm"));
        builder.append("-");
        builder.append(occupation.getEndHourMinuteSecond().toString("HH:mm"));

        return builder.toString();
    }

    public String renderSecondLine(final String context, final LessonSlot lessonSlot) {
        lessonSlot.getInfoLessonWrapper().setSecondRowAlreadyAppended(true);
        return "";
    }

}
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class ShiftPlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {                
        	if (object == null) {
                    return new HtmlText();
                }               
                
        	Shift shift = (Shift) object;
                final StringBuilder lessonsLabel = new StringBuilder();
                int index = 0;
                                
                Set<Lesson> shiftLessons = new TreeSet<Lesson>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);
                shiftLessons.addAll(shift.getAssociatedLessons());
                
                for (Lesson lesson : shiftLessons) {
                    index++;
                    lessonsLabel.append(lesson.getDiaSemana().toString()).append(" (");
                    lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getInicio().getTime())).append("-");
                    lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getFim().getTime())).append(") ");
                    if (lesson.hasSala()) {
                        lessonsLabel.append(((AllocatableSpace)lesson.getSala()).getName().toString());
                    }
                    if (index < shift.getAssociatedLessonsCount()) {
                        lessonsLabel.append(" ; ");
                    } else {
                        lessonsLabel.append(" ");
                    }
                }
                
                return new HtmlText(lessonsLabel.toString());                               
            }            
        };
    }
}

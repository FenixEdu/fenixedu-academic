package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class ShiftPlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText();
                }

                StringBuilder shiftText = new StringBuilder();
                boolean needsSeparator = false;
                
                Shift shift = (Shift) object;
                String name = shift.getNome();
                
                shiftText.append(name + " (");
                for (Lesson lesson : shift.getLessonsOrderedByWeekDayAndStartTime()) {
                    if (needsSeparator) {
                        shiftText.append("; ");
                    }
                    
                    shiftText.append(lesson.getDiaSemana().toString() + " ");
                    shiftText.append(lesson.getInicio().get(Calendar.HOUR_OF_DAY) + ":");
                    shiftText.append(lesson.getInicio().get(Calendar.MINUTE) + "-");
                    shiftText.append(lesson.getFim().get(Calendar.HOUR_OF_DAY) + ":");
                    shiftText.append(lesson.getFim().get(Calendar.MINUTE) + " ");
                    shiftText.append(lesson.getSala().getName());
                    
                    needsSeparator = true;
                }
                shiftText.append(")");
                
                return new HtmlText(shiftText.toString());
            }
            
        };
    }

}

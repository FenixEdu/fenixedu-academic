package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class LessonPlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {                
		
		if (object == null) {
		    return new HtmlText();
		}               

		Lesson lesson = (Lesson) object;
		final StringBuilder lessonsLabel = new StringBuilder();
		
		lessonsLabel.append(lesson.getDiaSemana().toString()).append(" (");
		lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getInicio().getTime())).append("-");
		lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getFim().getTime())).append(") ");
		if (lesson.hasSala()) {
		    lessonsLabel.append(((AllocatableSpace)lesson.getSala()).getName().toString());
		}
		
		return new HtmlText(lessonsLabel.toString());                               
	    }            
	};
    }

}

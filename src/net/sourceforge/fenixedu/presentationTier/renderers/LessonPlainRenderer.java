package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

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
		return new HtmlText(lesson.prettyPrint());                               
	    }            
	};
    }

}

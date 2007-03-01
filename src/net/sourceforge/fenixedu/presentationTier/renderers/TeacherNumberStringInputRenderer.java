package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.TeacherNumberConverter;
import net.sourceforge.fenixedu.renderers.StringInputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;

public class TeacherNumberStringInputRenderer extends StringInputRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        
        Teacher teacher = (Teacher) object; 
        String number = (teacher != null) ? teacher.getTeacherNumber().toString() : null; 
        
        HtmlFormComponent formComponent = (HtmlFormComponent) super.createTextField(number, type);        
        formComponent.setConverter(new TeacherNumberConverter());
        
        return formComponent;        
    }
}

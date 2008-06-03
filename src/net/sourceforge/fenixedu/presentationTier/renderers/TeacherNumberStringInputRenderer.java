package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.TeacherNumberConverter;
import pt.ist.fenixWebFramework.renderers.StringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;

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

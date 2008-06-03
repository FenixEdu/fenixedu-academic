package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PersonUsernameConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
      
        String usernameText = ((String) value).trim();        
        
        if (usernameText.length() == 0) {
            return null;
        }
        
        return Person.readPersonByUsername(usernameText);        
    }  
}

package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.PersonUsernameConverter;
import pt.ist.fenixWebFramework.renderers.StringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;

public class PersonUsernameStringInputRenderer extends StringInputRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {

	Person person = (Person) object;
	String username = (person != null) ? person.getUsername() : null;

	HtmlFormComponent formComponent = (HtmlFormComponent) super.createTextField(username, type);
	formComponent.setConverter(new PersonUsernameConverter());

	return formComponent;
    }
}

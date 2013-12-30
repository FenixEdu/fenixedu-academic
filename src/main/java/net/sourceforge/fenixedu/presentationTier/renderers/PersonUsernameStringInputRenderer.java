package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.PersonUsernameConverter;
import pt.ist.fenixWebFramework.renderers.StringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;

import com.google.common.base.Predicate;

public class PersonUsernameStringInputRenderer extends StringInputRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {

        Person person = (Person) object;
        String username = (person != null) ? person.getUsername() : null;

        final HtmlComponent container = super.createTextField(username, type);
        final HtmlFormComponent formComponent = (HtmlFormComponent) container.getChild(new Predicate<HtmlComponent>() {
            @Override
            public boolean apply(HtmlComponent input) {
                return input instanceof HtmlFormComponent;
            }
        });
        formComponent.setConverter(new PersonUsernameConverter());

        return formComponent;
    }
}

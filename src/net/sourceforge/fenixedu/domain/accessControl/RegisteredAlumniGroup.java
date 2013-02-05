package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;

public class RegisteredAlumniGroup extends Group {

    private static final long serialVersionUID = 1L;

    public RegisteredAlumniGroup() {
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();

        for (final Alumni alumni : RootDomainObject.getInstance().getAlumnisSet()) {
            elements.add(alumni.getStudent().getPerson());
        }
        return elements;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

}

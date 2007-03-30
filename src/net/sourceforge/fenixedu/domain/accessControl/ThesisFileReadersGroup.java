package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.joda.time.DateTime;

/**
 * This group represents all the persons that can read the files associated with 
 * a certain Thesis. This takes in a account the declaration accepted by the student.
 * 
 * @author cfgi
 */
public class ThesisFileReadersGroup extends DomainBackedGroup<Thesis> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    public ThesisFileReadersGroup(Thesis object) {
        super(object);
    }

    @Override
    public boolean isMember(Person person) {
        Thesis thesis = getObject();
        
        if (thesis == null) {
            return false;
        }
        
        if (thesis.getDocumentsAvailableAfter() != null) {
            DateTime time = thesis.getDocumentsAvailableAfter();
            
            if (time.isAfterNow()) {
                return false;
            }
        }
        
        if (thesis.getVisibility() == null) {
            return false;
        }
        
        switch (thesis.getVisibility()) {
        case INTRANET:
            return new InternalPersonGroup().isMember(person);
        case PUBLIC:
            return new EveryoneGroup().isMember(person);
        default:
            return false;
        }
    }

    @Override
    public Set<Person> getElements() {
        Thesis thesis = getObject();
        
        if (thesis == null) {
            return Collections.emptySet();
        }
        
        if (thesis.getDocumentsAvailableAfter() != null) {
            DateTime time = thesis.getDocumentsAvailableAfter();
            
            if (time.isAfterNow()) {
                return Collections.emptySet();
            }
        }
        
        if (thesis.getVisibility() == null) {
            return Collections.emptySet();
        }
        
        switch (thesis.getVisibility()) {
        case INTRANET:
            return new InternalPersonGroup().getElements();
        case PUBLIC:
            return new EveryoneGroup().getElements();
        default:
            return Collections.emptySet();
        }
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new IdOperator(getObject())
        };
    }

    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            Thesis thesis;
            
            try {
                thesis = (Thesis) arguments[0];
            }
            catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, Thesis.class, arguments[0].getClass());
            }
            
            return new ThesisFileReadersGroup(thesis);
        }

        public int getMinArguments() {
            return 1;
        }

        public int getMaxArguments() {
            return 1;
        }
        
    }
}

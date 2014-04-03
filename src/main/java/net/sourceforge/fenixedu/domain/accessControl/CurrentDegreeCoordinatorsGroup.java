package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;

/**
 * Group of all the persons in the last coordination team defined for the target
 * degree.
 * 
 * @author cfgi
 */
public class CurrentDegreeCoordinatorsGroup extends DegreeGroup {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;

    public CurrentDegreeCoordinatorsGroup(Degree degree) {
        super(degree);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDegree().getNameI18N().getContent() };
    }

    @Override
    public Set<Person> getElements() {
        Degree degree = getDegree();

        Set<Person> persons = buildSet();

        for (Coordinator coordinator : degree.getCurrentCoordinators()) {
            persons.add(coordinator.getPerson());
        }

        return persons;
    }

    public static class Builder extends DegreeGroup.DegreeGroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            return new CurrentDegreeCoordinatorsGroup(getDegree(arguments));
        }

    }

    @Override
    public PersistentCoordinatorGroup convert() {
        return PersistentCoordinatorGroup.getInstance(getDegree());
    }
}

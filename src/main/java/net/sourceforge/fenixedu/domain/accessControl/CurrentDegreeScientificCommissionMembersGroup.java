package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;

/**
 * Group of all the persons in the last scientific comission team defined for
 * the target degree.
 * 
 * @author cfgi
 */
public class CurrentDegreeScientificCommissionMembersGroup extends DegreeGroup {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;

    public CurrentDegreeScientificCommissionMembersGroup(Degree degree) {
        super(degree);
    }

    @Override
    public Set<Person> getElements() {
        Degree degree = getDegree();

        Set<Person> persons = buildSet();

        for (ScientificCommission member : degree.getCurrentScientificCommissionMembers()) {
            persons.add(member.getPerson());
        }

        return persons;
    }

    public static class Builder extends DegreeGroup.DegreeGroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            return new CurrentDegreeScientificCommissionMembersGroup(getDegree(arguments));
        }

    }

    @Override
    public PersistentScientificCommissionGroup convert() {
        return PersistentScientificCommissionGroup.getInstance(getDegree());
    }
}

package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

/**
 * Group of all the persons in the last scientific comission team defined for the target
 * degree.
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

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new IdOperator(getDegree()) };
    }

    public static class Builder implements GroupBuilder {
        
        public Group build(Object[] arguments) {
            Degree degree;

            try {
                degree = (Degree) arguments[0];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, Degree.class, arguments[0].getClass());
            }
            
            return new CurrentDegreeScientificCommissionMembersGroup(degree);
        }

        public int getMinArguments() {
            return 1;
        }

        public int getMaxArguments() {
            return 1;
        }   
    }
}

package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;

public class PhdProcessGuidingsGroup extends DomainBackedGroup<PhdIndividualProgramProcess> {

    public PhdProcessGuidingsGroup(PhdIndividualProgramProcess process) {
        super(process);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Set<Person> getElements() {
        Set<Person> persons = new HashSet<Person>();
        PhdIndividualProgramProcess process = getObject();
        for (PhdParticipant participant : process.getGuidingsAndAssistantGuidings()) {
            if (participant.isInternal()) {
                persons.add(((InternalPhdParticipant) participant).getPerson());
            }
        }
        return persons;
    }

    @Override
    public String getName() {
        Iterator<Person> iter = getElements().iterator();
        Person person;
        String name = new String();
        while (iter.hasNext()) {
            person = iter.next();
            name += person.getName();
            if (iter.hasNext()) {
                name += "\n";
            }
        }
        return name;
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return true;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            PhdIndividualProgramProcess phdIndividualProgramProcess = (PhdIndividualProgramProcess) arguments[0];
            if (phdIndividualProgramProcess == null) {
                throw new VariableNotDefinedException("phdIndividualProgramProcess");
            }
            return new PhdProcessGuidingsGroup(phdIndividualProgramProcess);

        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

    }

    @Override
    public PersistentGuidingsAndAssistantsOfPhdGroup convert() {
        return PersistentGuidingsAndAssistantsOfPhdGroup.getInstance(getObject());
    }
}

package net.sourceforge.fenixedu.domain.util.workflow;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;

public interface IState {
    public void nextState();

    public void nextState(String nextState);

    public void checkConditionsToForward();

    public void checkConditionsToForward(String nextState);

    public Set<String> getValidNextStates();

    public Collection<Operation> getOperationsForPerson(Person person);

    public void onOperationFinished(Operation operation, Person person);
}

package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;

public class ActiveCoordinatorGroup extends Group {

    private static final long serialVersionUID = -1670838873686375271L;

    @Override
    public Set<Person> getElements() {
        final Set<Person> elements = super.buildSet();
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            if (matches(executionDegree)) {
                for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    if (isToAddCoordinator(coordinator)) {
                        elements.add(coordinator.getPerson());
                    }
                }
            }
        }
        return elements;
    }

    @Override
    public boolean isMember(final Person person) {
        if (person != null) {
            for (final Coordinator coordinator : person.getCoordinatorsSet()) {
                if (isToAddCoordinator(coordinator)) {
                    final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
                    if (matches(executionDegree)) {
                        final ExecutionYear executionYear = executionDegree.getExecutionYear();
                        if (executionYear.isCurrent()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean isToAddCoordinator(Coordinator coordinator) {
        return coordinator.isResponsible();
    }

    protected boolean matches(final ExecutionDegree executionDegree) {
        return true;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        return PersistentCoordinatorGroup.getInstance();
    }
}
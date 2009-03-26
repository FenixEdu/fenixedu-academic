package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class DegreeAllCoordinatorsGroup extends DegreeGroup {

    public DegreeAllCoordinatorsGroup(Degree degree) {
	super(degree);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String getName() {
	String name = RenderUtils.getResourceString("GROUP_NAME_RESOURCES", "label.name." + getClass().getSimpleName(),
		new Object[] { getDegree().getNameI18N().getContent() });
	return name != null ? name : getExpression();
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> persons = super.buildSet();
	for (DegreeCurricularPlan plan : getDegree().getDegreeCurricularPlans()) {
	    for (ExecutionDegree executionDegree : plan.getExecutionDegrees()) {
		for (Coordinator coordinator : executionDegree.getCoordinatorsList()) {
		    persons.add(coordinator.getPerson());
		}
	    }
	}
	return super.freezeSet(persons);
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

	    return new DegreeAllCoordinatorsGroup(degree);
	}

	public int getMinArguments() {
	    return 1;
	}

	public int getMaxArguments() {
	    return 1;
	}
    }

}

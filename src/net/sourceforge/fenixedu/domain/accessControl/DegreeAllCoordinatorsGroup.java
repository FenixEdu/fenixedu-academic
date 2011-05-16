package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.BundleUtil;

public class DegreeAllCoordinatorsGroup extends DegreeGroup {

    public DegreeAllCoordinatorsGroup(Degree degree) {
	super(degree);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String getName() {
	String name = BundleUtil.getStringFromResourceBundle("resource.GroupNameResources", "label.name." + getClass().getSimpleName(),
		getDegree().getNameI18N().getContent());
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

    public static class Builder extends DegreeGroup.DegreeGroupBuilder {

	public Group build(Object[] arguments) {
	    return new DegreeAllCoordinatorsGroup(getDegree(arguments));
	}

    }

}

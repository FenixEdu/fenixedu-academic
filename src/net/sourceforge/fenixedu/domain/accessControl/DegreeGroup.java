package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;

public abstract class DegreeGroup extends DomainBackedGroup<Degree> {

    public DegreeGroup(Degree degree) {
	super(degree);
    }

    public Degree getDegree() {
	return getObject();
    }

}

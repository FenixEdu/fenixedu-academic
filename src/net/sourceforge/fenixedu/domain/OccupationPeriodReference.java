package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class OccupationPeriodReference extends OccupationPeriodReference_Base {

    public OccupationPeriodReference() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public OccupationPeriodReference(OccupationPeriod period, ExecutionDegree degree, OccupationPeriodType type,
	    Integer semester, CurricularYearList curricularYears) {
	this();
	if (period == null || degree == null)
	    throw new DomainException("exception.null.arguments");
	setOccupationPeriod(period);
	setExecutionDegree(degree);
	setPeriodType(type);
	setSemester(semester);
	setCurricularYears(curricularYears);
    }

    public void delete() {
	removeOccupationPeriod();
	removeExecutionDegree();
	removeRootDomainObject();

	deleteDomainObject();

    }

}

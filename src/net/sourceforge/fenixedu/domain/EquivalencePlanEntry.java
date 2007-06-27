package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public class EquivalencePlanEntry extends EquivalencePlanEntry_Base {

    public static Comparator<EquivalencePlanEntry> COMPARATOR = new Comparator<EquivalencePlanEntry>() {
	public int compare(EquivalencePlanEntry o1, EquivalencePlanEntry o2) {
	    return o1.getCompareString().compareTo(o2.getCompareString());
	}
    };

    public static class EquivalencePlanEntryCreator implements FactoryExecutor, Serializable {

	private DomainReference<EquivalencePlan> equivalencePlan;

	private Set<DomainReference<DegreeModule>> originDegreeModules = new HashSet<DomainReference<DegreeModule>>();

	private Set<DomainReference<DegreeModule>> destinationDegreeModules = new HashSet<DomainReference<DegreeModule>>();

	private DomainReference<DegreeModule> originDegreeModuleToAdd;

	private DomainReference<DegreeModule> destinationDegreeModuleToAdd;

	private LogicOperator originLogicOperator = LogicOperator.AND;

	private LogicOperator destinationLogicOperator = LogicOperator.AND;

	private Double ectsCredits;

	public EquivalencePlanEntryCreator(final EquivalencePlan equivalencePlan) {
	    setEquivalencePlan(equivalencePlan);
	}

	public Object execute() {
	    final EquivalencePlanEntry equivalencePlanEntry = new EquivalencePlanEntry(getEquivalencePlan());
	    equivalencePlanEntry.getOldDegreeModulesSet().addAll(getOriginDegreeModules());
	    equivalencePlanEntry.setSourceDegreeModulesOperator(getOriginLogicOperator());
	    equivalencePlanEntry.getNewDegreeModulesSet().addAll(getDestinationDegreeModules());
	    equivalencePlanEntry.setNewDegreeModulesOperator(getDestinationLogicOperator());
	    if (getEctsCredits() != null && getEctsCredits().doubleValue() > 0) {
		equivalencePlanEntry.setEctsCredits(getEctsCredits());
	    } else {
		equivalencePlanEntry.setEctsCredits(null);
	    }
	    return equivalencePlanEntry;
	}

	public EquivalencePlan getEquivalencePlan() {
	    return equivalencePlan == null ? null : equivalencePlan.getObject();
	}

	public void setEquivalencePlan(EquivalencePlan equivalencePlan) {
	    this.equivalencePlan = equivalencePlan == null ? null
		    : new DomainReference<EquivalencePlan>(equivalencePlan);
	}

	public Set<DegreeModule> getOriginDegreeModules() {
	    final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
	    for (final DomainReference<DegreeModule> degreeModuke : this.originDegreeModules) {
		degreeModules.add(degreeModuke.getObject());
	    }
	    return degreeModules;
	}

	public Set<DegreeModule> getDestinationDegreeModules() {
	    final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
	    for (final DomainReference<DegreeModule> degreeModule : this.destinationDegreeModules) {
		degreeModules.add(degreeModule.getObject());
	    }
	    return degreeModules;
	}

	public DegreeModule getOriginDegreeModuleToAdd() {
	    return originDegreeModuleToAdd == null ? null : originDegreeModuleToAdd.getObject();
	}

	public void setOriginDegreeModuleToAdd(DegreeModule degreeModule) {
	    this.originDegreeModuleToAdd = degreeModule == null ? null : new DomainReference<DegreeModule>(degreeModule);
	}

	public DegreeModule getDestinationDegreeModuleToAdd() {
	    return destinationDegreeModuleToAdd == null ? null : destinationDegreeModuleToAdd.getObject();
	}

	public void setDestinationDegreeModuleToAdd(DegreeModule degreeModule) {
	    this.destinationDegreeModuleToAdd = degreeModule == null ? null : new DomainReference<DegreeModule>(degreeModule);
	}

	public void addOrigin(DegreeModule degreeModule) {
	    if (degreeModule != null) {
		originDegreeModules.add(new DomainReference<DegreeModule>(degreeModule));
	    }
	}

	public void addDestination(DegreeModule degreeModule) {
	    if (degreeModule != null) {
		destinationDegreeModules.add(new DomainReference<DegreeModule>(degreeModule));
	    }
	}

	public LogicOperator getOriginLogicOperator() {
	    return originLogicOperator;
	}

	public void setOriginLogicOperator(LogicOperator originLogicOperator) {
	    this.originLogicOperator = originLogicOperator;
	}

	public LogicOperator getDestinationLogicOperator() {
	    return destinationLogicOperator;
	}

	public void setDestinationLogicOperator(LogicOperator destinationLogicOperator) {
	    this.destinationLogicOperator = destinationLogicOperator;
	}

	public Double getEctsCredits() {
	    return ectsCredits;
	}

	public void setEctsCredits(Double ectsCredits) {
	    this.ectsCredits = ectsCredits;
	}
    }

    //
    protected EquivalencePlanEntry() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan) {
	this();

	init(equivalencePlan);
    }

    public EquivalencePlanEntry(final DegreeCurricularPlanEquivalencePlan equivalencePlan, final CourseGroup oldCourseGroup, final CourseGroup newCourseGroup) {
	this(equivalencePlan);
	getOldDegreeModulesSet().add(oldCourseGroup);
	getNewDegreeModulesSet().add(newCourseGroup);
    }

    public EquivalencePlanEntry(final DegreeCurricularPlanEquivalencePlan equivalencePlan,
	    final List<CurricularCourse> oldCurricularCourses,
	    final List<DegreeModule> newDegreeModules,
	    final LogicOperator sourceCurricularCoursesOperator,
	    final LogicOperator newCurricularCoursesOperator,
	    final Double ectsCredits) {
	this(equivalencePlan);
	getOldDegreeModulesSet().addAll(oldCurricularCourses);
	getNewDegreeModulesSet().addAll(newDegreeModules);
	setSourceDegreeModulesOperator(sourceCurricularCoursesOperator);
	setNewDegreeModulesOperator(newCurricularCoursesOperator);
	setEctsCredits(ectsCredits);
    }

    protected void init(EquivalencePlan equivalencePlan) {
	checkParameters(equivalencePlan);

	super.setEquivalencePlan(equivalencePlan);

    }

    private void checkParameters(EquivalencePlan equivalencePlan) {
	if (equivalencePlan == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.equivalencePlan.cannot.be.null");
	}

    }

    private boolean isOneCourseGroup(final Set<DegreeModule> degreeModules) {
	return degreeModules.size() == 1 && !degreeModules.iterator().next().isLeaf();
    }

    public boolean isCourseGroupEntry() {
	return isOneCourseGroup(getOldDegreeModulesSet()) && isOneCourseGroup(getNewDegreeModulesSet());
    }

    public boolean isCurricularCourseEntry() {
	boolean hasAtLeastOneCourseGroup = false;
	for (final DegreeModule degreeModule : getOldDegreeModulesSet()) {
	    hasAtLeastOneCourseGroup = true;
	    if (!degreeModule.isLeaf()) {
		return false;
	    }
	}
	return hasAtLeastOneCourseGroup;
    }

    public void delete() {
	removeRootDomainObject();
	removeEquivalencePlan();
	getEquivalencePlansSet().clear();
	getOldDegreeModulesSet().clear();
	getNewDegreeModulesSet().clear();
	deleteDomainObject();
    }

    public boolean hasAnyDestinationDegreeModuleFor(final DegreeCurricularPlan degreeCurricularPlan) {
	for (final DegreeModule degreeModule : getNewDegreeModules()) {
	    if (degreeCurricularPlan.hasDegreeModule(degreeModule)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isFor(final DegreeCurricularPlan degreeCurricularPlan) {
	for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
	    if (degreeModule.getParentDegreeCurricularPlan() == degreeCurricularPlan) {
		return true;
	    }
	}
	return false;
    }

    public boolean isFor(final DegreeModule degreeModule) {
	return getNewDegreeModulesSet().contains(degreeModule) || getOldDegreeModulesSet().contains(degreeModule);
    }

    protected String getCompareString() {
	final StringBuilder stringBuilder = new StringBuilder();
	appendCompareString(stringBuilder, getOldDegreeModulesSet());
	appendCompareString(stringBuilder, getNewDegreeModulesSet());
	stringBuilder.append(getEctsCredits());
	if (getSourceDegreeModulesOperator() != null) { 
	    stringBuilder.append(getSourceDegreeModulesOperator().name());
    	}
	if (getNewDegreeModulesOperator() != null) {	    
	    stringBuilder.append(getNewDegreeModulesOperator().name());
	}
	return stringBuilder.toString();
    }

    protected void appendCompareString(final StringBuilder stringBuilder, final Set<DegreeModule> degreeModules) {
	for (final DegreeModule degreeModule : degreeModules) {
	    stringBuilder.append(degreeModule.getName());
	}	
    }

    public void checkPermissions(final Person person) {
	for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = degreeModule.getParentDegreeCurricularPlan();
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
		    if (coordinator.getPerson() == person) {
			return;
		    }
		}
	    }
	}
	throw new Error("error.not.authorized");
    }

    public boolean hasOnlyCourseGroupsInDestination() {
	for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
	    if (degreeModule.isLeaf()) {
		return false;
	    }
	}
	return getNewDegreeModulesCount() > 0;
    }

    public boolean hasOnlyCurricularCoursesInDestination() {
	for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
	    if (!degreeModule.isLeaf()) {
		return false;
	    }
	}
	return getNewDegreeModulesCount() > 0;
    }

    public CourseGroup getPreviousCourseGroupForNewCurricularCourses() {
	throw new Error("not.implemented");
    }

}

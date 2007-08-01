package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.util.LogicOperator;
import net.sourceforge.fenixedu.injectionCode.Checked;

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

	private Boolean transitiveOrigin = true;

	private Double ectsCredits;

	private DomainReference<CourseGroup> destinationDegreeModulesPreviousCourseGroup;

	public EquivalencePlanEntryCreator(final EquivalencePlan equivalencePlan) {
	    setEquivalencePlan(equivalencePlan);
	}

	public Object execute() {

	    final Double ectsCredits;
	    if (getEctsCredits() != null && getEctsCredits().doubleValue() > 0) {
		ectsCredits = getEctsCredits();
	    } else {
		ectsCredits = null;
	    }

	    return new EquivalencePlanEntry(getEquivalencePlan(), getOriginDegreeModules(),
		    getDestinationDegreeModules(), getDestinationDegreeModulesPreviousCourseGroup(),
		    getOriginLogicOperator(), getDestinationLogicOperator(), getTransitiveOrigin(),
		    ectsCredits);

	}

	public EquivalencePlan getEquivalencePlan() {
	    return equivalencePlan == null ? null : equivalencePlan.getObject();
	}

	public void setEquivalencePlan(EquivalencePlan equivalencePlan) {
	    this.equivalencePlan = equivalencePlan == null ? null
		    : new DomainReference<EquivalencePlan>(equivalencePlan);
	}

	public Set<DegreeModule> getOriginDegreeModules() {
	    final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(
		    DegreeModule.COMPARATOR_BY_NAME);
	    for (final DomainReference<DegreeModule> degreeModuke : this.originDegreeModules) {
		degreeModules.add(degreeModuke.getObject());
	    }
	    return degreeModules;
	}

	public Set<DegreeModule> getDestinationDegreeModules() {
	    final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(
		    DegreeModule.COMPARATOR_BY_NAME);
	    for (final DomainReference<DegreeModule> degreeModule : this.destinationDegreeModules) {
		degreeModules.add(degreeModule.getObject());
	    }
	    return degreeModules;
	}

	public DegreeModule getOriginDegreeModuleToAdd() {
	    return originDegreeModuleToAdd == null ? null : originDegreeModuleToAdd.getObject();
	}

	public void setOriginDegreeModuleToAdd(DegreeModule degreeModule) {
	    this.originDegreeModuleToAdd = degreeModule == null ? null
		    : new DomainReference<DegreeModule>(degreeModule);
	}

	public DegreeModule getDestinationDegreeModuleToAdd() {
	    return destinationDegreeModuleToAdd == null ? null : destinationDegreeModuleToAdd
		    .getObject();
	}

	public void setDestinationDegreeModuleToAdd(DegreeModule degreeModule) {
	    this.destinationDegreeModuleToAdd = degreeModule == null ? null
		    : new DomainReference<DegreeModule>(degreeModule);
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

	public Boolean getTransitiveOrigin() {
	    return transitiveOrigin;
	}

	public void setTransitiveOrigin(Boolean transitiveOrigin) {
	    this.transitiveOrigin = transitiveOrigin;
	}

	public Double getEctsCredits() {
	    return ectsCredits;
	}

	public void setEctsCredits(Double ectsCredits) {
	    this.ectsCredits = ectsCredits;
	}

	public void setDestinationDegreeModulesPreviousCourseGroup(final CourseGroup previousCourseGroup) {
	    this.destinationDegreeModulesPreviousCourseGroup = (previousCourseGroup != null) ? new DomainReference<CourseGroup>(
		    previousCourseGroup)
		    : null;
	}

	public CourseGroup getDestinationDegreeModulesPreviousCourseGroup() {
	    return (this.destinationDegreeModulesPreviousCourseGroup != null) ? this.destinationDegreeModulesPreviousCourseGroup
		    .getObject()
		    : null;
	}

    }

    //
    protected EquivalencePlanEntry() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    @Checked("EquivalencePlanEntryPredicates.checkPermissionsToCreate")
    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan, final CourseGroup oldCourseGroup,
	    final CourseGroup newCourseGroup) {
	this();
	init(equivalencePlan, Collections.singleton(oldCourseGroup), Collections
		.singleton(newCourseGroup), null, null, null, true, null);
    }

    @Checked("EquivalencePlanEntryPredicates.checkPermissionsToCreate")
    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final Collection<? extends DegreeModule> oldDegreeModules,
	    final Collection<? extends DegreeModule> newDegreeModules,
	    final CourseGroup previousCourseGroupForNewDegreeModules,
	    final LogicOperator sourceDegreeModulesOperator,
	    final LogicOperator newDegreeModulesOperator, final Boolean transitiveSource,
	    final Double ectsCredits) {
	this();
	init(equivalencePlan, oldDegreeModules, newDegreeModules,
		previousCourseGroupForNewDegreeModules, sourceDegreeModulesOperator,
		newDegreeModulesOperator, transitiveSource, ectsCredits);

    }

    protected void init(final EquivalencePlan equivalencePlan,
	    final Collection<? extends DegreeModule> oldDegreeModules,
	    final Collection<? extends DegreeModule> newDegreeModules,
	    final CourseGroup previousCourseGroupForNewDegreeModules,
	    final LogicOperator sourceDegreeModulesOperator,
	    final LogicOperator newDegreeModulesOperator, final Boolean transitiveSource,
	    final Double ectsCredits) {

	checkParameters(equivalencePlan);

	checkRulesToCreate(newDegreeModules, previousCourseGroupForNewDegreeModules);

	super.setEquivalencePlan(equivalencePlan);
	super.getOldDegreeModulesSet().addAll(oldDegreeModules);
	super.getNewDegreeModulesSet().addAll(newDegreeModules);
	super.setPreviousCourseGroupForNewDegreeModules(previousCourseGroupForNewDegreeModules);
	super.setSourceDegreeModulesOperator(sourceDegreeModulesOperator);
	super.setNewDegreeModulesOperator(newDegreeModulesOperator);
	super.setTransitiveSource(transitiveSource);
	super.setEctsCredits(ectsCredits);

    }

    private void checkRulesToCreate(final Collection<? extends DegreeModule> newDegreeModules,
	    final CourseGroup previousCourseGroupForNewDegreeModules) {

	if (previousCourseGroupForNewDegreeModules != null) {
	    for (final DegreeModule degreeModule : newDegreeModules) {
		if (!previousCourseGroupForNewDegreeModules.hasDegreeModule(degreeModule)) {
		    throw new DomainException(
			    "error.EquivalencePlanEntry.new.degree.modules.must.be.children.of.choosen.course.group");
		}
	    }
	}

    }

    private void checkParameters(final EquivalencePlan equivalencePlan) {
	if (equivalencePlan == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.equivalencePlan.cannot.be.null");
	}
    }

    @Override
    public void addNewDegreeModules(DegreeModule newDegreeModule) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.add.newDegreeModule");
    }

    @Override
    public List<DegreeModule> getNewDegreeModules() {
	return Collections.unmodifiableList(super.getNewDegreeModules());
    }

    @Override
    public Set<DegreeModule> getNewDegreeModulesSet() {
	return Collections.unmodifiableSet(super.getNewDegreeModulesSet());
    }

    @Override
    public Iterator<DegreeModule> getNewDegreeModulesIterator() {
	return getNewDegreeModulesSet().iterator();
    }

    @Override
    public void removeNewDegreeModules(DegreeModule newDegreeModule) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.remove.newDegreeModule");
    }

    @Override
    public void addOldDegreeModules(DegreeModule oldDegreeModules) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.add.oldDegreeModules");
    }

    @Override
    public List<DegreeModule> getOldDegreeModules() {
	return Collections.unmodifiableList(super.getOldDegreeModules());
    }

    @Override
    public Set<DegreeModule> getOldDegreeModulesSet() {
	return Collections.unmodifiableSet(super.getOldDegreeModulesSet());
    }

    @Override
    public Iterator<DegreeModule> getOldDegreeModulesIterator() {
	return getOldDegreeModulesSet().iterator();
    }

    @Override
    public void removeOldDegreeModules(DegreeModule oldDegreeModules) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.remove.oldDegreeModules");
    }

    @Override
    public void setPreviousCourseGroupForNewDegreeModules(
	    CourseGroup previousCourseGroupForNewDegreeModules) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.modify.previousCourseGroupForNewDegreeModules");
    }

    @Override
    public void setEctsCredits(Double ectsCredits) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.modify.ectsCredits");
    }

    @Override
    public void setSourceDegreeModulesOperator(LogicOperator sourceDegreeModulesOperator) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.modify.sourceDegreeModulesOperator");
    }

    @Override
    public void setNewDegreeModulesOperator(LogicOperator newDegreeModulesOperator) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.cannot.modify.newDegreeModulesOperator");
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
	super.getEquivalencePlansSet().clear();
	super.getOldDegreeModulesSet().clear();
	super.getNewDegreeModulesSet().clear();
	super.deleteDomainObject();
    }

    @Override
    public void removeEquivalencePlan() {
	super.setEquivalencePlan(null);
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
	return getNewDegreeModulesSet().contains(degreeModule)
		|| getOldDegreeModulesSet().contains(degreeModule);
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
	stringBuilder.append(getIdInternal());
	return stringBuilder.toString();
    }

    protected void appendCompareString(final StringBuilder stringBuilder,
	    final Set<DegreeModule> degreeModules) {
	for (final DegreeModule degreeModule : degreeModules) {
	    stringBuilder.append(degreeModule.getName());
	}
    }

    public void checkPermissions(final Person person) {
	for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = degreeModule
		    .getParentDegreeCurricularPlan();
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

    public boolean isTransitiveSource() {
	return getTransitiveSource();
    }

    public boolean canApply(StudentCurricularPlan oldStudentCurricularPlan) {
	boolean isApprovedInAll = true;
	for (final DegreeModule degreeModule : getOldDegreeModulesSet()) {
	    final boolean isApprovedOrEnroled = oldStudentCurricularPlan
		    .hasEnrolmentOrAprovalInCurriculumModule(degreeModule);
	    if (getSourceDegreeModulesOperator().isOR() && isApprovedOrEnroled) {
		return true;
	    }
	    isApprovedInAll &= isApprovedOrEnroled;
	}
	return isApprovedInAll;
    }

}

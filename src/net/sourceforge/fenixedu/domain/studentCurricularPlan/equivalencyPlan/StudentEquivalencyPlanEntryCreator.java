package net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CourseGroupEquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.util.LogicOperator;


public class StudentEquivalencyPlanEntryCreator implements Serializable {

    private final DomainReference<DegreeCurricularPlanEquivalencePlan> degreeCurricularPlanEquivalencePlan;

    private final DomainReference<StudentCurricularPlanEquivalencePlan> studentCurricularPlanEquivalencePlan;

    private Set<DomainReference<DegreeModule>> originDegreeModules = new HashSet<DomainReference<DegreeModule>>();

    private Set<DomainReference<DegreeModule>> destinationDegreeModules = new HashSet<DomainReference<DegreeModule>>();

    private transient DegreeModule originDegreeModule;

    private transient DegreeModule destinationDegreeModule;

    private LogicOperator destinationOperator = LogicOperator.AND;

    public StudentEquivalencyPlanEntryCreator(final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan,
	    final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan) {
        this.studentCurricularPlanEquivalencePlan = studentCurricularPlanEquivalencePlan == null ? null
        	: new DomainReference<StudentCurricularPlanEquivalencePlan>(studentCurricularPlanEquivalencePlan);
        this.degreeCurricularPlanEquivalencePlan = degreeCurricularPlanEquivalencePlan == null ?
        	null : new DomainReference<DegreeCurricularPlanEquivalencePlan>(degreeCurricularPlanEquivalencePlan);
    }

    protected Set<DegreeModule> getDegreeModules(final Set<DomainReference<DegreeModule>> degreeModuleDomainReferences) {
	final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
	for (final DomainReference<DegreeModule> domainReference : degreeModuleDomainReferences) {
	    if (domainReference != null) {
		final DegreeModule degreeModule = domainReference.getObject();
		if (degreeModule != null) {
		    degreeModules.add(degreeModule);
		}
	    }
	}
	return degreeModules;
    }

    public Set<DegreeModule> getOriginDegreeModules() {
	return getDegreeModules(originDegreeModules);
    }

    public Set<DegreeModule> getDestinationDegreeModules() {
        return getDegreeModules(destinationDegreeModules);
    }

    public LogicOperator getDestinationOperator() {
        return destinationOperator;
    }

    public void setDestinationOperator(LogicOperator destinationOperator) {
        this.destinationOperator = destinationOperator;
    }

    public DegreeCurricularPlanEquivalencePlan getDegreeCurricularPlanEquivalencePlan() {
	return degreeCurricularPlanEquivalencePlan == null ? null : degreeCurricularPlanEquivalencePlan.getObject();
    }

    public void addOriginDegreeModule(final DegreeModule degreeModule) {
	addDegreeModule(originDegreeModules, degreeModule);
    }

    public void addDestinationDegreeModule(final DegreeModule degreeModule) {
	addDegreeModule(destinationDegreeModules, degreeModule);
    }

    private void addDegreeModule(final Set<DomainReference<DegreeModule>> degreeModules, final DegreeModule degreeModule) {
	if (degreeModule != null) {
	    degreeModules.add(new DomainReference<DegreeModule>(degreeModule));
	}
    }

    public StudentCurricularPlanEquivalencePlan getStudentCurricularPlanEquivalencePlan() {
        return studentCurricularPlanEquivalencePlan == null ? null : studentCurricularPlanEquivalencePlan.getObject();
    }

    public DegreeModule getOriginDegreeModule() {
        return originDegreeModule;
    }

    public void setOriginDegreeModule(DegreeModule originDegreeModule) {
        this.originDegreeModule = originDegreeModule;
    }

    public DegreeModule getDestinationDegreeModule() {
        return destinationDegreeModule;
    }

    public void setDestinationDegreeModule(DegreeModule destinationDegreeModule) {
        this.destinationDegreeModule = destinationDegreeModule;
    }

    public Object execute() {
	if (originDegreeModules.size() == 1 && !originDegreeModules.iterator().next().getObject().isLeaf()) {
	    final CourseGroup originCourseGroup = (CourseGroup) originDegreeModules.iterator().next().getObject();
	    final CourseGroup destinationCourseGrouo = (CourseGroup) destinationDegreeModules.iterator().next().getObject();
	    return new CourseGroupEquivalencePlanEntry(getStudentCurricularPlanEquivalencePlan(), originCourseGroup, destinationCourseGrouo);
	} else {
	    final Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
	    for (final DegreeModule degreeModule : getOriginDegreeModules()) {
		if (degreeModule.isLeaf()) {
		    curricularCourses.add((CurricularCourse)  degreeModule);
		}
	    }
	    final CurricularCourseEquivalencePlanEntry curricularCourseEquivalencePlanEntry = new CurricularCourseEquivalencePlanEntry(
		    getStudentCurricularPlanEquivalencePlan(), curricularCourses,
		    getDestinationDegreeModules(), null, LogicOperator.AND, getDestinationOperator());
	    return curricularCourseEquivalencePlanEntry;
	}
    }

}

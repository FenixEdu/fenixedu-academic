package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class Degree extends Degree_Base implements Comparable {

    public static final ComparatorChain DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE = new ComparatorChain();

    static {
	DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE.addComparator(new BeanComparator("tipoCurso"));
	DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE.addComparator(new BeanComparator("nome"));
    }

    protected Degree() {
        super();
        
	    setRootDomainObject(RootDomainObject.getInstance());
        new DegreeSite(this);
    }

    public Degree(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale,
	    String concreteClassForDegreeCurricularPlans) {
	this();
	commonFieldsChange(name, nameEn, code, gradeScale);

	if (degreeType == null) {
	    throw new DomainException("degree.degree.type.not.null");
	}
	this.setDegreeType(degreeType);

	if (concreteClassForDegreeCurricularPlans == null) {
	    throw new DomainException("degree.concrete.class.not.null");
	}
	this.setConcreteClassForDegreeCurricularPlans(concreteClassForDegreeCurricularPlans);
    }

    public Degree(String name, String nameEn, String acronym, DegreeType degreeType, Double ectsCredits,
	    GradeScale gradeScale, String prevailingScientificArea) {
	this();
	commonFieldsChange(name, nameEn, acronym, gradeScale);
	newStructureFieldsChange(degreeType, ectsCredits, prevailingScientificArea);
    }

    private void commonFieldsChange(String name, String nameEn, String code, GradeScale gradeScale) {
	if (name == null) {
	    throw new DomainException("degree.name.not.null");
	} else if (nameEn == null) {
	    throw new DomainException("degree.name.en.not.null");
	} else if (code == null) {
	    throw new DomainException("degree.code.not.null");
	}

	this.setNome(name.trim());
	this.setNameEn(nameEn.trim());
	this.setSigla(code.trim());
	this.setGradeScale(gradeScale);
    }

    private void newStructureFieldsChange(DegreeType degreeType, Double ectsCredits,
	    String prevailingScientificArea) {
	if (degreeType == null) {
	    throw new DomainException("degree.degree.type.not.null");
	} else if (ectsCredits == null) {
	    throw new DomainException("degree.ectsCredits.not.null");
	}

	this.setDegreeType(degreeType);
	this.setEctsCredits(ectsCredits);
	this.setPrevailingScientificArea(prevailingScientificArea == null ? null : prevailingScientificArea.trim());
    }

    public void edit(String name, String nameEn, String code, DegreeType degreeType,
	    GradeScale gradeScale) {
	commonFieldsChange(name, nameEn, code, gradeScale);

	if (degreeType == null) {
	    throw new DomainException("degree.degree.type.not.null");
	}
	this.setDegreeType(degreeType);
    }

    public void edit(String name, String nameEn, String acronym, DegreeType degreeType,
	    Double ectsCredits, GradeScale gradeScale, String prevailingScientificArea) {
	checkIfCanEdit(degreeType);
	commonFieldsChange(name, nameEn, acronym, gradeScale);
	newStructureFieldsChange(degreeType, ectsCredits, prevailingScientificArea);
    }

    private void checkIfCanEdit(final DegreeType degreeType) {
	if (!this.getDegreeType().equals(degreeType) && hasAnyDegreeCurricularPlans()) {
	    throw new DomainException("degree.cant.edit.bolonhaDegreeType");
	}
    }

    public Boolean getCanBeDeleted() {
        if (hasAnyDegreeCurricularPlans()) {
            return false;   
        }
        
        if (hasSite() && getSite().canBeDeleted()) {
            return false;
        }
        
        return true;
    }

    private void checkDeletion() {
        if (hasAnyDegreeCurricularPlans()) {
            throw new DomainException("error.degree.has.degree.curricular.plans");  
        }
        
        if (hasSite() && getSite().canBeDeleted()) {
            throw new DomainException("error.degree.has.site.undeletable");  
        }
    }

    public void delete() throws DomainException {

        checkDeletion();
        
	    Iterator oicrIterator = getAssociatedOldInquiriesCoursesResIterator();
	    while (oicrIterator.hasNext()) {
		OldInquiriesCoursesRes oicr = (OldInquiriesCoursesRes) oicrIterator.next();
		oicrIterator.remove();
		oicr.removeDegree();
		oicr.delete();
	    }

	    Iterator oitrIterator = getAssociatedOldInquiriesTeachersResIterator();
	    while (oitrIterator.hasNext()) {
		OldInquiriesTeachersRes oitr = (OldInquiriesTeachersRes) oitrIterator.next();
		oitrIterator.remove();
		oitr.removeDegree();
		oitr.delete();
	    }

	    Iterator oisIterator = getAssociatedOldInquiriesSummariesIterator();
	    while (oisIterator.hasNext()) {
		OldInquiriesSummary ois = (OldInquiriesSummary) oisIterator.next();
		oisIterator.remove();
		ois.removeDegree();
		ois.delete();
	    }

	    Iterator delegatesIterator = getDelegateIterator();
	    while (delegatesIterator.hasNext()) {
		Delegate delegate = (Delegate) delegatesIterator.next();
		delegatesIterator.remove();
		delegate.removeDegree();
		delegate.delete();
	    }

	    Iterator degreeInfosIterator = getDegreeInfosIterator();
	    while (degreeInfosIterator.hasNext()) {
		DegreeInfo degreeInfo = (DegreeInfo) degreeInfosIterator.next();
		degreeInfosIterator.remove();
		degreeInfo.removeDegree();
		degreeInfo.delete();
	    }

	    for (; !getParticipatingAnyCurricularCourseCurricularRules().isEmpty(); getParticipatingAnyCurricularCourseCurricularRules()
		    .get(0).delete())
		;

	    removeRootDomainObject();
	    deleteDomainObject();
    }

    public DegreeCurricularPlan getNewDegreeCurricularPlan() {
	DegreeCurricularPlan degreeCurricularPlan = null;

	try {
	    Class classDefinition = Class.forName(getConcreteClassForDegreeCurricularPlans());
	    degreeCurricularPlan = (DegreeCurricularPlan) classDefinition.newInstance();
	} catch (InstantiationException e) {
	    throw new RuntimeException(e);
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e);
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException(e);
	}

	return degreeCurricularPlan;
    }

    public GradeScale getGradeScaleChain() {
	return super.getGradeScale() != null ? super.getGradeScale() : getDegreeType().getGradeScale();
    }

    public DegreeCurricularPlan createPreBolonhaDegreeCurricularPlan(String name,
	    DegreeCurricularPlanState state, Date initialDate, Date endDate, Integer degreeDuration,
	    Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType,
	    Integer numerusClausus, String anotation, GradeScale gradeScale) {
	if (!this.isBolonhaDegree()) {
	    for (DegreeCurricularPlan dcp : this.getDegreeCurricularPlansSet()) {
		if (dcp.getName().equalsIgnoreCase(name)) {
		    throw new DomainException("DEGREE.degreeCurricularPlan.existing.name.and.degree");
		}
	    }

	    return new DegreeCurricularPlan(this, name, state, initialDate, endDate, degreeDuration,
		    minimalYearForOptionalCourses, neededCredits, markType, numerusClausus, anotation,
		    gradeScale);
	} else {
	    throw new DomainException("DEGREE.calling.pre.bolonha.method.to.bolonha.degree");
	}
    }

    public DegreeCurricularPlan createBolonhaDegreeCurricularPlan(String name, GradeScale gradeScale,
	    Person creator) {
	if (this.isBolonhaDegree()) {
	    if (name == null) {
		throw new DomainException("DEGREE.degree.curricular.plan.name.cannot.be.null");
	    }
	    for (DegreeCurricularPlan dcp : this.getDegreeCurricularPlansSet()) {
		if (dcp.getName().equalsIgnoreCase(name)) {
		    throw new DomainException("DEGREE.degreeCurricularPlan.existing.name.and.degree");
		}
	    }

	    if (creator == null) {
		throw new DomainException("DEGREE.degree.curricular.plan.creator.cannot.be.null");
	    }
	    final Role bolonhaRole = Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER);
	    if (!creator.hasPersonRoles(bolonhaRole)) {
		creator.addPersonRoles(bolonhaRole);
	    }

	    CurricularPeriod curricularPeriod = new CurricularPeriod(this.getDegreeType()
		    .getCurricularPeriodType());

	    return new DegreeCurricularPlan(this, name, gradeScale, creator, curricularPeriod);
	} else {
	    throw new DomainException("DEGREE.calling.bolonha.method.to.non.bolonha.degree");
	}
    }

    @Override
    @Deprecated
    public DegreeType getTipoCurso() {
        return getDegreeType();
    }
    
    @Override
    @Deprecated
    public void setTipoCurso(final DegreeType degreeType) {
        setDegreeType(degreeType);
    }
    
    public DegreeType getDegreeType() {
	return super.getTipoCurso();
    }

    public void setDegreeType(final DegreeType degreeType) {
        super.setTipoCurso(degreeType);
    }
    
    @Deprecated
    public DegreeType getBolonhaDegreeType() {
	return getDegreeType();
    }

    public boolean isBolonhaDegree() {
	return getDegreeType().isBolonhaType();
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return getDegreeType().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
    }

    public String getPresentationName() {
	final ResourceBundle enumResourceBundle = ResourceBundle
		.getBundle("resources.EnumerationResources");
	final ResourceBundle appResourceBundle = ResourceBundle
		.getBundle("resources.ApplicationResources");
	return enumResourceBundle.getString(getDegreeType().toString()) + " "
		+ appResourceBundle.getString("label.in") + " " + getNome();
    }

    public List<DegreeCurricularPlan> findDegreeCurricularPlansByState(DegreeCurricularPlanState state) {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (!isBolonhaDegree()) {
	    for (DegreeCurricularPlan degreeCurricularPlan : this.getDegreeCurricularPlansSet()) {
		if (degreeCurricularPlan.getState().equals(state)) {
		    result.add(degreeCurricularPlan);
		}
	    }
	}
	return result;
    }

    public List<DegreeCurricularPlan> getActiveDegreeCurricularPlans() {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

	for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
		result.add(degreeCurricularPlan);
	    }
	}

	return result;
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlansForYear(ExecutionYear year) {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.hasExecutionDegreeFor(year)) {
		result.add(degreeCurricularPlan);
	    }
	}
	return result;
    }

    public List<ExecutionYear> getDegreeCurricularPlansExecutionYears() {
	Set<ExecutionYear> result = new TreeSet<ExecutionYear>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		result.add(executionDegree.getExecutionYear());
	    }
	}
	return new ArrayList<ExecutionYear>(result);
    }

    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYear(ExecutionYear executionYear) {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
		for (CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		    if (!course.isBolonha()) {
			for (ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
			    if (executionCourse.getExecutionPeriod().getExecutionYear().equals(
				    executionYear)) {
				result.add(course);
				break;
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYearAndYear(
	    ExecutionYear executionYear, Integer curricularYear) {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
		for (CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		    if (!course.isBolonha()) {
			xpto: for (ExecutionCourse executionCourse : course
				.getAssociatedExecutionCourses()) {
			    if (executionCourse.getExecutionPeriod().getExecutionYear().equals(
				    executionYear)) {
				for (CurricularCourseScope curricularCourseScope : course.getScopes()) {
				    if (curricularCourseScope.getCurricularSemester()
					    .getCurricularYear().getYear().equals(curricularYear)) {
					result.add(course);
					break xpto;
				    }
				}
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    public String getName() {
	return this.getNome();
    }

    public OldInquiriesCoursesRes getOldInquiriesCoursesResByCourseCodeAndExecutionPeriod(String code,
	    ExecutionPeriod executionPeriod) {
	for (OldInquiriesCoursesRes oldInquiriesCoursesRes : this.getAssociatedOldInquiriesCoursesRes()) {
	    if (oldInquiriesCoursesRes.getCourseCode().equalsIgnoreCase(code)
		    && oldInquiriesCoursesRes.getExecutionPeriod().equals(executionPeriod)) {
		return oldInquiriesCoursesRes;
	    }
	}
	return null;
    }

    public List<OldInquiriesSummary> getOldInquiriesSummariesByExecutionPeriod(
	    ExecutionPeriod executionPeriod) {
	List<OldInquiriesSummary> result = new ArrayList<OldInquiriesSummary>();
	for (OldInquiriesSummary oldInquiriesSummary : this.getAssociatedOldInquiriesSummaries()) {
	    if (oldInquiriesSummary.getExecutionPeriod().equals(executionPeriod)) {
		result.add(oldInquiriesSummary);
	    }
	}
	return result;
    }

    public List<OldInquiriesTeachersRes> getOldInquiriesTeachersResByExecutionPeriodAndCurricularYearAndCourseCode(
	    ExecutionPeriod executionPeriod, Integer curricularYear, String courseCode) {
	List<OldInquiriesTeachersRes> result = new ArrayList<OldInquiriesTeachersRes>();
	for (OldInquiriesTeachersRes oldInquiriesTeachersRes : this
		.getAssociatedOldInquiriesTeachersRes()) {
	    if (oldInquiriesTeachersRes.getExecutionPeriod().equals(executionPeriod)
		    && oldInquiriesTeachersRes.getCurricularYear().equals(curricularYear)
		    && oldInquiriesTeachersRes.getCourseCode().equalsIgnoreCase(courseCode)) {
		result.add(oldInquiriesTeachersRes);
	    }
	}
	return result;
    }

    public List<OldInquiriesTeachersRes> getOldInquiriesTeachersResByExecutionPeriodAndCurricularYearAndCourseCodeAndTeacher(
	    ExecutionPeriod executionPeriod, Integer curricularYear, String courseCode, Teacher teacher) {
	List<OldInquiriesTeachersRes> result = new ArrayList<OldInquiriesTeachersRes>();
	for (OldInquiriesTeachersRes oldInquiriesTeachersRes : this
		.getAssociatedOldInquiriesTeachersRes()) {
	    if (oldInquiriesTeachersRes.getExecutionPeriod().equals(executionPeriod)
		    && oldInquiriesTeachersRes.getCurricularYear().equals(curricularYear)
		    && oldInquiriesTeachersRes.getCourseCode().equalsIgnoreCase(courseCode)
		    && oldInquiriesTeachersRes.getTeacher().equals(teacher)) {
		result.add(oldInquiriesTeachersRes);
	    }
	}
	return result;
    }

    public DegreeCurricularPlan getMostRecentDegreeCurricularPlan() {
	ExecutionDegree mostRecentExecutionDegree = null;
	boolean mustGetByInitialDate = false;

	for (final DegreeCurricularPlan degreeCurricularPlan : this.getActiveDegreeCurricularPlans()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
	    if (mostRecentExecutionDegree == null) {
		mostRecentExecutionDegree = executionDegree;
	    } else {
		if (mostRecentExecutionDegree.getExecutionYear().equals(
			executionDegree.getExecutionYear())) {
		    mustGetByInitialDate = true;
		} else if (mostRecentExecutionDegree.isBefore(executionDegree)) {
		    mustGetByInitialDate = false;
		    mostRecentExecutionDegree = executionDegree;
		}
	    }
	}

	if (mustGetByInitialDate) {
	    // investigate dcps initial dates
	    return getMostRecentDegreeCurricularPlanByInitialDate();
	} else {
	    return (mostRecentExecutionDegree != null) ? mostRecentExecutionDegree
		    .getDegreeCurricularPlan() : null;
	}
    }

    private DegreeCurricularPlan getMostRecentDegreeCurricularPlanByInitialDate() {
	DegreeCurricularPlan mostRecentDegreeCurricularPlan = null;
	for (final DegreeCurricularPlan degreeCurricularPlan : this.getActiveDegreeCurricularPlans()) {
	    if (mostRecentDegreeCurricularPlan == null
		    || degreeCurricularPlan.getInitialDateYearMonthDay().isAfter(
			    mostRecentDegreeCurricularPlan.getInitialDateYearMonthDay())) {
		mostRecentDegreeCurricularPlan = degreeCurricularPlan;
	    }
	}
	return mostRecentDegreeCurricularPlan;
    }

    public Collection<Registration> getActiveRegistrations() {
	final Collection<Registration> result = new HashSet<Registration>();
	
	for (final DegreeCurricularPlan degreeCurricularPlan : getActiveDegreeCurricularPlans()) {
	    result.addAll(degreeCurricularPlan.getActiveRegistrations());
	}
	
	return result;
    }
    
    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    private static final Map<String, SoftReference<Degree>> degrees = new Hashtable<String, SoftReference<Degree>>();

    private static void loadCache() {
	synchronized (degrees) {
	    degrees.clear();
	    for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
		degrees.put(degree.getSigla().toLowerCase(), new SoftReference<Degree>(degree));
	    }
	}
    }

    private static void updateCache(final Degree degree, final String newLowerCaseSigla) {
	final String currentLowerCaseSigla = (degree.getSigla() != null) ? degree.getSigla()
		.toLowerCase() : "";
	synchronized (degrees) {
	    degrees.remove(currentLowerCaseSigla);
	    degrees.put(newLowerCaseSigla, new SoftReference<Degree>(degree));
	}
    }

    @Override
    public void setSigla(final String sigla) {
	updateCache(this, sigla.toLowerCase());
	super.setSigla(sigla);
    }

    public static Degree readBySigla(final String sigla) {
	if (degrees.isEmpty()) {
	    loadCache();
	}
	final String lowerCaseString = sigla.toLowerCase();
	final SoftReference<Degree> degreeReference = degrees.get(lowerCaseString);
	if (degreeReference != null) {
	    final Degree degree = degreeReference.get();
	    if (degree != null && degree.getRootDomainObject() == RootDomainObject.getInstance()
		    && degree.getSigla().equalsIgnoreCase(lowerCaseString)) {
		return degree;
	    } else {
		loadCache();
		final SoftReference<Degree> otherDegreeReference = degrees.get(lowerCaseString);
		if (otherDegreeReference != null) {
		    final Degree otherDegree = otherDegreeReference.get();
		    if (otherDegree != null
			    && otherDegree.getRootDomainObject() == RootDomainObject.getInstance()
			    && otherDegree.getSigla().equalsIgnoreCase(lowerCaseString)) {
			return otherDegree;
		    }
		}
	    }
	}

	return null;
    }

    public static List<Degree> readOldDegrees() {
	List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
	    if (!degree.isBolonhaDegree()) {
		result.add(degree);
	    }
	}
	return result;
    }

    public static List<Degree> readBolonhaDegrees() {
	List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
	    if (degree.isBolonhaDegree()) {
		result.add(degree);
	    }
	}
	return result;
    }

    public static List<Degree> readAllByDegreeType(final DegreeType degreeType) {
	List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
	    if (degree.getDegreeType() != null && degree.getDegreeType() == degreeType) {
		result.add(degree);
	    }
	}
	return result;
    }

    public static List<Degree> readAllByDegreeCode(final String degreeCode) {
	final List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
	    if (degree.getCode() != null && degree.getCode().equals(degreeCode)) {
		result.add(degree);
	    }
	}

	return result;
    }

    public DegreeInfo getMostRecentDegreeInfo() {
	return getMostRecentDegreeInfo(ExecutionYear.readCurrentExecutionYear());
    }

    public DegreeInfo getMostRecentDegreeInfo(ExecutionYear executionYear) {
	DegreeInfo result = executionYear.getDegreeInfo(this);

	if (result != null) {
	    return result;
	}

	for (; executionYear != null; executionYear = executionYear.getPreviousExecutionYear()) {
	    result = executionYear.getDegreeInfo(this);

	    if (result != null) {
		return result;
	    }
	}

	return null;
    }

    public DegreeInfo createCurrentDegreeInfo() {
	// first let's check if the current degree info exists already
	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	DegreeInfo shouldBeThisOne = currentExecutionYear.getDegreeInfo(this);
	if (shouldBeThisOne != null) {
	    return shouldBeThisOne;
	}

	// ok, so let's create a new one based on the most recent one, if
        // existing
	DegreeInfo mostRecentDegreeInfo = this.getMostRecentDegreeInfo();
	if (mostRecentDegreeInfo != null) {
	    return new DegreeInfo(mostRecentDegreeInfo, currentExecutionYear);
	} else {
	    return new DegreeInfo(this, currentExecutionYear);
	}
    }

    public List<Integer> buildFullCurricularYearList() {
	List<Integer> result = new ArrayList<Integer>();

	if (this.isBolonhaDegree()) {
	    for (int i = 1; i <= this.getDegreeType().getYears(); i++) {
		result.add(i);
	    }
	} else if (this.getDegreeType().equals(DegreeType.DEGREE)) {
	    for (int i = 1; i <= 5; i++) {
		result.add(i);
	    }
	} else if (this.getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
	    for (int i = 1; i <= 2; i++) {
		result.add(i);
	    }
	}
	return result;
    }

    public boolean isMemberOfCurrentScientificCommission(Person person) {
        for (ScientificCommission member : getCurrentScientificCommissionMembers()) {
            if (member.getPerson() == person) {
                return true;
            }
        }
        
        return false;
    }
    
    public Collection<ScientificCommission> getCurrentScientificCommissionMembers() {
        for (ExecutionYear ey = ExecutionYear.readCurrentExecutionYear(); ey != null; ey = ey.getPreviousExecutionYear()) {
            Collection<ScientificCommission> members = getScientificCommissionMembers(ey);
            
            if (! members.isEmpty()) {
                return members;
            }
        }
        
        return Collections.emptyList();
    }
    
    public Collection<ScientificCommission> getScientificCommissionMembers(ExecutionYear executionYear) {
        for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansForYear(executionYear)) {
            ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            
            if (executionDegree != null) {
                return new ArrayList<ScientificCommission>(executionDegree.getScientificCommissionMembers());
            }
        }
        
        return Collections.emptyList();
    }

    public boolean isCurrentCoordinator(Person person) {
        for (Coordinator coordinator : getCurrentResponsibleCoordinators()) {
            if (coordinator.getPerson() == person) {
                return true;
            }
        }
        
        return false;
    }
    
    public Collection<Coordinator> getCurrentResponsibleCoordinators() {
        for (ExecutionYear ey = ExecutionYear.readCurrentExecutionYear(); ey != null; ey = ey.getPreviousExecutionYear()) {
            Collection<Coordinator> coordinators = getResponsibleCoordinators(ey);
            
            if (! coordinators.isEmpty()) {
                return coordinators;
            }
        }
        
        return Collections.emptyList();
    }
    
    public Collection<Coordinator> getResponsibleCoordinators(ExecutionYear executionYear) {
        List<Coordinator> coordinators = new ArrayList<Coordinator>();
        
        for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansForYear(executionYear)) {
            ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            
            if (executionDegree != null) {
                coordinators.addAll(executionDegree.getCoordinatorsList());
            }
        }
        
        return coordinators;
    }
    
    public Collection<Teacher> getResponsibleCoordinatorsTeachers(ExecutionYear executionYear) {
	Set<Teacher> result = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan
		    .getExecutionDegreeByYear(executionYear);
	    if (executionDegree != null) {
		for (final Coordinator coordinator : executionDegree.getResponsibleCoordinators()) {
                    final Person person = coordinator.getPerson();
                    final Teacher teacher = person.getTeacher();
                    if (teacher != null) { 
                        result.add(teacher);
                    }
		}
	    }
	}
	return new ArrayList<Teacher>(result);
    }

    public Collection<Teacher> getCurrentResponsibleCoordinatorsTeachers() {
	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	Collection<Teacher> result = this.getResponsibleCoordinatorsTeachers(executionYear);

	if (!result.isEmpty()) {
	    return result;
	}

	for (; executionYear != null; executionYear = executionYear.getNextExecutionYear()) {
	    result = this.getResponsibleCoordinatorsTeachers(executionYear);

	    if (!result.isEmpty()) {
		return result;
	    }
	}

	return new ArrayList<Teacher>();
    }

    public Collection<Campus> getCampus(ExecutionYear executionYear) {
	Set<Campus> result = new HashSet<Campus>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan
		    .getExecutionDegreeByYear(executionYear);
	    if (executionDegree != null && executionDegree.hasCampus()) {
		result.add(executionDegree.getCampus());
	    }
	}
	return new ArrayList<Campus>(result);
    }

    public Collection<Campus> getCurrentCampus() {
	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	Collection<Campus> result = this.getCampus(executionYear);

	if (!result.isEmpty()) {
	    return result;
	}

	for (; executionYear != null; executionYear = executionYear.getNextExecutionYear()) {
	    result = this.getCampus(executionYear);

	    if (!result.isEmpty()) {
		return result;
	    }
	}

	return new ArrayList<Campus>();
    }

    public String constructSchoolClassPrefix(final Integer curricularYear) {
	return isBolonhaDegree() ? StringAppender.append(getSigla(), "0", curricularYear.toString())
		: "";
    }

    public MultiLanguageString getNameI18N() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	if (getName() != null && getName().length() > 0) {
	    multiLanguageString.setContent(Language.pt, getName());
	}
	if (getNameEn() != null && getNameEn().length() > 0) {
	    multiLanguageString.setContent(Language.en, getNameEn());
	}
	return multiLanguageString;
    }
    
    public int compareTo(Object object) {
	final Degree degree = (Degree) object;
	return getName().compareTo(degree.getName());
    }

}

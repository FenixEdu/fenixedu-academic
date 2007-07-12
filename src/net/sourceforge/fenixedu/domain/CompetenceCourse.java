package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.CompetenceCourseInformationRequestBean;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.UniqueAcronymCreator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

public class CompetenceCourse extends CompetenceCourse_Base {

    public static final Comparator<CompetenceCourse> COMPETENCE_COURSE_COMPARATOR_BY_NAME = new BeanComparator(
	    "name", Collator.getInstance());

    protected CompetenceCourse() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CompetenceCourse(String code, String name, Collection<Department> departments) {
	this();
	super.setCurricularStage(CurricularStage.OLD);
	fillFields(code, name);
	if (departments != null) {
	    addDepartments(departments);
	}
    }

    public CompetenceCourse(String name, String nameEn, Boolean basic, RegimeType regimeType,
	    CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type,
	    CurricularStage curricularStage, CompetenceCourseGroupUnit unit) {

	this();
	super.setCurricularStage(curricularStage);
	setType(type);

	if (!unit.isCompetenceCourseGroupUnit()) {
	    throw new DomainException("");
	}
	super.setCompetenceCourseGroupUnit(unit);

	CompetenceCourseInformation competenceCourseInformation = new CompetenceCourseInformation(name
		.trim(), nameEn.trim(), basic, regimeType, competenceCourseLevel, null);
	super.addCompetenceCourseInformations(competenceCourseInformation);

	// unique acronym creation
	try {
	    final UniqueAcronymCreator<CompetenceCourse> uniqueAcronymCreator = new UniqueAcronymCreator<CompetenceCourse>(
		    "name", "acronym", (Set<CompetenceCourse>) CompetenceCourse
			    .readBolonhaCompetenceCourses(), true);
	    competenceCourseInformation.setAcronym((String) uniqueAcronymCreator.create(this).getLeft());
	} catch (Exception e) {
	    throw new DomainException("competence.course.unable.to.create.acronym");
	}
    }

    public boolean isBolonha() {
	return !getCurricularStage().equals(CurricularStage.OLD);
    }

    public void addCompetenceCourseLoad(Double theoreticalHours, Double problemsHours,
	    Double laboratorialHours, Double seminaryHours, Double fieldWorkHours,
	    Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
	    Double ectsCredits, Integer order, CurricularPeriodType curricularPeriodType) {
	checkIfCanEdit(false);
	getRecentCompetenceCourseInformation().addCompetenceCourseLoads(
		new CompetenceCourseLoad(theoreticalHours, problemsHours, laboratorialHours,
			seminaryHours, fieldWorkHours, trainingPeriodHours, tutorialOrientationHours,
			autonomousWorkHours, ectsCredits, order, curricularPeriodType));
    }

    public BibliographicReference getBibliographicReference(Integer oid) {
	return getRecentCompetenceCourseInformation().getBibliographicReferences()
		.getBibliographicReference(oid);
    }

    public BibliographicReferences getBibliographicReferences() {
	return getBibliographicReferences(null);
    }

    public BibliographicReferences getBibliographicReferences(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return (information != null) ? information.getBibliographicReferences() : null;
    }
    
    public List<BibliographicReferences.BibliographicReference> getMainBibliographicReferences() {
	return getMainBibliographicReferences(null);
    }

    public List<BibliographicReferences.BibliographicReference> getMainBibliographicReferences(ExecutionPeriod period) {
	return this.getBibliographicReferences(period).getMainBibliographicReferences();
    }
    
    public List<BibliographicReferences.BibliographicReference> getSecondaryBibliographicReferences() {
	return getSecondaryBibliographicReferences(null);
    }
    
    public List<BibliographicReferences.BibliographicReference> getSecondaryBibliographicReferences(ExecutionPeriod period) {
	return this.getBibliographicReferences(period).getSecondaryBibliographicReferences();
    }

    public void createBibliographicReference(String year, String title, String authors,
	    String reference, String url, BibliographicReferenceType type) {
	checkIfCanEdit(false);
	getBibliographicReferences().createBibliographicReference(year, title, authors, reference, url,
		type);
	getRecentCompetenceCourseInformation().setBibliographicReferences(getBibliographicReferences());
    }

    public void editBibliographicReference(Integer oid, String year, String title, String authors,
	    String reference, String url, BibliographicReferenceType type) {
	getBibliographicReferences().editBibliographicReference(oid, year, title, authors, reference,
		url, type);
	getRecentCompetenceCourseInformation().setBibliographicReferences(getBibliographicReferences());
    }

    public void deleteBibliographicReference(Integer oid) {
	getBibliographicReferences().deleteBibliographicReference(oid);
	getRecentCompetenceCourseInformation().setBibliographicReferences(getBibliographicReferences());
    }

    public void switchBibliographicReferencePosition(Integer oldPosition, Integer newPosition) {
	getBibliographicReferences().switchBibliographicReferencePosition(oldPosition, newPosition);
	getRecentCompetenceCourseInformation().setBibliographicReferences(getBibliographicReferences());
    }

    private void fillFields(String code, String name) {
	if (code == null || code.length() == 0) {
	    throw new DomainException("invalid.competenceCourse.values");
	}
	if (name == null || name.length() == 0) {
	    throw new DomainException("invalid.competenceCourse.values");
	}
	super.setCode(code);
	super.setName(name);
    }

    public void edit(String code, String name, Collection<Department> departments) {
	fillFields(code, name);
	for (final Department department : this.getDepartments()) {
	    if (!departments.contains(department)) {
		super.removeDepartments(department);
	    }
	}
	for (final Department department : departments) {
	    if (!hasDepartments(department)) {
		super.addDepartments(department);
	    }
	}
    }

    public void edit(String name, String nameEn, Boolean basic,
	    CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type,
	    CurricularStage curricularStage) {
	changeCurricularStage(curricularStage);
	setType(type);

	getRecentCompetenceCourseInformation().edit(name.trim(), nameEn.trim(), basic,
		competenceCourseLevel);

	// unique acronym creation
	String acronym = null;
	try {
	    final UniqueAcronymCreator<CompetenceCourse> uniqueAcronymCreator = new UniqueAcronymCreator<CompetenceCourse>(
		    "name", "acronym", (Set<CompetenceCourse>) CompetenceCourse
			    .readBolonhaCompetenceCourses(), true);
	    acronym = (String) uniqueAcronymCreator.create(this).getLeft();
	} catch (Exception e) {
	    throw new DomainException("competence.course.unable.to.create.acronym");
	}
	getRecentCompetenceCourseInformation().setAcronym(acronym);
    }

    public void editAcronym(String acronym) {
	Set<CompetenceCourse> bolonhaCompetenceCourses = (Set<CompetenceCourse>) CompetenceCourse
		.readBolonhaCompetenceCourses();
	for (final CompetenceCourse competenceCourse : bolonhaCompetenceCourses) {
	    if (!competenceCourse.equals(this)
		    && competenceCourse.getAcronym().equalsIgnoreCase(acronym.trim())) {
		throw new DomainException("competenceCourse.existing.acronym", competenceCourse
			.getName(), competenceCourse.getDepartmentUnit().getDepartment().getRealName());
	    }
	}

	getRecentCompetenceCourseInformation().setAcronym(acronym);
    }

    public void changeCurricularStage(CurricularStage curricularStage) {
	if (curricularStage.equals(CurricularStage.APPROVED)) {
	    super.setCreationDateYearMonthDay(new YearMonthDay());
	}
	setCurricularStage(curricularStage);
    }

    private void checkIfCanEdit(boolean scientificCouncilEdit) {
	if (!scientificCouncilEdit && this.getCurricularStage().equals(CurricularStage.APPROVED)) {
	    throw new DomainException("competenceCourse.approved");
	}
    }

    public void edit(String objectives, String program, String evaluationMethod, String objectivesEn,
	    String programEn, String evaluationMethodEn) {
	getRecentCompetenceCourseInformation().edit(objectives, program, evaluationMethod, objectivesEn,
		programEn, evaluationMethodEn);
    }

    public void delete() {
	if (hasAnyAssociatedCurricularCourses()) {
	    throw new DomainException("mustDeleteCurricularCoursesFirst");
	} else if (this.getCurricularStage().equals(CurricularStage.APPROVED)) {
	    throw new DomainException("competenceCourse.approved");
	}
	getDepartments().clear();
	removeCompetenceCourseGroupUnit();
	for (; !getCompetenceCourseInformations().isEmpty(); getCompetenceCourseInformations().get(0)
		.delete())
	    ;
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public void addCurricularCourses(Collection<CurricularCourse> curricularCourses) {
	for (CurricularCourse curricularCourse : curricularCourses) {
	    addAssociatedCurricularCourses(curricularCourse);
	}
    }

    public void addDepartments(Collection<Department> departments) {
	for (Department department : departments) {
	    super.addDepartments(department);
	}
    }

    private TreeSet<CompetenceCourseInformation> getOrderedCompetenceCourseInformations() {
	TreeSet<CompetenceCourseInformation> informations = new TreeSet<CompetenceCourseInformation>(
		new Comparator<CompetenceCourseInformation>() {

		    public int compare(CompetenceCourseInformation o1, CompetenceCourseInformation o2) {
			return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
		    }
		});

	informations.addAll(getCompetenceCourseInformationsSet());
	return informations;
    }

    private CompetenceCourseInformation getRecentCompetenceCourseInformation() {
	return getOrderedCompetenceCourseInformations().last();
    }

    private CompetenceCourseInformation getOldestCompetenceCourseInformation() {
	return getOrderedCompetenceCourseInformations().first();
    }

    public boolean isCompetenceCourseInformationDefinedAtExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(executionPeriod);
	return information != null && information.getExecutionPeriod().equals(executionPeriod);
    }

    public CompetenceCourseInformation findCompetenceCourseInformationForExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {

	if(!isBolonha()) {
	    return null;
	}
	
	if(executionPeriod == null) {
	    return getRecentCompetenceCourseInformation();
	}
	
	if (executionPeriod.isBefore(getOldestCompetenceCourseInformation().getExecutionPeriod())) {
	    return null;
	}

	CompetenceCourseInformation minCompetenceCourseInformation = null;
	for (final CompetenceCourseInformation competenceCourseInformation : getOrderedCompetenceCourseInformations()) {
	    if (competenceCourseInformation.getExecutionPeriod().isAfter(executionPeriod)) {
		return minCompetenceCourseInformation;
	    } else {
		minCompetenceCourseInformation = competenceCourseInformation;
	    }
	}

	return minCompetenceCourseInformation;
    }

    public String getName(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	if ((super.getName() == null || super.getName().length() == 0) && information != null) {
	    return information.getName();
	}
	return super.getName();
    }

    @Override
    public String getName() {
	return getName(null);
    }

    public String getNameEn(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getNameEn() : null;
    }

    public String getNameEn() {
	return getNameEn(null);
    }

    public String getAcronym(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getAcronym() : null;
    }

    public String getAcronym() {
	return getAcronym(null);
    }

    public void setAcronym(String acronym) {
	getRecentCompetenceCourseInformation().setAcronym(acronym);
    }

    public boolean isBasic(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getBasic() : null;
    }

    public boolean isBasic() {
	return isBasic(null);
    }

    public RegimeType getRegime(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getRegime() : null;
    }

    public RegimeType getRegime() {
	return getRegime(null);
    }

    public void setRegime(RegimeType regimeType) {
	getRecentCompetenceCourseInformation().setRegime(regimeType);
    }

    public CompetenceCourseLevel getCompetenceCourseLevel(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getCompetenceCourseLevel() : null;
    }

    public CompetenceCourseLevel getCompetenceCourseLevel() {
	return getCompetenceCourseLevel(null);
    }

    public List<CompetenceCourseLoad> getCompetenceCourseLoads(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getCompetenceCourseLoads() : null;
    }

    public List<CompetenceCourseLoad> getCompetenceCourseLoads() {
	return getCompetenceCourseLoads(null);
    }

    public int getCompetenceCourseLoadsCount(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getCompetenceCourseLoadsCount() : 0;
    }

    public int getCompetenceCourseLoadsCount() {
	return getCompetenceCourseLoadsCount(null);
    }

    public String getObjectives(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getObjectives() : null;
    }

    public String getObjectives() {
	return getObjectives(null);
    }

    public String getProgram(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getProgram() : null;
    }

    public String getProgram() {
	return getProgram(null);
    }

    public String getEvaluationMethod(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getEvaluationMethod() : null;
    }

    public String getEvaluationMethod() {
	return getEvaluationMethod(null);
    }

    public String getObjectivesEn(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getObjectivesEn() : null;
    }
    
    public String getObjectivesEn() {
	return getObjectivesEn(null);
    }

    public String getProgramEn(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getProgramEn() : null;
    }
    
    public String getProgramEn() {
	return getProgramEn(null);
    }

    public String getEvaluationMethodEn(ExecutionPeriod period) {
	CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
	return information != null ? information.getEvaluationMethodEn() : null;
    }
    
    public String getEvaluationMethodEn() {
	return getEvaluationMethodEn(null);
    }

    public double getTheoreticalHours() {
	return getTheoreticalHours(null,null);
    }

    public Double getTheoreticalHours(Integer order) {
	return getTheoreticalHours(order, null);
    }
    
    public double getTheoreticalHours(ExecutionPeriod period) {
	return getTheoreticalHours(null,period);
    }
    
    public double getTheoreticalHours(Integer order,ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getTheoreticalHours(order) : 0.0;
    }

    public double getProblemsHours() {
	return getProblemsHours(null,null);
    }

    public Double getProblemsHours(Integer order) {
	return getProblemsHours(order, null);
    }
    
    public double getProblemsHours(ExecutionPeriod period) {
	return getProblemsHours(null,period);
    }
    
    public double getProblemsHours(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getProblemsHours(order) : 0.0;
    }

    public double getLaboratorialHours() {
	return getLaboratorialHours(null,null);
    }

    public Double getLaboratorialHours(Integer order) {
	return getLaboratorialHours(order, null);
    }
    
    public double getLaboratorialHours(ExecutionPeriod period) {
	return getLaboratorialHours(null,period);
    }
    
    public double getLaboratorialHours(Integer order,ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getLaboratorialHours(order) : 0.0;
    }

    public double getSeminaryHours() {
	return getSeminaryHours(null,null);
    }

    public double getSeminaryHours(ExecutionPeriod period) {
	return getSeminaryHours(null,period);
    }
    
    public Double getSeminaryHours(Integer order) {
	return getSeminaryHours(order, null);
    }
    
    public double getSeminaryHours(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getSeminaryHours(order) : 0.0;
    }

    public double getFieldWorkHours() {
	return getFieldWorkHours(null,null);
    }

    public double getFieldWorkHours(ExecutionPeriod period) {
	return getFieldWorkHours(null,period);
    }
    
    public Double getFieldWorkHours(Integer order) {
	return getFieldWorkHours(order, null);
    }
    
    public double getFieldWorkHours(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getFieldWorkHours(order) : 0.0;
    }

    public double getTrainingPeriodHours() {
	return getTrainingPeriodHours(null,null);
    }

    public double getTrainingPeriodHours(ExecutionPeriod period) {
	return getTrainingPeriodHours(null,period);
    }
    
    public Double getTrainingPeriodHours(Integer order) {
	return getTrainingPeriodHours(order, null);
    }
    
    public double getTrainingPeriodHours(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getTrainingPeriodHours(order) : 0.0;
    }

    public double getTutorialOrientationHours() {
	return getTutorialOrientationHours(null,null);
    }

    public Double getTutorialOrientationHours(Integer order) {
	return getTutorialOrientationHours(order, null);
    }
    
    public double getTutorialOrientationHours(ExecutionPeriod period) {
	return getTutorialOrientationHours(null,period);
    }
    
    public double getTutorialOrientationHours(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getTutorialOrientationHours(order) : 0.0;
    }

    public double getAutonomousWorkHours() {
	return getAutonomousWorkHours(null,null);
    }

    public double getAutonomousWorkHours(ExecutionPeriod period) {
	return getAutonomousWorkHours(null, period);
    }
    
    public Double getAutonomousWorkHours(Integer order) {
	return getAutonomousWorkHours(order, null);
    }
    
    public Double getAutonomousWorkHours(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getAutonomousWorkHours(order) : 0.0;
    }

    public double getContactLoad() {
	return getContactLoad(null, null);
    }

    public Double getContactLoad(ExecutionPeriod period) {
	return getContactLoad(null,period);
    }
    
    public Double getContactLoad(Integer order) {
	return getContactLoad(order, null);
    }
    
    public Double getContactLoad(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getContactLoad(order) : 0.0;
    }

    public double getTotalLoad() {
	return getTotalLoad(null, null);
    }

    public Double getTotalLoad(Integer order) {
	return getTotalLoad(order, null);
    }
    
    public double getTotalLoad(ExecutionPeriod period) {
	return getTotalLoad(null, period);
    }
    
    public Double getTotalLoad(Integer order, ExecutionPeriod period) {
	CompetenceCourseInformation information = (period != null) ? findCompetenceCourseInformationForExecutionPeriod(period) : getRecentCompetenceCourseInformation();
	return (information != null) ? information.getTotalLoad(order) : 0.0;
    }

    public double getEctsCredits() {
	return getEctsCredits((Integer) null, (ExecutionPeriod) null);
    }

    public double getEctsCredits(final Integer order) {
	return getEctsCredits(order, (ExecutionPeriod) null);
    }

    public double getEctsCredits(final ExecutionPeriod executionPeriod) {
	return getEctsCredits((Integer) null, executionPeriod);
    }

    public Double getEctsCredits(final Integer order, final ExecutionPeriod executionPeriod) {
	final CompetenceCourseInformation competenceCourseInformation = executionPeriod == null ? getRecentCompetenceCourseInformation()
		: findCompetenceCourseInformationForExecutionPeriod(executionPeriod);

	if (competenceCourseInformation != null) {
	    return competenceCourseInformation.getEctsCredits(order);
	}

	return 0.0d;
    }

    public Map<Degree, List<CurricularCourse>> getAssociatedCurricularCoursesGroupedByDegree() {
	Map<Degree, List<CurricularCourse>> curricularCoursesMap = new HashMap<Degree, List<CurricularCourse>>();
	for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
	    Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
	    List<CurricularCourse> curricularCourses = curricularCoursesMap.get(degree);
	    if (curricularCourses == null) {
		curricularCourses = new ArrayList<CurricularCourse>();
		curricularCoursesMap.put(degree, curricularCourses);
	    }
	    curricularCourses.add(curricularCourse);
	}
	return curricularCoursesMap;
    }

    public Set<DegreeCurricularPlan> presentIn() {
	Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
	for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
	    result.add(curricularCourse.getDegreeCurricularPlan());
	}

	return result;
    }

    public boolean isAssociatedToAnyDegree(final Set<Degree> degrees) {
	for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
	    final Degree degree = degreeCurricularPlan.getDegree();
	    if (degrees.contains(degree)) {
		return true;
	    }
	}
	return false;
    }

    @SuppressWarnings("unchecked")
    public List<CurricularCourse> getCurricularCoursesWithActiveScopesInExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {
	return (List<CurricularCourse>) CollectionUtils.select(getAssociatedCurricularCourses(),
		new Predicate() {

		    public boolean evaluate(Object arg0) {
			CurricularCourse curricularCourse = (CurricularCourse) arg0;

			for (DegreeModuleScope moduleScope : curricularCourse.getDegreeModuleScopes()) {
			    if (moduleScope.isActiveForExecutionPeriod(executionPeriod)) {
				return true;
			    }
			}
			return false;
		    }
		});
    }

    public CurricularCourse getCurricularCourse(final DegreeCurricularPlan degreeCurricularPlan) {
	for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
	    if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
		return curricularCourse;
	    }
	}

	return null;
    }

    public List<Enrolment> getActiveEnrollments(ExecutionYear executionYear) {
	List<Enrolment> results = new ArrayList<Enrolment>();
	for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
	    results.addAll(curricularCourse.getActiveEnrollments(executionYear));
	}
	return results;
    }

    public List<Enrolment> getActiveEnrollments(ExecutionPeriod executionPeriod) {
	List<Enrolment> results = new ArrayList<Enrolment>();
	for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
	    curricularCourse.addActiveEnrollments(results, executionPeriod);
	}
	return results;
    }

    public Boolean hasActiveScopesInExecutionYear(ExecutionYear executionYear) {
	List<ExecutionPeriod> executionPeriods = executionYear.getExecutionPeriods();
	List<CurricularCourse> curricularCourses = this.getAssociatedCurricularCourses();
	for (ExecutionPeriod executionPeriod : executionPeriods) {
	    for (CurricularCourse curricularCourse : curricularCourses) {
		if (curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod).size() > 0) {
		    return Boolean.TRUE;
		}
	    }
	}
	return Boolean.FALSE;
    }

    public boolean hasActiveScopesInExecutionPeriod(ExecutionPeriod executionPeriod) {
	List<CurricularCourse> curricularCourses = this.getAssociatedCurricularCourses();
	for (CurricularCourse curricularCourse : curricularCourses) {
	    if (curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod).size() > 0) {
		return true;
	    }
	}
	return false;
    }

    public DepartmentUnit getDepartmentUnit() {
	return this.getCompetenceCourseGroupUnit().getDepartmentUnit();
    }

    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads(ExecutionPeriod period) {
	final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>(
		getCompetenceCourseLoadsCount(period));
	result.addAll(getCompetenceCourseLoads(period));
	Collections.sort(result);
	return result;
    }
    
    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads() {
	final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>(
		getCompetenceCourseLoadsCount());
	result.addAll(getCompetenceCourseLoads());
	Collections.sort(result);
	return result;
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void addCompetenceCourseInformations(CompetenceCourseInformation competenceCourseInformations) {
	super.addCompetenceCourseInformations(competenceCourseInformations);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void addDepartments(Department departments) {
	super.addDepartments(departments);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void removeCompetenceCourseInformations(
	    CompetenceCourseInformation competenceCourseInformations) {
	super.removeCompetenceCourseInformations(competenceCourseInformations);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void removeDepartments(Department departments) {
	super.removeDepartments(departments);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void removeCompetenceCourseGroupUnit() {
	super.removeCompetenceCourseGroupUnit();
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void setCode(String code) {
	super.setCode(code);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void setCreationDate(Date creationDate) {
	super.setCreationDate(creationDate);
    }

    @Override
    @Checked("CompetenceCoursePredicates.editCurricularStagePredicate")
    public void setCurricularStage(CurricularStage curricularStage) {
	if (this.hasAnyAssociatedCurricularCourses() && curricularStage.equals(CurricularStage.DRAFT)) {
	    throw new DomainException("competenceCourse.has.already.associated.curricular.courses");
	}
	super.setCurricularStage(curricularStage);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void setName(String name) {
	super.setName(name);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void setCompetenceCourseGroupUnit(CompetenceCourseGroupUnit competenceCourseGroupUnit) {
	super.setCompetenceCourseGroupUnit(competenceCourseGroupUnit);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void addAssociatedCurricularCourses(CurricularCourse associatedCurricularCourses) {
	super.addAssociatedCurricularCourses(associatedCurricularCourses);
    }

    public ScientificAreaUnit getScientificAreaUnit() {
	if (this.getCompetenceCourseGroupUnit().hasAnyParentUnits()) {
	    if (this.getCompetenceCourseGroupUnit().getParentUnits().size() > 1) {
		throw new DomainException("compentence.course.should.have.only.one.scientific.area");
	    }

	    return (ScientificAreaUnit) this.getCompetenceCourseGroupUnit().getParentUnits().iterator()
		    .next();
	}
	return null;
    }

    public boolean isAnual() {
	return this.getRegime() == RegimeType.ANUAL;
    }

    public boolean isApproved() {
	return getCurricularStage() == CurricularStage.APPROVED;
    }

    public void transfer(CompetenceCourseGroupUnit competenceCourseGroupUnit) {
	super.setCompetenceCourseGroupUnit(competenceCourseGroupUnit);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static List<CompetenceCourse> readOldCompetenceCourses() {
	final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
	for (final CompetenceCourse competenceCourse : RootDomainObject.getInstance()
		.getCompetenceCoursesSet()) {
	    if (!competenceCourse.isBolonha()) {
		result.add(competenceCourse);
	    }
	}
	return result;
    }

    public static Collection<CompetenceCourse> readBolonhaCompetenceCourses() {
	final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(
		COMPETENCE_COURSE_COMPARATOR_BY_NAME);
	for (final CompetenceCourse competenceCourse : RootDomainObject.getInstance()
		.getCompetenceCoursesSet()) {
	    if (competenceCourse.isBolonha()) {
		result.add(competenceCourse);
	    }
	}
	return result;
    }

    public static Collection<CompetenceCourse> readApprovedBolonhaCompetenceCourses() {
	final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(
		COMPETENCE_COURSE_COMPARATOR_BY_NAME);
	for (final CompetenceCourse competenceCourse : RootDomainObject.getInstance()
		.getCompetenceCoursesSet()) {
	    if (competenceCourse.isBolonha() && competenceCourse.isApproved()) {
		result.add(competenceCourse);
	    }
	}
	return result;
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

    public MultiLanguageString getObjectivesI18N() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	if (getObjectives() != null && getObjectives().length() > 0) {
	    multiLanguageString.setContent(Language.pt, getObjectives());
	}
	if (getObjectivesEn() != null && getObjectivesEn().length() > 0) {
	    multiLanguageString.setContent(Language.en, getObjectivesEn());
	}
	return multiLanguageString;
    }

    public MultiLanguageString getProgramI18N() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	if (getProgram() != null && getProgram().length() > 0) {
	    multiLanguageString.setContent(Language.pt, getProgram());
	}
	if (getProgramEn() != null && getProgramEn().length() > 0) {
	    multiLanguageString.setContent(Language.en, getProgramEn());
	}
	return multiLanguageString;
    }

    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(
	    final ExecutionPeriod executionPeriod) {
	Set<ExecutionCourse> executionCourseSet = new HashSet<ExecutionCourse>();

	List<CurricularCourse> curricularCourseList = getCurricularCoursesWithActiveScopesInExecutionPeriod(executionPeriod);

	for (CurricularCourse curricularCourse : curricularCourseList) {
	    executionCourseSet.addAll(curricularCourse
		    .getExecutionCoursesByExecutionPeriod(executionPeriod));
	}

	List<ExecutionCourse> executionCourseList = new ArrayList<ExecutionCourse>();
	executionCourseList.addAll(executionCourseSet);

	return executionCourseList;
    }

    public boolean hasEnrolmentForPeriod(ExecutionPeriod executionPeriod) {
	for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
	    if (curricularCourse.hasEnrolmentForPeriod(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isDissertation() {
	return getType() == CompetenceCourseType.DISSERTATION;
    }

    public Integer getDraftCompetenceCourseInformationChangeRequestsCount() {
	int count = 0;
	for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequests()) {
	    if (request.getApproved() == null) {
		count++;
	    }
	}
	return count;
    }
    
    public CompetenceCourseInformationChangeRequest getCompetenceCourseInformationChangeRequests(ExecutionPeriod period) { 
	for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequests()) {
	    if (request.getExecutionPeriod() == period) {
		return request;
	    }
	}
	return null;
    }
    
    @Override
    public void addCompetenceCourseInformationChangeRequests(CompetenceCourseInformationChangeRequest request) {
        if(getCompetenceCourseInformationChangeRequests(request.getExecutionPeriod()) != null) {
            throw new DomainException("error.can.only.exist.one.request.per.execution.period");
        }
        super.addCompetenceCourseInformationChangeRequests(request);
    }
}

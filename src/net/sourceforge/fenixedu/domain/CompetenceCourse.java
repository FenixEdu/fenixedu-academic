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

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.UniqueAcronymCreator;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class CompetenceCourse extends CompetenceCourse_Base {

    public static final Comparator<CompetenceCourse> COMPETENCE_COURSE_COMPARATOR_BY_NAME = new BeanComparator("name", Collator.getInstance());
    
    private CompetenceCourseInformation recentCompetenceCourseInformation;

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

    public CompetenceCourse(String name, String nameEn, Boolean basic,
            RegimeType regimeType, CompetenceCourseLevel competenceCourseLevel,
            CurricularStage curricularStage, Unit unit) {
     
        this();
        super.setCurricularStage(curricularStage);
        
        if (unit.getType() != PartyTypeEnum.COMPETENCE_COURSE_GROUP) {
            throw new DomainException("");
        }
        super.setCompetenceCourseGroupUnit(unit);

        CompetenceCourseInformation competenceCourseInformation = new CompetenceCourseInformation(name.trim(), nameEn.trim(), basic, regimeType, competenceCourseLevel, null);
        super.addCompetenceCourseInformations(competenceCourseInformation);

        // unique acronym creation
        try {
            final UniqueAcronymCreator uniqueAcronymCreator = new UniqueAcronymCreator<CompetenceCourse>(
                    "name", 
                    "acronym", 
                    (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses(), 
                    true);
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
        return getRecentCompetenceCourseInformation().getBibliographicReferences().getBibliographicReference(oid);
    }

    public BibliographicReferences getBibliographicReferences() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getBibliographicReferences() : null;
    }

    public void createBibliographicReference(String year, String title, String authors, String reference,
            String url, BibliographicReferenceType type) {
        checkIfCanEdit(false);
        getBibliographicReferences().createBibliographicReference(year, title, authors, reference, url, type);
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

    public void edit(String name, String nameEn, Boolean basic, CompetenceCourseLevel competenceCourseLevel, CurricularStage curricularStage) {
        changeCurricularStage(curricularStage);
        getRecentCompetenceCourseInformation().edit(name.trim(), nameEn.trim(), basic, competenceCourseLevel);
        
        // unique acronym creation
        String acronym = null;
        try {
            final UniqueAcronymCreator uniqueAcronymCreator = new UniqueAcronymCreator<CompetenceCourse>(
                    "name", 
                    "acronym", 
                    (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses(), 
                    true);
            acronym = (String) uniqueAcronymCreator.create(this).getLeft();
        } catch (Exception e) {
            throw new DomainException("competence.course.unable.to.create.acronym");
        }
        getRecentCompetenceCourseInformation().setAcronym(acronym);
    }

    public void editAcronym(String acronym) {
        Set<CompetenceCourse> bolonhaCompetenceCourses = (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses();
        for (final CompetenceCourse competenceCourse : bolonhaCompetenceCourses) {
            if (!competenceCourse.equals(this) && competenceCourse.getAcronym().equalsIgnoreCase(acronym.trim())) {
                throw new DomainException("competenceCourse.existing.acronym", 
                        competenceCourse.getName(),
                        competenceCourse.getDepartmentUnit().getDepartment().getRealName());
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
        for (;!getCompetenceCourseInformations().isEmpty();getCompetenceCourseInformations().get(0).delete());
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

    private CompetenceCourseInformation getRecentCompetenceCourseInformation() {
        return (recentCompetenceCourseInformation != null) ? recentCompetenceCourseInformation
                : (recentCompetenceCourseInformation = findRecentCompetenceCourseInformation());
    }

    private CompetenceCourseInformation findRecentCompetenceCourseInformation() {
        for (final CompetenceCourseInformation competenceCourseInformation : getCompetenceCourseInformations()) {
            if (competenceCourseInformation.getEndDate() == null) {
                // endDate not defined: most recent information
                return competenceCourseInformation;
            }
        }
        return null;
    }

    @Override
    public String getName() {

        if ((super.getName() == null || super.getName().length() == 0)
                && getRecentCompetenceCourseInformation() != null) {
            return getRecentCompetenceCourseInformation().getName();
        }
        return super.getName();
    }

    public String getNameEn() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation()
                .getNameEn() : null;
    }

    public String getAcronym() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation()
                .getAcronym() : null;
    }

    public void setAcronym(String acronym) {
        getRecentCompetenceCourseInformation().setAcronym(acronym);
    }
    
    public boolean isBasic() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation()
                .getBasic().booleanValue() : false;
    }

    public RegimeType getRegime() {
        return getRecentCompetenceCourseInformation().getRegime();
    }

    public void setRegime(RegimeType regimeType) {
        getRecentCompetenceCourseInformation().setRegime(regimeType);
    }
    
    public CompetenceCourseLevel getCompetenceCourseLevel() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation() .getCompetenceCourseLevel() : null;
    }

    public List<CompetenceCourseLoad> getCompetenceCourseLoads() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getCompetenceCourseLoads() : null;
    }
    
    public int getCompetenceCourseLoadsCount() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getCompetenceCourseLoadsCount() : 0;
    }

    public String getObjectives() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getObjectives() : null;
    }

    public String getProgram() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getProgram() : null;
    }

    public String getEvaluationMethod() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getEvaluationMethod() : null;
    }

    public String getObjectivesEn() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getObjectivesEn() : null;
    }

    public String getProgramEn() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getProgramEn() : null;
    }

    public String getEvaluationMethodEn() {
        return (getRecentCompetenceCourseInformation() != null) ? getRecentCompetenceCourseInformation().getEvaluationMethodEn() : null;
    }

    public double getTheoreticalHours() {
        return getTheoreticalHours(null);
    }
    
    public double getTheoreticalHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getTheoreticalHours(order);
        }
        return result;
    }

    public double getProblemsHours() {
        return getProblemsHours(null);
    }
    
    public double getProblemsHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getProblemsHours(order);
        }
        return result;
    }

    public double getLaboratorialHours() {
        return getLaboratorialHours(null);
    }
    
    public double getLaboratorialHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getLaboratorialHours(order);
        }
        return result;
    }

    public double getSeminaryHours() {
        return getSeminaryHours(null);
    }
    
    public double getSeminaryHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getSeminaryHours(order);
        }
        return result;
    }

    public double getFieldWorkHours() {
        return getFieldWorkHours(null);
    }
    
    public double getFieldWorkHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getFieldWorkHours(order);
        }
        return result;
    }

    public double getTrainingPeriodHours() {
        return getTrainingPeriodHours(null);
    }
    
    public double getTrainingPeriodHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getTrainingPeriodHours(order);
        }
        return result;
    }

    public double getTutorialOrientationHours() {
        return getTutorialOrientationHours(null);
    }
    
    public double getTutorialOrientationHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getTutorialOrientationHours(order);
        }
        return result;
    }

    public double getAutonomousWorkHours() {
        return getAutonomousWorkHours(null);
    }
    
    public Double getAutonomousWorkHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getAutonomousWorkHours(order);
        }
        return result;
    }
    
    public double getContactLoad() {
        return getContactLoad(null);
    }
    
    public Double getContactLoad(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getContactLoad(order);
        }
        return result;
    }
    
    public double getTotalLoad() {
        return getTotalLoad(null);
    }
    
    public Double getTotalLoad(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getTotalLoad(order);
        }
        return result;
    }
    
    public double getEctsCredits() {
        return getEctsCredits(null);
    }
    
    public Double getEctsCredits(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getEctsCredits(order);
        }
        return result;
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
            results.addAll(curricularCourse.getActiveEnrollments(executionPeriod));
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

    public Unit getDepartmentUnit() {
        return this.getCompetenceCourseGroupUnit().getDepartmentUnit();
    }
    
    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads() {
        final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>(getCompetenceCourseLoadsCount());
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
    public void setCompetenceCourseGroupUnit(Unit unit) {
        super.setCompetenceCourseGroupUnit(unit);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void addAssociatedCurricularCourses(CurricularCourse associatedCurricularCourses) {
        super.addAssociatedCurricularCourses(associatedCurricularCourses);
    }

    public Unit getScientificAreaUnit() {
        if (this.getCompetenceCourseGroupUnit().hasAnyParentUnits()) {
            if (this.getCompetenceCourseGroupUnit().getParentUnits().size() > 1) { 
                throw new DomainException("compentence.course.should.have.only.one.scientific.area");
            }
            return (Unit) this.getCompetenceCourseGroupUnit().getParentUnits().get(0);
        }  
        return null;
    }
    
    public Set<DegreeCurricularPlan> presentIn() {
        Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            result.add(curricularCourse.getDegreeCurricularPlan());
        }
        
        return result;
    }

    public boolean isAnual() {
        return this.getRegime() == RegimeType.ANUAL;
    }

    public boolean isApproved() {
        return getCurricularStage() == CurricularStage.APPROVED;
    }
    
    public void transfer(Unit competenceCourseGroupUnit) {
        super.setCompetenceCourseGroupUnit(competenceCourseGroupUnit);
    }
    
    //  -------------------------------------------------------------
    // read static methods 
    // -------------------------------------------------------------
    public static List<CompetenceCourse> readOldCompetenceCourses() {
        final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (final CompetenceCourse competenceCourse : RootDomainObject.getInstance().getCompetenceCoursesSet()) {
            if (!competenceCourse.isBolonha()) {
                result.add(competenceCourse);
            }
        }
        return result;
    }
    
    public static Collection<CompetenceCourse> readBolonhaCompetenceCourses() {
        final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (final CompetenceCourse competenceCourse : RootDomainObject.getInstance().getCompetenceCoursesSet()) {
            if (competenceCourse.isBolonha()) {
                result.add(competenceCourse);
            }
        }
        return result;
    }

    public static Collection<CompetenceCourse> readApprovedBolonhaCompetenceCourses() {
        final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (final CompetenceCourse competenceCourse : RootDomainObject.getInstance().getCompetenceCoursesSet()) {
            if (competenceCourse.isBolonha() && competenceCourse.isApproved()) {
                result.add(competenceCourse);
            }
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

}

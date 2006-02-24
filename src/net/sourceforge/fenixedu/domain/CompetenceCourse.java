package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CompetenceCourse extends CompetenceCourse_Base {

    private CompetenceCourseInformation recentCompetenceCourseInformation;

    protected CompetenceCourse() {
        super();
    }

    public CompetenceCourse(String code, String name, Collection<Department> departments,
            CurricularStage curricularStage) {
        this();
        setCurricularStage(curricularStage);
        fillFields(code, name);
        if (departments != null) {
            addDepartments(departments);
        }
    }

    public CompetenceCourse(String name, String nameEn, String acronym, Boolean basic,
            RegimeType regimeType, CurricularStage curricularStage, Unit unit) {
        this();
        super.setCurricularStage(curricularStage);
        super.setUnit(unit);
        super.addCompetenceCourseInformations(new CompetenceCourseInformation(name.trim(), nameEn.trim(), acronym.trim(), basic,
                regimeType, null));
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
        return getRecentCompetenceCourseInformation().getBibliographicReferences();
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
        setCode(code);
        setName(name);
    }

    public void edit(String code, String name, Collection<Department> departments) {
        fillFields(code, name);
        for (final Department department : this.getDepartments()) {
            if (!departments.contains(department)) {
                removeDepartments(department);
            }
        }
        for (final Department department : departments) {
            if (!hasDepartments(department)) {
                addDepartments(department);
            }
        }
    }

    public void edit(String name, String nameEn, String acronym, Boolean basic,
            CurricularStage curricularStage, boolean scientificCouncilEdit) {
        if (curricularStage.equals(CurricularStage.APPROVED)) {
            super.setCreationDate(Calendar.getInstance().getTime());
        }
        setCurricularStage(curricularStage);
        getRecentCompetenceCourseInformation().edit(name.trim(), nameEn.trim(), acronym.trim(), basic);
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
        removeUnit();
        for (; !getCompetenceCourseInformations().isEmpty(); getCompetenceCourseInformations().get(0)
                .delete())
            ;
        super.deleteDomainObject();
    }

    
    public void addCurricularCourses(Collection<CurricularCourse> curricularCourses) {
        for (CurricularCourse curricularCourse : curricularCourses) {
            addAssociatedCurricularCourses(curricularCourse);
        }
    }

    public void addDepartments(Collection<Department> departments) {
        for (Department department : departments) {
            addDepartments(department);
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

    public List<CompetenceCourseLoad> getCompetenceCourseLoads() {
        return getRecentCompetenceCourseInformation().getCompetenceCourseLoads();
    }
    
    public int getCompetenceCourseLoadsCount() {
        return getRecentCompetenceCourseInformation().getCompetenceCourseLoadsCount();
    }

    public String getObjectives() {
        return getRecentCompetenceCourseInformation().getObjectives();
    }

    public String getProgram() {
        return getRecentCompetenceCourseInformation().getProgram();
    }

    public String getEvaluationMethod() {
        return getRecentCompetenceCourseInformation().getEvaluationMethod();
    }

    public String getObjectivesEn() {
        return getRecentCompetenceCourseInformation().getObjectivesEn();
    }

    public String getProgramEn() {
        return getRecentCompetenceCourseInformation().getProgramEn();
    }

    public String getEvaluationMethodEn() {
        return getRecentCompetenceCourseInformation().getEvaluationMethodEn();
    }

    public double getEctsCredits() {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getEctsCredits();
        }
        return result;
    }

    public Double getContactLoad(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getContactLoad(order);
        }
        return result;
    }
    
    public Double getAutonomousWorkHours(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getAutonomousWorkHours(order);
        }
        return result;
    }
    
    public Double getTotalLoad(Integer order) {
        double result = 0.0;
        if (getRecentCompetenceCourseInformation() != null) {
            result = getRecentCompetenceCourseInformation().getTotalLoad(order);
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

    public List<EnrolmentEvaluation> getActiveEnrollmentEvaluations(ExecutionYear executionYear) {
        List<EnrolmentEvaluation> results = new ArrayList<EnrolmentEvaluation>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            results.addAll(curricularCourse.getActiveEnrollmentEvaluations(executionYear));
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

    public Unit getDepartmentUnit() {
        return this.getUnit().getDepartmentUnit();
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
    public void removeUnit() {
        super.removeUnit();
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
    public void setUnit(Unit unit) {
        super.setUnit(unit);
    }

    @Override
    @Checked("CompetenceCoursePredicates.writePredicate")
    public void addAssociatedCurricularCourses(CurricularCourse associatedCurricularCourses) {
        super.addAssociatedCurricularCourses(associatedCurricularCourses);
    }

}

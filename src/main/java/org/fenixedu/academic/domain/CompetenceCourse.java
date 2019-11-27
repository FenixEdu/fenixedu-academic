/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevel;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoad;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.service.services.bolonhaManager.CompetenceCourseManagementAccessControl;
import org.fenixedu.academic.util.UniqueAcronymCreator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.YearMonthDay;

public class CompetenceCourse extends CompetenceCourse_Base {

    public static final Comparator<CompetenceCourse> COMPETENCE_COURSE_COMPARATOR_BY_NAME = new Comparator<CompetenceCourse>() {

        @Override
        public int compare(CompetenceCourse o1, CompetenceCourse o2) {
            final int result = Collator.getInstance().compare(o1.getName(), o2.getName());
            return result != 0 ? result : DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
        }

    };

    protected CompetenceCourse() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CompetenceCourse(String name, String nameEn, Boolean basic, AcademicPeriod academicPeriod,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, CurricularStage curricularStage,
            CompetenceCourseGroupUnit unit, ExecutionInterval startInterval) {

        this();
        super.setCurricularStage(curricularStage);
        setType(type);

        CompetenceCourseInformation competenceCourseInformation = new CompetenceCourseInformation(name.trim(), nameEn.trim(),
                basic, academicPeriod, competenceCourseLevel, startInterval, unit);
        super.addCompetenceCourseInformations(competenceCourseInformation);

        // unique acronym creation
        try {
            Set<CompetenceCourse> competenceCourses = (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses();
            competenceCourses.remove(this);
            final UniqueAcronymCreator<CompetenceCourse> uniqueAcronymCreator = new UniqueAcronymCreator<CompetenceCourse>(
                    CompetenceCourse::getName, CompetenceCourse::getAcronym, competenceCourses);
            competenceCourseInformation.setAcronym(uniqueAcronymCreator.create(this).getLeft());
        } catch (Exception e) {
            throw new DomainException("competence.course.unable.to.create.acronym");
        }
    }

    public void addCompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double otherHours, Double autonomousWorkHours, Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {
        checkIfCanEdit(false);
        findInformationMostRecentUntil(null).addCompetenceCourseLoads(new CompetenceCourseLoad(theoreticalHours, problemsHours,
                laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours, tutorialOrientationHours, otherHours,
                autonomousWorkHours, ectsCredits, order, academicPeriod));
    }

    public BibliographicReference getBibliographicReference(Integer oid) {
        return findInformationMostRecentUntil(null).getBibliographicReferences().getBibliographicReference(oid);
    }

    public BibliographicReferences getBibliographicReferences() {
        return getBibliographicReferences(null);
    }

    public BibliographicReferences getBibliographicReferences(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getBibliographicReferences() : null;
    }

    public List<BibliographicReferences.BibliographicReference> getMainBibliographicReferences() {
        return getMainBibliographicReferences(null);
    }

    public List<BibliographicReferences.BibliographicReference> getMainBibliographicReferences(final ExecutionInterval interval) {
        return this.getBibliographicReferences(interval).getMainBibliographicReferences();
    }

    public List<BibliographicReferences.BibliographicReference> getSecondaryBibliographicReferences() {
        return getSecondaryBibliographicReferences(null);
    }

    public List<BibliographicReferences.BibliographicReference> getSecondaryBibliographicReferences(
            final ExecutionInterval interval) {
        return this.getBibliographicReferences(interval).getSecondaryBibliographicReferences();
    }

    public List<BibliographicReferences.BibliographicReference> getAllBibliographicReferences(final ExecutionInterval interval) {
        final List<BibliographicReferences.BibliographicReference> result =
                new ArrayList<BibliographicReferences.BibliographicReference>();
        result.addAll(getMainBibliographicReferences(interval));
        result.addAll(getSecondaryBibliographicReferences(interval));
        return result;
    }

    public void createBibliographicReference(String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type) {
        checkIfCanEdit(false);
        CompetenceCourseInformation info = findInformationMostRecentUntil(null);
        info.setBibliographicReferences(info.getBibliographicReferences().with(year, title, authors, reference, url, type));
    }

    public void editBibliographicReference(Integer index, String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type) {
        CompetenceCourseInformation info = findInformationMostRecentUntil(null);
        info.setBibliographicReferences(
                info.getBibliographicReferences().replacing(index, year, title, authors, reference, url, type));
    }

    public void deleteBibliographicReference(Integer index) {
        CompetenceCourseInformation info = findInformationMostRecentUntil(null);
        info.setBibliographicReferences(info.getBibliographicReferences().without(index));
    }

    public void switchBibliographicReferencePosition(Integer oldPosition, Integer newPosition) {
        CompetenceCourseInformation info = findInformationMostRecentUntil(null);
        info.setBibliographicReferences(info.getBibliographicReferences().movingBibliographicReference(oldPosition, newPosition));
    }

    public void edit(String name, String nameEn, Boolean basic, CompetenceCourseLevel competenceCourseLevel,
            CompetenceCourseType type, CurricularStage curricularStage) {
        changeCurricularStage(curricularStage);
        setType(type);

        findInformationMostRecentUntil(null).edit(name.trim(), nameEn.trim(), basic, competenceCourseLevel);

        // unique acronym creation
        String acronym = null;
        try {
            final UniqueAcronymCreator<CompetenceCourse> uniqueAcronymCreator =
                    new UniqueAcronymCreator<CompetenceCourse>(CompetenceCourse::getName, CompetenceCourse::getAcronym,
                            (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses());
            acronym = uniqueAcronymCreator.create(this).getLeft();
        } catch (Exception e) {
            throw new DomainException("competence.course.unable.to.create.acronym");
        }
        findInformationMostRecentUntil(null).setAcronym(acronym);
    }

    public void editAcronym(String acronym) {
        Set<CompetenceCourse> bolonhaCompetenceCourses = (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses();
        for (final CompetenceCourse competenceCourse : bolonhaCompetenceCourses) {
            if (!competenceCourse.equals(this) && competenceCourse.getAcronym().equalsIgnoreCase(acronym.trim())) {
                throw new DomainException("competenceCourse.existing.acronym", competenceCourse.getName(),
                        competenceCourse.getDepartmentUnit().getDepartment().getRealName());
            }
        }

        findInformationMostRecentUntil(null).setAcronym(acronym);
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

    public void edit(String objectives, String program, String evaluationMethod, String objectivesEn, String programEn,
            String evaluationMethodEn) {
        findInformationMostRecentUntil(null).edit(objectives, program, evaluationMethod, objectivesEn, programEn,
                evaluationMethodEn);
    }

    public void delete() {
        if (!getAssociatedCurricularCoursesSet().isEmpty()) {
            throw new DomainException("mustDeleteCurricularCoursesFirst");
        } else if (this.getCurricularStage().equals(CurricularStage.APPROVED)) {
            throw new DomainException("competenceCourse.approved");
        }
        getDepartmentsSet().clear();
        for (; !getCompetenceCourseInformationsSet().isEmpty(); getCompetenceCourseInformationsSet().iterator().next().delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private CompetenceCourseInformation getOldestCompetenceCourseInformation() {
        return getCompetenceCourseInformationsSet().stream().min(CompetenceCourseInformation.COMPARATORY_BY_EXECUTION_INTERVAL)
                .orElse(null);
    }

    public boolean isLoggedPersonAllowedToView() {
        return CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToViewCompetenceCourse(this);
    }

    public boolean isLoggedPersonAllowedToViewChangeRequests() {
        return CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToViewChangeRequests(this, null);
    }

    public boolean isLoggedPersonAllowedToCreateChangeRequests(ExecutionInterval interval) {
        return CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToManageChangeRequests(this, interval);
    }

    public boolean isRequestDraftAvailable(ExecutionInterval interval) {
        return getChangeRequestDraft(interval) != null;
    }

    /**
     * Find for <b>exactly</b> the given {@link ExecutionInterval}
     */
    public CompetenceCourseInformation findInformation(final ExecutionInterval input) {
        return getCompetenceCourseInformationsSet().stream().filter(cci -> cci.getExecutionInterval() == input).findAny()
                .orElse(null);
    }

    public CompetenceCourseInformation findInformationMostRecentUntil(final ExecutionInterval interval) {

        // special case: if interval is year, normally is intended to check the info of its last period 
        final ExecutionInterval intervalNormalized =
                interval instanceof ExecutionYear ? ((ExecutionYear) interval).getLastExecutionPeriod() : interval;

        final ExecutionInterval intervalNullSafe =
                Optional.ofNullable(intervalNormalized).orElseGet(() -> ExecutionSemester.findCurrent(null));

        CompetenceCourseInformation result = null;

        final List<CompetenceCourseInformation> orderedInformations = getCompetenceCourseInformationsSet().stream()
                .sorted(CompetenceCourseInformation.COMPARATORY_BY_EXECUTION_INTERVAL).collect(Collectors.toList());

        for (CompetenceCourseInformation information : orderedInformations) {
            if (information.getExecutionInterval().isAfter(intervalNullSafe)) {
                if (result != null) { // only return if there is an previous information already found
                    return result;
                }
            } else {
                result = information;
            }
        }

        // if no result found and no explicit interval specified, return first information to attempt more null safety
        if (result == null && interval == null && !orderedInformations.isEmpty()) {
            return orderedInformations.get(0);
        }

        return result;
    }

    /**
     * @deprecated use {@link #findInformationMostRecentUntil(ExecutionInterval)}
     */
    @Deprecated
    public CompetenceCourseInformation findCompetenceCourseInformationForExecutionPeriod(
            final ExecutionSemester executionSemester) {
        return findInformationMostRecentUntil(executionSemester);
    }

    /**
     * @deprecated use {@link #findInformationMostRecentUntil(ExecutionInterval)}
     */
    @Deprecated
    public CompetenceCourseInformation findCompetenceCourseInformationForExecutionYear(final ExecutionYear executionYear) {
        return findInformationMostRecentUntil(executionYear);
    }

    public String getName(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getName() : null;
    }

    @Override
    public String getName() {
        return getName(null);
    }

    public String getNameEn(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getNameEn() : null;
    }

    public String getNameEn() {
        return getNameEn(null);
    }

    public String getAcronym(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getAcronym() : null;
    }

    public String getAcronym() {
        return getAcronym(null);
    }

    public void setAcronym(String acronym) {
        findInformationMostRecentUntil(null).setAcronym(acronym);
    }

    public boolean isBasic(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getBasic() : false;
    }

    public boolean isBasic() {
        return isBasic(null);
    }

    public RegimeType getRegime(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getRegime() : null;
    }

    public RegimeType getRegime() {
        return getRegime(null);
    }

    @Deprecated
    public void setRegime(RegimeType regimeType) {
        findInformationMostRecentUntil(null).setAcademicPeriod(regimeType.convertToAcademicPeriod());
    }

    public CompetenceCourseLevel getCompetenceCourseLevel(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getCompetenceCourseLevel() : null;
    }

    public CompetenceCourseLevel getCompetenceCourseLevel() {
        return getCompetenceCourseLevel(null);
    }

    public Collection<CompetenceCourseLoad> getCompetenceCourseLoads(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getCompetenceCourseLoadsSet() : Collections.emptyList();
    }

    public Collection<CompetenceCourseLoad> getCompetenceCourseLoads() {
        return getCompetenceCourseLoads(null);
    }

    public int getCompetenceCourseLoadsCount(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getCompetenceCourseLoadsSet().size() : 0;
    }

    public int getCompetenceCourseLoadsCount() {
        return getCompetenceCourseLoadsCount(null);
    }

    public String getObjectives(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getObjectives() : null;
    }

    public String getObjectives() {
        return getObjectives(null);
    }

    public String getProgram(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getProgram() : null;
    }

    public String getProgram() {
        return getProgram(null);
    }

    public String getEvaluationMethod(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getEvaluationMethod() : null;
    }

    public String getEvaluationMethod() {
        return getEvaluationMethod(null);
    }

    public String getObjectivesEn(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getObjectivesEn() : null;
    }

    public String getObjectivesEn() {
        return getObjectivesEn(null);
    }

    public String getProgramEn(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getProgramEn() : null;
    }

    public String getProgramEn() {
        return getProgramEn(null);
    }

    public String getEvaluationMethodEn(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getEvaluationMethodEn() : null;
    }

    public String getEvaluationMethodEn() {
        return getEvaluationMethodEn(null);
    }

    public double getTheoreticalHours() {
        return getTheoreticalHours(null);
    }

    public double getTheoreticalHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getTheoreticalHours(null) : 0.0;
    }

    public double getProblemsHours() {
        return getProblemsHours(null);
    }

    public double getProblemsHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getProblemsHours(null) : 0.0;
    }

    public double getLaboratorialHours() {
        return getLaboratorialHours(null);
    }

    public double getLaboratorialHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getLaboratorialHours(null) : 0.0;
    }

    public double getSeminaryHours() {
        return getSeminaryHours(null);
    }

    public double getSeminaryHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getSeminaryHours(null) : 0.0;
    }

    public double getFieldWorkHours() {
        return getFieldWorkHours(null);
    }

    public double getFieldWorkHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getFieldWorkHours(null) : 0.0;
    }

    public double getTrainingPeriodHours() {
        return getTrainingPeriodHours(null);
    }

    public double getTrainingPeriodHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getTrainingPeriodHours(null) : 0.0;
    }

    public double getTutorialOrientationHours() {
        return getTutorialOrientationHours(null);
    }

    public double getTutorialOrientationHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getTutorialOrientationHours(null) : 0.0;
    }

    public double getOtherHours() {
        return getOtherHours(null);
    }

    public double getOtherHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getOtherHours(null) : 0.0;
    }

    public double getAutonomousWorkHours() {
        return getAutonomousWorkHours(null);
    }

    public double getAutonomousWorkHours(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getAutonomousWorkHours(null) : 0.0;
    }

    public Double getAutonomousWorkHours(final Integer order, final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getAutonomousWorkHours(order) : 0.0;
    }

    public double getContactLoad() {
        return getContactLoad(null);
    }

    public Double getContactLoad(final ExecutionInterval interval) {
        return getContactLoad(null, interval);
    }

    public Double getContactLoad(final Integer order, final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getContactLoad(order) : 0.0;
    }

    public double getTotalLoad() {
        return getTotalLoad(null);
    }

    public double getTotalLoad(final ExecutionInterval interval) {
        return getTotalLoad((Integer) null, interval);
    }

    public Double getTotalLoad(final Integer order, final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getTotalLoad(order) : 0.0;
    }

    public double getEctsCredits() {
        return getEctsCredits(null);
    }

    public double getEctsCredits(final ExecutionInterval interval) {
        return getEctsCredits((Integer) null, interval);
    }

    public Double getEctsCredits(final Integer order, final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return (information != null) ? information.getEctsCredits(order) : 0.0;
    }

    @SuppressWarnings("unchecked")
    public List<CurricularCourse> getCurricularCoursesWithActiveScopesInExecutionPeriod(final ExecutionInterval interval) {
        return getAssociatedCurricularCoursesSet().stream().filter(cc -> cc.isActive(interval)).collect(Collectors.toList());
    }

    public Collection<Context> getCurricularCourseContexts() {
        final Set<Context> result = new HashSet<Context>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            for (Context context : curricularCourse.getParentContextsSet()) {
                result.add(context);
            }
        }
        return result;
    }

    public CurricularCourse getCurricularCourse(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
                return curricularCourse;
            }
        }

        return null;
    }

    public List<Enrolment> getActiveEnrollments(ExecutionInterval interval) {
        final AcademicInterval academicInterval = interval.getAcademicInterval();
        return getAssociatedCurricularCoursesSet().stream()
                .flatMap(cc -> cc.getEnrolmentsByAcademicInterval(academicInterval).stream()).collect(Collectors.toList());
    }

    public ExecutionInterval getBeginExecutionInterval() {
        final CompetenceCourseInformation firstInformation = getOldestCompetenceCourseInformation();
        return firstInformation != null ? firstInformation.getExecutionInterval() : null;
    }

    public Boolean hasActiveScopesInExecutionYear(ExecutionYear executionYear) {
        Collection<ExecutionInterval> executionIntervals = executionYear.getChildIntervals();
        Collection<CurricularCourse> curricularCourses = this.getAssociatedCurricularCoursesSet();
        for (ExecutionInterval executionInterval : executionIntervals) {
            for (CurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.hasAnyActiveContext(executionInterval)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * @see #getDepartmentUnit(ExecutionYear)
     */
    public DepartmentUnit getDepartmentUnit() {
        return getDepartmentUnit(null);
    }

    /**
     * In an ExecutionInterval the CompetenceCourse belongs to a Department.
     * This association is built by CompetenceCourseGroupUnit which aggregates
     * versions of CompetenceCourses (CompetenceCourseInformation). We can see
     * CompetenceCourseGroupUnit like a bag of CompetenceCourses beloging to a
     * Department.
     * 
     * The association between a CompetenceCourse and the ExecutionInterval is
     * represented by CompetenceCourseInformation. We can see
     * CompetenceCourseInformation as a version of CompetenceCourse's
     * attributes.
     * 
     * ExecutionInterval assumes the role of start period of this version
     * 
     * @see CompetenceCourseInformation
     * @see CompetenceCourseGroupUnit
     * @param semester semester of the competence course to be searched for
     * @return Department unit for the given semester
     */
    public DepartmentUnit getDepartmentUnit(ExecutionInterval interval) {
        return findInformationMostRecentUntil(interval).getDepartmentUnit();
    }

    /**
     * @see #getDepartmentUnit(ExecutionInterval)
     */
    public CompetenceCourseGroupUnit getCompetenceCourseGroupUnit() {
        return getCompetenceCourseGroupUnit(null);
    }

    public CompetenceCourseGroupUnit getCompetenceCourseGroupUnit(ExecutionInterval interval) {
        return findInformationMostRecentUntil(interval).getCompetenceCourseGroupUnit();
    }

    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads() {
        final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>(getCompetenceCourseLoadsCount());
        result.addAll(getCompetenceCourseLoads());
        Collections.sort(result);
        return result;
    }

    @Override
    public void setCode(String code) {
        final CompetenceCourse existing = CompetenceCourse.find(code);

        if (existing != null && existing != this) {
            throw new DomainException("error.CompetenceCourse.found.duplicate");
        }

        super.setCode(code);
    }

    @Override
    public void setCurricularStage(CurricularStage curricularStage) {
        if (!this.getAssociatedCurricularCoursesSet().isEmpty() && curricularStage.equals(CurricularStage.DRAFT)) {
            throw new DomainException("competenceCourse.has.already.associated.curricular.courses");
        }
        super.setCurricularStage(curricularStage);
    }

    public ScientificAreaUnit getScientificAreaUnit() {
        return getScientificAreaUnit(null);
    }

    public ScientificAreaUnit getScientificAreaUnit(ExecutionInterval interval) {
        CompetenceCourseInformation mostRecentCompetenceCourseInformationUntil = findInformationMostRecentUntil(interval);
        return mostRecentCompetenceCourseInformationUntil != null ? mostRecentCompetenceCourseInformationUntil
                .getScientificAreaUnit() : null;
    }

    public boolean isAnual() {
        return isAnual(null);
    }

//    public boolean isAnual(final ExecutionYear executionYear) {
//        final CompetenceCourseInformation information = findInformationMostRecentUntil(executionYear);
//        return information != null ? information.isAnual() : null;
//    }

//    public boolean isSemestrial(final ExecutionYear executionYear) {
//        return getRegime(executionYear) == RegimeType.SEMESTRIAL;
//    }

    public boolean isApproved() {
        return getCurricularStage() == CurricularStage.APPROVED;
    }

    public void transfer(CompetenceCourseGroupUnit competenceCourseGroupUnit, ExecutionInterval interval, String justification,
            Person requester) {

        CompetenceCourseInformation information = null;
        for (CompetenceCourseInformation existingInformation : getCompetenceCourseInformationsSet()) {
            if (existingInformation.getExecutionInterval() == interval) {
                information = existingInformation;
            }
        }
        if (information == null) {
            CompetenceCourseInformation latestInformation = findInformationMostRecentUntil(interval);
            information = new CompetenceCourseInformation(latestInformation);
            information.setExecutionInterval(interval);
        }

        CompetenceCourseInformationChangeRequest changeRequest =
                new CompetenceCourseInformationChangeRequest(information, justification, requester);
        changeRequest.setCompetenceCourseGroupUnit(competenceCourseGroupUnit);
        changeRequest.approve(requester);
    }

    public LocalizedString getNameI18N() {
        return getNameI18N(null);
    }

    public LocalizedString getNameI18N(ExecutionInterval interval) {
        LocalizedString LocalizedString = new LocalizedString();
        String name = getName(interval);
        if (name != null && name.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, name);
        }
        String nameEn = getNameEn(interval);
        if (nameEn != null && nameEn.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, nameEn);
        }
        return LocalizedString;
    }

    public LocalizedString getObjectivesI18N() {
        return getObjectivesI18N(null);
    }

    public LocalizedString getObjectivesI18N(ExecutionInterval interval) {
        LocalizedString LocalizedString = new LocalizedString();
        String objectives = getObjectives(interval);
        if (objectives != null && objectives.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, objectives);
        }
        String objectivesEn = getObjectivesEn(interval);
        if (objectivesEn != null && objectivesEn.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, objectivesEn);
        }
        return LocalizedString;
    }

    public LocalizedString getProgramI18N() {
        return getProgramI18N(null);
    }

    public LocalizedString getProgramI18N(ExecutionInterval interval) {
        LocalizedString LocalizedString = new LocalizedString();
        String program = getProgram(interval);
        if (program != null && program.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, program);
        }
        String programEn = getProgramEn(interval);
        if (programEn != null && programEn.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, programEn);
        }
        return LocalizedString;
    }

    public LocalizedString getEvaluationMethodI18N() {
        return getEvaluationMethodI18N(null);
    }

    public LocalizedString getEvaluationMethodI18N(ExecutionInterval interval) {
        LocalizedString LocalizedString = new LocalizedString();
        String evaluationMethod = getEvaluationMethod(interval);
        if (evaluationMethod != null && evaluationMethod.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, evaluationMethod);
        }
        String evaluationMethodEn = getEvaluationMethodEn(interval);
        if (evaluationMethodEn != null && evaluationMethodEn.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, evaluationMethodEn);
        }
        return LocalizedString;
    }

    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionInterval executionInterval) {
        return getCurricularCoursesWithActiveScopesInExecutionPeriod(executionInterval).stream()
                .flatMap(cc -> cc.getExecutionCoursesByExecutionPeriod(executionInterval).stream()).distinct()
                .collect(Collectors.toList());
    }

    public boolean isDissertation() {
        return getType() == CompetenceCourseType.DISSERTATION;
    }

    public Integer getDraftCompetenceCourseInformationChangeRequestsCount() {
        int count = 0;
        for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequestsSet()) {
            if (request.getApproved() == null) {
                count++;
            }
        }
        return count;
    }

    public Set<CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequests(
            final ExecutionInterval interval) {
        Set<CompetenceCourseInformationChangeRequest> changeRequests = new HashSet<CompetenceCourseInformationChangeRequest>();
        for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequestsSet()) {
            if (request.getExecutionPeriod() == interval) {
                changeRequests.add(request);
            }
        }
        return changeRequests;
    }

    public CompetenceCourseInformationChangeRequest getChangeRequestDraft(final ExecutionInterval interval) {
        for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequests(interval)) {
            if (request.getApproved() == null) {
                return request;
            }
        }
        return null;
    }

    @Override
    public void addCompetenceCourseInformationChangeRequests(CompetenceCourseInformationChangeRequest request) {
        if (isRequestDraftAvailable(request.getExecutionPeriod())) {
            throw new DomainException("error.can.only.exist.one.request.draft.per.execution.period");
        }
        super.addCompetenceCourseInformationChangeRequests(request);
    }

    public boolean hasOneCourseLoad(final ExecutionYear executionYear) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(executionYear);
        return information != null && information.getCompetenceCourseLoadsSet().size() == 1;
    }

    public boolean matchesName(String name) {
        name = StringNormalizer.normalize(name).replaceAll("[^0-9a-zA-Z]", " ").trim();
        for (final CompetenceCourseInformation information : getCompetenceCourseInformationsSet()) {
            if (StringNormalizer.normalize(information.getName()).matches(".*" + name.replaceAll(" ", ".*") + ".*")) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesCode(String code) {
        if (getCode() == null) {
            return false;
        }
        code = StringNormalizer.normalize(code).replaceAll("[^0-9a-zA-Z]", " ").trim();
        return (StringNormalizer.normalize(getCode()).matches(".*" + code.replaceAll(" ", ".*") + ".*"));
    }

    public ExecutionInterval getStartExecutionInterval() {
        return getOldestCompetenceCourseInformation().getExecutionInterval();
    }

    @Deprecated
    static public Collection<CompetenceCourse> readBolonhaCompetenceCourses() {
        final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (final CompetenceCourse competenceCourse : Bennu.getInstance().getCompetenceCoursesSet()) {
            result.add(competenceCourse);
        }
        return result;
    }

    static public Collection<CompetenceCourse> findAll() {
        return Bennu.getInstance().getCompetenceCoursesSet().stream().collect(Collectors.toSet());
    }

    static public Collection<CompetenceCourse> searchBolonhaCompetenceCourses(String searchName, String searchCode) {
        final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (final CompetenceCourse competenceCourse : Bennu.getInstance().getCompetenceCoursesSet()) {
            if ((!searchName.isEmpty()) && (!competenceCourse.matchesName(searchName))) {
                continue;
            }
            if ((!searchCode.isEmpty()) && (!competenceCourse.matchesCode(searchCode))) {
                continue;
            }
            result.add(competenceCourse);
        }
        return result;
    }

    static public Collection<CompetenceCourse> readApprovedBolonhaCompetenceCourses() {
        final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (final CompetenceCourse competenceCourse : Bennu.getInstance().getCompetenceCoursesSet()) {
            if (competenceCourse.isApproved()) {
                result.add(competenceCourse);
            }
        }
        return result;
    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.YearMonthDay ymd = getCreationDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setCreationDate(java.util.Date date) {
        if (date == null) {
            setCreationDateYearMonthDay(null);
        } else {
            setCreationDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    public static CompetenceCourse find(final String code) {
        if (StringUtils.isNotBlank(code)) {
            for (final CompetenceCourse iter : Bennu.getInstance().getCompetenceCoursesSet()) {
                if (StringUtils.equals(code, iter.getCode())) {
                    return iter;
                }
            }
        }
        return null;
    }

    public boolean isAnual(final ExecutionInterval input) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(input);
        return information != null ? information.isAnual() : null;
    }

    public AcademicPeriod getAcademicPeriod(final ExecutionInterval input) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(input);
        return information != null ? information.getAcademicPeriod() : null;
    }

    public AcademicPeriod getAcademicPeriod() {
        return getAcademicPeriod(null);
    }

}

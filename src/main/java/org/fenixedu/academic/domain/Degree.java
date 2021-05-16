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

import com.google.common.base.Strings;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.accessControl.CoordinatorGroup;
import org.fenixedu.academic.domain.accessControl.StudentGroup;
import org.fenixedu.academic.domain.accessControl.TeacherGroup;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisState;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Sender;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Degree extends Degree_Base implements Comparable<Degree> {

    public static Function<Degree, Stream<Degree>> CONNECTED_DEGREE_STREAM = degree -> Stream.of(degree);

    public static final String CREATED_SIGNAL = "academic.degree.create";

    public static final String DEFAULT_MINISTRY_CODE = "9999";

    private static final Collator collator = Collator.getInstance();

    static final public Comparator<Degree> COMPARATOR_BY_NAME = (o1, o2) -> {
        String name1;
        String name2;
        name1 = o1.getNameFor((AcademicInterval) null).getContent(I18N.getLocale());
        name2 = o2.getNameFor((AcademicInterval) null).getContent(I18N.getLocale());

        if (Strings.isNullOrEmpty(name1) || Strings.isNullOrEmpty(name2)) {
            name1 = o1.getNameFor((AcademicInterval) null).getContent();
            name2 = o2.getNameFor((AcademicInterval) null).getContent();
        }

        return collator.compare(name1, name2);
    };

    static final public Comparator<Degree> COMPARATOR_BY_NAME_AND_ID = (o1, o2) -> {
        final int nameResult = COMPARATOR_BY_NAME.compare(o1, o2);
        return nameResult == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : nameResult;
    };

    static final private Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE_NAME =
            (o1, o2) -> collator.compare(o1.getDegreeType().getName().getContent(), o2.getDegreeType().getName().getContent());

    static final private Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE = Comparator.comparing(Degree_Base::getDegreeType);

    private static class ComparatorByDegreeTypeAndNameAndId implements Serializable, Comparator<Degree> {
        @Override
        public int compare(final Degree o1, final Degree o2) {
            final int typeResult = COMPARATOR_BY_DEGREE_TYPE_NAME.compare(o1, o2);
            return typeResult == 0 ? COMPARATOR_BY_NAME_AND_ID.compare(o1, o2) : typeResult;
        }
    }

    static final public Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE_DEGREE_NAME_AND_ID = (o1, o2) -> {
        final int typeResult = COMPARATOR_BY_DEGREE_TYPE.compare(o1, o2);
        return typeResult == 0 ? COMPARATOR_BY_NAME_AND_ID.compare(o1, o2) : typeResult;
    };

    static final public Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID = new ComparatorByDegreeTypeAndNameAndId();

    static final public Comparator<Degree> COMPARATOR_BY_FIRST_ENROLMENTS_PERIOD_AND_ID = (degree1, degree2) -> {
        ExecutionSemester semester1 = degree1.getFirstDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
        ExecutionSemester semester2 = degree2.getFirstDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
        final int result = semester1.compareTo(semester2);
        return result == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(degree1, degree2) : result;
    };

    @Override
    public int compareTo(final Degree o) {
        return Degree.COMPARATOR_BY_NAME_AND_ID.compare(this, o);
    }

    protected Degree() {
        super();

        setRootDomainObject(Bennu.getInstance());
    }

    public Degree(LocalizedString name, String code, DegreeType degreeType, GradeScale gradeScale) {
        this(name, code, new LocalizedString(), degreeType, gradeScale, ExecutionYear.readCurrentExecutionYear());
    }

    public Degree(LocalizedString name, String code, LocalizedString associatedInstitutions, DegreeType degreeType, GradeScale gradeScale, ExecutionYear executionYear) {
        this();
        commonFieldsChange(name, code, associatedInstitutions, gradeScale, executionYear);

        if (degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        }
        this.setDegreeType(degreeType);
    }

    public Degree(LocalizedString name, String acronym, LocalizedString associatedInstitutions, DegreeType degreeType, Double ectsCredits, GradeScale gradeScale,
            String prevailingScientificArea, AdministrativeOffice administrativeOffice) {
        this();
        commonFieldsChange(name, acronym, associatedInstitutions, gradeScale, ExecutionYear.readCurrentExecutionYear());
        newStructureFieldsChange(degreeType, ectsCredits, prevailingScientificArea);
        setAdministrativeOffice(administrativeOffice);
    }

    private void commonFieldsChange(LocalizedString name, String code, LocalizedString associatedInstitutions, GradeScale gradeScale, ExecutionYear executionYear) {
        if (name == null || name.isEmpty()) {
            throw new DomainException("degree.name.not.null");
        } else if (code == null) {
            throw new DomainException("degree.code.not.null");
        }

        DegreeInfo degreeInfo = getDegreeInfoFor(executionYear);
        if (degreeInfo == null) {
            degreeInfo = tryCreateUsingMostRecentInfo(executionYear);
        }

        degreeInfo.setName(name);
        degreeInfo.setAssociatedInstitutions(associatedInstitutions);

        this.setNome(name.getContent(LocaleUtils.PT));
        this.setNameEn(name.getContent(LocaleUtils.EN));
        this.setSigla(code.trim());
        this.setGradeScale(gradeScale);
    }

    private void newStructureFieldsChange(DegreeType degreeType, Double ectsCredits, String prevailingScientificArea) {
        if (degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        } else if (ectsCredits == null) {
            throw new DomainException("degree.ectsCredits.not.null");
        }

        this.setDegreeType(degreeType);
        this.setEctsCredits(ectsCredits);
        this.setPrevailingScientificArea(prevailingScientificArea == null ? null : prevailingScientificArea.trim());
    }

    public void edit(LocalizedString name, String code, LocalizedString associatedInstitutions, DegreeType degreeType,
                        GradeScale gradeScale,
            ExecutionYear executionYear) {
        commonFieldsChange(name, code, associatedInstitutions, gradeScale, executionYear);

        if (degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        }
        this.setDegreeType(degreeType);
    }

    public void edit(LocalizedString name, String acronym, LocalizedString associatedInstitutions, DegreeType degreeType, Double ectsCredits,
            GradeScale gradeScale, String prevailingScientificArea, ExecutionYear executionYear) {
        checkIfCanEdit(degreeType);
        commonFieldsChange(name, acronym, associatedInstitutions, gradeScale, executionYear);
        newStructureFieldsChange(degreeType, ectsCredits, prevailingScientificArea);
    }

    private void checkIfCanEdit(final DegreeType degreeType) {
        if (!this.getDegreeType().equals(degreeType) && !getDegreeCurricularPlansSet().isEmpty()) {
            throw new DomainException("degree.cant.edit.bolonhaDegreeType");
        }
    }

    public Boolean getCanBeDeleted() {
        return getDeletionBlockers().isEmpty();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getDegreeCurricularPlansSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, "error.degree.has.degree.curricular.plans"));
        }

        if (!getStudentGroupSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION,
                    "error.academicProgram.cannotDeleteBacauseUsedInAccessControl"));
        }

        if (!getTeacherGroupSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION,
                    "error.academicProgram.cannotDeleteBacauseUsedInAccessControl"));
        }

        if (getScientificCommissionGroup() != null) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION,
                    "error.academicProgram.cannotDeleteBacauseUsedInAccessControl"));
        }

        if (!getCoordinatorGroupSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION,
                    "error.academicProgram.cannotDeleteBacauseUsedInAccessControl"));
        }

        if (!getStudentsConcludedInExecutionYearGroupSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION,
                    "error.academicProgram.cannotDeleteBacauseUsedInAccessControl"));
        }

        if (getAlumniGroup() != null) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION,
                    "error.academicProgram.cannotDeleteBacauseUsedInAccessControl"));
        }
    }

    @Override
    protected void disconnect() {
        Iterator<DegreeInfo> degreeInfosIterator = getDegreeInfosSet().iterator();
        while (degreeInfosIterator.hasNext()) {
            DegreeInfo degreeInfo = degreeInfosIterator.next();
            degreeInfosIterator.remove();
            degreeInfo.delete();
        }

        for (; !getParticipatingAnyCurricularCourseCurricularRulesSet().isEmpty(); getParticipatingAnyCurricularCourseCurricularRulesSet()
                .iterator().next().delete()) {
            ;
        }

        // checkDeletion assures that site is deletable
        if (super.getSender() != null) {
            final Sender sender = super.getSender();
            setSender(null);
            sender.delete();
        }

        setUnit(null);
        setDegreeType(null);
        setPhdProgram(null);
        setRootDomainObject(null);
        super.disconnect();
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale();
    }

    public DegreeCurricularPlan createDegreeCurricularPlan(String name, GradeScale gradeScale, Person creator,
            AcademicPeriod duration) {
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
        if (!RoleType.BOLONHA_MANAGER.isMember(creator.getUser())) {
            RoleType.grant(RoleType.BOLONHA_MANAGER, creator.getUser());
        }

        CurricularPeriod curricularPeriod = new CurricularPeriod(duration);

        return new DegreeCurricularPlan(this, name, gradeScale, creator, curricularPeriod);
    }

    @Override
    public Collection<CycleType> getCycleTypes() {
        return getDegreeType().getCycleTypes();
    }

    @Deprecated
    public DegreeType getBolonhaDegreeType() {
        return getDegreeType();
    }

    public boolean isBolonhaDegree() {
        return getDegreeType().isBolonhaType();
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return getDegreeType().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
    }

    public static Degree find(final String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (Degree degree : Degree.readNotEmptyDegrees()) {
            if (StringUtils.equalsIgnoreCase(degree.getCode(), code)) {
                return degree;
            }
        }

        return null;
    }

    public List<DegreeCurricularPlan> findDegreeCurricularPlansByState(DegreeCurricularPlanState state) {
        List<DegreeCurricularPlan> result = new ArrayList<>();
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
        List<DegreeCurricularPlan> result = new ArrayList<>();

        for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
                result.add(degreeCurricularPlan);
            }
        }

        return result;
    }

    public List<DegreeCurricularPlan> getPastDegreeCurricularPlans() {
        List<DegreeCurricularPlan> result = new ArrayList<>();
        for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.PAST) {
                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlansForYear(ExecutionYear year) {
        List<DegreeCurricularPlan> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.hasExecutionDegreeFor(year)) {
                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }

    public boolean getCanBeAccessedByUser() {
        return AcademicPredicates.MANAGE_DEGREE_CURRICULAR_PLANS.evaluate(this);
    }

    private List<ExecutionDegree> getInternalExecutionDegrees() {
        List<ExecutionDegree> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            result.addAll(degreeCurricularPlan.getExecutionDegreesSet());
        }
        return result;
    }

    public List<ExecutionDegree> getExecutionDegrees() {
        return getExecutionDegrees(null);
    }

    public List<ExecutionDegree> getExecutionDegrees(final AcademicInterval academicInterval) {
        if (academicInterval == null) {
            return getInternalExecutionDegrees();
        }
        return getInternalExecutionDegrees().stream().filter(input -> academicInterval.equals(input.getAcademicInterval()))
                .collect(Collectors.toList());
    }

    public List<ExecutionDegree> getExecutionDegreesForExecutionYear(final ExecutionYear executionYear) {
        final List<ExecutionDegree> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            if (executionDegree != null) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    public List<ExecutionYear> getDegreeCurricularPlansExecutionYears() {
        Set<ExecutionYear> result = new TreeSet<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                result.add(executionDegree.getExecutionYear());
            }
        }
        return new ArrayList<>(result);
    }

    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYear(final ExecutionYear executionYear) {

        if (isBolonhaDegree()) {
            return Collections.emptyList();
        }

        final List<CurricularCourse> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
                for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                    for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                        if (executionCourse.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                            result.add(course);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYearAndYear(final ExecutionYear executionYear,
            final Integer curricularYear) {

        if (isBolonhaDegree()) {
            return Collections.emptyList();
        }

        final List<CurricularCourse> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
                for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                    xpto: for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                        if (executionCourse.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                            for (final CurricularCourseScope curricularCourseScope : course.getScopesSet()) {
                                if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear()
                                        .equals(curricularYear)) {
                                    result.add(course);
                                    break xpto;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<ExecutionCourse> getExecutionCourses(String curricularCourseAcronym, ExecutionSemester executionSemester) {
        final List<ExecutionCourse> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                if (course.getAcronym() != null && course.getAcronym().equalsIgnoreCase(curricularCourseAcronym)) {
                    for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                        if (executionSemester == executionCourse.getExecutionPeriod()) {
                            result.add(executionCourse);
                        }
                    }
                }
            }
        }
        return result;
    }

    // início da nova operação -- Ricardo Marcão
    public List<ExecutionCourse> getExecutionCourses(final AcademicInterval academicInterval) {
        final List<ExecutionCourse> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                    if (academicInterval.isEqualOrEquivalent(executionCourse.getAcademicInterval())) {
                        for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
                            if (scope.isActiveForAcademicInterval(academicInterval)
                                    && scope.getCurricularSemester() == academicInterval.getAcademicSemesterOfAcademicYear()) {
                                result.add(executionCourse);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    // -- fim da nova operação -- Ricardo Marcão

    public List<ExecutionCourse> getExecutionCourses(final Integer curricularYear, final ExecutionSemester executionSemester) {
        final List<ExecutionCourse> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                    if (executionSemester == executionCourse.getExecutionPeriod()) {
                        for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
                            if (scope.isActiveForExecutionPeriod(executionSemester)
                                    && scope.getCurricularYear() == curricularYear
                                    && scope.getCurricularSemester() == executionSemester.getSemester()) {
                                result.add(executionCourse);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Deprecated
    public LocalizedString getNameFor(final ExecutionYear executionYear) {
        DegreeInfo degreeInfo = executionYear == null ? getMostRecentDegreeInfo() : getMostRecentDegreeInfo(executionYear);
        return degreeInfo == null ? new LocalizedString().with(LocaleUtils.PT, super.getNome()).with(
                LocaleUtils.EN, super.getNameEn()) : degreeInfo.getName();
    }

    @Deprecated
    public LocalizedString getNameFor(final ExecutionSemester executionSemester) {
        return getNameFor(executionSemester != null ? executionSemester.getExecutionYear() : null);
    }

    public LocalizedString getNameFor(final AcademicInterval academicInterval) {
        DegreeInfo degreeInfo = academicInterval == null ? getMostRecentDegreeInfo() : getMostRecentDegreeInfo(academicInterval);
        return degreeInfo == null ? new LocalizedString().with(LocaleUtils.PT, super.getNome()).with(
                LocaleUtils.EN, super.getNameEn()) : degreeInfo.getName();
    }

    @Override
    @Deprecated
    public String getNome() {
        return getName();
    }

    /**
     * @deprecated Use {@link #getNameFor(ExecutionYear)}
     */
    @Deprecated
    public String getName() {
        DegreeInfo degreeInfo = getMostRecentDegreeInfo();
        return degreeInfo == null ? StringUtils.EMPTY : degreeInfo.getName().getContent(LocaleUtils.PT);
    }

    /**
     * @deprecated Use {@link #getNameFor(ExecutionYear)}
     */
    @Override
    @Deprecated
    public String getNameEn() {
        DegreeInfo degreeInfo = getMostRecentDegreeInfo();
        return degreeInfo == null ? StringUtils.EMPTY : degreeInfo.getName().getContent(LocaleUtils.EN);
    }

    final public LocalizedString getNameI18N() {
        return getNameFor(ExecutionYear.readCurrentExecutionYear());
    }

    final public LocalizedString getNameI18N(ExecutionYear executionYear) {
        return getNameFor(executionYear);
    }

    public LocalizedString getPresentationNameI18N() {
        return getPresentationNameI18N(ExecutionYear.readCurrentExecutionYear());
    }

    public LocalizedString getPresentationNameI18N(final ExecutionYear executionYear) {
        LocalizedString degreeType = getDegreeType().getName();

        LocalizedString.Builder builder = new LocalizedString.Builder();
        CoreConfiguration.supportedLocales().forEach(locale -> builder.with(locale, degreeType.getContent(locale)
                        + " " + BundleUtil.getString(Bundle.APPLICATION, locale, "label.in")
                        + " " + getNameI18N(executionYear).getContent(locale)));
        return builder.build();
    }

    final public String getPresentationName() {
        return getPresentationNameI18N().getContent();
    }

    public String getPresentationName(final ExecutionYear executionYear) {
        return getPresentationNameI18N(executionYear).getContent();
    }

    protected String getPresentationName(final ExecutionYear executionYear, final Locale locale) {
        return getPresentationNameI18N(executionYear).getContent(locale);
    }

    final public String getFilteredName() {
        return getFilteredName(ExecutionYear.readCurrentExecutionYear(), I18N.getLocale());
    }

    final public String getFilteredName(final ExecutionYear executionYear) {
        return getFilteredName(executionYear, I18N.getLocale());
    }

    public String getFilteredName(final ExecutionYear executionYear, final Locale locale) {
        final StringBuilder res = new StringBuilder(getNameFor(executionYear).getContent(locale));

        for (final Space campus : Space.getAllCampus()) {
            String toRemove = " - " + campus.getName();
            if (res.toString().contains(toRemove)) {
                res.replace(res.indexOf(toRemove), res.indexOf(toRemove) + toRemove.length(), StringUtils.EMPTY);
            }
        }

        return res.toString();
    }

    public DegreeCurricularPlan getMostRecentDegreeCurricularPlan() {
        ExecutionDegree mostRecentExecutionDegree = null;
        boolean mustGetByInitialDate = false;

        for (final DegreeCurricularPlan degreeCurricularPlan : this.getActiveDegreeCurricularPlans()) {
            ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
            if (executionDegree == null) {
                continue;
            }

            if (mostRecentExecutionDegree == null) {
                mostRecentExecutionDegree = executionDegree;
            } else {
                if (mostRecentExecutionDegree.getExecutionYear().equals(executionDegree.getExecutionYear())) {
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
            return mostRecentExecutionDegree != null ? mostRecentExecutionDegree.getDegreeCurricularPlan() : null;
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
        final Collection<Registration> result = new HashSet<>();

        for (final DegreeCurricularPlan degreeCurricularPlan : getActiveDegreeCurricularPlans()) {
            result.addAll(degreeCurricularPlan.getActiveRegistrations());
        }

        return result;
    }

    public DegreeCurricularPlan getFirstDegreeCurricularPlan() {
        if (getDegreeCurricularPlansSet().isEmpty()) {
            return null;
        }

        DegreeCurricularPlan firstDCP = getDegreeCurricularPlansSet().iterator().next();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getInitialDateYearMonthDay() == null) {
                continue;
            }
            if (firstDCP.getInitialDateYearMonthDay() == null
                    || degreeCurricularPlan.getInitialDateYearMonthDay().isBefore(firstDCP.getInitialDateYearMonthDay())) {
                firstDCP = degreeCurricularPlan;
            }
        }
        return firstDCP.getInitialDateYearMonthDay() == null ? null : firstDCP;
    }

    public DegreeCurricularPlan getLastActiveDegreeCurricularPlan() {
        DegreeCurricularPlan result = null;
        ExecutionDegree mostRecentExecutionDegree = null;

        for (final DegreeCurricularPlan degreeCurricularPlan : getActiveDegreeCurricularPlans()) {
            final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

            if (executionDegree == null) {
                result = degreeCurricularPlan;
            } else if (mostRecentExecutionDegree == null || mostRecentExecutionDegree.isBefore(executionDegree)) {
                mostRecentExecutionDegree = executionDegree;
            }
        }

        return mostRecentExecutionDegree == null ? result : mostRecentExecutionDegree.getDegreeCurricularPlan();
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    private static final Map<String, SoftReference<Degree>> degrees = new Hashtable<>();

    private static void loadCache() {
        synchronized (degrees) {
            degrees.clear();
            for (final Degree degree : Degree.readNotEmptyDegrees()) {
                degrees.put(degree.getSigla().toLowerCase(), new SoftReference<>(degree));
            }
        }
    }

    private static void updateCache(final Degree degree, final String newLowerCaseSigla) {
        final String currentLowerCaseSigla = degree.getSigla() != null ? degree.getSigla().toLowerCase() : StringUtils.EMPTY;
        synchronized (degrees) {
            degrees.remove(currentLowerCaseSigla);
            degrees.put(newLowerCaseSigla, new SoftReference<>(degree));
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
            if (degree != null && degree.getRootDomainObject() == Bennu.getInstance()
                    && degree.getSigla().equalsIgnoreCase(lowerCaseString)) {
                return degree;
            } else {
                loadCache();
                final SoftReference<Degree> otherDegreeReference = degrees.get(lowerCaseString);
                if (otherDegreeReference != null) {
                    final Degree otherDegree = otherDegreeReference.get();
                    if (otherDegree != null && otherDegree.getRootDomainObject() == Bennu.getInstance()
                            && otherDegree.getSigla().equalsIgnoreCase(lowerCaseString)) {
                        return otherDegree;
                    }
                }
            }
        }

        return null;
    }

    public static List<Degree> readNotEmptyDegrees() {
        final List<Degree> result = new ArrayList<>(Bennu.getInstance().getDegreesSet());
        result.remove(readEmptyDegree());
        return result;
    }

    public static Degree readEmptyDegree() {
        return EmptyDegree.getInstance();
    }

    public static List<Degree> readOldDegrees() {
        List<Degree> result = new ArrayList<>();
        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (!degree.isBolonhaDegree()) {
                result.add(degree);
            }
        }
        return result;
    }

    public static List<Degree> readBolonhaDegrees() {
        List<Degree> result = new ArrayList<>();
        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (degree.isBolonhaDegree()) {
                result.add(degree);
            }
        }
        return result;
    }

    public static List<Degree> readAllMatching(Predicate<DegreeType> predicate) {
        return DegreeType.all().filter(predicate).flatMap(type -> type.getDegreeSet().stream()).collect(Collectors.toList());
    }

    public static List<Degree> readAllByDegreeCode(final String degreeCode) {
        final List<Degree> result = new ArrayList<>();
        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (degree.hasMinistryCode() && degree.getMinistryCode().equals(degreeCode)) {
                result.add(degree);
            }
        }

        return result;
    }

    private boolean hasMinistryCode() {
        return getMinistryCode() != null;
    }

    public LocalizedString getQualificationLevel(final ExecutionYear executionYear) {
        return getMostRecentDegreeInfo(executionYear).getQualificationLevel();
    }

    public LocalizedString getProfessionalExits(final ExecutionYear executionYear) {
        return getMostRecentDegreeInfo(executionYear).getProfessionalExits();
    }

    public DegreeInfo getMostRecentDegreeInfo() {
        return getMostRecentDegreeInfo(ExecutionYear.readCurrentExecutionYear());
    }

    public DegreeInfo getDegreeInfoFor(ExecutionYear executionYear) {
        return executionYear.getDegreeInfo(this);
    }

    @Deprecated
    public DegreeInfo getMostRecentDegreeInfo(ExecutionYear executionYear) {
        DegreeInfo result = null;
        for (final DegreeInfo degreeInfo : getDegreeInfosSet()) {
            final ExecutionYear executionYear2 = degreeInfo.getExecutionYear();
            if (executionYear == executionYear2) {
                return degreeInfo;
            }
            if (executionYear2.isBefore(executionYear)) {
                if (result == null || executionYear2.isAfter(result.getExecutionYear())) {
                    result = degreeInfo;
                }
            }
        }

        if (result == null && executionYear.hasNextExecutionYear()) {
            result = getMostRecentDegreeInfo(executionYear.getNextExecutionYear());
        }

        return result;
    }

    public DegreeInfo getMostRecentDegreeInfo(AcademicInterval academicInterval) {
        DegreeInfo result = null;
        for (final DegreeInfo degreeInfo : getDegreeInfosSet()) {
            AcademicInterval academicInterval2 = degreeInfo.getAcademicInterval();
            if (academicInterval.equals(academicInterval2)) {
                return degreeInfo;
            }

            if (academicInterval2.isBefore(academicInterval)) {
                if (result == null || academicInterval2.isAfter(result.getAcademicInterval())) {
                    result = degreeInfo;
                }
            }
        }

        if (result == null && academicInterval.getNextAcademicInterval() != null) {
            result = getMostRecentDegreeInfo(academicInterval.getNextAcademicInterval());
        }

        return result;
    }

    private DegreeInfo createCurrentDegreeInfo(ExecutionYear executionYear) {
        // first let's check if the current degree info exists already
        final DegreeInfo shouldBeThisOne = executionYear.getDegreeInfo(this);
        if (shouldBeThisOne != null) {
            return shouldBeThisOne;
        }

        // ok, so let's create a new one based on the most recent one, if
        // existing
        return tryCreateUsingMostRecentInfo(executionYear);
    }

    private DegreeInfo tryCreateUsingMostRecentInfo(final ExecutionYear executionYear) {
        final DegreeInfo mostRecentDegreeInfo = getMostRecentDegreeInfo(executionYear);
        return mostRecentDegreeInfo != null ? new DegreeInfo(mostRecentDegreeInfo, executionYear) : new DegreeInfo(this,
                executionYear);
    }

    public DegreeInfo createCurrentDegreeInfo() {
        return createCurrentDegreeInfo(ExecutionYear.readCurrentExecutionYear());
    }

    /**
     * @deprecated Degree should not answer duration questions.
     * 
     *             For more accurate results use {@link DegreeCurricularPlan#getDurationInYears()}
     * 
     */
    @Deprecated
    public List<Integer> buildFullCurricularYearList() {

        final DegreeCurricularPlan degreeCurricularPlan = getMostRecentDegreeCurricularPlan();
        if (degreeCurricularPlan == null) {
            throw new DomainException("error.degree.unable.to.find.degree.curricular.plan.to.calculate.duration");
        }

        final List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= degreeCurricularPlan.getDurationInYears(); i++) {
            result.add(i);
        }
        return result;
    }

    public ScientificCommission getMostRecentScientificCommission(Person person) {
        for (ExecutionYear ey = ExecutionYear.readCurrentExecutionYear(); ey != null; ey = ey.getPreviousExecutionYear()) {
            for (ScientificCommission member : getScientificCommissionMembers(ey)) {
                if (member.getPerson() == person) {
                    return member;
                }
            }
        }

        return null;
    }

    public boolean isMemberOfAnyScientificCommission(Person person) {
        return person != null && getMostRecentScientificCommission(person) != null;
    }

    public boolean isMemberOfCurrentScientificCommission(Person person) {
        for (ScientificCommission member : getCurrentScientificCommissionMembers()) {
            if (member.getPerson() == person) {
                return true;
            }
        }

        return false;
    }

    public boolean isMemberOfCurrentScientificCommission(final Person person, final ExecutionYear executionYear) {
        if (person != null) {
            for (ScientificCommission member : getScientificCommissionMembers(executionYear)) {
                if (member.getPerson() == person) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<ScientificCommission> getCurrentScientificCommissionMembers() {
        for (ExecutionYear ey = ExecutionYear.readCurrentExecutionYear(); ey != null; ey = ey.getPreviousExecutionYear()) {
            Collection<ScientificCommission> members = getScientificCommissionMembers(ey);

            if (!members.isEmpty()) {
                return members;
            }
        }

        return Collections.emptyList();
    }

    public Collection<ScientificCommission> getScientificCommissionMembers(ExecutionYear executionYear) {
        for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansForYear(executionYear)) {
            ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);

            if (executionDegree != null) {
                return new ArrayList<>(executionDegree.getScientificCommissionMembersSet());
            }
        }

        return Collections.emptyList();
    }

    public boolean isCoordinator(final Person person, final ExecutionYear executionYear) {
        for (final Coordinator coordinator : getCoordinators(executionYear, false)) {
            if (coordinator.getPerson() == person) {
                return true;
            }
        }

        return false;
    }

    final public boolean isCurrentCoordinator(final Person person) {
        for (final Coordinator coordinator : getCurrentCoordinators(false)) {
            if (coordinator.getPerson() == person) {
                return true;
            }
        }

        return false;
    }

    /**
     * Verifies if the given person was a coordinator for this degree regardless
     * of the execution year.
     *
     * @param person
     *            the person to check
     * @return <code>true</code> if the person was a coordinator for a certain
     *         execution degree
     */
    final public boolean isCoordinatorInSomeExecutionYear(final Person person) {
        if (person != null) {
            for (Coordinator coordinator : person.getCoordinatorsSet()) {
                if (coordinator.getExecutionDegree().getDegree() == this) {
                    return true;
                }
            }
        }

        return false;
    }

    final private Collection<Coordinator> getCoordinators(final ExecutionYear executionYear, final boolean responsible) {
        final Collection<Coordinator> result = new HashSet<>();

        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            if (executionDegree != null) {
                result.addAll(responsible ? executionDegree.getResponsibleCoordinators() : executionDegree
                        .getCoordinatorsListSet());
            }
        }

        return result;
    }

    final private Collection<Coordinator> getCurrentCoordinators(final boolean responsible) {
        SortedSet<ExecutionYear> years = new TreeSet<ExecutionYear>(new ReverseComparator(ExecutionYear.COMPARATOR_BY_YEAR));
        years.addAll(getDegreeCurricularPlansExecutionYears());

        ExecutionYear current = ExecutionYear.readCurrentExecutionYear();
        for (ExecutionYear year : years) {
            if (year.isAfter(current)) {
                continue;
            }

            Collection<Coordinator> coordinators = getCoordinators(year, responsible);
            if (!coordinators.isEmpty()) {
                return coordinators;
            }
        }

        return Collections.emptyList();
    }

    public Collection<Coordinator> getResponsibleCoordinators(final ExecutionYear executionYear) {
        return getCoordinators(executionYear, true);
    }

    public Collection<Coordinator> getCurrentCoordinators() {
        return getCurrentCoordinators(false);
    }

    public Collection<Coordinator> getCurrentResponsibleCoordinators() {
        return getCurrentCoordinators(true);
    }

    public Collection<Teacher> getResponsibleCoordinatorsTeachers(final ExecutionYear executionYear) {
        final Collection<Teacher> result = new TreeSet<>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);

        collectCoordinatorsTeachers(result, getResponsibleCoordinators(executionYear));

        return result;
    }

    public Collection<Teacher> getCurrentResponsibleCoordinatorsTeachers() {
        final Collection<Teacher> result = new TreeSet<>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);

        collectCoordinatorsTeachers(result, getCurrentResponsibleCoordinators());

        return result;
    }

    final private void collectCoordinatorsTeachers(final Collection<Teacher> result, final Collection<Coordinator> coordinators) {
        for (final Coordinator coordinator : coordinators) {
            final Teacher teacher = coordinator.getTeacher();
            if (teacher != null) {
                result.add(teacher);
            }
        }
    }

    public Collection<Space> getCampus(ExecutionYear executionYear) {
        Set<Space> result = new HashSet<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            if (executionDegree != null && executionDegree.getCampus() != null) {
                result.add(executionDegree.getCampus());
            }
        }
        return new ArrayList<>(result);
    }

    public Collection<Space> getCurrentCampus() {
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        Collection<Space> result = this.getCampus(executionYear);

        if (!result.isEmpty()) {
            return result;
        }

        for (; executionYear != null; executionYear = executionYear.getNextExecutionYear()) {
            result = this.getCampus(executionYear);

            if (!result.isEmpty()) {
                return result;
            }
        }

        return new ArrayList<>();
    }

    public String constructSchoolClassPrefix(final Integer curricularYear) {
        return isBolonhaDegree() ? getSigla() + "0" + curricularYear.toString() : StringUtils.EMPTY;
    }

    public List<StudentCurricularPlan> getLastStudentCurricularPlans() {
        final List<StudentCurricularPlan> result = new ArrayList<>();

        for (final DegreeCurricularPlan degreeCurricularPlan : this.getDegreeCurricularPlansSet()) {
            for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
                if (studentCurricularPlan.getRegistration() == null) {
                    continue;
                }

                if (!result.contains(studentCurricularPlan)) {

                    result.add(studentCurricularPlan.getRegistration().getLastStudentDegreeCurricularPlansByDegree(this));
                }
            }
        }

        return new ArrayList<>(result);
    }

    public List<StudentCurricularPlan> getStudentCurricularPlans(ExecutionYear executionYear) {
        List<StudentCurricularPlan> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansForYear(executionYear)) {
            degreeCurricularPlan.getStudentsCurricularPlans(executionYear, result);
        }
        return result;
    }

    public boolean isFirstCycle() {
        return getDegreeType().isFirstCycle();
    }

    public boolean isSecondCycle() {
        return getDegreeType().isSecondCycle();
    }

    public boolean isThirdCycle() {
        return getDegreeType().isThirdCycle();
    }

    public boolean isAnyPublishedThesisAvailable() {
        for (DegreeCurricularPlan dcp : getDegreeCurricularPlansSet()) {
            for (CurricularCourse curricularCourse : dcp.getDissertationCurricularCourses(null)) {
                List<IEnrolment> enrolments = new ArrayList<>();

                for (CurriculumModule module : curricularCourse.getCurriculumModulesSet()) {
                    if (module.isEnrolment()) {
                        enrolments.add((IEnrolment) module);
                    } else if (module.isDismissal()) {
                        Dismissal dismissal = (Dismissal) module;

                        enrolments.addAll(dismissal.getSourceIEnrolments());
                    }
                }

                for (IEnrolment enrolment : enrolments) {
                    Thesis thesis = enrolment.getThesis();

                    if (thesis != null && thesis.getState().equals(ThesisState.EVALUATED)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isAnyThesisAvailable() {
        return !getThesisSet().isEmpty();
    }

    /*
     * STUDENTS FROM DEGREE
     */
    public List<Student> getAllStudents() {
        List<Student> result = new ArrayList<>();
        for (Registration registration : getActiveRegistrations()) {
            result.add(registration.getStudent());
        }
        return result;
    }

    public List<Student> getStudentsFromGivenCurricularYear(int curricularYear, ExecutionYear executionYear) {
        List<Student> result = new ArrayList<>();
        for (Registration registration : getActiveRegistrations()) {
            if (registration.getCurricularYear(executionYear) == curricularYear) {
                result.add(registration.getStudent());
            }
        }
        return result;
    }

    /*
     * CURRICULAR COURSES FROM DEGREE
     */
    public Set<CurricularCourse> getAllCurricularCourses(ExecutionYear executionYear) {
        Set<CurricularCourse> result = new HashSet<>();
        for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
            result.addAll(dcp.getCurricularCoursesWithExecutionIn(executionYear));
        }
        return result;
    }

    public Set<CurricularCourse> getCurricularCoursesFromGivenCurricularYear(int curricularYear, ExecutionYear executionYear) {
        Set<CurricularCourse> result = new HashSet<>();
        for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
            result.addAll(dcp.getCurricularCoursesByExecutionYearAndCurricularYear(executionYear, curricularYear));
        }
        return result;
    }

    /*
     * This method is directed to Bolonha Integrated Master Degrees
     */
    public Set<CurricularCourse> getFirstCycleCurricularCourses(ExecutionYear executionYear) {
        Set<CurricularCourse> result = new HashSet<>();
        for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
            // TODO how to make this nothardcoded?
            for (int i = 1; i <= 3; i++) {
                result.addAll(dcp.getCurricularCoursesByExecutionYearAndCurricularYear(executionYear, i));
            }
        }
        return result;
    }

    /*
     * This method is directed to Bolonha Integrated Master Degrees
     */
    public Set<CurricularCourse> getSecondCycleCurricularCourses(ExecutionYear executionYear) {
        Set<CurricularCourse> result = new HashSet<>();
        for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
            for (int i = 4; i <= 5; i++) { // TODO: how to make this not
                // hardcoded?
                result.addAll(dcp.getCurricularCoursesByExecutionYearAndCurricularYear(executionYear, i));
            }
        }
        return result;
    }

    /**
     * @return <code>true</code> if any of the thesis associated with this
     *         degree is not final
     */
    public boolean hasPendingThesis() {
        for (Thesis thesis : getThesisSet()) {
            if (!thesis.isFinalThesis()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Double getEctsCredits() {
        final Double ectsCredits = super.getEctsCredits();
        return ectsCredits != null ? ectsCredits : 0.0;
    }

    public boolean hasEctsCredits() {
        return super.getEctsCredits() != null;
    }

    @Override
    public String getMinistryCode() {
        final String ministryCode = super.getMinistryCode();
        if (!StringUtils.isEmpty(ministryCode)) {
            return ministryCode;
        }

        return DEFAULT_MINISTRY_CODE;
    }

    @Override
    public void setMinistryCode(final String ministryCode) {
        super.setMinistryCode(ministryCode == null || ministryCode.length() == 0 ? null : ministryCode);
    }

    @Override
    public void setIdCardName(final String idCardName) {
        super.setIdCardName(idCardName.toUpperCase());
    }

    @Override
    public void setNome(final String nome) {
        super.setNome(nome);
        setIdCardName(nome);
    }

    public boolean isActive() {
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (DegreeCurricularPlan curricularPlan : getDegreeCurricularPlansSet()) {
            if (curricularPlan.getExecutionDegreeByYear(currentExecutionYear) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean canCreateGratuityEvent() {
        return true;
    }

    public boolean isDEA() {
        return getDegreeType().isAdvancedSpecializationDiploma();
    }

    public DegreeOfficialPublication getOfficialPublication(DateTime when) {
        DegreeOfficialPublication found = null;
        for (DegreeOfficialPublication publication : getOfficialPublicationSet()) {
            DateTime publicationDate = publication.getPublication().toDateTimeAtStartOfDay();
            if (found == null && publicationDate.isBefore(when)) {
                found = publication;
            } else if (found != null && publicationDate.isBefore(when)
                    && publication.getPublication().isAfter(found.getPublication())) {
                found = publication;
            }
        }
        return found;
    }

    public List<Teacher> getAllTeachers(AcademicInterval academicInterval) {
        List<Teacher> teachers = new ArrayList<>();
        for (Department department : getDepartmentsSet()) {
            teachers.addAll(department.getAllTeachers(academicInterval));
        }
        return teachers;
    }

    public String getDegreeTypeName() {
        return getDegreeType().getName().getContent();
    }

    @Override
    public void setCode(String code) {
        final Degree existingDegree = Degree.find(code);
        if (existingDegree != null && existingDegree != this) {
            throw new DomainException("error.degree.already.exists.degree.with.same.code");
        }

        super.setCode(code);
    }

    @Override
    public Sender getSender() {
        return Optional.ofNullable(super.getSender()).orElseGet(this::buildDefaultSender);
    }

    @Atomic
    private Sender buildDefaultSender() {
        Group current = CoordinatorGroup.get(this);
        Group teachers = TeacherGroup.get(this);
        Group students = StudentGroup.get(this, null);
        List<Group> cycleGroups = getDegreeType().getCycleTypes().stream()
                .map(cycleType -> StudentGroup.get(this, cycleType)).collect(Collectors.toList());
        Sender sender = Sender
                .from(Installation.getInstance().getInstituitionalEmailAddress("noreply"))
                .as(createFromName())
                .members(CoordinatorGroup.get(this))
                .recipients(cycleGroups)
                .recipients(current,teachers,students)
                .recipients(RoleType.TEACHER.actualGroup(), StudentGroup.get())
                .build();
        setSender(sender);
        return sender;
    }

    private String createFromName() {
        return String.format("%s (%s: %s)", Unit.getInstitutionAcronym(), getSigla(), "Coordenação");
    }

}

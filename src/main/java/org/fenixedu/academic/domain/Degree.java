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
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;

import com.google.common.base.Strings;

public class Degree extends Degree_Base implements Comparable<Degree> {
    public static final String CREATED_SIGNAL = "academic.degree.create";

    public static final String DEFAULT_MINISTRY_CODE = "9999";

    private static final Collator collator = Collator.getInstance();

    static final public Comparator<Degree> COMPARATOR_BY_NAME = new Comparator<Degree>() {
        @Override
        public int compare(final Degree o1, final Degree o2) {
            String name1;
            String name2;
            name1 = o1.getNameFor((AcademicInterval) null).getContent(I18N.getLocale());
            name2 = o2.getNameFor((AcademicInterval) null).getContent(I18N.getLocale());

            if (Strings.isNullOrEmpty(name1) || Strings.isNullOrEmpty(name2)) {
                name1 = o1.getNameFor((AcademicInterval) null).getContent();
                name2 = o2.getNameFor((AcademicInterval) null).getContent();
            }

            return collator.compare(name1, name2);
        }
    };

    static final public Comparator<Degree> COMPARATOR_BY_NAME_AND_ID = new Comparator<Degree>() {
        @Override
        public int compare(final Degree o1, final Degree o2) {
            final int nameResult = COMPARATOR_BY_NAME.compare(o1, o2);
            return nameResult == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : nameResult;
        }
    };

    static final private Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE_NAME = new Comparator<Degree>() {
        @Override
        public int compare(final Degree o1, final Degree o2) {
            return collator.compare(o1.getDegreeType().getName().getContent(), o2.getDegreeType().getName().getContent());
        }
    };

    static final private Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE = new Comparator<Degree>() {
        @Override
        public int compare(final Degree o1, final Degree o2) {
            return o1.getDegreeType().compareTo(o2.getDegreeType());
        }
    };

    private static class ComparatorByDegreeTypeAndNameAndId implements Serializable, Comparator<Degree> {
        @Override
        public int compare(final Degree o1, final Degree o2) {
            final int typeResult = COMPARATOR_BY_DEGREE_TYPE_NAME.compare(o1, o2);
            return typeResult == 0 ? COMPARATOR_BY_NAME_AND_ID.compare(o1, o2) : typeResult;
        }
    }

    static final public Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE_DEGREE_NAME_AND_ID = new Comparator<Degree>() {

        @Override
        public int compare(final Degree o1, final Degree o2) {
            final int typeResult = COMPARATOR_BY_DEGREE_TYPE.compare(o1, o2);
            return typeResult == 0 ? COMPARATOR_BY_NAME_AND_ID.compare(o1, o2) : typeResult;
        }

    };

    static final public Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID = new ComparatorByDegreeTypeAndNameAndId();

    @Override
    public int compareTo(final Degree o) {
        return Degree.COMPARATOR_BY_NAME_AND_ID.compare(this, o);
    }

    protected Degree() {
        super();

        setRootDomainObject(Bennu.getInstance());
    }

    public Degree(final String name, final String nameEn, final String code, final DegreeType degreeType,
            final GradeScale gradeScale, final ExecutionYear executionYear) {
        this();
        commonFieldsChange(name, nameEn, code, gradeScale, executionYear);

        if (degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        }
        this.setDegreeType(degreeType);
    }

    public Degree(final String name, final String nameEn, final String acronym, final DegreeType degreeType,
            final Double ectsCredits, final GradeScale gradeScale, final String prevailingScientificArea,
            final AdministrativeOffice administrativeOffice) {
        this();
        commonFieldsChange(name, nameEn, acronym, gradeScale, ExecutionYear.findCurrent(getCalendar()));
        newStructureFieldsChange(degreeType, ectsCredits, prevailingScientificArea);
        setAdministrativeOffice(administrativeOffice);
    }

    private void commonFieldsChange(final String name, final String nameEn, final String code, final GradeScale gradeScale,
            final ExecutionYear executionYear) {
        if (name == null) {
            throw new DomainException("degree.name.not.null");
        } else if (nameEn == null) {
            throw new DomainException("degree.name.en.not.null");
        } else if (code == null) {
            throw new DomainException("degree.code.not.null");
        }

        DegreeInfo degreeInfo = getDegreeInfoFor(executionYear);
        if (degreeInfo == null) {
            degreeInfo = tryCreateUsingMostRecentInfo(executionYear);
        }
        degreeInfo.setName(new LocalizedString().with(org.fenixedu.academic.util.LocaleUtils.PT, name.trim())
                .with(org.fenixedu.academic.util.LocaleUtils.EN, nameEn.trim()));

        this.setNome(name);
        this.setNameEn(nameEn);
        this.setSigla(code.trim());
        this.setGradeScale(gradeScale);
    }

    private void newStructureFieldsChange(final DegreeType degreeType, final Double ectsCredits,
            final String prevailingScientificArea) {
        if (degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        } else if (ectsCredits == null) {
            throw new DomainException("degree.ectsCredits.not.null");
        }

        this.setDegreeType(degreeType);
        this.setEctsCredits(ectsCredits);
        this.setPrevailingScientificArea(prevailingScientificArea == null ? null : prevailingScientificArea.trim());
    }

    public void edit(final String name, final String nameEn, final String code, final DegreeType degreeType,
            final GradeScale gradeScale, final ExecutionYear executionYear) {
        commonFieldsChange(name, nameEn, code, gradeScale, executionYear);

        if (degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        }
        this.setDegreeType(degreeType);
    }

    public void edit(final String name, final String nameEn, final String acronym, final DegreeType degreeType,
            final Double ectsCredits, final GradeScale gradeScale, final String prevailingScientificArea,
            final ExecutionYear executionYear) {
        checkIfCanEdit(degreeType);
        commonFieldsChange(name, nameEn, acronym, gradeScale, executionYear);
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
    protected void checkForDeletionBlockers(final Collection<String> blockers) {
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

        for (; !getParticipatingAnyCurricularCourseCurricularRulesSet()
                .isEmpty(); getParticipatingAnyCurricularCourseCurricularRulesSet().iterator().next().delete()) {
            ;
        }

        // checkDeletion assures that site is deletable
        if (getSender() != null) {
            setSender(null);
        }

        setCalendar(null);
        setUnit(null);
        setDegreeType(null);
        setRootDomainObject(null);
        super.disconnect();
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale();
    }

    public TreeSet<DegreeInfo> getDegreeInfosSorted() {
        final TreeSet<DegreeInfo> result = new TreeSet<>(DegreeInfo.COMPARATOR_BY_EXECUTION_YEAR);
        result.addAll(getDegreeInfosSet());
        return result;
    }

    public DegreeCurricularPlan createDegreeCurricularPlan(final String name, final GradeScale gradeScale, final Person creator,
            final AcademicPeriod duration) {
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

    public DegreeCurricularPlan findDegreeCurricularPlan(final LocalizedString name) {
        if (name == null || StringUtils.isEmpty(name.getContent(Locale.getDefault()))) {
            return null;
        }
        String degreeName = getNameI18N().getContent(Locale.getDefault());
        String dcpName = name.getContent(Locale.getDefault());

        DegreeCurricularPlan foundDcp = null;
        for (DegreeCurricularPlan dcp : getDegreeCurricularPlansSet()) {
            if (dcp.getName().equalsIgnoreCase(dcpName)) {
                if (foundDcp != null) {
                    throw new DomainException("error.Degree.found.duplicate.DegreeCurricularPlan", dcpName, degreeName);
                }
                foundDcp = dcp;
            }
        }
        return foundDcp;
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

    @Deprecated
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

    public List<DegreeCurricularPlan> getActiveDegreeCurricularPlans() {
        List<DegreeCurricularPlan> result = new ArrayList<>();

        for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
                result.add(degreeCurricularPlan);
            }
        }

        return result;
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlansForYear(final ExecutionYear year) {
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

    // início da nova operação -- Ricardo Marcão
    public List<ExecutionCourse> getExecutionCourses(final AcademicInterval academicInterval) {
        final List<ExecutionCourse> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            for (final CurricularCourse course : degreeCurricularPlan.getCurricularCoursesSet()) {
                for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCoursesSet()) {
                    if (academicInterval.isEqualOrEquivalent(executionCourse.getAcademicInterval())) {
                        if (course.getParentContextsSet().stream()
                                .anyMatch(ctx -> ctx.isValid(academicInterval) && ctx.getCurricularPeriod()
                                        .getChildOrder() == academicInterval.getAcademicCalendarEntry().getCardinality())) {
                            result.add(executionCourse);
                        }
                    }
                }
            }
        }
        return result;
    }

    // -- fim da nova operação -- Ricardo Marcão

    @Deprecated
    public LocalizedString getNameFor(final ExecutionYear executionYear) {
        DegreeInfo degreeInfo = executionYear == null ? getMostRecentDegreeInfo() : getMostRecentDegreeInfo(executionYear);
        return degreeInfo == null ? new LocalizedString().with(org.fenixedu.academic.util.LocaleUtils.PT, super.getNome())
                .with(org.fenixedu.academic.util.LocaleUtils.EN, super.getNameEn()) : degreeInfo.getName();
    }

    @Deprecated
    public LocalizedString getNameFor(final ExecutionSemester executionSemester) {
        return getNameFor(executionSemester != null ? executionSemester.getExecutionYear() : null);
    }

    public LocalizedString getNameFor(final AcademicInterval academicInterval) {
        DegreeInfo degreeInfo = academicInterval == null ? getMostRecentDegreeInfo() : getMostRecentDegreeInfo(academicInterval);
        return degreeInfo == null ? new LocalizedString().with(org.fenixedu.academic.util.LocaleUtils.PT, super.getNome())
                .with(org.fenixedu.academic.util.LocaleUtils.EN, super.getNameEn()) : degreeInfo.getName();
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
        return degreeInfo == null ? StringUtils.EMPTY : degreeInfo.getName()
                .getContent(org.fenixedu.academic.util.LocaleUtils.PT);
    }

    /**
     * @deprecated Use {@link #getNameFor(ExecutionYear)}
     */
    @Override
    @Deprecated
    public String getNameEn() {
        DegreeInfo degreeInfo = getMostRecentDegreeInfo();
        return degreeInfo == null ? StringUtils.EMPTY : degreeInfo.getName()
                .getContent(org.fenixedu.academic.util.LocaleUtils.EN);
    }

    final public LocalizedString getNameI18N() {
        return getNameFor(ExecutionYear.findCurrent(getCalendar()));
    }

    final public LocalizedString getNameI18N(final ExecutionYear executionYear) {
        return getNameFor(executionYear);
    }

    public LocalizedString getPresentationNameI18N() {
        return getPresentationNameI18N(ExecutionYear.findCurrent(getCalendar()));
    }

    public LocalizedString getPresentationNameI18N(final ExecutionYear executionYear) {
        LocalizedString degreeType = getDegreeType().getName();

        LocalizedString.Builder builder = new LocalizedString.Builder();
        CoreConfiguration.supportedLocales().forEach(l -> builder.with(l, degreeType.getContent(l) + " "
                + BundleUtil.getString(Bundle.APPLICATION, "label.in") + " " + getNameI18N(executionYear).getContent(l)));
        return builder.build();
    }

    final public String getPresentationName() {
        return getPresentationNameI18N().getContent();
    }

    public String getPresentationName(final ExecutionYear executionYear) {
        return getPresentationNameI18N(executionYear).getContent();
    }

    public String getPresentationName(final ExecutionYear executionYear, final Locale locale) {
        return getPresentationNameI18N(executionYear).getContent(locale);
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
            if (mostRecentDegreeCurricularPlan == null || degreeCurricularPlan.getInitialDateYearMonthDay()
                    .isAfter(mostRecentDegreeCurricularPlan.getInitialDateYearMonthDay())) {
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

    public String getAcronym() {
        return super.getSigla();
    }

    public void setAcronym(final String input) {
        if (Strings.isNullOrEmpty(input)) {
            throw new DomainException("error.Degree.required.acronym");
        }

        setSigla(input.trim());
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

    /**
     * @deprecated Degrees cannot be empty anymore so usage of this method is unecessary
     */
    @Deprecated
    public static List<Degree> readNotEmptyDegrees() {
        return new ArrayList<>(Bennu.getInstance().getDegreesSet());
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

    public static List<Degree> readAllMatching(final Predicate<DegreeType> predicate) {
        return DegreeType.all().filter(predicate).flatMap(type -> type.getDegreeSet().stream()).collect(Collectors.toList());
    }

    public DegreeInfo getMostRecentDegreeInfo() {
        return getMostRecentDegreeInfo(ExecutionYear.findCurrent(getCalendar()));
    }

    public DegreeInfo getDegreeInfoFor(final ExecutionYear executionYear) {
        return getDegreeInfosSet().stream().filter(di -> di.getExecutionYear() == executionYear).findFirst().orElse(null);
    }

    @Deprecated
    public DegreeInfo getMostRecentDegreeInfo(final ExecutionYear executionYear) {
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

        if (result == null && executionYear.getNextExecutionYear() != null) {
            result = getMostRecentDegreeInfo(executionYear.getNextExecutionYear());
        }

        return result;
    }

    public DegreeInfo getMostRecentDegreeInfo(final AcademicInterval academicInterval) {
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

    private DegreeInfo tryCreateUsingMostRecentInfo(final ExecutionYear executionYear) {
        final DegreeInfo mostRecentDegreeInfo = getMostRecentDegreeInfo(executionYear);
        return mostRecentDegreeInfo != null ? new DegreeInfo(mostRecentDegreeInfo, executionYear) : new DegreeInfo(this,
                executionYear);
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
                result.addAll(
                        responsible ? executionDegree.getResponsibleCoordinators() : executionDegree.getCoordinatorsListSet());
            }
        }

        return result;
    }

    final private Collection<Coordinator> getCurrentCoordinators(final boolean responsible) {
        SortedSet<ExecutionYear> years = new TreeSet<ExecutionYear>(new ReverseComparator(ExecutionYear.COMPARATOR_BY_YEAR));
        years.addAll(getDegreeCurricularPlansExecutionYears());

        ExecutionYear current = ExecutionYear.findCurrent(getCalendar());
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

    public Collection<Space> getCampus(final ExecutionYear executionYear) {
        Set<Space> result = new HashSet<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
            final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            if (executionDegree != null && executionDegree.getCampus() != null) {
                result.add(executionDegree.getCampus());
            }
        }
        return new ArrayList<>(result);
    }

    public String constructSchoolClassPrefix(final Integer curricularYear) {
        return isBolonhaDegree() ? getSigla() + "0" + curricularYear.toString() : StringUtils.EMPTY;
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

    public String getDegreeTypeName() {
        return getDegreeType().getName().getContent();
    }

    @Override
    public void setCode(final String code) {
        final Degree existingDegree = Degree.find(code);
        if (existingDegree != null && existingDegree != this) {
            throw new DomainException("error.degree.already.exists.degree.with.same.code");
        }

        super.setCode(code);
    }

}

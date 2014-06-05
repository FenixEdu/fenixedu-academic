/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * DegreeType.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.degree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
public enum DegreeType {

    DEGREE(GradeScale.TYPE20, AcademicPeriod.FIVE_YEAR, true, // canCreateStudent
            false, // canCreateStudentOnlyWithCandidacy
            true // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            final StringBuilder result = new StringBuilder(localizedName(locale));

            final String remove =
                    " (" + Integer.toString(getYears()) + " " + BundleUtil.getString(Bundle.APPLICATION, "years") + ")";

            if (result.toString().contains(remove)) {
                result.replace(result.indexOf(remove), result.indexOf(remove) + remove.length(), StringUtils.EMPTY);
            }

            return result.toString();
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return Collections.emptySet();
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return Collections.emptySet();
        }

    },

    MASTER_DEGREE(GradeScale.TYPE5, AcademicPeriod.TWO_YEAR, false, // canCreateStudent
            true, // canCreateStudentOnlyWithCandidacy
            true // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            return localizedName(locale);
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.MASTER_DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return Collections.emptySet();
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return Collections.emptySet();
        }

    },

    BOLONHA_DEGREE(GradeScale.TYPE20, AcademicPeriod.THREE_YEAR, true, // canCreateStudent
            false, // canCreateStudentOnlyWithCandidacy
            true // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            final StringBuilder result = new StringBuilder(localizedName(locale));

            final String remove = " Bolonha";
            if (result.toString().contains(remove)) {
                result.replace(result.indexOf(remove), result.indexOf(remove) + remove.length(), StringUtils.EMPTY);
            }

            return result.toString();
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return FIRST_CYCLE_TYPE;
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return FIRST_AND_SECOND_CYCLE_TYPE;
        }

        @Override
        public boolean canRemoveEnrolmentIn(CycleType cycleType) {
            return cycleType == CycleType.SECOND_CYCLE;
        }

        @Override
        public boolean hasSeniorEligibility(Registration registration, ExecutionYear executionYear) {
            return hasConditionsToFinishBachelorDegree(registration, executionYear);
        }

    },

    BOLONHA_MASTER_DEGREE(GradeScale.TYPE20, AcademicPeriod.TWO_YEAR, true, // canCreateStudent
            false, // canCreateStudentOnlyWithCandidacy
            true // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            final StringBuilder result = new StringBuilder(localizedName(locale));

            final String remove = " Bolonha";
            if (result.toString().contains(remove)) {
                result.replace(result.indexOf(remove), result.indexOf(remove) + remove.length(), StringUtils.EMPTY);
            }

            return result.toString();
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return SECOND_CYCLE_TYPE;
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return SECOND_CYCLE_TYPE;
        }

        @Override
        public boolean hasSeniorEligibility(Registration registration, ExecutionYear executionYear) {
            return hasConditionsToFinishMasterDegree(registration, executionYear);
        }

    },

    BOLONHA_INTEGRATED_MASTER_DEGREE(GradeScale.TYPE20, AcademicPeriod.FIVE_YEAR, true, // canCreateStudent
            false, // canCreateStudentOnlyWithCandidacy
            true // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            final StringBuilder result = new StringBuilder(localizedName(locale));

            final String remove = " Bolonha";
            if (result.toString().contains(remove)) {
                result.replace(result.indexOf(remove), result.indexOf(remove) + remove.length(), StringUtils.EMPTY);
            }

            return result.toString();
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return FIRST_AND_SECOND_CYCLE_TYPE;
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return FIRST_AND_SECOND_CYCLE_TYPE;
        }

        @Override
        public boolean canRemoveEnrolmentIn(CycleType cycleType) {
            return true;
        }

        @Override
        public Integer getYears(final CycleType cycleType) {
            if (cycleType == null) {
                return getYears();
            }

            switch (cycleType) {
            case FIRST_CYCLE:
                return BOLONHA_DEGREE.getYears(cycleType);
            case SECOND_CYCLE:
                return BOLONHA_MASTER_DEGREE.getYears(cycleType);
            }

            return null;
        }

        @Override
        public boolean hasSeniorEligibility(Registration registration, ExecutionYear executionYear) {
            if (!isNotEnrolledInFirstCycleOrIsConcluded(registration, executionYear)) {
                return false;
            }

            return hasConditionsToFinishMasterDegree(registration, executionYear);
        }

    },

    BOLONHA_ADVANCED_FORMATION_DIPLOMA(GradeScale.TYPE20, AcademicPeriod.YEAR, true, // canCreateStudent
            true, // canCreateStudentOnlyWithCandidacy
            false // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            final StringBuilder result = new StringBuilder(localizedName(locale));

            final String remove = " Bolonha";
            if (result.toString().contains(remove)) {
                result.replace(result.indexOf(remove), result.indexOf(remove) + remove.length(), StringUtils.EMPTY);
            }

            return result.toString();
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.MASTER_DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return THIRD_CYCLE_TYPE;
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return THIRD_CYCLE_TYPE;
        }

    },

    BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA(GradeScale.TYPE20, AcademicPeriod.TWO_YEAR, true, // canCreateStudent
            false, // canCreateStudentOnlyWithCandidacy
            false // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            final StringBuilder result = new StringBuilder(localizedName(locale));

            final String remove = " Bolonha";
            if (result.toString().contains(remove)) {
                result.replace(result.indexOf(remove), result.indexOf(remove) + remove.length(), StringUtils.EMPTY);
            }

            return result.toString();
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.MASTER_DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return THIRD_CYCLE_TYPE;
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return THIRD_CYCLE_TYPE;
        }

    },

    BOLONHA_SPECIALIZATION_DEGREE(GradeScale.TYPE20, AcademicPeriod.YEAR, true, // canCreateStudent
            false, // canCreateStudentOnlyWithCandidacy
            false // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return name();
        }

        @Override
        protected String qualifiedName() {
            return DegreeType.class.getSimpleName() + "." + name();
        }

        @Override
        protected String localizedName() {
            return localizedName(I18N.getLocale());
        }

        @Override
        protected String localizedName(Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, locale, qualifiedName());
        }

        @Override
        protected String filteredName() {
            return filteredName(I18N.getLocale());
        }

        @Override
        protected String filteredName(Locale locale) {
            final StringBuilder result = new StringBuilder(localizedName(locale));

            final String remove = " Bolonha";
            if (result.toString().contains(remove)) {
                result.replace(result.indexOf(remove), result.indexOf(remove) + remove.length(), StringUtils.EMPTY);
            }

            return result.toString();
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.MASTER_DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return SPECIALIZATION_CYCLE_TYPE;
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return SPECIALIZATION_CYCLE_TYPE;
        }

    },

    EMPTY(null, null, true, // canCreateStudent
            false, // canCreateStudentOnlyWithCandidacy
            false // qualifiesForGraduateTitle
    ) {

        @Override
        protected String concreteName() {
            return StringUtils.EMPTY;
        }

        @Override
        protected String qualifiedName() {
            return StringUtils.EMPTY;
        }

        @Override
        protected String localizedName() {
            return StringUtils.EMPTY;
        }

        @Override
        protected String localizedName(Locale locale) {
            return StringUtils.EMPTY;
        }

        @Override
        protected String filteredName() {
            return StringUtils.EMPTY;
        }

        @Override
        protected String filteredName(Locale locale) {
            return StringUtils.EMPTY;
        }

        @Override
        protected AdministrativeOfficeType administrativeOfficeType() {
            return AdministrativeOfficeType.DEGREE;
        }

        @Override
        protected Collection<CycleType> cycleTypes() {
            return Collections.emptySet();
        }

        @Override
        protected Collection<CycleType> supportedCyclesToEnrol() {
            return Collections.emptySet();
        }

    };

    private static final String GRADUATE_TITLE_SUFFIX = ".graduate.title";

    public static final Set<DegreeType> NOT_EMPTY_VALUES;
    static {
        final Set<DegreeType> result = new HashSet<DegreeType>();
        for (final DegreeType degreeType : values()) {
            if (!degreeType.isEmpty()) {
                result.add(degreeType);
            }
        }
        NOT_EMPTY_VALUES = Collections.unmodifiableSet(result);
    }

    public static final List<DegreeType> NOT_EMPTY_BOLONHA_VALUES;
    static {
        final List<DegreeType> result = new ArrayList<DegreeType>();
        for (final DegreeType degreeType : NOT_EMPTY_VALUES) {
            if (degreeType.isBolonhaType()) {
                result.add(degreeType);
            }
        }
        NOT_EMPTY_BOLONHA_VALUES = Collections.unmodifiableList(result);
    }

    private static final Set<CycleType> FIRST_CYCLE_TYPE = Collections.singleton(CycleType.FIRST_CYCLE);

    private static final Set<CycleType> SECOND_CYCLE_TYPE = Collections.singleton(CycleType.SECOND_CYCLE);

    private static final Set<CycleType> THIRD_CYCLE_TYPE = Collections.singleton(CycleType.THIRD_CYCLE);

    private static final Set<CycleType> SPECIALIZATION_CYCLE_TYPE = Collections.singleton(CycleType.SPECIALIZATION_CYCLE);

    private static final List<CycleType> FIRST_AND_SECOND_CYCLE_TYPE = Collections.unmodifiableList(Arrays
            .asList(new CycleType[] { CycleType.FIRST_CYCLE, CycleType.SECOND_CYCLE }));

    private GradeScale gradeScale;

    private AcademicPeriod academicPeriod;

    private boolean canCreateStudent;

    private boolean canCreateStudentOnlyWithCandidacy;

    private boolean qualifiesForGraduateTitle;

    private DegreeType(GradeScale gradeScale, AcademicPeriod academicPeriod, boolean canCreateStudent,
            boolean canCreateStudentOnlyWithCandidacy, boolean qualifiesForGraduateTitle) {
        this.gradeScale = gradeScale;
        this.academicPeriod = academicPeriod;
        this.canCreateStudent = canCreateStudent;
        this.canCreateStudentOnlyWithCandidacy = canCreateStudentOnlyWithCandidacy;
        this.qualifiesForGraduateTitle = qualifiesForGraduateTitle;
    }

    public String getName() {
        return concreteName();
    }

    public String getMinistryCode() {
        return getCategory() == null ? StringUtils.EMPTY : String.valueOf(getCategory().getCode());
    }

    public Category getCategory() {
        for (final Category category : Category.values()) {
            if (category.getDegreeTypes().contains(this)) {
                return category;
            }
        }

        return null;
    }

    abstract protected String concreteName();

    public String getQualifiedName() {
        return qualifiedName();
    }

    abstract protected String qualifiedName();

    public String getLocalizedName() {
        return localizedName();
    }

    abstract protected String localizedName();

    public String getLocalizedName(final Locale locale) {
        return localizedName(locale);
    }

    abstract protected String localizedName(final Locale locale);

    public String getFilteredName() {
        return filteredName();
    }

    abstract protected String filteredName();

    public String getFilteredName(final Locale locale) {
        return filteredName(locale);
    }

    abstract protected String filteredName(final Locale locale);

    @Deprecated
    public AdministrativeOfficeType getAdministrativeOfficeType() {
        return administrativeOfficeType();
    }

    @Deprecated
    abstract protected AdministrativeOfficeType administrativeOfficeType();

    public Collection<CycleType> getCycleTypes() {
        return cycleTypes();
    }

    abstract protected Collection<CycleType> cycleTypes();

    public Collection<CycleType> getSupportedCyclesToEnrol() {
        return supportedCyclesToEnrol();
    }

    abstract protected Collection<CycleType> supportedCyclesToEnrol();

    public GradeScale getGradeScale() {
        return this.gradeScale;
    }

    public AcademicPeriod getAcademicPeriod() {
        return academicPeriod;
    }

    public boolean canCreateStudent() {
        return canCreateStudent;
    }

    public boolean canCreateStudentOnlyWithCandidacy() {
        return canCreateStudentOnlyWithCandidacy;
    }

    public boolean canRemoveEnrolmentIn(@SuppressWarnings("unused") final CycleType cycleType) {
        return false;
    }

    final public boolean getQualifiesForGraduateTitle() {
        return qualifiesForGraduateTitle;
    }

    public boolean isEmpty() {
        return this == DegreeType.EMPTY;
    }

    public boolean isBolonhaType() {
        return this != DegreeType.DEGREE && this != DegreeType.MASTER_DEGREE;
    }

    public boolean isDegree() {
        return this == DegreeType.DEGREE || this == DegreeType.BOLONHA_DEGREE;
    }

    public boolean isMasterDegree() {
        return this == DegreeType.MASTER_DEGREE || this == DegreeType.BOLONHA_MASTER_DEGREE;
    }

    public boolean isIntegratedMasterDegree() {
        return this == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return this.isDegree() || isIntegratedMasterDegree();
    }

    final public boolean hasAcademicPeriod() {
        return academicPeriod != null;
    }

    final public boolean hasExactlyOneCurricularYear() {
        return getYears() == 1;
    }

    public int getYears() {
        return hasAcademicPeriod() ? Float.valueOf(academicPeriod.getWeight()).intValue() : Integer.valueOf(0);
    }

    public int getSemesters() {
        return hasAcademicPeriod() ? Float.valueOf(academicPeriod.getWeight() / AcademicPeriod.SEMESTER.getWeight()).intValue() : Integer
                .valueOf(0);
    }

    public Integer getYears(final CycleType cycleType) {
        if (cycleType == null) {
            return getYears();
        }

        return hasCycleTypes(cycleType) ? Float.valueOf(getAcademicPeriod().getWeight()).intValue() : Integer.valueOf(0);
    }

    public Integer getSemesters(final CycleType cycleType) {
        if (cycleType == null) {
            return getSemesters();
        }

        return hasCycleTypes(cycleType) ? Float.valueOf(getYears(cycleType) / AcademicPeriod.SEMESTER.getWeight()).intValue() : null;
    }

    public double getDefaultEctsCredits() {
        if (!hasAcademicPeriod()) {
            return 0d;
        }

        if (getAcademicPeriod().equals(AcademicPeriod.YEAR)) {
            return 30d;
        } else if (getAcademicPeriod().equals(AcademicPeriod.TWO_YEAR)) {
            return 120d;
        } else if (getAcademicPeriod().equals(AcademicPeriod.THREE_YEAR)) {
            return 180d;
        } else if (getAcademicPeriod().equals(AcademicPeriod.FIVE_YEAR)) {
            return 300d;
        } else {
            return 0d;
        }
    }

    final public String getCreditsDescription() {
        return this == DegreeType.MASTER_DEGREE ? " Créd." : " ECTS";
    }

    public String getPrefix() {
        return getPrefix(I18N.getLocale());
    }

    public String getPrefix(final Locale locale) {
        final StringBuilder result = new StringBuilder();

        switch (this) {
        case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA:
            return result.toString();
        case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
            result.append(BundleUtil.getString(Bundle.ACADEMIC, "degree.DegreeType.prefix.one")).append(" ");
            return result.toString();
        default:
            final String string = BundleUtil.getString(Bundle.ACADEMIC, "degree.DegreeType.prefix.two");
            result.append(string).append(string.isEmpty() ? StringUtils.EMPTY : " ");
            return result.toString();
        }
    }

    @Deprecated
    final public String getSeniorTitle() {
        return getGraduateTitle();
    }

    final public String getGraduateTitle() {
        return getGraduateTitle(I18N.getLocale());
    }

    final public String getGraduateTitle(final Locale locale) {
        return getGraduateTitle((CycleType) null, locale);
    }

    final public String getGraduateTitle(final CycleType cycleType, final Locale locale) {
        if (getQualifiesForGraduateTitle()) {
            if (cycleType == null) {
                return BundleUtil.getString(Bundle.ENUMERATION, qualifiedName() + GRADUATE_TITLE_SUFFIX);
            }

            if (cycleTypes().isEmpty()) {
                throw new DomainException("DegreeType.has.no.cycle.type");
            }

            if (!hasCycleTypes(cycleType)) {
                throw new DomainException("DegreeType.doesnt.have.such.cycle.type");
            }

            return BundleUtil.getString(Bundle.ENUMERATION, qualifiedName() + (isComposite() ? "." + cycleType.name() : StringUtils.EMPTY)
                    + GRADUATE_TITLE_SUFFIX);
        }

        return StringUtils.EMPTY;
    }

    public boolean isFirstCycle() {
        return cycleTypes().contains(CycleType.FIRST_CYCLE);
    }

    public boolean isSecondCycle() {
        return cycleTypes().contains(CycleType.SECOND_CYCLE);
    }

    public boolean isThirdCycle() {
        return cycleTypes().contains(CycleType.THIRD_CYCLE);
    }

    public boolean isSpecializationCycle() {
        return cycleTypes().contains(CycleType.SPECIALIZATION_CYCLE);
    }

    final public boolean hasAnyCycleTypes() {
        return !cycleTypes().isEmpty();
    }

    final public boolean hasCycleTypes(final CycleType cycleType) {
        return cycleTypes().contains(cycleType);
    }

    final public boolean isComposite() {
        return cycleTypes().size() > 1;
    }

    final public boolean hasExactlyOneCycleType() {
        return cycleTypes().size() == 1;
    }

    final public CycleType getCycleType() {
        if (hasExactlyOneCycleType()) {
            return cycleTypes().iterator().next();
        }

        throw new DomainException("DegreeType.has.more.than.one.cycle.type");
    }

    final public boolean isStrictlyFirstCycle() {
        return hasExactlyOneCycleType() && cycleTypes().contains(CycleType.FIRST_CYCLE);
    }

    public CycleType getFirstOrderedCycleType() {
        final TreeSet<CycleType> ordered = getOrderedCycleTypes();
        return ordered.isEmpty() ? null : ordered.first();
    }

    public CycleType getLastOrderedCycleType() {
        final TreeSet<CycleType> ordered = getOrderedCycleTypes();
        return ordered.isEmpty() ? null : ordered.last();
    }

    public TreeSet<CycleType> getOrderedCycleTypes() {
        TreeSet<CycleType> result = new TreeSet<CycleType>(CycleType.COMPARATOR_BY_LESS_WEIGHT);
        result.addAll(cycleTypes());
        return result;
    }

    public boolean hasSeniorEligibility(Registration registration, ExecutionYear executionYear) {
        return false;
    }

    protected boolean hasConditionsToFinishBachelorDegree(final Registration registration, final ExecutionYear executionYear) {
        Double floor = new Double(165.00);
        Double ceiling = new Double(180.00);
        return registration.getStudentCurricularPlan(executionYear).getApprovedEctsCredits(CycleType.FIRST_CYCLE)
                .compareTo(floor) >= 0
                && registration.getStudentCurricularPlan(executionYear).getApprovedEctsCredits(CycleType.FIRST_CYCLE)
                        .compareTo(ceiling) < 0;
    }

    protected boolean hasConditionsToFinishMasterDegree(final Registration registration, final ExecutionYear executionYear) {
        Enrolment dissertationEnrolment = registration.getStudentCurricularPlan(executionYear).getLatestDissertationEnrolment();

        if (dissertationEnrolment == null) {
            return false;
        }

        if (dissertationEnrolment.getExecutionYear() != executionYear && !dissertationEnrolment.isApproved()) {
            return false;
        }

        Double dissContrib = dissertationEnrolment.isApproved() ? 0.0 : dissertationEnrolment.getEctsCredits();
        Double threshold = 120.00 - (15.00 + dissContrib);
        return registration.getStudentCurricularPlan(executionYear).getApprovedEctsCredits(CycleType.SECOND_CYCLE) >= threshold;
    }

    protected boolean isNotEnrolledInFirstCycleOrIsConcluded(final Registration registration, final ExecutionYear executionYear) {
        return registration.getStudentCurricularPlan(executionYear).getFirstCycle() == null
                || registration.getStudentCurricularPlan(executionYear).getFirstCycle().isConcluded();
    }

    static public Set<DegreeType> getDegreeTypesFor(AdministrativeOfficeType administrativeOfficeType) {
        final Set<DegreeType> result = new HashSet<DegreeType>();

        for (final DegreeType degreeType : values()) {
            if (degreeType.getAdministrativeOfficeType() == administrativeOfficeType) {
                result.add(degreeType);
            }
        }

        return result;
    }

}

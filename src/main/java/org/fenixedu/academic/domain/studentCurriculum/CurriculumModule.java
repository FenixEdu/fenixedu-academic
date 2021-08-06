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
package org.fenixedu.academic.domain.studentCurriculum;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.academic.util.predicates.ResultCollection;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

abstract public class CurriculumModule extends CurriculumModule_Base {

    static final public Comparator<CurriculumModule> COMPARATOR_BY_NAME_AND_ID = new Comparator<CurriculumModule>() {
        @Override
        public int compare(CurriculumModule o1, CurriculumModule o2) {
            int result = o1.getName().compareTo(o2.getName());
            return (result == 0) ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : result;
        }
    };

    static final public Comparator<CurriculumModule> COMPARATOR_BY_FULL_PATH_NAME_AND_ID = new Comparator<CurriculumModule>() {
        @Override
        public int compare(CurriculumModule o1, CurriculumModule o2) {
            int result = o1.getFullPath().compareTo(o2.getFullPath());
            return (result == 0) ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : result;
        }
    };

    static final public Comparator<CurriculumModule> COMPARATOR_BY_CREATION_DATE = new Comparator<CurriculumModule>() {

        @Override
        public int compare(CurriculumModule o1, CurriculumModule o2) {
            return o1.getCreationDateDateTime().compareTo(o2.getCreationDateDateTime());
        }
    };

    public CurriculumModule() {
        super();
        setCreationDateDateTime(new DateTime());
        setRootDomainObject(Bennu.getInstance());
    }

    public void deleteRecursive() {
        delete();
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        setDegreeModule(null);
        setCurriculumGroup(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public RootCurriculumGroup getRootCurriculumGroup() {
        return getCurriculumGroup() != null ? getCurriculumGroup().getRootCurriculumGroup() : (RootCurriculumGroup) this;
    }

    public CycleCurriculumGroup getParentCycleCurriculumGroup() {
        return getCurriculumGroup() != null ? getCurriculumGroup().getParentCycleCurriculumGroup() : null;
    }

    public boolean isCycleCurriculumGroup() {
        return false;
    }

    public BranchCurriculumGroup getParentBranchCurriculumGroup() {
        return getCurriculumGroup() != null ? getCurriculumGroup().getParentBranchCurriculumGroup() : null;
    }

    public boolean isBranchCurriculumGroup() {
        return false;
    }

    public boolean isNoCourseGroupCurriculumGroup() {
        return false;
    }

    public boolean isEnrolment() {
        return false;
    }

    public boolean isDismissal() {
        return false;
    }

    public boolean isCreditsDismissal() {
        return false;
    }

    public boolean isCurriculumLine() {
        return isLeaf();
    }

    abstract public boolean isLeaf();

    abstract public boolean isRoot();

    abstract public StringBuilder print(String tabs);

    abstract public List<Enrolment> getEnrolments();

    public abstract StudentCurricularPlan getStudentCurricularPlan();

    final public boolean isBolonhaDegree() {
        return getStudentCurricularPlan().isBolonhaDegree();
    }

    public DegreeCurricularPlan getDegreeCurricularPlanOfStudent() {
        return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public DegreeCurricularPlan getDegreeCurricularPlanOfDegreeModule() {
        if (getDegreeModule() != null) {
            return getDegreeModule().getParentDegreeCurricularPlan();
        }
        return null;
    }

    public LocalizedString getName() {
        LocalizedString LocalizedString = new LocalizedString();

        if (this.getDegreeModule().getName() != null && this.getDegreeModule().getName().length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, this.getDegreeModule().getName());
        }
        if (this.getDegreeModule().getNameEn() != null && this.getDegreeModule().getNameEn().length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, this.getDegreeModule().getNameEn());
        }
        return LocalizedString;
    }

    public LocalizedString getPresentationName() {
        return getName();
    }

    public boolean isApproved(final CurricularCourse curricularCourse) {
        return isApproved(curricularCourse, null);
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return this.getDegreeModule().equals(degreeModule);
    }

    public boolean hasCurriculumModule(final CurriculumModule curriculumModule) {
        return this.equals(curriculumModule);
    }

    public boolean parentCurriculumGroupIsNoCourseGroupCurriculumGroup() {
        return getCurriculumGroup() != null && getCurriculumGroup().isNoCourseGroupCurriculumGroup();
    }

    public boolean parentAllowAccumulatedEctsCredits() {
        return !parentCurriculumGroupIsNoCourseGroupCurriculumGroup()
                || ((NoCourseGroupCurriculumGroup) getCurriculumGroup()).allowAccumulatedEctsCredits();
    }

    public Set<ICurricularRule> getCurricularRules(ExecutionSemester executionSemester) {
        final Set<ICurricularRule> result =
                getCurriculumGroup() != null ? getCurriculumGroup().getCurricularRules(executionSemester) : new HashSet<ICurricularRule>();
        result.addAll(getDegreeModule().getCurricularRules(executionSemester));

        return result;
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
        return getDegreeModule().getMostRecentActiveCurricularRule(ruleType, getCurriculumGroup().getDegreeModule(),
                executionYear);
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType,
            final ExecutionSemester executionSemester) {
        return getDegreeModule().getMostRecentActiveCurricularRule(ruleType, getCurriculumGroup().getDegreeModule(),
                executionSemester);
    }

    public String getFullPath() {
        if (isRoot()) {
            return getName().getContent();
        } else {
            return getCurriculumGroup().getFullPath() + " > " + getName().getContent();
        }
    }

    public boolean isFor(final DegreeCurricularPlan degreeCurricularPlan) {
        return getDegreeModule().getParentDegreeCurricularPlan() == degreeCurricularPlan;
    }

    public boolean isFor(final Registration registration) {
        return getRegistration() == registration;
    }

    final public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

    final public Student getStudent() {
        return getRegistration().getStudent();
    }

    final public Person getPerson() {
        return getStudent().getPerson();
    }

    public boolean isConcluded() {
        return isConcluded(getApprovedCurriculumLinesLastExecutionYear()).value();
    }

    public ExecutionYear getApprovedCurriculumLinesLastExecutionYear() {
        final SortedSet<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);

        for (final CurriculumLine curriculumLine : getApprovedCurriculumLines()) {
            if (curriculumLine.hasExecutionPeriod()) {
                executionYears.add(curriculumLine.getExecutionPeriod().getExecutionYear());
            }
        }

        return executionYears.isEmpty() ? ExecutionYear.readCurrentExecutionYear() : executionYears.last();
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();
        addApprovedCurriculumLines(result);
        return result;
    }

    final public CurriculumLine getLastApprovement() {
        final SortedSet<CurriculumLine> curriculumLines =
                new TreeSet<CurriculumLine>(CurriculumLine.COMPARATOR_BY_APPROVEMENT_DATE_AND_ID);
        curriculumLines.addAll(getApprovedCurriculumLines());

        if (curriculumLines.isEmpty()) {
            throw new DomainException("error.curriculum.group.has.no.approved.curriculum.lines", getName().getContent());
        }

        return curriculumLines.last();
    }

    final public YearMonthDay getLastApprovementDate() {
        return getLastApprovement().getApprovementDate();
    }

    final public ExecutionYear getLastApprovementExecutionYear() {
        return getLastApprovement().getExecutionYear();
    }

    final protected boolean wasCreated(final DateTime when) {
        return getCreationDateDateTime() == null || getCreationDateDateTime().isBefore(when);
    }

    final public Curriculum getCurriculum() {
        return getCurriculum(new DateTime(), (ExecutionYear) null);
    }

    final public Curriculum getCurriculum(final DateTime when) {
        return wasCreated(when) ? getCurriculum(when, (ExecutionYear) null) : Curriculum.createEmpty(this, (ExecutionYear) null);
    }

    final public Curriculum getCurriculum(final ExecutionYear executionYear) {
        return getCurriculum(new DateTime(), executionYear);
    }

    public Grade calculateRawGrade() {
        return getCurriculum().getRawGrade();
    }

    public Grade calculateFinalGrade() {
        return getCurriculum().getFinalGrade();
    }

    public Double getCreditsConcluded() {
        return getCreditsConcluded(getApprovedCurriculumLinesLastExecutionYear());
    }

    abstract public Double getEctsCredits();

    abstract public Double getAprovedEctsCredits();

    abstract public Double getEnroledEctsCredits(final ExecutionSemester executionSemester);

    abstract public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester);

    abstract public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester);

    abstract public boolean hasAnyEnrolments();

    abstract public void addApprovedCurriculumLines(final Collection<CurriculumLine> result);

    abstract public boolean hasAnyApprovedCurriculumLines();

    abstract public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester);

    abstract public ExecutionYear getIEnrolmentsLastExecutionYear();

    abstract public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester);

    abstract public Set getDegreeModulesToEvaluate(final ExecutionSemester executionSemester);

    abstract public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse);

    abstract public CurriculumLine getApprovedCurriculumLine(final CurricularCourse curricularCourse);

    abstract public Dismissal getDismissal(final CurricularCourse curricularCourse);

    abstract public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear);

    abstract public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionSemester executionSemester);

    abstract public Collection<Enrolment> getExtraordinarySeasonEnrolments(ExecutionYear executionYear);

    abstract public Collection<Enrolment> getExtraordinarySeasonEnrolments(ExecutionSemester executionSemester);

    abstract public void collectDismissals(final List<Dismissal> result);

    abstract public void getAllDegreeModules(Collection<DegreeModule> degreeModules);

    abstract public Set<CurriculumLine> getAllCurriculumLines();

	abstract public Stream<CurriculumLine> getCurriculumLineStream();

    abstract public ConclusionValue isConcluded(ExecutionYear executionYear);

    abstract public boolean hasConcluded(DegreeModule degreeModule, ExecutionYear executionYear);

    public abstract YearMonthDay calculateConclusionDate();

    abstract public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear);

    abstract public Double getCreditsConcluded(ExecutionYear executionYear);

    abstract public boolean isPropaedeutic();

    abstract public boolean hasEnrolment(ExecutionYear executionYear);

    abstract public boolean hasEnrolment(ExecutionSemester executionSemester);

    abstract public boolean isEnroledInSpecialSeason(final ExecutionSemester executionSemester);

    abstract public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear);

    /**
     * This enum represent possible conclusion values when checking registration
     * processed - UNKNOWN: is used when some group doesn't have information to
     * calculate it's value, for instance, doesn't have any curricular rules
     * 
     */
    static public enum ConclusionValue {
        CONCLUDED(true) {
            @Override
            public boolean isValid() {
                return true;
            }
        },

        NOT_CONCLUDED(false) {
            @Override
            public boolean isValid() {
                return false;
            }
        },

        UNKNOWN(false) {
            @Override
            public boolean isValid() {
                return true;
            }
        };

        private boolean value;

        private ConclusionValue(final boolean value) {
            this.value = value;
        }

        public boolean value() {
            return this.value;
        }

        abstract public boolean isValid();

        static public ConclusionValue create(final boolean value) {
            return value ? CONCLUDED : NOT_CONCLUDED;
        }

        public String getLocalizedName() {
            return BundleUtil.getString(Bundle.ENUMERATION, ConclusionValue.class.getSimpleName() + "." + name());
        }
    }

    abstract public int getNumberOfAllApprovedEnrolments(final ExecutionSemester executionSemester);

    abstract public void getCurriculumModules(final ResultCollection<CurriculumModule> collection);

    public boolean hasAnyCurriculumModules(final Predicate<CurriculumModule> predicate) {
        return predicate.test(this);
    }

    public boolean hasAnyCurriculumLines() {
        return hasAnyCurriculumModules(new CurriculumModulePredicateByType(CurriculumLine.class));
    }

    abstract public Set<CurriculumGroup> getAllCurriculumGroups();

    abstract public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups();

    static public class CurriculumModulePredicateByType implements Predicate<CurriculumModule> {

        private final Class<? extends CurriculumModule> clazz;

        public CurriculumModulePredicateByType(final Class<? extends CurriculumModule> clazz) {
            this.clazz = clazz;
        }

        @Override
        public boolean test(final CurriculumModule curriculumModule) {
            if (clazz.isAssignableFrom(curriculumModule.getClass())) {
                return true;
            }

            return false;
        }

    }

    static public class CurriculumModulePredicateByExecutionSemester implements Predicate<CurriculumModule> {

        private final ExecutionSemester executionSemester;

        public CurriculumModulePredicateByExecutionSemester(final ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
        }

        @Override
        public boolean test(final CurriculumModule curriculumModule) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.getExecutionPeriod().equals(executionSemester)) {
                    return true;
                }
            }

            return false;
        }

    }

    static public class CurriculumModulePredicateByExecutionYear implements Predicate<CurriculumModule> {

        private final ExecutionYear executionYear;

        public CurriculumModulePredicateByExecutionYear(final ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }

        @Override
        public boolean test(final CurriculumModule curriculumModule) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.getExecutionYear().equals(executionYear)) {
                    return true;
                }
            }

            return false;
        }

    }

    static public class CurriculumModulePredicateByApproval implements Predicate<CurriculumModule> {

        @Override
        public boolean test(final CurriculumModule curriculumModule) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isApproved()) {
                    return true;
                }
            }

            return false;
        }

    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.DateTime dt = getCreationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreationDate(java.util.Date date) {
        if (date == null) {
            setCreationDateDateTime(null);
        } else {
            setCreationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}

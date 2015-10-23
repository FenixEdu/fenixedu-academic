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

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevel;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoad;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.CompetenceCoursePredicates;
import org.fenixedu.academic.util.MultiLanguageString;
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

    public CompetenceCourse(String code, String name, Collection<Department> departments) {
        this();
        super.setCurricularStage(CurricularStage.OLD);
        fillFields(code, name);
        if (departments != null) {
            addDepartments(departments);
        }
    }

    public CompetenceCourse(String name, String nameEn, Boolean basic, RegimeType regimeType,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, CurricularStage curricularStage,
            CompetenceCourseGroupUnit unit, ExecutionSemester startSemester) {

        this();
        super.setCurricularStage(curricularStage);
        setType(type);

        CompetenceCourseInformation competenceCourseInformation =
                new CompetenceCourseInformation(name.trim(), nameEn.trim(), basic, regimeType, competenceCourseLevel,
                        startSemester, unit);
        super.addCompetenceCourseInformations(competenceCourseInformation);

        // unique acronym creation
        try {
            final UniqueAcronymCreator<CompetenceCourse> uniqueAcronymCreator =
                    new UniqueAcronymCreator<CompetenceCourse>(CompetenceCourse::getName, CompetenceCourse::getAcronym,
                            (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses());
            competenceCourseInformation.setAcronym(uniqueAcronymCreator.create(this).getLeft());
        } catch (Exception e) {
            throw new DomainException("competence.course.unable.to.create.acronym");
        }
    }

    public CompetenceCourse(String name, String nameEn, Boolean basic, RegimeType regimeType,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, CurricularStage curricularStage,
            CompetenceCourseGroupUnit unit) {

        this(name, nameEn, basic, regimeType, competenceCourseLevel, type, curricularStage, unit, ExecutionSemester
                .readActualExecutionSemester());
    }

    public boolean isBolonha() {
        return !getCurricularStage().equals(CurricularStage.OLD);
    }

    public void addCompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {
        checkIfCanEdit(false);
        getMostRecentCompetenceCourseInformation().addCompetenceCourseLoads(
                new CompetenceCourseLoad(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                        trainingPeriodHours, tutorialOrientationHours, autonomousWorkHours, ectsCredits, order, academicPeriod));
    }

    public BibliographicReference getBibliographicReference(Integer oid) {
        return getMostRecentCompetenceCourseInformation().getBibliographicReferences().getBibliographicReference(oid);
    }

    public BibliographicReferences getBibliographicReferences() {
        return getBibliographicReferences(null);
    }

    public BibliographicReferences getBibliographicReferences(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getBibliographicReferences() : null;
    }

    public List<BibliographicReferences.BibliographicReference> getMainBibliographicReferences() {
        return getMainBibliographicReferences(null);
    }

    public List<BibliographicReferences.BibliographicReference> getMainBibliographicReferences(final ExecutionSemester period) {
        return this.getBibliographicReferences(period).getMainBibliographicReferences();
    }

    public List<BibliographicReferences.BibliographicReference> getSecondaryBibliographicReferences() {
        return getSecondaryBibliographicReferences(null);
    }

    public List<BibliographicReferences.BibliographicReference> getSecondaryBibliographicReferences(final ExecutionSemester period) {
        return this.getBibliographicReferences(period).getSecondaryBibliographicReferences();
    }

    public List<BibliographicReferences.BibliographicReference> getAllBibliographicReferences(
            final ExecutionSemester executionSemester) {
        final List<BibliographicReferences.BibliographicReference> result =
                new ArrayList<BibliographicReferences.BibliographicReference>();
        result.addAll(getMainBibliographicReferences(executionSemester));
        result.addAll(getSecondaryBibliographicReferences(executionSemester));
        return result;
    }

    public void createBibliographicReference(String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type) {
        checkIfCanEdit(false);
        CompetenceCourseInformation info = getMostRecentCompetenceCourseInformation();
        info.setBibliographicReferences(info.getBibliographicReferences().with(year, title, authors, reference, url, type));
    }

    public void editBibliographicReference(Integer index, String year, String title, String authors, String reference,
            String url, BibliographicReferenceType type) {
        CompetenceCourseInformation info = getMostRecentCompetenceCourseInformation();
        info.setBibliographicReferences(info.getBibliographicReferences().replacing(index, year, title, authors, reference, url,
                type));
    }

    public void deleteBibliographicReference(Integer index) {
        CompetenceCourseInformation info = getMostRecentCompetenceCourseInformation();
        info.setBibliographicReferences(info.getBibliographicReferences().without(index));
    }

    public void switchBibliographicReferencePosition(Integer oldPosition, Integer newPosition) {
        CompetenceCourseInformation info = getMostRecentCompetenceCourseInformation();
        info.setBibliographicReferences(info.getBibliographicReferences().movingBibliographicReference(oldPosition, newPosition));
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
        for (final Department department : this.getDepartmentsSet()) {
            if (!departments.contains(department)) {
                super.removeDepartments(department);
            }
        }
        for (final Department department : departments) {
            if (!getDepartmentsSet().contains(department)) {
                super.addDepartments(department);
            }
        }
    }

    public void edit(String name, String nameEn, Boolean basic, CompetenceCourseLevel competenceCourseLevel,
            CompetenceCourseType type, CurricularStage curricularStage) {
        changeCurricularStage(curricularStage);
        setType(type);

        getMostRecentCompetenceCourseInformation().edit(name.trim(), nameEn.trim(), basic, competenceCourseLevel);

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
        getMostRecentCompetenceCourseInformation().setAcronym(acronym);
    }

    public void editAcronym(String acronym) {
        Set<CompetenceCourse> bolonhaCompetenceCourses = (Set<CompetenceCourse>) CompetenceCourse.readBolonhaCompetenceCourses();
        for (final CompetenceCourse competenceCourse : bolonhaCompetenceCourses) {
            if (!competenceCourse.equals(this) && competenceCourse.getAcronym().equalsIgnoreCase(acronym.trim())) {
                throw new DomainException("competenceCourse.existing.acronym", competenceCourse.getName(), competenceCourse
                        .getDepartmentUnit().getDepartment().getRealName());
            }
        }

        getMostRecentCompetenceCourseInformation().setAcronym(acronym);
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
        getMostRecentCompetenceCourseInformation().edit(objectives, program, evaluationMethod, objectivesEn, programEn,
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
        TreeSet<CompetenceCourseInformation> informations =
                new TreeSet<CompetenceCourseInformation>(CompetenceCourseInformation.COMPARATORY_BY_EXECUTION_PERIOD);
        informations.addAll(getCompetenceCourseInformationsSet());
        return informations;
    }

    private CompetenceCourseInformation getMostRecentCompetenceCourseInformation() {
        return getMostRecentCompetenceCourseInformationUntil(ExecutionSemester.readActualExecutionSemester());
    }

    private CompetenceCourseInformation getMostRecentCompetenceCourseInformationUntil(ExecutionSemester semester) {
        CompetenceCourseInformation mostRecentInformation = getOldestCompetenceCourseInformation();
        for (CompetenceCourseInformation information : getCompetenceCourseInformationsSet()) {
            if (information.getExecutionPeriod().isAfter(mostRecentInformation.getExecutionPeriod())
                    && !information.getExecutionPeriod().isAfter(semester)) {
                mostRecentInformation = information;
            }
        }
        return mostRecentInformation;
    }

    private CompetenceCourseInformation getOldestCompetenceCourseInformation() {
        final Set<CompetenceCourseInformation> competenceCourseInformations = getCompetenceCourseInformationsSet();
        if (competenceCourseInformations.isEmpty()) {
            return null;
        }
        return Collections.min(competenceCourseInformations, CompetenceCourseInformation.COMPARATORY_BY_EXECUTION_PERIOD);
    }

    public boolean isCompetenceCourseInformationDefinedAtExecutionPeriod(final ExecutionSemester executionSemester) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(executionSemester);
        return information != null && information.getExecutionPeriod().equals(executionSemester);
    }

    public boolean isLoggedPersonAllowedToView() {
        Person person = AccessControl.getPerson();
        if (isApproved()) {
            return true;
        }
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        return getDepartmentUnit().getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person);
    }

    public boolean isLoggedPersonAllowedToViewChangeRequests() {
        Person person = AccessControl.getPerson();
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        for (CompetenceCourseInformation information : getCompetenceCourseInformationsSet()) {
            if (information.getDepartmentUnit().getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLoggedPersonAllowedToCreateChangeRequests(ExecutionSemester semester) {
        Person person = AccessControl.getPerson();
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        return getDepartmentUnit(semester).getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person);
    }

    public boolean isRequestDraftAvailable(ExecutionSemester semester) {
        return getChangeRequestDraft(semester) != null;
    }

    public CompetenceCourseInformation findCompetenceCourseInformationForExecutionPeriod(final ExecutionSemester executionSemester) {
        if (!isBolonha()) {
            return null;
        }

        if (executionSemester == null) {
            return getMostRecentCompetenceCourseInformation();
        }

        if (executionSemester.isBefore(getOldestCompetenceCourseInformation().getExecutionPeriod())) {
            return null;
        }

        CompetenceCourseInformation minCompetenceCourseInformation = null;
        for (final CompetenceCourseInformation competenceCourseInformation : getOrderedCompetenceCourseInformations()) {
            if (competenceCourseInformation.getExecutionPeriod().isAfter(executionSemester)) {
                return minCompetenceCourseInformation;
            } else {
                minCompetenceCourseInformation = competenceCourseInformation;
            }
        }

        return minCompetenceCourseInformation;
    }

    public CompetenceCourseInformation findCompetenceCourseInformationForExecutionYear(final ExecutionYear executionYear) {
        if (!isBolonha()) {
            return null;
        }

        if (executionYear == null) {
            return getMostRecentCompetenceCourseInformation();
        }

        if (executionYear.isBefore(getOldestCompetenceCourseInformation().getExecutionYear())) {
            return null;
        }

        CompetenceCourseInformation minCompetenceCourseInformation = null;
        for (final CompetenceCourseInformation competenceCourseInformation : getOrderedCompetenceCourseInformations()) {
            if (competenceCourseInformation.getExecutionYear().isBeforeOrEquals(executionYear)) {
                minCompetenceCourseInformation = competenceCourseInformation;
            } else {
                return minCompetenceCourseInformation;
            }
        }

        return minCompetenceCourseInformation;
    }

    public String getName(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        if ((super.getName() == null || super.getName().length() == 0) && information != null) {
            return information.getName();
        }
        return super.getName();
    }

    @Override
    public String getName() {
        return getName(null);
    }

    public String getNameEn(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getNameEn() : null;
    }

    public String getNameEn() {
        return getNameEn(null);
    }

    public String getAcronym(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getAcronym() : null;
    }

    public String getAcronym() {
        return getAcronym(null);
    }

    public void setAcronym(String acronym) {
        getMostRecentCompetenceCourseInformation().setAcronym(acronym);
    }

    public boolean isBasic(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getBasic() : null;
    }

    public boolean isBasic() {
        return isBasic(null);
    }

    public RegimeType getRegime(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getRegime() : null;
    }

    public RegimeType getRegime(final ExecutionYear executionYear) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionYear(executionYear);
        return information != null ? information.getRegime() : null;
    }

    public RegimeType getRegime() {
        return getRegime((ExecutionSemester) null);
    }

    public void setRegime(RegimeType regimeType) {
        getMostRecentCompetenceCourseInformation().setRegime(regimeType);
    }

    public CompetenceCourseLevel getCompetenceCourseLevel(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getCompetenceCourseLevel() : null;
    }

    public CompetenceCourseLevel getCompetenceCourseLevel() {
        return getCompetenceCourseLevel(null);
    }

    public Collection<CompetenceCourseLoad> getCompetenceCourseLoads(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getCompetenceCourseLoadsSet() : null;
    }

    public Collection<CompetenceCourseLoad> getCompetenceCourseLoads() {
        return getCompetenceCourseLoads(null);
    }

    public int getCompetenceCourseLoadsCount(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getCompetenceCourseLoadsSet().size() : 0;
    }

    public int getCompetenceCourseLoadsCount() {
        return getCompetenceCourseLoadsCount(null);
    }

    public boolean hasCompetenceCourseInformationFor(ExecutionSemester semester) {
        for (CompetenceCourseInformation competenceInfo : getCompetenceCourseInformationsSet()) {
            if (competenceInfo.getExecutionPeriod().equals(semester)) {
                return true;
            }
        }
        return false;
    }

    public String getObjectives(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getObjectives() : null;
    }

    public String getObjectives() {
        return getObjectives(null);
    }

    public String getProgram(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getProgram() : null;
    }

    public String getProgram() {
        return getProgram(null);
    }

    public LocalizedString getLocalizedEvaluationMethod(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information == null ? null : new LocalizedString(Locale.getDefault(), information.getEvaluationMethod()).with(
                Locale.ENGLISH, information.getEvaluationMethodEn());
    }

    public String getEvaluationMethod(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getEvaluationMethod() : null;
    }

    public String getEvaluationMethod() {
        return getEvaluationMethod(null);
    }

    public String getObjectivesEn(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getObjectivesEn() : null;
    }

    public String getObjectivesEn() {
        return getObjectivesEn(null);
    }

    public String getProgramEn(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getProgramEn() : null;
    }

    public String getProgramEn() {
        return getProgramEn(null);
    }

    public String getEvaluationMethodEn(final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return information != null ? information.getEvaluationMethodEn() : null;
    }

    public String getEvaluationMethodEn() {
        return getEvaluationMethodEn(null);
    }

    public double getTheoreticalHours() {
        return getTheoreticalHours((Integer) null, (ExecutionSemester) null);
    }

    public Double getTheoreticalHours(final Integer order) {
        return getTheoreticalHours(order, (ExecutionSemester) null);
    }

    public double getTheoreticalHours(final ExecutionSemester period) {
        return getTheoreticalHours(null, period);
    }

    public double getTheoreticalHours(Integer order, ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getTheoreticalHours(order) : 0.0;
    }

    public double getProblemsHours() {
        return getProblemsHours((Integer) null, (ExecutionSemester) null);
    }

    public Double getProblemsHours(final Integer order) {
        return getProblemsHours(order, (ExecutionSemester) null);
    }

    public double getProblemsHours(final ExecutionSemester period) {
        return getProblemsHours(null, period);
    }

    public double getProblemsHours(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getProblemsHours(order) : 0.0;
    }

    public double getLaboratorialHours() {
        return getLaboratorialHours((Integer) null, (ExecutionSemester) null);
    }

    public Double getLaboratorialHours(final Integer order) {
        return getLaboratorialHours(order, (ExecutionSemester) null);
    }

    public double getLaboratorialHours(final ExecutionSemester period) {
        return getLaboratorialHours(null, period);
    }

    public double getLaboratorialHours(Integer order, ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getLaboratorialHours(order) : 0.0;
    }

    public double getSeminaryHours() {
        return getSeminaryHours((Integer) null, (ExecutionSemester) null);
    }

    public double getSeminaryHours(final ExecutionSemester period) {
        return getSeminaryHours(null, period);
    }

    public Double getSeminaryHours(final Integer order) {
        return getSeminaryHours(order, (ExecutionSemester) null);
    }

    public double getSeminaryHours(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getSeminaryHours(order) : 0.0;
    }

    public double getFieldWorkHours() {
        return getFieldWorkHours((Integer) null, (ExecutionSemester) null);
    }

    public double getFieldWorkHours(final ExecutionSemester period) {
        return getFieldWorkHours(null, period);
    }

    public Double getFieldWorkHours(final Integer order) {
        return getFieldWorkHours(order, (ExecutionSemester) null);
    }

    public double getFieldWorkHours(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getFieldWorkHours(order) : 0.0;
    }

    public double getTrainingPeriodHours() {
        return getTrainingPeriodHours((Integer) null, (ExecutionSemester) null);
    }

    public double getTrainingPeriodHours(final ExecutionSemester period) {
        return getTrainingPeriodHours(null, period);
    }

    public Double getTrainingPeriodHours(final Integer order) {
        return getTrainingPeriodHours(order, (ExecutionSemester) null);
    }

    public double getTrainingPeriodHours(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getTrainingPeriodHours(order) : 0.0;
    }

    public double getTutorialOrientationHours() {
        return getTutorialOrientationHours((Integer) null, (ExecutionSemester) null);
    }

    public Double getTutorialOrientationHours(final Integer order) {
        return getTutorialOrientationHours(order, (ExecutionSemester) null);
    }

    public double getTutorialOrientationHours(final ExecutionSemester period) {
        return getTutorialOrientationHours(null, period);
    }

    public double getTutorialOrientationHours(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getTutorialOrientationHours(order) : 0.0;
    }

    public double getAutonomousWorkHours() {
        return getAutonomousWorkHours((Integer) null, (ExecutionSemester) null);
    }

    public double getAutonomousWorkHours(final ExecutionSemester period) {
        return getAutonomousWorkHours((Integer) null, period);
    }

    public Double getAutonomousWorkHours(final Integer order) {
        return getAutonomousWorkHours(order, (ExecutionSemester) null);
    }

    final public Double getAutonomousWorkHours(final Integer order, final ExecutionYear year) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionYear(year);
        return (information != null) ? information.getAutonomousWorkHours(order) : 0.0;
    }

    public Double getAutonomousWorkHours(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getAutonomousWorkHours(order) : 0.0;
    }

    public double getContactLoad() {
        return getContactLoad((Integer) null, (ExecutionSemester) null);
    }

    public Double getContactLoad(final ExecutionSemester period) {
        return getContactLoad(null, period);
    }

    public Double getContactLoad(final Integer order) {
        return getContactLoad(order, (ExecutionSemester) null);
    }

    final public Double getContactLoad(final Integer order, final ExecutionYear executionYear) {
        return getContactLoad(order, executionYear == null ? null : executionYear.getFirstExecutionPeriod());
    }

    public Double getContactLoad(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getContactLoad(order) : 0.0;
    }

    public double getTotalLoad() {
        return getTotalLoad((Integer) null, (ExecutionSemester) null);
    }

    public Double getTotalLoad(final Integer order) {
        return getTotalLoad(order, (ExecutionSemester) null);
    }

    public double getTotalLoad(final ExecutionSemester period) {
        return getTotalLoad((Integer) null, period);
    }

    final public Double getTotalLoad(final Integer order, final ExecutionYear executionYear) {
        return getTotalLoad(order, executionYear == null ? null : executionYear.getFirstExecutionPeriod());
    }

    public Double getTotalLoad(final Integer order, final ExecutionSemester period) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(period);
        return (information != null) ? information.getTotalLoad(order) : 0.0;
    }

    public double getEctsCredits() {
        return getEctsCredits((Integer) null, (ExecutionSemester) null);
    }

    public double getEctsCredits(final Integer order) {
        return getEctsCredits(order, (ExecutionSemester) null);
    }

    public double getEctsCredits(final ExecutionSemester executionSemester) {
        return getEctsCredits((Integer) null, executionSemester);
    }

    final public Double getEctsCredits(final Integer order, final ExecutionYear executionYear) {
        return getEctsCredits(order, executionYear == null ? null : executionYear.getFirstExecutionPeriod());
    }

    public Double getEctsCredits(final Integer order, final ExecutionSemester executionSemester) {
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionPeriod(executionSemester);
        return (information != null) ? information.getEctsCredits(order) : 0.0;
    }

    public Map<Degree, List<CurricularCourse>> getAssociatedCurricularCoursesGroupedByDegree() {
        Map<Degree, List<CurricularCourse>> curricularCoursesMap = new HashMap<Degree, List<CurricularCourse>>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
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
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
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
    public List<CurricularCourse> getCurricularCoursesWithActiveScopesInExecutionPeriod(final ExecutionSemester executionSemester) {
        return (List<CurricularCourse>) CollectionUtils.select(getAssociatedCurricularCoursesSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                CurricularCourse curricularCourse = (CurricularCourse) arg0;

                for (DegreeModuleScope moduleScope : curricularCourse.getDegreeModuleScopes()) {
                    if (moduleScope.isActiveForExecutionPeriod(executionSemester)) {
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
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            results.addAll(curricularCourse.getActiveEnrollments(executionYear));
        }
        return results;
    }

    public List<Enrolment> getActiveEnrollments(ExecutionSemester executionSemester) {
        List<Enrolment> results = new ArrayList<Enrolment>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            curricularCourse.addActiveEnrollments(results, executionSemester);
        }
        return results;
    }

    public Boolean hasActiveScopesInExecutionYear(ExecutionYear executionYear) {
        Collection<ExecutionSemester> executionSemesters = executionYear.getExecutionPeriodsSet();
        Collection<CurricularCourse> curricularCourses = this.getAssociatedCurricularCoursesSet();
        for (ExecutionSemester executionSemester : executionSemesters) {
            for (CurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getActiveDegreeModuleScopesInAcademicInterval(executionSemester.getAcademicInterval())
                        .size() > 0) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public boolean hasActiveScopesInExecutionPeriod(ExecutionSemester executionSemester) {
        Collection<CurricularCourse> curricularCourses = this.getAssociatedCurricularCoursesSet();
        for (CurricularCourse curricularCourse : curricularCourses) {
            if (curricularCourse.getActiveDegreeModuleScopesInAcademicInterval(executionSemester.getAcademicInterval()).size() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDepartmentUnit() {
        return getDepartmentUnit() != null;
    }

    /**
     * @see #getDeparmentUnit(ExecutionYear)
     */
    public DepartmentUnit getDepartmentUnit() {
        return getDepartmentUnit(ExecutionSemester.readActualExecutionSemester());
    }

    /**
     * @see #getDeparmentUnit(ExecutionSemester)
     */
    public DepartmentUnit getDepartmentUnit(ExecutionYear executionYear) {
        ExecutionSemester semester = ExecutionSemester.readBySemesterAndExecutionYear(2, executionYear.getYear());
        return getDepartmentUnit(semester);
    }

    /**
     * In an ExecutionSemester the CompetenceCourse belongs to a Department.
     * This association is built by CompetenceCourseGroupUnit which aggregates
     * versions of CompetenceCourses (CompetenceCourseInformation). We can see
     * CompetenceCourseGroupUnit like a bag of CompetenceCourses beloging to a
     * Department.
     * 
     * The association between a CompetenceCourse and the ExecutionSemester is
     * represented by CompetenceCourseInformation. We can see
     * CompetenceCourseInformation as a version of CompetenceCourse's
     * attributes.
     * 
     * ExecutionSemester assumes the role of start period of this version
     * 
     * @see CompetenceCourseInformation
     * @see CompetenceCourseGroupUnit
     */
    public DepartmentUnit getDepartmentUnit(ExecutionSemester semester) {
        return getMostRecentCompetenceCourseInformationUntil(semester).getDepartmentUnit();
    }

    /**
     * @see #getDeparmentUnit(ExecutionSemester)
     */
    public CompetenceCourseGroupUnit getCompetenceCourseGroupUnit() {
        return getCompetenceCourseGroupUnit(ExecutionSemester.readActualExecutionSemester());
    }

    /**
     * @see #getDeparmentUnit(ExecutionSemester)
     */
    public CompetenceCourseGroupUnit getCompetenceCourseGroupUnit(ExecutionYear executionYear) {
        ExecutionSemester semester = ExecutionSemester.readBySemesterAndExecutionYear(2, executionYear.getYear());
        return getCompetenceCourseGroupUnit(semester);
    }

    public CompetenceCourseGroupUnit getCompetenceCourseGroupUnit(ExecutionSemester semester) {
        return getMostRecentCompetenceCourseInformationUntil(semester).getCompetenceCourseGroupUnit();
    }

    public CompetenceCourseGroupUnit getMostRecentGroupInDepartment(DepartmentUnit departmentUnit) {
        ExecutionSemester semester = ExecutionSemester.readActualExecutionSemester();
        while (semester != null) {
            if (getDepartmentUnit(semester) == departmentUnit) {
                return getCompetenceCourseGroupUnit(semester);
            }
            semester = semester.getPreviousExecutionPeriod();
        }
        return null;
    }

    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads(final ExecutionSemester period) {
        final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>(getCompetenceCourseLoadsCount(period));
        result.addAll(getCompetenceCourseLoads(period));
        Collections.sort(result);
        return result;
    }

    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads() {
        final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>(getCompetenceCourseLoadsCount());
        result.addAll(getCompetenceCourseLoads());
        Collections.sort(result);
        return result;
    }

    @Override
    public void addCompetenceCourseInformations(CompetenceCourseInformation competenceCourseInformations) {
        check(this, CompetenceCoursePredicates.writePredicate);
        super.addCompetenceCourseInformations(competenceCourseInformations);
    }

    @Override
    public void addDepartments(Department departments) {
        check(this, CompetenceCoursePredicates.writePredicate);
        super.addDepartments(departments);
    }

    @Override
    public void removeCompetenceCourseInformations(CompetenceCourseInformation competenceCourseInformations) {
        check(this, CompetenceCoursePredicates.writePredicate);
        super.removeCompetenceCourseInformations(competenceCourseInformations);
    }

    @Override
    public void removeDepartments(Department departments) {
        check(this, CompetenceCoursePredicates.writePredicate);
        super.removeDepartments(departments);
    }

    @Override
    public void setCode(String code) {
        check(this, CompetenceCoursePredicates.writePredicate);
        final CompetenceCourse existing = CompetenceCourse.find(code);

        if (existing != null && existing != this) {
            throw new DomainException("error.CompetenceCourse.found.duplicate");
        }

        super.setCode(code);
    }

    @Override
    public void setCurricularStage(CurricularStage curricularStage) {
        check(this, CompetenceCoursePredicates.editCurricularStagePredicate);
        if (!this.getAssociatedCurricularCoursesSet().isEmpty() && curricularStage.equals(CurricularStage.DRAFT)) {
            throw new DomainException("competenceCourse.has.already.associated.curricular.courses");
        }
        super.setCurricularStage(curricularStage);
    }

    @Override
    public void setName(String name) {
        check(this, CompetenceCoursePredicates.writePredicate);
        super.setName(name);
    }

    @Override
    public void addAssociatedCurricularCourses(CurricularCourse associatedCurricularCourses) {
        check(this, CompetenceCoursePredicates.writePredicate);
        super.addAssociatedCurricularCourses(associatedCurricularCourses);
    }

    public ScientificAreaUnit getScientificAreaUnit() {
        return getScientificAreaUnit(ExecutionSemester.readActualExecutionSemester());
    }

    public ScientificAreaUnit getScientificAreaUnit(ExecutionYear executionYear) {
        ExecutionSemester semester = ExecutionSemester.readBySemesterAndExecutionYear(2, executionYear.getYear());
        return getScientificAreaUnit(semester);
    }

    public ScientificAreaUnit getScientificAreaUnit(ExecutionSemester semester) {
        CompetenceCourseInformation mostRecentCompetenceCourseInformationUntil =
                getMostRecentCompetenceCourseInformationUntil(semester);
        return mostRecentCompetenceCourseInformationUntil != null ? mostRecentCompetenceCourseInformationUntil
                .getScientificAreaUnit() : null;
    }

    public boolean isAnual() {
        return getRegime() == RegimeType.ANUAL;
    }

    public boolean isAnual(final ExecutionYear executionYear) {
        return getRegime(executionYear) == RegimeType.ANUAL;
    }

    public boolean isSemestrial(final ExecutionYear executionYear) {
        return getRegime(executionYear) == RegimeType.SEMESTRIAL;
    }

    public boolean isApproved() {
        return getCurricularStage() == CurricularStage.APPROVED;
    }

    public void transfer(CompetenceCourseGroupUnit competenceCourseGroupUnit, ExecutionSemester period, String justification,
            Person requester) {

        CompetenceCourseInformation information = null;
        for (CompetenceCourseInformation existingInformation : getCompetenceCourseInformationsSet()) {
            if (existingInformation.getExecutionPeriod() == period) {
                information = existingInformation;
            }
        }
        if (information == null) {
            CompetenceCourseInformation latestInformation = getMostRecentCompetenceCourseInformationUntil(period);
            information = new CompetenceCourseInformation(latestInformation);
            information.setExecutionPeriod(period);
        }

        CompetenceCourseInformationChangeRequest changeRequest =
                new CompetenceCourseInformationChangeRequest(information, justification, requester);
        changeRequest.setCompetenceCourseGroupUnit(competenceCourseGroupUnit);
        changeRequest.approve(requester);
    }

    public MultiLanguageString getNameI18N() {
        return getNameI18N(null);
    }

    public MultiLanguageString getNameI18N(ExecutionSemester semester) {
        MultiLanguageString multiLanguageString = new MultiLanguageString();
        String name = getName(semester);
        if (name != null && name.length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.pt, name);
        }
        String nameEn = getNameEn(semester);
        if (nameEn != null && nameEn.length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.en, nameEn);
        }
        return multiLanguageString;
    }

    public MultiLanguageString getObjectivesI18N() {
        return getObjectivesI18N(null);
    }

    public MultiLanguageString getObjectivesI18N(ExecutionSemester semester) {
        MultiLanguageString multiLanguageString = new MultiLanguageString();
        String objectives = getObjectives(semester);
        if (objectives != null && objectives.length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.pt, objectives);
        }
        String objectivesEn = getObjectivesEn(semester);
        if (objectivesEn != null && objectivesEn.length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.en, objectivesEn);
        }
        return multiLanguageString;
    }

    public MultiLanguageString getProgramI18N() {
        return getProgramI18N(null);
    }

    public MultiLanguageString getProgramI18N(ExecutionSemester semester) {
        MultiLanguageString multiLanguageString = new MultiLanguageString();
        String program = getProgram(semester);
        if (program != null && program.length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.pt, program);
        }
        String programEn = getProgramEn(semester);
        if (programEn != null && programEn.length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.en, programEn);
        }
        return multiLanguageString;
    }

    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionSemester executionSemester) {
        List<ExecutionCourse> executionCourseList = new ArrayList<ExecutionCourse>();
        executionCourseList.addAll(getExecutionCoursesByExecutionPeriod(executionSemester, new HashSet<ExecutionCourse>()));
        return executionCourseList;
    }

    public Set<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionSemester executionSemester,
            final Set<ExecutionCourse> resultSet) {

        List<CurricularCourse> curricularCourseList = getCurricularCoursesWithActiveScopesInExecutionPeriod(executionSemester);

        for (CurricularCourse curricularCourse : curricularCourseList) {
            resultSet.addAll(curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester));
        }

        return resultSet;
    }

    public boolean hasEnrolmentForPeriod(ExecutionSemester executionSemester) {
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.hasEnrolmentForPeriod(executionSemester)) {
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
        for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequestsSet()) {
            if (request.getApproved() == null) {
                count++;
            }
        }
        return count;
    }

    public Set<CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequests(
            final ExecutionSemester semester) {
        Set<CompetenceCourseInformationChangeRequest> changeRequests = new HashSet<CompetenceCourseInformationChangeRequest>();
        for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequestsSet()) {
            if (request.getExecutionPeriod() == semester) {
                changeRequests.add(request);
            }
        }
        return changeRequests;
    }

    public CompetenceCourseInformationChangeRequest getChangeRequestDraft(final ExecutionSemester semester) {
        for (CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequests(semester)) {
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
        final CompetenceCourseInformation information = findCompetenceCourseInformationForExecutionYear(executionYear);
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

    public ExecutionSemester getStartExecutionSemester() {
        return getOldestCompetenceCourseInformation().getExecutionPeriod();
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    static public List<CompetenceCourse> readOldCompetenceCourses() {
        final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (final CompetenceCourse competenceCourse : Bennu.getInstance().getCompetenceCoursesSet()) {
            if (!competenceCourse.isBolonha()) {
                result.add(competenceCourse);
            }
        }
        return result;
    }

    static public Collection<CompetenceCourse> readBolonhaCompetenceCourses() {
        final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (final CompetenceCourse competenceCourse : Bennu.getInstance().getCompetenceCoursesSet()) {
            if (competenceCourse.isBolonha()) {
                result.add(competenceCourse);
            }
        }
        return result;
    }

    static public Collection<CompetenceCourse> searchBolonhaCompetenceCourses(String searchName, String searchCode) {
        final Set<CompetenceCourse> result = new TreeSet<CompetenceCourse>(COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        for (final CompetenceCourse competenceCourse : Bennu.getInstance().getCompetenceCoursesSet()) {
            if (!competenceCourse.isBolonha()) {
                continue;
            }
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
            if (competenceCourse.isBolonha() && competenceCourse.isApproved()) {
                result.add(competenceCourse);
            }
        }
        return result;
    }

    static public Collection<CompetenceCourse> readApprovedBolonhaDissertations() {
        final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (final CompetenceCourse competenceCourse : Bennu.getInstance().getCompetenceCoursesSet()) {
            if (competenceCourse.isBolonha() && competenceCourse.isApproved() && competenceCourse.isDissertation()) {
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
        check(this, CompetenceCoursePredicates.writePredicate);
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

    /**
     * Find the most recent <b>until</b> given {@link ExecutionInterval}:
     * usefull for getting current info
     * 
     */
    public CompetenceCourseInformation findInformationMostRecentUntil(final ExecutionInterval input) {
        CompetenceCourseInformation result = null;

        if (!getCompetenceCourseInformationsSet().isEmpty()) {
            final TreeSet<CompetenceCourseInformation> sorted = getOrderedCompetenceCourseInformations();
            final ExecutionInterval until = input == null ? ExecutionSemester.readActualExecutionSemester() : input;

            result = getOrderedCompetenceCourseInformations().first();
            for (final CompetenceCourseInformation iter : sorted) {
                if (!iter.getExecutionInterval().isAfter(until)
                        && iter.getExecutionInterval().isAfter(result.getExecutionInterval())) {
                    result = iter;
                }
            }
        }

        return result;
    }

    public boolean isAnual(final ExecutionInterval input) {
        return getAcademicPeriod(input) == AcademicPeriod.YEAR;
    }

    public CompetenceCourseLevel getCompetenceCourseLevel(final ExecutionInterval interval) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(interval);
        return information != null ? information.getCompetenceCourseLevel() : null;
    }

    public AcademicPeriod getAcademicPeriod(final ExecutionInterval input) {
        final CompetenceCourseInformation information = findInformationMostRecentUntil(input);
        return information != null ? information.getAcademicPeriod() : null;
    }

    public AcademicPeriod getAcademicPeriod() {
        return getAcademicPeriod(null);
    }

}

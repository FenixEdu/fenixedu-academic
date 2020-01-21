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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class EmptyDegree extends EmptyDegree_Base {

    private static volatile EmptyDegree instance = null;

    private EmptyDegree() {
        super();
        setRootDomainObject(Bennu.getInstance());
        super.setDegreeType(DegreeType.matching(DegreeType::isEmpty).orElseGet(
                () -> {
                    DegreeType type =
                            new DegreeType(BundleUtil.getLocalizedString("resources.EnumerationResources", "DegreeType.EMPTY"));
                    type.setEmpty(true);
                    return type;
                }));
        super.setGradeScale(GradeScale.TYPE20);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    public static EmptyDegree getInstance() {
        if (instance == null) {
            synchronized (EmptyDegree.class) {
                if (instance == null) {
                    for (final Degree degree : Bennu.getInstance().getDegreesSet()) {
                        if (degree.isEmpty()) {
                            instance = (EmptyDegree) degree;
                        }
                    }
                }
            }
        }

        return instance;
    }

    public static void init() {
        synchronized (EmptyDegree.class) {
            final EmptyDegree existing = getInstance();
            if (existing == null) {
                final EmptyDegree newinstance = new EmptyDegree();
                newinstance.setNomeOnSuper("Curso de Unidades Isoladas");
                instance = newinstance;
            }
        }
    }

    private void setNomeOnSuper(final String nome) {
        super.setNome(nome);
    }

    @Override
    public void edit(LocalizedString name, String acronym, LocalizedString associatedInstitutions, DegreeType degreeType, Double ectsCredits,
                            GradeScale gradeScale, String prevailingScientificArea, ExecutionYear executionYear) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public void edit(LocalizedString name, String code, LocalizedString associatedInstitutions, DegreeType degreeType,
                            GradeScale gradeScale,
                            ExecutionYear executionYear) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        blockers.add(BundleUtil.getString(Bundle.APPLICATION, "EmptyDegree.not.available"));
    }

    @Override
    public DegreeCurricularPlan createDegreeCurricularPlan(String name, GradeScale gradeScale, Person creator,
            AcademicPeriod duration) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public void setNome(final String nome) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public void setNameEn(String nameEn) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public void setSigla(final String sigla) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public String getSigla() {
        return StringUtils.EMPTY;
    }

    @Override
    public Double getEctsCredits() {
        return null;
    }

    @Override
    public void setEctsCredits(Double ectsCredits) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public boolean hasEctsCredits() {
        return false;
    }

    @Override
    public void setGradeScale(GradeScale gradeScale) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public void setPrevailingScientificArea(String prevailingScientificArea) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public void setDegreeType(final DegreeType degreeType) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public boolean isBolonhaDegree() {
        return true;
    }

    @Override
    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return false;
    }

    @Override
    public List<DegreeCurricularPlan> findDegreeCurricularPlansByState(DegreeCurricularPlanState state) {
        if (state == DegreeCurricularPlanState.ACTIVE) {
            return getActiveDegreeCurricularPlans();
        }

        return Collections.emptyList();
    }

    @Override
    public List<DegreeCurricularPlan> getActiveDegreeCurricularPlans() {
        return Collections.singletonList(getMostRecentDegreeCurricularPlan());
    }

    @Override
    public List<DegreeCurricularPlan> getPastDegreeCurricularPlans() {
        return Collections.emptyList();
    }

    @Override
    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYear(final ExecutionYear executionYear) {
        return Collections.emptyList();
    }

    @Override
    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYearAndYear(final ExecutionYear ey, final Integer cy) {
        return Collections.emptyList();
    }

    @Override
    public List<ExecutionCourse> getExecutionCourses(String curricularCourseAcronym, ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }

    @Override
    @Deprecated
    final public String getName() {
        return getPresentationName();
    }

    @Override
    public String getNameEn() {
        return getPresentationName();
    }

    @Override
    final public String getPresentationName(ExecutionYear executionYear) {
        return getNameFor((ExecutionYear) null).getContent(org.fenixedu.academic.util.LocaleUtils.PT);
    }

    @Override
    final public String getFilteredName(final ExecutionYear executionYear, final Locale locale) {
        return getNameFor(executionYear).getContent(locale);
    }

    @Override
    public DegreeCurricularPlan getMostRecentDegreeCurricularPlan() {
        return getDegreeCurricularPlansSet().iterator().next();
    }

    @Override
    public DegreeCurricularPlan getLastActiveDegreeCurricularPlan() {
        return getMostRecentDegreeCurricularPlan();
    }

    @Override
    public LocalizedString getQualificationLevel(final ExecutionYear executionYear) {
        return new LocalizedString();
    }

    @Override
    public LocalizedString getProfessionalExits(final ExecutionYear executionYear) {
        return new LocalizedString();
    }

    @Override
    public DegreeInfo getMostRecentDegreeInfo() {
        return null;
    }

    @Override
    public DegreeInfo getDegreeInfoFor(ExecutionYear executionYear) {
        return getMostRecentDegreeInfo();
    }

    @Override
    public DegreeInfo getMostRecentDegreeInfo(ExecutionYear executionYear) {
        return getMostRecentDegreeInfo();
    }

    @Override
    public DegreeInfo createCurrentDegreeInfo() {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public List<Integer> buildFullCurricularYearList() {
        return Collections.emptyList();
    }

    @Override
    final public boolean isCoordinator(final Person person, final ExecutionYear executionYear) {
        return false;
    }

    @Override
    final public Collection<Coordinator> getResponsibleCoordinators(final ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    final public Collection<Coordinator> getCurrentCoordinators() {
        return Collections.emptySet();
    }

    @Override
    final public Collection<Coordinator> getCurrentResponsibleCoordinators() {
        return Collections.emptySet();
    }

    @Override
    final public Collection<Teacher> getResponsibleCoordinatorsTeachers(final ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    final public Collection<Teacher> getCurrentResponsibleCoordinatorsTeachers() {
        return Collections.emptySet();
    }

    @Override
    public String constructSchoolClassPrefix(final Integer curricularYear) {
        return StringUtils.EMPTY;
    }

    @Override
    public boolean isFirstCycle() {
        return false;
    }

    @Override
    public boolean isSecondCycle() {
        return false;
    }

    @Override
    public boolean isAnyPublishedThesisAvailable() {
        return false;
    }

    @Override
    public boolean isAnyThesisAvailable() {
        return false;
    }

    @Override
    public Set<CurricularCourse> getAllCurricularCourses(ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    public Set<CurricularCourse> getCurricularCoursesFromGivenCurricularYear(int curricularYear, ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    public Set<CurricularCourse> getFirstCycleCurricularCourses(ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    public Set<CurricularCourse> getSecondCycleCurricularCourses(ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    public boolean canCreateGratuityEvent() {
        return false;
    }

}

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
package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EmptyDegree extends EmptyDegree_Base {

    private static EmptyDegree instance = null;

    private EmptyDegree() {
        super();
        setRootDomainObject(Bennu.getInstance());
        super.setDegreeType(DegreeType.EMPTY);
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
    public void edit(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale,
            ExecutionYear executionYear) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public void edit(String name, String nameEn, String acronym, DegreeType degreeType, Double ectsCredits,
            GradeScale gradeScale, String prevailingScientificArea, ExecutionYear executionYear) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public Boolean getCanBeDeleted() {
        return false;
    }

    @Override
    public void delete() {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public DegreeCurricularPlan createPreBolonhaDegreeCurricularPlan(String name, DegreeCurricularPlanState state,
            Date initialDate, Date endDate, Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits,
            MarkType markType, Integer numerusClausus, String anotation, GradeScale gradeScale) {
        throw new DomainException("EmptyDegree.not.available");
    }

    @Override
    public DegreeCurricularPlan createBolonhaDegreeCurricularPlan(String name, GradeScale gradeScale, Person creator) {
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
    public GradeScale getGradeScale() {
        return null;
    }

    @Override
    public GradeScale getGradeScaleChain() {
        return null;
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
        return getNameFor((ExecutionYear) null).getContent(MultiLanguageString.pt);
    }

    @Override
    final public String getFilteredName(final ExecutionYear executionYear, final Locale locale) {
        return getNameFor(executionYear).getContent(locale);
    }

    @Override
    public DegreeCurricularPlan getMostRecentDegreeCurricularPlan() {
        return getDegreeCurricularPlans().iterator().next();
    }

    @Override
    public DegreeCurricularPlan getLastActiveDegreeCurricularPlan() {
        return getMostRecentDegreeCurricularPlan();
    }

    @Override
    public MultiLanguageString getQualificationLevel(final ExecutionYear executionYear) {
        return new MultiLanguageString();
    }

    @Override
    public MultiLanguageString getProfessionalExits(final ExecutionYear executionYear) {
        return new MultiLanguageString();
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
    public List<YearDelegateElection> getYearDelegateElectionsGivenExecutionYear(ExecutionYear executionYear) {
        return Collections.emptyList();
    }

    @Override
    public List<YearDelegateElection> getYearDelegateElectionsGivenExecutionYearAndCurricularYear(ExecutionYear executionYear,
            CurricularYear curricularYear) {
        return Collections.emptyList();
    }

    @Override
    public YearDelegateElection getYearDelegateElectionWithLastCandidacyPeriod(ExecutionYear executionYear,
            CurricularYear curricularYear) {
        return null;
    }

    @Override
    public YearDelegateElection getYearDelegateElectionWithLastVotingPeriod(ExecutionYear executionYear,
            CurricularYear curricularYear) {
        return null;
    }

    @Override
    public List<Student> getAllActiveDelegates() {
        return Collections.emptyList();
    }

    @Override
    public List<Student> getAllActiveYearDelegates() {
        return Collections.emptyList();
    }

    @Override
    public Student getActiveYearDelegateByCurricularYear(CurricularYear curricularYear) {
        return null;
    }

    @Override
    public List<Student> getAllActiveDelegatesByFunctionType(FunctionType functionType, ExecutionYear executionYear) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasActiveDelegateFunctionForStudent(Student student, ExecutionYear executionYear,
            FunctionType delegateFunctionType) {
        return false;
    }

    @Override
    public boolean hasAnyActiveDelegateFunctionForStudent(Student student) {
        return false;
    }

    @Override
    public PersonFunction getActiveDelegatePersonFunctionByStudentAndFunctionType(Student student, ExecutionYear executionYear,
            FunctionType functionType) {
        return null;
    }

    @Override
    public Student getYearDelegateByExecutionYearAndCurricularYear(ExecutionYear executionYear, CurricularYear curricularYear) {
        return null;
    }

    @Override
    public List<Student> getAllDelegatesByExecutionYearAndFunctionType(ExecutionYear executionYear, FunctionType functionType) {
        return Collections.emptyList();
    }

    @Override
    public List<Student> getSecondCycleStudents(ExecutionYear executionYear) {
        return Collections.emptyList();
    }

    @Override
    public List<Student> getFirstCycleStudents(ExecutionYear executionYear) {
        return Collections.emptyList();
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

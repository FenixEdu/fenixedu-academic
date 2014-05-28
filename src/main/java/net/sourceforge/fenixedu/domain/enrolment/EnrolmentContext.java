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
package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class EnrolmentContext {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionSemester executionSemester;

    private final Set<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private final List<CurriculumModule> curriculumModulesToRemove;

    private CurricularRuleLevel curricularRuleLevel;

    //private Person responsiblePerson;
    private final User userView;

    public EnrolmentContext(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol, final List<CurriculumModule> curriculumModulesToRemove,
            final CurricularRuleLevel curricularRuleLevel) {

        this.userView = Authenticate.getUser();
        this.studentCurricularPlan = studentCurricularPlan;

        this.degreeModulesToEvaluate = new HashSet<IDegreeModuleToEvaluate>();
        for (final IDegreeModuleToEvaluate moduleToEnrol : degreeModulesToEnrol) {
            if (curriculumModulesToRemove.contains(moduleToEnrol.getCurriculumGroup())) {
                throw new DomainException(
                        "error.StudentCurricularPlan.cannot.remove.enrollment.on.curriculum.group.because.other.enrollments.depend.on.it",
                        moduleToEnrol.getCurriculumGroup().getName().getContent());
            }

            this.addDegreeModuleToEvaluate(moduleToEnrol);
        }

        this.executionSemester = executionSemester;
        this.curriculumModulesToRemove = curriculumModulesToRemove;
        this.curricularRuleLevel = curricularRuleLevel;
    }

    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate() {
        return degreeModulesToEvaluate;
    }

    public Set<IDegreeModuleToEvaluate> getAllChildDegreeModulesToEvaluateFor(final DegreeModule degreeModule) {
        final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : this.degreeModulesToEvaluate) {
            if (degreeModule.hasDegreeModule(degreeModuleToEvaluate.getDegreeModule())) {
                result.add(degreeModuleToEvaluate);
            }
        }

        return result;
    }

    public void addDegreeModuleToEvaluate(final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
        getDegreeModulesToEvaluate().add(degreeModuleToEvaluate);
    }

    public boolean hasDegreeModulesToEvaluate() {
        return degreeModulesToEvaluate != null && !degreeModulesToEvaluate.isEmpty();
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    public Registration getRegistration() {
        return studentCurricularPlan.getRegistration();
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List<CurriculumModule> getToRemove() {
        return curriculumModulesToRemove;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
        return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
        this.curricularRuleLevel = curricularRuleLevel;
    }

    public Person getResponsiblePerson() {
        return userView.getPerson();
    }

    public boolean hasResponsiblePerson() {
        return getResponsiblePerson() != null;
    }

    public boolean isResponsiblePersonStudent() {
        return userView.getPerson().hasRole(RoleType.STUDENT);
    }

    public boolean isRegistrationFromResponsiblePerson() {
        return getResponsiblePerson() == getRegistration().getPerson();
    }

    public boolean isNormal() {
        return getCurricularRuleLevel().isNormal();
    }

    public boolean isImprovement() {
        return getCurricularRuleLevel() == CurricularRuleLevel.IMPROVEMENT_ENROLMENT;
    }

    public boolean isSpecialSeason() {
        return getCurricularRuleLevel() == CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT;
    }

    public boolean isExtra() {
        return getCurricularRuleLevel() == CurricularRuleLevel.EXTRA_ENROLMENT;
    }

    public boolean isPropaeudeutics() {
        return getCurricularRuleLevel() == CurricularRuleLevel.PROPAEUDEUTICS_ENROLMENT;
    }

    public boolean isStandalone() {
        return getCurricularRuleLevel() == CurricularRuleLevel.STANDALONE_ENROLMENT
                || getCurricularRuleLevel() == CurricularRuleLevel.STANDALONE_ENROLMENT_NO_RULES;
    }

    public boolean isEnrolmentWithoutRules() {
        return getCurricularRuleLevel() == CurricularRuleLevel.ENROLMENT_NO_RULES;
    }

    public boolean isPhdDegree() {
        return studentCurricularPlan.getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    @SuppressWarnings("unchecked")
    static public EnrolmentContext createForVerifyWithRules(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester) {
        return createForVerifyWithRules(studentCurricularPlan, executionSemester, Collections.EMPTY_SET);
    }

    @SuppressWarnings("unchecked")
    static public EnrolmentContext createForVerifyWithRules(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final Set<IDegreeModuleToEvaluate> degreeModulesToEvaluate) {
        return new EnrolmentContext(studentCurricularPlan, executionSemester, degreeModulesToEvaluate, Collections.EMPTY_LIST,
                CurricularRuleLevel.ENROLMENT_WITH_RULES);
    }

    @SuppressWarnings("unchecked")
    static public EnrolmentContext createForNoCourseGroupCurriculumGroupEnrolment(
            final StudentCurricularPlan studentCurricularPlan, final NoCourseGroupEnrolmentBean bean) {

        final IDegreeModuleToEvaluate moduleToEvaluate =
                new ExternalCurricularCourseToEnrol(readOrCreateNoCourseGroupCurriculumGroup(studentCurricularPlan,
                        bean.getGroupType()), bean.getSelectedCurricularCourse(), bean.getExecutionPeriod());

        return new EnrolmentContext(studentCurricularPlan, bean.getExecutionPeriod(), Collections.singleton(moduleToEvaluate),
                Collections.EMPTY_LIST, bean.getCurricularRuleLevel());
    }

    static private NoCourseGroupCurriculumGroup readOrCreateNoCourseGroupCurriculumGroup(
            final StudentCurricularPlan studentCurricularPlan, final NoCourseGroupCurriculumGroupType groupType) {
        NoCourseGroupCurriculumGroup group = studentCurricularPlan.getNoCourseGroupCurriculumGroup(groupType);
        if (group == null) {
            group = studentCurricularPlan.createNoCourseGroupCurriculumGroup(groupType);
        }
        return group;
    }

}

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
package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DegreeModuleToEnrolKeyConverter;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BolonhaStudentEnrollmentBean implements Serializable, IStudentCurricularPlanBean {

    private static final long serialVersionUID = -5614162187691303580L;

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionSemester executionSemester;

    private StudentCurriculumGroupBean rootStudentCurriculumGroupBean;

    private List<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private List<CurriculumModule> curriculumModulesToRemove;

    private IDegreeModuleToEvaluate optionalDegreeModuleToEnrol;

    private CurricularRuleLevel curricularRuleLevel;

    private CycleType cycleTypeToEnrol;

    private LocalDate endStageDate;

    public BolonhaStudentEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final int[] curricularYears, CurricularRuleLevel curricularRuleLevel) {
        this(studentCurricularPlan, executionSemester, new StudentCurriculumGroupBean(studentCurricularPlan.getRoot(),
                executionSemester, curricularYears), curricularRuleLevel);

        setEndStageDate(studentCurricularPlan.getEndStageDate());
    }

    protected BolonhaStudentEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final StudentCurriculumGroupBean rootStudentCurriculumGroupBean,
            CurricularRuleLevel curricularRuleLevel) {
        super();
        setStudentCurricularPlan(studentCurricularPlan);
        setExecutionPeriod(executionSemester);
        setRootStudentCurriculumGroupBean(rootStudentCurriculumGroupBean);

        setDegreeModulesToEvaluate(new ArrayList<IDegreeModuleToEvaluate>());
        setCurriculumModulesToRemove(new ArrayList<CurriculumModule>());
        setCurricularRuleLevel(curricularRuleLevel);
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    private void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public StudentCurriculumGroupBean getRootStudentCurriculumGroupBean() {
        return rootStudentCurriculumGroupBean;
    }

    public void setRootStudentCurriculumGroupBean(StudentCurriculumGroupBean studentCurriculumGroupBean) {
        this.rootStudentCurriculumGroupBean = studentCurriculumGroupBean;
    }

    public List<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate() {
        return degreeModulesToEvaluate;
    }

    public void setDegreeModulesToEvaluate(List<IDegreeModuleToEvaluate> degreeModulesToEnrol) {
        this.degreeModulesToEvaluate = degreeModulesToEnrol;
    }

    public IDegreeModuleToEvaluate getOptionalDegreeModuleToEnrol() {
        return optionalDegreeModuleToEnrol;
    }

    public void setOptionalDegreeModuleToEnrol(IDegreeModuleToEvaluate optionalDegreeModuleToEnrol) {
        this.optionalDegreeModuleToEnrol = optionalDegreeModuleToEnrol;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
        return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
        this.curricularRuleLevel = curricularRuleLevel;
    }

    public List<CurriculumModule> getCurriculumModulesToRemove() {
        final List<CurriculumModule> result = new ArrayList<CurriculumModule>();

        for (final CurriculumModule domainReference : this.curriculumModulesToRemove) {
            result.add(domainReference);
        }

        return result;
    }

    public void setCurriculumModulesToRemove(List<CurriculumModule> curriculumModules) {
        this.curriculumModulesToRemove = new ArrayList<CurriculumModule>();

        for (final CurriculumModule curriculumModule : curriculumModules) {
            this.curriculumModulesToRemove.add(curriculumModule);
        }
    }

    public Converter getDegreeModulesToEvaluateConverter() {
        return new DegreeModuleToEnrolKeyConverter();
    }

    public String getFuncionalityTitle() {
        final StringBuilder result = new StringBuilder();
        result.append(BundleUtil.getString(Bundle.ACADEMIC, "label.student.enrollment.courses")).append(" ");

        switch (curricularRuleLevel) {

        case ENROLMENT_WITH_RULES:
            result.append("(").append(BundleUtil.getString(Bundle.ACADEMIC, "label.student.enrollment.withRules")).append(")");
            break;

        case ENROLMENT_NO_RULES:
            result.append("(").append(BundleUtil.getString(Bundle.ACADEMIC, "label.student.enrollment.withoutRules")).append(")");
            break;
        }

        return result.toString();
    }

    public CycleType getCycleTypeToEnrol() {
        return cycleTypeToEnrol;
    }

    public void setCycleTypeToEnrol(CycleType cycleTypeToEnrol) {
        this.cycleTypeToEnrol = cycleTypeToEnrol;
    }

    public LocalDate getEndStageDate() {
        return this.endStageDate;
    }

    public void setEndStageDate(LocalDate date) {
        this.endStageDate = date;
    }

    public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

}

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
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.CompetenceCourseLoadBean;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * Represents a set of attributes that defines a CompetenceCourse in a given
 * period of time.
 * 
 * <pre>
 * 
 * This attributes can be: 
 * - Deparment which belongs; 
 * - Name, descriptions, goals and bibliographic references; 
 * - Study and work load (number hours of theoretical or pratical classes)
 * 
 * </pre>
 * 
 * In the perspective of a CompetenceCourse we can see this class as a version
 * of attributes that defines it. The start period of the version is done by an
 * association with ExecutionSemester.
 * 
 * A CompetenceCourseInformation (the version of the CompetenceCourse) belongs
 * to a CompetenceCourseGroupUnit which belongs to a DepartmentUnit.
 * 
 * @see CompetenceCourse
 * @see CompetenceCourseGroupUnit
 * 
 */
public class CompetenceCourseInformation extends CompetenceCourseInformation_Base {

    static public final Comparator<CompetenceCourseInformation> COMPARATORY_BY_EXECUTION_PERIOD =
            new Comparator<CompetenceCourseInformation>() {
                @Override
                public int compare(CompetenceCourseInformation o1, CompetenceCourseInformation o2) {
                    return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
                }
            };

    public CompetenceCourseInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CompetenceCourseInformation(CompetenceCourseInformation existingInformation) {
        this(existingInformation.getName(), existingInformation.getNameEn(), existingInformation.getBasic(), existingInformation
                .getRegime(), existingInformation.getCompetenceCourseLevel(), existingInformation.getExecutionPeriod(),
                existingInformation.getCompetenceCourseGroupUnit());
        setCompetenceCourse(existingInformation.getCompetenceCourse());
        for (CompetenceCourseLoad load : existingInformation.getCompetenceCourseLoads()) {
            CompetenceCourseLoad newLoad = new CompetenceCourseLoad(load);
            addCompetenceCourseLoads(newLoad);
        }
        setAcronym(existingInformation.getAcronym());
        setBibliographicReferences(existingInformation.getBibliographicReferences());
        setEvaluationMethod(existingInformation.getEvaluationMethod());
        setEvaluationMethodEn(existingInformation.getEvaluationMethodEn());
        setObjectives(existingInformation.getObjectives());
        setObjectivesEn(existingInformation.getObjectivesEn());
        setProgram(existingInformation.getProgram());
        setProgramEn(existingInformation.getProgramEn());
    }

    public CompetenceCourseInformation(String name, String nameEn, Boolean basic, RegimeType regimeType,
            CompetenceCourseLevel competenceCourseLevel, ExecutionSemester period, CompetenceCourseGroupUnit unit) {

        this();
        checkParameters(name, nameEn, basic, regimeType, competenceCourseLevel, unit);
        setName(StringFormatter.prettyPrint(name));
        setNameEn(StringFormatter.prettyPrint(nameEn));
        setBasic(basic);
        setRegime(regimeType);
        setCompetenceCourseLevel(competenceCourseLevel);
        setBibliographicReferences(new BibliographicReferences());
        setExecutionPeriod(period);
        setCompetenceCourseGroupUnit(unit);
    }

    public ScientificAreaUnit getScientificAreaUnit() {
        return getCompetenceCourseGroupUnit().getScientificAreaUnit();
    }

    public DepartmentUnit getDepartmentUnit() {
        return getCompetenceCourseGroupUnit().getDepartmentUnit();
    }

    private void checkParameters(String name, String nameEn, Boolean basic, RegimeType regimeType,
            CompetenceCourseLevel competenceCourseLevel) {

        if (name == null || nameEn == null || basic == null || regimeType == null) {
            throw new DomainException("competence.course.information.invalid.parameters");
        }
        if (competenceCourseLevel == null) {
            competenceCourseLevel = CompetenceCourseLevel.UNKNOWN;
        }
    }

    private void checkParameters(String name, String nameEn, Boolean basic, RegimeType regimeType,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseGroupUnit unit) {

        checkParameters(name, nameEn, basic, regimeType, competenceCourseLevel);
        if (unit == null || !unit.isCompetenceCourseGroupUnit()) {
            throw new DomainException("competence.course.information.invalid.group.unit");
        }
    }

    public void edit(String name, String nameEn, Boolean basic, CompetenceCourseLevel competenceCourseLevel,
            CompetenceCourseGroupUnit unit) {
        checkParameters(name, nameEn, basic, getRegime(), competenceCourseLevel, unit);
        setName(StringFormatter.prettyPrint(name));
        setNameEn(StringFormatter.prettyPrint(nameEn));
        setBasic(basic);
        setCompetenceCourseLevel(competenceCourseLevel);
        setCompetenceCourseGroupUnit(unit);
    }

    public void edit(String name, String nameEn, Boolean basic, CompetenceCourseLevel competenceCourseLevel) {
        checkParameters(name, nameEn, basic, getRegime(), competenceCourseLevel);
        setName(StringFormatter.prettyPrint(name));
        setNameEn(StringFormatter.prettyPrint(nameEn));
        setBasic(basic);
        setCompetenceCourseLevel(competenceCourseLevel);
    }

    public void edit(String objectives, String program, String evaluationMethod, String objectivesEn, String programEn,
            String evaluationMethodEn) {
        setObjectives(objectives);
        setProgram(program);
        setEvaluationMethod(evaluationMethod);
        setObjectivesEn(objectivesEn);
        setProgramEn(programEn);
        setEvaluationMethodEn(evaluationMethodEn);
    }

    public void delete() {
        setExecutionPeriod(null);
        setCompetenceCourse(null);
        setCompetenceCourseGroupUnit(null);
        for (; !getCompetenceCourseLoads().isEmpty(); getCompetenceCourseLoads().iterator().next().delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public BibliographicReference getBibliographicReference(Integer oid) {
        return getBibliographicReferences().getBibliographicReference(oid);
    }

    public Double getTheoreticalHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getTheoreticalHours();
        }
        return result;
    }

    public Double getProblemsHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getProblemsHours();
        }
        return result;
    }

    public Double getLaboratorialHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getLaboratorialHours();
        }
        return result;
    }

    public Double getSeminaryHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getSeminaryHours();
        }
        return result;
    }

    public Double getFieldWorkHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getFieldWorkHours();
        }
        return result;
    }

    public Double getTrainingPeriodHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getTrainingPeriodHours();
        }
        return result;
    }

    public Double getTutorialOrientationHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getTutorialOrientationHours();
        }
        return result;
    }

    public Double getAutonomousWorkHours(Integer order) {
        BigDecimal result = new BigDecimal(0.0);
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result = result.add(new BigDecimal(competenceCourseLoad.getAutonomousWorkHours()));
        }

        result = result.setScale(1, RoundingMode.HALF_EVEN);
        return result.doubleValue();
    }

    public Double getContactLoad(Integer order) {
        BigDecimal result = new BigDecimal(0.0);
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result = result.add(new BigDecimal(competenceCourseLoad.getContactLoad()));
        }

        result = result.setScale(1, RoundingMode.HALF_EVEN);
        return result.doubleValue();
    }

    public Double getTotalLoad(Integer order) {
        BigDecimal result = new BigDecimal(0.0);
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result = result.add(new BigDecimal(competenceCourseLoad.getTotalLoad()));
        }

        result = result.setScale(1, RoundingMode.HALF_EVEN);
        return result.doubleValue();
    }

    public double getEctsCredits(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getEctsCredits();
        }

        return result;
    }

    private List<CompetenceCourseLoadBean> getCompetenceCourseLoadBeans(final Integer order) {

        if (isSemestrial()) {
            return Collections.singletonList(new CompetenceCourseLoadBean(getCompetenceCourseLoads().iterator().next()));
        }

        if (isAnual()) {
            final List<CompetenceCourseLoadBean> result = new ArrayList<CompetenceCourseLoadBean>();

            for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
                result.add(new CompetenceCourseLoadBean(competenceCourseLoad));
            }

            if (getCompetenceCourseLoadsSet().size() == 1) { // hack
                final CompetenceCourseLoad courseLoad = getCompetenceCourseLoads().iterator().next();
                final CompetenceCourseLoadBean courseLoadBean = new CompetenceCourseLoadBean(courseLoad);
                courseLoadBean.setLoadOrder(courseLoad.getLoadOrder() + 1);
                result.add(courseLoadBean);
            }

            final Iterator<CompetenceCourseLoadBean> loads = result.iterator();
            while (loads.hasNext()) {
                final CompetenceCourseLoadBean courseLoadBean = loads.next();
                if (order != null && !courseLoadBean.getLoadOrder().equals(order)) {
                    loads.remove();
                }
            }
            return result;
        }

        return Collections.emptyList();
    }

    public boolean isAnual() {
        return getRegime() == RegimeType.ANUAL;
    }

    public boolean isSemestrial() {
        return getRegime() == RegimeType.SEMESTRIAL;
    }

    public List<CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequest() {
        final List<CompetenceCourseInformationChangeRequest> requests = new ArrayList<CompetenceCourseInformationChangeRequest>();
        for (final CompetenceCourseInformationChangeRequest request : this.getCompetenceCourse()
                .getCompetenceCourseInformationChangeRequests()) {
            if (request.getExecutionPeriod().equals(this.getExecutionPeriod())) {
                requests.add(request);
            }
        }
        return requests;
    }

    public boolean isCompetenceCourseInformationChangeRequestDraftAvailable() {
        for (final CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequest()) {
            if (request.getApproved() == null) {
                return true;
            }
        }
        return false;
    }

    public boolean isLoggedPersonAllowedToEdit() {
        Person person = AccessControl.getPerson();
        if (isCompetenceCourseInformationChangeRequestDraftAvailable()) {
            return false;
        }
        if (person.hasPersonRoles(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL))) {
            return true;
        }
        if (!person.hasPersonRoles(Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER))) {
            return false;
        }
        return getDepartmentUnit().getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person);
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad> getCompetenceCourseLoads() {
        return getCompetenceCourseLoadsSet();
    }

    @Deprecated
    public boolean hasAnyCompetenceCourseLoads() {
        return !getCompetenceCourseLoadsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBibliographicReferences() {
        return getBibliographicReferences() != null;
    }

    @Deprecated
    public boolean hasObjectivesEn() {
        return getObjectivesEn() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasCompetenceCourseLevel() {
        return getCompetenceCourseLevel() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBasic() {
        return getBasic() != null;
    }

    @Deprecated
    public boolean hasRegime() {
        return getRegime() != null;
    }

    @Deprecated
    public boolean hasObjectives() {
        return getObjectives() != null;
    }

    @Deprecated
    public boolean hasNameEn() {
        return getNameEn() != null;
    }

    @Deprecated
    public boolean hasAcronym() {
        return getAcronym() != null;
    }

    @Deprecated
    public boolean hasEvaluationMethod() {
        return getEvaluationMethod() != null;
    }

    @Deprecated
    public boolean hasCompetenceCourse() {
        return getCompetenceCourse() != null;
    }

    @Deprecated
    public boolean hasProgram() {
        return getProgram() != null;
    }

    @Deprecated
    public boolean hasProgramEn() {
        return getProgramEn() != null;
    }

    @Deprecated
    public boolean hasCompetenceCourseGroupUnit() {
        return getCompetenceCourseGroupUnit() != null;
    }

    @Deprecated
    public boolean hasEvaluationMethodEn() {
        return getEvaluationMethodEn() != null;
    }

}

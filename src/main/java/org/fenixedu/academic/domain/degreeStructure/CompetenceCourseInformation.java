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
package org.fenixedu.academic.domain.degreeStructure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.StringFormatter;
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
        this(existingInformation.getName(), existingInformation.getNameEn(), existingInformation.getBasic(),
                existingInformation.getRegime(), existingInformation.getCompetenceCourseLevel(),
                existingInformation.getExecutionPeriod(), existingInformation.getCompetenceCourseGroupUnit());
        setCompetenceCourse(existingInformation.getCompetenceCourse());
        for (CompetenceCourseLoad load : existingInformation.getCompetenceCourseLoadsSet()) {
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
        new CompetenceCourseExtraInformation(this, existingInformation.getPrerequisites(),
                existingInformation.getPrerequisitesEn(), existingInformation.getLaboratorialComponent(),
                existingInformation.getLaboratorialComponentEn(), existingInformation.getProgrammingAndComputingComponent(),
                existingInformation.getProgrammingAndComputingComponentEn(), existingInformation.getCrossCompetenceComponent(),
                existingInformation.getCrossCompetenceComponentEn(), existingInformation.getEthicalPrinciples(),
                existingInformation.getEthicalPrinciplesEn());
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

    public void edit(String objectives, String program, String evaluationMethod, String prerequisites,
            String laboratorialComponent, String programmingAndComputingComponent, String crossCompetenceComponent,
            String ethicalPrinciples, String objectivesEn, String programEn, String evaluationMethodEn, String prerequisitesEn,
            String laboratorialComponentEn, String programmingAndComputingComponentEn, String crossCompetenceComponentEn,
            String ethicalPrinciplesEn) {
        setObjectives(objectives);
        setProgram(program);
        setEvaluationMethod(evaluationMethod);
        setObjectivesEn(objectivesEn);
        setProgramEn(programEn);
        setEvaluationMethodEn(evaluationMethodEn);

        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        if (extraInformation == null) {
            new CompetenceCourseExtraInformation(this, prerequisites, prerequisitesEn, laboratorialComponent,
                    laboratorialComponentEn, programmingAndComputingComponent, programmingAndComputingComponentEn,
                    crossCompetenceComponent, crossCompetenceComponentEn, ethicalPrinciples, ethicalPrinciplesEn);
        } else {
            extraInformation.edit(prerequisites, prerequisitesEn, laboratorialComponent, laboratorialComponentEn,
                    programmingAndComputingComponent, programmingAndComputingComponentEn, crossCompetenceComponent,
                    crossCompetenceComponentEn, ethicalPrinciples, ethicalPrinciplesEn);
        }
    }

    public void delete() {
        if (getCompetenceCourseExtraInformation() != null) {
            getCompetenceCourseExtraInformation().delete();
        }
        setExecutionPeriod(null);
        setCompetenceCourse(null);
        setCompetenceCourseGroupUnit(null);
        for (; !getCompetenceCourseLoadsSet().isEmpty(); getCompetenceCourseLoadsSet().iterator().next().delete()) {
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
            return Collections.singletonList(new CompetenceCourseLoadBean(getCompetenceCourseLoadsSet().iterator().next()));
        }

        if (isAnual()) {
            final List<CompetenceCourseLoadBean> result = new ArrayList<CompetenceCourseLoadBean>();

            for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoadsSet()) {
                result.add(new CompetenceCourseLoadBean(competenceCourseLoad));
            }

            if (getCompetenceCourseLoadsSet().size() == 1) { // hack
                final CompetenceCourseLoad courseLoad = getCompetenceCourseLoadsSet().iterator().next();
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
                .getCompetenceCourseInformationChangeRequestsSet()) {
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
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        return getDepartmentUnit().getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person);
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

    public ExecutionInterval getExecutionInterval() {
        return getExecutionPeriod();
    }

    public AcademicPeriod getAcademicPeriod() {
        AcademicPeriod result = null;
        final RegimeType regime = getRegime();
        if (regime.equals(RegimeType.SEMESTRIAL)) {
            result = AcademicPeriod.SEMESTER;
        } else if (regime.equals(RegimeType.ANUAL)) {
            result = AcademicPeriod.YEAR;
        } else {
            throw new DomainException("error.CompetenceCourseInformation.unsupported.AcademicPeriod");
        }
        return result;
    }

    public String getPrerequisites() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getPrerequisites();
    }

    public String getLaboratorialComponent() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getLaboratorialComponent();
    }

    public String getProgrammingAndComputingComponent() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getProgrammingAndComputingComponent();
    }

    public String getCrossCompetenceComponent() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getCrossCompetenceComponent();
    }

    public String getEthicalPrinciples() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getEthicalPrinciples();
    }

    public String getPrerequisitesEn() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getPrerequisitesEn();
    }

    public String getLaboratorialComponentEn() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getLaboratorialComponentEn();
    }

    public String getProgrammingAndComputingComponentEn() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getProgrammingAndComputingComponentEn();
    }

    public String getCrossCompetenceComponentEn() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getCrossCompetenceComponentEn();
    }

    public String getEthicalPrinciplesEn() {
        CompetenceCourseExtraInformation extraInformation = getCompetenceCourseExtraInformation();
        return extraInformation == null ? null : extraInformation.getEthicalPrinciplesEn();
    }

}

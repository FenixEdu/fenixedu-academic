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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.service.services.bolonhaManager.CompetenceCourseManagementAccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

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
 * to a CompetenceCourseGroup Unit which may belong to a Department Unit.
 *
 * @see CompetenceCourse
 *
 */
public class CompetenceCourseInformation extends CompetenceCourseInformation_Base {

    static public final Comparator<CompetenceCourseInformation> COMPARATORY_BY_EXECUTION_INTERVAL =
            Comparator.comparing(CompetenceCourseInformation::getExecutionInterval);

    @Deprecated
    static public final Comparator<CompetenceCourseInformation> COMPARATORY_BY_EXECUTION_PERIOD =
            new Comparator<CompetenceCourseInformation>() {
                @Override
                public int compare(final CompetenceCourseInformation o1, final CompetenceCourseInformation o2) {
                    return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
                }
            };

    public CompetenceCourseInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CompetenceCourseInformation(final CompetenceCourseInformation existingInformation) {
        this(existingInformation.getName(), existingInformation.getNameEn(), existingInformation.getBasic(),
                existingInformation.getAcademicPeriod(), existingInformation.getCompetenceCourseLevel(),
                existingInformation.getExecutionInterval(), existingInformation.getCompetenceCourseGroupUnit());
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
    }

    public CompetenceCourseInformation(final String name, final String nameEn, final Boolean basic,
            final AcademicPeriod academicPeriod, final CompetenceCourseLevel competenceCourseLevel,
            final ExecutionInterval interval, final Unit unit) {

        this();
        checkParameters(name, nameEn, basic, academicPeriod, competenceCourseLevel, unit);
        setName(name);
        setNameEn(nameEn);
        setBasic(basic);
        setAcademicPeriod(academicPeriod);
        setCompetenceCourseLevel(competenceCourseLevel);
        setBibliographicReferences(new BibliographicReferences());
        setExecutionInterval(interval);
        setCompetenceCourseGroupUnit(unit);
    }

//    public ScientificAreaUnit getScientificAreaUnit() {
//        return getCompetenceCourseGroupUnit().getScientificAreaUnit();
//    }

//    public DepartmentUnit getDepartmentUnit() {
//        return getCompetenceCourseGroupUnit().getAssociatedDepartmentUnit();
//    }

    private void checkParameters(final String name, final String nameEn, final Boolean basic, final AcademicPeriod academicPeriod,
            CompetenceCourseLevel competenceCourseLevel) {

        if (name == null || nameEn == null || basic == null || academicPeriod == null) {
            throw new DomainException("competence.course.information.invalid.parameters");
        }
        if (competenceCourseLevel == null) {
            competenceCourseLevel = CompetenceCourseLevel.UNKNOWN;
        }
    }

    private void checkParameters(final String name, final String nameEn, final Boolean basic, final AcademicPeriod academicPeriod,
            final CompetenceCourseLevel competenceCourseLevel, final Unit unit) {

        checkParameters(name, nameEn, basic, academicPeriod, competenceCourseLevel);
        if (unit == null || !unit.isCompetenceCourseGroupUnit()) {
            throw new DomainException("competence.course.information.invalid.group.unit");
        }
    }

    public void edit(final String name, final String nameEn, final Boolean basic,
            final CompetenceCourseLevel competenceCourseLevel, final Unit unit) {
        checkParameters(name, nameEn, basic, getAcademicPeriod(), competenceCourseLevel, unit);
        setName(name);
        setNameEn(nameEn);
        setBasic(basic);
        setCompetenceCourseLevel(competenceCourseLevel);
        setCompetenceCourseGroupUnit(unit);
    }

//    public void edit(final String name, final String nameEn, final Boolean basic,
//            final CompetenceCourseLevel competenceCourseLevel) {
//        checkParameters(name, nameEn, basic, getAcademicPeriod(), competenceCourseLevel);
//        setName(name);
//        setNameEn(nameEn);
//        setBasic(basic);
//        setCompetenceCourseLevel(competenceCourseLevel);
//    }

    public void edit(final String objectives, final String program, final String evaluationMethod, final String objectivesEn,
            final String programEn, final String evaluationMethodEn) {
        setObjectives(objectives);
        setProgram(program);
        setEvaluationMethod(evaluationMethod);
        setObjectivesEn(objectivesEn);
        setProgramEn(programEn);
        setEvaluationMethodEn(evaluationMethodEn);
    }

    public ExecutionInterval getExecutionInterval() {
        return getExecutionPeriod();
    }

    public void setExecutionInterval(final ExecutionInterval input) {
        if (input == null) {
            throw new DomainException("error.CompetenceCourseInformation.required.ExecutionInterval");
        }

        super.setExecutionPeriod(input);
    }

//    public AcademicPeriod getAcademicPeriod() {
//        if (super.getAcademicPeriod() != null) {
//            return super.getAcademicPeriod();
//        }
//
//        AcademicPeriod result = null;
//        final RegimeType regime = getRegime();
//        if (regime.equals(RegimeType.SEMESTRIAL)) {
//            result = AcademicPeriod.SEMESTER;
//        } else if (regime.equals(RegimeType.ANUAL)) {
//            result = AcademicPeriod.YEAR;
//        } else {
//            throw new DomainException("error.CompetenceCourseInformation.unsupported.AcademicPeriod");
//        }
//        return result;
//    }

    public void setAcademicPeriod(final AcademicPeriod input) {
        if (input == null) {
            throw new DomainException("error.CompetenceCourseInformation.required.AcademicPeriod");
        }

        if (input.getWeight() > 1f) {
            throw new DomainException("error.CompetenceCourseInformation.unsupported.AcademicPeriod");
        }

        super.setAcademicPeriod(input);

        // for backward compatibility
        setRegime(input.equals(AcademicPeriod.YEAR) ? RegimeType.ANUAL : RegimeType.SEMESTRIAL);
    }

    @Override
    public void setCompetenceCourseLevel(CompetenceCourseLevel competenceCourseLevel) {
        super.setCompetenceCourseLevel(competenceCourseLevel);
        
        if (competenceCourseLevel != null) {
            CompetenceCourseLevelType.findByCode(competenceCourseLevel.name()).ifPresent(lt -> setLevelType(lt));
        } else {
            setLevelType(null);
        }
    }

    public LocalizedString getNameI18N() {
        LocalizedString result = new LocalizedString();

        if (!StringUtils.isEmpty(getName())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.PT, getName());
        }
        if (!StringUtils.isEmpty(getNameEn())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.EN, getNameEn());
        }

        return result;
    }

    public void setNameI18N(final LocalizedString input) {
        if (input != null) {
            setName(input.getContent(Locale.getDefault()));
            setNameEn(input.getContent(Locale.ENGLISH));
        } else {
            setName(null);
            setNameEn(null);
        }
    }

    public LocalizedString getObjectivesI18N() {
        LocalizedString result = new LocalizedString();

        if (!StringUtils.isEmpty(getObjectives())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.PT, getObjectives());
        }
        if (!StringUtils.isEmpty(getObjectivesEn())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.EN, getObjectivesEn());
        }

        return result;
    }

    public void setObjectivesI18N(final LocalizedString input) {
        if (input != null) {
            setObjectives(input.getContent(Locale.getDefault()));
            setObjectivesEn(input.getContent(Locale.ENGLISH));
        } else {
            setObjectives(null);
            setObjectivesEn(null);
        }
    }

    public LocalizedString getProgramI18N() {
        LocalizedString result = new LocalizedString();

        if (!StringUtils.isEmpty(getProgram())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.PT, getProgram());
        }
        if (!StringUtils.isEmpty(getProgramEn())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.EN, getProgramEn());
        }

        return result;
    }

    public void setProgramI18N(final LocalizedString input) {
        if (input != null) {
            setProgram(input.getContent(Locale.getDefault()));
            setProgramEn(input.getContent(Locale.ENGLISH));
        } else {
            setProgram(null);
            setProgramEn(null);
        }
    }

    public LocalizedString getEvaluationMethodI18N() {
        LocalizedString result = new LocalizedString();

        if (!StringUtils.isEmpty(getEvaluationMethod())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.PT, getEvaluationMethod());
        }
        if (!StringUtils.isEmpty(getEvaluationMethodEn())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.EN, getEvaluationMethodEn());
        }

        return result;
    }

    public void setEvaluationMethodI18N(final LocalizedString input) {
        if (input != null) {
            setEvaluationMethod(input.getContent(Locale.getDefault()));
            setEvaluationMethodEn(input.getContent(Locale.ENGLISH));
        } else {
            setEvaluationMethod(null);
            setEvaluationMethodEn(null);
        }
    }

    protected CompetenceCourseLoad findLoad(final Integer order) {
        CompetenceCourseLoad result = null;

        for (final CompetenceCourseLoad iter : getCompetenceCourseLoadsSet()) {
            if (iter.getLoadOrder().equals(order)) {
                if (result != null) {
                    throw new DomainException("error.CompetenceCourseInformation.found.duplicate.CompetenceCourseLoad",
                            result.toString(), iter.toString());
                }

                result = iter;
            }
        }

        return result;
    }

    public void delete() {
        setExecutionPeriod(null);
        setCompetenceCourse(null);
        setCompetenceCourseGroupUnit(null);
        setCompetenceCourseLevel(null);
        for (; !getCompetenceCourseLoadsSet().isEmpty(); getCompetenceCourseLoadsSet().iterator().next().delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public BibliographicReference getBibliographicReference(final Integer oid) {
        return getBibliographicReferences().getBibliographicReference(oid);
    }

    public Double getTheoreticalHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getTheoreticalHours();
        }
        return result;
    }

    public Double getProblemsHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getProblemsHours();
        }
        return result;
    }

    public Double getLaboratorialHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getLaboratorialHours();
        }
        return result;
    }

    public Double getSeminaryHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getSeminaryHours();
        }
        return result;
    }

    public Double getFieldWorkHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getFieldWorkHours();
        }
        return result;
    }

    public Double getTrainingPeriodHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getTrainingPeriodHours();
        }
        return result;
    }

    public Double getTutorialOrientationHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getTutorialOrientationHours();
        }
        return result;
    }

    public Double getOtherHours(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getOtherHours();
        }
        return result;
    }

    public Double getAutonomousWorkHours(final Integer order) {
        BigDecimal result = new BigDecimal(0.0);
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result = result.add(new BigDecimal(competenceCourseLoad.getAutonomousWorkHours()));
        }

        result = result.setScale(1, RoundingMode.HALF_EVEN);
        return result.doubleValue();
    }

    public Double getContactLoad(final Integer order) {
        BigDecimal result = new BigDecimal(0.0);
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result = result.add(new BigDecimal(competenceCourseLoad.getContactLoad()));
        }

        result = result.setScale(1, RoundingMode.HALF_EVEN);
        return result.doubleValue();
    }

    public Double getTotalLoad(final Integer order) {
        BigDecimal result = new BigDecimal(0.0);
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result = result.add(new BigDecimal(competenceCourseLoad.getTotalLoad()));
        }

        result = result.setScale(1, RoundingMode.HALF_EVEN);
        return result.doubleValue();
    }

    public double getEctsCredits(final Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
            result += competenceCourseLoad.getEctsCredits();
        }

        return result;
    }

    private List<CompetenceCourseLoadBean> getCompetenceCourseLoadBeans(final Integer order) {

        if (isAnual()) {
            final List<CompetenceCourseLoadBean> result = new ArrayList<>();

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

        return getCompetenceCourseLoadsSet().stream().limit(1).map(ccl -> new CompetenceCourseLoadBean(ccl))
                .collect(Collectors.toList());
    }

    public boolean isAnual() {
        return AcademicPeriod.YEAR.equals(getAcademicPeriod());
    }

//    public boolean isSemestrial() {
//        return getRegime() == RegimeType.SEMESTRIAL;
//    }

    public List<CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequest() {
        final List<CompetenceCourseInformationChangeRequest> requests = new ArrayList<>();
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
        return CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToManageCompetenceCourseInformation(this);
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

}

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

import java.util.Comparator;
import java.util.Locale;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.bennu.core.domain.Bennu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Tania Pousao Created on 30/Out/2003
 */
public class DegreeInfo extends DegreeInfo_Base {

    private static final Logger logger = LoggerFactory.getLogger(DegreeInfo.class);

    public static Comparator<DegreeInfo> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<DegreeInfo>() {
        @Override
        public int compare(DegreeInfo info1, DegreeInfo info2) {
            int result = ExecutionYear.COMPARATOR_BY_YEAR.compare(info1.getExecutionYear(), info2.getExecutionYear());
            if (result != 0) {
                return result;
            }
            return DomainObjectUtil.COMPARATOR_BY_ID.compare(info1, info2);
        }
    };

    protected DegreeInfo(Degree degree, ExecutionYear executionYear) {
        super();
        setRootDomainObject(Bennu.getInstance());

        DegreeInfo degreeInfo = degree.getMostRecentDegreeInfo(executionYear);

        if (degreeInfo != null && degreeInfo.getExecutionYear() == executionYear) {
            throw new DomainException(
                    "error.net.sourceforge.fenixdu.domain.cannot.create.degreeInfo.already.exists.one.for.that.degree.and.executionYear");
        }

        super.setExecutionYear(executionYear);
        super.setName(degree.getNameFor(executionYear));
        super.setDegree(degree);

        new DegreeInfoCandidacy(this);
        new DegreeInfoFuture(this);
    }

    protected DegreeInfo() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ExecutionInterval getExecutionInterval() {
        return getExecutionYear();
    }

    public void setExecutionInterval(final ExecutionInterval input) {
        if (input == null) {
            throw new DomainException("error.DegreeInfo.required.ExecutionInterval");
        }
        super.setExecutionYear(ExecutionInterval.assertExecutionIntervalType(ExecutionYear.class, input));
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (hasSameName(name)) {
            return;
        }

        if (hasName() && !canEdit()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.DegreeInfo.can.only.change.name.for.future.execution.years");
        }
        super.setName(name);
    }

    private boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    private boolean hasSameName(final MultiLanguageString name) {
        return hasName() && getName().equals(name);
    }

    public boolean canEdit() {
        final DegreeCurricularPlan firstDegreeCurricularPlan = getDegree().getFirstDegreeCurricularPlan();
        final DegreeCurricularPlan lastActiveDegreeCurricularPlan = getDegree().getLastActiveDegreeCurricularPlan();
        if (firstDegreeCurricularPlan == null) {
            return true;
        }
        ExecutionYear firstExecutionYear =
                ExecutionYear.readByDateTime(firstDegreeCurricularPlan.getInitialDateYearMonthDay().toDateTimeAtMidnight());
        if (getExecutionYear().isBefore(firstExecutionYear)) {
            return true;
        }
        if (lastActiveDegreeCurricularPlan == null) {
            return true;
        }
        if (lastActiveDegreeCurricularPlan.getExecutionDegreesSet().isEmpty()) {
            return true;
        }
        if (getExecutionYear().isAfter(ExecutionYear.readCurrentExecutionYear())) {
            return true;
        }
        if (getExecutionYear().isCurrent()) {
            return true;
        }
        return false;
    }

    protected DegreeInfo(DegreeInfo degreeInfo, ExecutionYear executionYear) {
        this(degreeInfo.getDegree(), executionYear);

        setName(degreeInfo.getName());
        setDescription(degreeInfo.getDescription());
        setHistory(degreeInfo.getHistory());
        setObjectives(degreeInfo.getObjectives());
        setDesignedFor(degreeInfo.getDesignedFor());
        setProfessionalExits(degreeInfo.getProfessionalExits());
        setOperationalRegime(degreeInfo.getOperationalRegime());
        setGratuity(degreeInfo.getGratuity());
        setAdditionalInfo(degreeInfo.getAdditionalInfo());
        setLinks(degreeInfo.getLinks());

        setTestIngression(degreeInfo.getTestIngression());
        setClassifications(degreeInfo.getClassifications());
        setAccessRequisites(degreeInfo.getAccessRequisites());
        setCandidacyDocuments(degreeInfo.getCandidacyDocuments());
        setDriftsInitial(degreeInfo.getDriftsInitial());
        setDriftsFirst(degreeInfo.getDriftsFirst());
        setDriftsSecond(degreeInfo.getDriftsSecond());
        setMarkMin(degreeInfo.getMarkMin());
        setMarkMax(degreeInfo.getMarkMax());
        setMarkAverage(degreeInfo.getMarkAverage());

        setQualificationLevel(degreeInfo.getQualificationLevel());
        setRecognitions(degreeInfo.getRecognitions());
    }

    public void delete() {
        getDegreeInfoCandidacy().delete();
        getDegreeInfoFuture().delete();

        setRootDomainObject(null);
        setDegree(null);
        setExecutionYear(null);

        deleteDomainObject();
    }

    public MultiLanguageString getAccessRequisites() {
        return getDegreeInfoCandidacy().getAccessRequisites();
    }

    public MultiLanguageString getCandidacyDocuments() {
        return getDegreeInfoCandidacy().getCandidacyDocuments();
    }

    public MultiLanguageString getCandidacyPeriod() {
        return getDegreeInfoCandidacy().getCandidacyPeriod();
    }

    public MultiLanguageString getClassifications() {
        return getDegreeInfoFuture().getClassifications();
    }

    public MultiLanguageString getDesignedFor() {
        return getDegreeInfoFuture().getDesignedFor();
    }

    public String getDesignedFor(final Locale language) {
        return hasDesignedFor(language) ? getDesignedFor().getContent(language) : "";
    }

    public boolean hasDesignedFor(final Locale language) {
        return getDesignedFor() != null && getDesignedFor().hasContent(language);
    }

    public MultiLanguageString getEnrolmentPeriod() {
        return getDegreeInfoCandidacy().getEnrolmentPeriod();
    }

    public MultiLanguageString getObjectives() {
        return getDegreeInfoFuture().getObjectives();
    }

    public boolean hasObjectives(final Locale language) {
        return getObjectives() != null && getObjectives().hasContent(language);
    }

    public String getObjectives(final Locale language) {
        return hasObjectives(language) ? getObjectives().getContent(language) : "";
    }

    public MultiLanguageString getProfessionalExits() {
        return getDegreeInfoFuture().getProfessionalExits();
    }

    public boolean hasProfessionalExits(final Locale language) {
        return getProfessionalExits() != null && getProfessionalExits().hasContent(language);
    }

    public String getProfessionalExits(final Locale language) {
        return hasProfessionalExits(language) ? getProfessionalExits().getContent(language) : "";
    }

    public MultiLanguageString getQualificationLevel() {
        return getDegreeInfoFuture().getQualificationLevel();
    }

    public MultiLanguageString getRecognitions() {
        return getDegreeInfoFuture().getRecognitions();
    }

    public MultiLanguageString getSelectionResultDeadline() {
        return getDegreeInfoCandidacy().getSelectionResultDeadline();
    }

    public MultiLanguageString getTestIngression() {
        return getDegreeInfoCandidacy().getTestIngression();
    }

    public void setAccessRequisites(MultiLanguageString accessRequisites) {
        getDegreeInfoCandidacy().setAccessRequisites(accessRequisites);
    }

    public void setCandidacyDocuments(MultiLanguageString candidacyDocuments) {
        getDegreeInfoCandidacy().setCandidacyDocuments(candidacyDocuments);
    }

    public void setCandidacyPeriod(MultiLanguageString candidacyPeriod) {
        getDegreeInfoCandidacy().setCandidacyPeriod(candidacyPeriod);
    }

    public void setClassifications(MultiLanguageString classifications) {
        getDegreeInfoFuture().setClassifications(classifications);
    }

    public void setDesignedFor(MultiLanguageString designedFor) {
        getDegreeInfoFuture().setDesignedFor(designedFor);
    }

    public void setEnrolmentPeriod(MultiLanguageString enrolmentPeriod) {
        getDegreeInfoCandidacy().setEnrolmentPeriod(enrolmentPeriod);
    }

    public void setObjectives(MultiLanguageString objectives) {
        getDegreeInfoFuture().setObjectives(objectives);
    }

    public void setProfessionalExits(MultiLanguageString professionalExits) {
        getDegreeInfoFuture().setProfessionalExits(professionalExits);
    }

    public void setQualificationLevel(MultiLanguageString qualificationLevel) {
        getDegreeInfoFuture().setQualificationLevel(qualificationLevel);
    }

    public void setRecognitions(MultiLanguageString recognitions) {
        getDegreeInfoFuture().setRecognitions(recognitions);
    }

    public void setSelectionResultDeadline(MultiLanguageString selectionResultDeadline) {
        getDegreeInfoCandidacy().setSelectionResultDeadline(selectionResultDeadline);
    }

    public void setTestIngression(MultiLanguageString testIngression) {
        getDegreeInfoCandidacy().setTestIngression(testIngression);
    }

    public boolean hasOperationalRegime(final Locale language) {
        return getOperationalRegime() != null && getOperationalRegime().hasContent(language);
    }

    public String getOperationalRegime(final Locale language) {
        return hasOperationalRegime(language) ? getOperationalRegime().getContent(language) : "";
    }

    public boolean hasAdditionalInfo(final Locale language) {
        return getAdditionalInfo() != null && getAdditionalInfo().hasContent(language);
    }

    public String getAdditionalInfo(final Locale language) {
        return hasAdditionalInfo(language) ? getAdditionalInfo().getContent(language) : "";
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionYear().getAcademicInterval();
    }
}

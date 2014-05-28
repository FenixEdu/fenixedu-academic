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
package net.sourceforge.fenixedu.domain.credits;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class AnnualTeachingCredits extends AnnualTeachingCredits_Base {

    public AnnualTeachingCredits(Teacher teacher, AnnualCreditsState annualCreditsState) {
        super();
        setTeacher(teacher);
        setAnnualCreditsState(annualCreditsState);
        setHasAnyLimitation(false);
        setCreationDate(new DateTime());
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isPastResume() {
        return getTeachingCredits() == null && getMasterDegreeThesesCredits() == null && getPhdDegreeThesesCredits() == null
                && getProjectsTutorialsCredits() == null && getManagementFunctionCredits() == null && getOthersCredits() == null
                && getCreditsReduction() == null && getServiceExemptionCredits() == null && getAnnualTeachingLoad() == null
                && getYearCredits() == null && getFinalCredits() == null;
    }

    public static AnnualTeachingCredits readByYearAndTeacher(ExecutionYear executionYear, Teacher teacher) {
        if (executionYear != null) {
            for (AnnualTeachingCredits annualTeachingCredits : teacher.getAnnualTeachingCredits()) {
                if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(executionYear)) {
                    return annualTeachingCredits;
                }
            }
        }
        return null;
    }

    public boolean isClosed() {
        return getAnnualCreditsState() != null ? getAnnualCreditsState().getIsCreditsClosed() : false;
    }

    @Atomic
    public void calculateCredits() {
        setMasterDegreeThesesCredits(getTeacher().getMasterDegreeThesesCredits(getAnnualCreditsState().getExecutionYear()));
        setPhdDegreeThesesCredits(getTeacher().getPhdDegreeThesesCredits(getAnnualCreditsState().getExecutionYear()));
        setProjectsTutorialsCredits(getTeacher().getProjectsTutorialsCredits(getAnnualCreditsState().getExecutionYear()));

        BigDecimal teachingCredits = BigDecimal.ZERO;
        BigDecimal managementFunctionsCredits = BigDecimal.ZERO;
        BigDecimal reductionServiceCredits = BigDecimal.ZERO;
        BigDecimal serviceExemptionCredits = BigDecimal.ZERO;
        BigDecimal othersCredits = BigDecimal.ZERO;
        BigDecimal annualTeachingLoad = BigDecimal.ZERO;
        BigDecimal yearCredits = BigDecimal.ZERO;
        BigDecimal yearCreditsForFinalCredits = BigDecimal.ZERO;
        BigDecimal annualTeachingLoadFinalCredits = BigDecimal.ZERO;

        boolean hasOrientantionCredits = false;
        boolean hasFinalAndAccumulatedCredits = false;

        for (ExecutionSemester executionSemester : getAnnualCreditsState().getExecutionYear().getExecutionPeriods()) {
            if (getTeacher().isActiveForSemester(executionSemester) || getTeacher().hasTeacherAuthorization(executionSemester)) {
                BigDecimal thisSemesterManagementFunctionCredits =
                        new BigDecimal(getTeacher().getManagementFunctionsCredits(executionSemester));
                managementFunctionsCredits = managementFunctionsCredits.add(thisSemesterManagementFunctionCredits);
                serviceExemptionCredits =
                        serviceExemptionCredits.add(new BigDecimal(getTeacher().getServiceExemptionCredits(executionSemester)));
                BigDecimal thisSemesterTeachingLoad = new BigDecimal(getTeacher().getMandatoryLessonHours(executionSemester));
                annualTeachingLoad = annualTeachingLoad.add(thisSemesterTeachingLoad).setScale(2, BigDecimal.ROUND_HALF_UP);
                TeacherService teacherService = getTeacher().getTeacherServiceByExecutionPeriod(executionSemester);
                BigDecimal thisSemesterCreditsReduction = BigDecimal.ZERO;
                if (teacherService != null) {
                    teachingCredits = teachingCredits.add(new BigDecimal(teacherService.getTeachingDegreeCredits()));
                    thisSemesterCreditsReduction = teacherService.getReductionServiceCredits();
                    othersCredits = othersCredits.add(new BigDecimal(teacherService.getOtherServiceCredits()));
                }
                reductionServiceCredits = reductionServiceCredits.add(thisSemesterCreditsReduction);
                BigDecimal reductionAndManagement = thisSemesterManagementFunctionCredits.add(thisSemesterCreditsReduction);
                BigDecimal thisSemesterYearCredits = thisSemesterTeachingLoad;
                if (thisSemesterTeachingLoad.compareTo(reductionAndManagement) > 0) {
                    thisSemesterYearCredits = reductionAndManagement;
                } else {
                    setHasAnyLimitation(true);
                }
                yearCredits = yearCredits.add(thisSemesterYearCredits);
                if (canHaveFinalCredits(executionSemester, getTeacher())) {
                    yearCreditsForFinalCredits = yearCreditsForFinalCredits.add(thisSemesterYearCredits);
                    annualTeachingLoadFinalCredits = annualTeachingLoadFinalCredits.add(thisSemesterTeachingLoad);
                    if (executionSemester.getSemester() == 2) {
                        hasFinalAndAccumulatedCredits = true;
                    } else {
                        hasOrientantionCredits = true;
                    }
                }
            }
        }
        setTeachingCredits(teachingCredits);
        setManagementFunctionCredits(managementFunctionsCredits);
        setCreditsReduction(reductionServiceCredits);
        setServiceExemptionCredits(serviceExemptionCredits);
        setOthersCredits(othersCredits);
        setAnnualTeachingLoad(annualTeachingLoad);

        yearCredits = yearCredits.add(teachingCredits).add(serviceExemptionCredits).add(othersCredits);
        yearCreditsForFinalCredits =
                yearCreditsForFinalCredits.add(teachingCredits).add(serviceExemptionCredits).add(othersCredits);
        if (hasOrientantionCredits) {
            yearCredits =
                    yearCredits.add(getMasterDegreeThesesCredits()).add(getPhdDegreeThesesCredits())
                            .add(getProjectsTutorialsCredits()).setScale(2, BigDecimal.ROUND_HALF_UP);
            yearCreditsForFinalCredits =
                    yearCreditsForFinalCredits.add(getMasterDegreeThesesCredits()).add(getPhdDegreeThesesCredits())
                            .add(getProjectsTutorialsCredits());
        }

        setYearCredits(yearCredits);
        BigDecimal accumulatedCredits = BigDecimal.ZERO;
        BigDecimal finalCredits = BigDecimal.ZERO;
        if (hasFinalAndAccumulatedCredits) {
            finalCredits =
                    yearCreditsForFinalCredits.subtract(annualTeachingLoadFinalCredits).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal lastYearAccumulated = getPreviousAccumulatedCredits();
            accumulatedCredits = finalCredits.add(lastYearAccumulated).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        setFinalCredits(finalCredits);
        setAccumulatedCredits(accumulatedCredits);
        setLastModifiedDate(new DateTime());

    }

    private boolean canHaveFinalCredits(ExecutionSemester executionSemester, Teacher teacher) {
        return getTeacher().isActiveForSemester(executionSemester) && !getTeacher().isMonitor(executionSemester);
    }

    private BigDecimal getPreviousAccumulatedCredits() {
        AnnualTeachingCredits previousAnnualTeachingCredits =
                AnnualTeachingCredits.readByYearAndTeacher(getAnnualCreditsState().getExecutionYear().getPreviousExecutionYear(),
                        getTeacher());
        return previousAnnualTeachingCredits != null ? previousAnnualTeachingCredits.getAccumulatedCredits() : BigDecimal.ZERO;
    }

    public AnnualTeachingCreditsDocument getLastTeacherCreditsDocument(Boolean withConfidencialInformation) {
        AnnualTeachingCreditsDocument lastAnnualTeachingCreditsDocument = null;
        for (AnnualTeachingCreditsDocument annualTeachingCreditsDocument : getAnnualTeachingCreditsDocument()) {
            if (annualTeachingCreditsDocument.getHasConfidencialInformation() == withConfidencialInformation
                    && (lastAnnualTeachingCreditsDocument == null || lastAnnualTeachingCreditsDocument.getUploadTime().isBefore(
                            annualTeachingCreditsDocument.getUploadTime()))) {
                lastAnnualTeachingCreditsDocument = annualTeachingCreditsDocument;
            }
        }
        return lastAnnualTeachingCreditsDocument;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.AnnualTeachingCreditsDocument> getAnnualTeachingCreditsDocument() {
        return getAnnualTeachingCreditsDocumentSet();
    }

    @Deprecated
    public boolean hasAnyAnnualTeachingCreditsDocument() {
        return !getAnnualTeachingCreditsDocumentSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnnualTeachingLoad() {
        return getAnnualTeachingLoad() != null;
    }

    @Deprecated
    public boolean hasCreditsReduction() {
        return getCreditsReduction() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeThesesCredits() {
        return getMasterDegreeThesesCredits() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasServiceExemptionCredits() {
        return getServiceExemptionCredits() != null;
    }

    @Deprecated
    public boolean hasPhdDegreeThesesCredits() {
        return getPhdDegreeThesesCredits() != null;
    }

    @Deprecated
    public boolean hasAnnualCreditsState() {
        return getAnnualCreditsState() != null;
    }

    @Deprecated
    public boolean hasProjectsTutorialsCredits() {
        return getProjectsTutorialsCredits() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasManagementFunctionCredits() {
        return getManagementFunctionCredits() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasOthersCredits() {
        return getOthersCredits() != null;
    }

    @Deprecated
    public boolean hasHasAnyLimitation() {
        return getHasAnyLimitation() != null;
    }

    @Deprecated
    public boolean hasYearCredits() {
        return getYearCredits() != null;
    }

    @Deprecated
    public boolean hasTeachingCredits() {
        return getTeachingCredits() != null;
    }

    @Deprecated
    public boolean hasFinalCredits() {
        return getFinalCredits() != null;
    }

    @Deprecated
    public boolean hasAccumulatedCredits() {
        return getAccumulatedCredits() != null;
    }

}

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
/**
 * Dec 5, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherCredits;
import net.sourceforge.fenixedu.domain.TeacherCreditsDocument;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class CreditLineDTO {

    private double teachingDegreeCredits = 0;

    private double supportLessonHours = 0;

    private double masterDegreeCredits = 0;

    private double tfcAdviseCredits = 0;

    private double thesesCredits = 0;

    private double otherCredits = 0;

    private double institutionWorkingHours = 0;

    private double managementCredits = 0;

    private double serviceExemptionCredits = 0;

    private double pastServiceCredits = 0;

    private double balanceOfCredits = 0;

    private double mandatoryLessonHours = 0;

    private double totalCredits = 0;

    private ExecutionSemester executionSemester;

    private Teacher teacher;

    private Set<ExecutionYear> correctionInYears = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);

    public CreditLineDTO(ExecutionSemester executionSemester, TeacherService teacherService, double managementCredits,
            double exemptionCredits, double lessonHours, Teacher teacher, double thesesCredits) throws ParseException {

        setExecutionPeriod(executionSemester);
        if (teacherService != null) {
            setTeachingDegreeCredits(teacherService.getTeachingDegreeCredits());
            setSupportLessonHours(teacherService.getSupportLessonHours());
            setMasterDegreeCredits(teacherService.getMasterDegreeServiceCredits());
            setTfcAdviseCredits(teacherService.getTeacherAdviseServiceCredits());
            setOtherCredits(teacherService.getOtherServiceCredits());
            setInstitutionWorkingHours(teacherService.getInstitutionWorkingHours());
            setPastServiceCredits(teacherService.getPastServiceCredits());
        }
        setThesesCredits(thesesCredits);
        setBalanceOfCredits(teacher.getBalanceOfCreditsUntil(executionSemester.getPreviousExecutionPeriod()));
        setMandatoryLessonHours(lessonHours);
        setManagementCredits(managementCredits);
        setServiceExemptionCredits(exemptionCredits);
        setTeacher(teacher);

        double totalCredits = 0;
        if (!getTeacher().isMonitor(executionSemester)) {
            totalCredits =
                    getTeachingDegreeCredits() + getMasterDegreeCredits() + getTfcAdviseCredits() + getThesesCredits()
                            + getOtherCredits() + getManagementCredits() + getServiceExemptionCredits();
        }
        setTotalCredits(round(totalCredits));

        for (OtherService otherService : executionSemester.getOtherServicesCorrections()) {
            if (otherService.getTeacherService().getTeacher().equals(teacher)
                    && !otherService.getCorrectedExecutionSemester()
                            .equals(otherService.getTeacherService().getExecutionPeriod())) {
                correctionInYears.add(otherService.getTeacherService().getExecutionPeriod().getExecutionYear());
            }
        }
    }

    public CreditLineDTO(ExecutionSemester executionSemester, TeacherCredits teacherCredits) {
        setExecutionPeriod(executionSemester);
        setTeacher(teacherCredits.getTeacher());
        setTeachingDegreeCredits(teacherCredits.getTeachingDegreeCredits().doubleValue());
        setSupportLessonHours(teacherCredits.getSupportLessonHours().doubleValue());
        setMasterDegreeCredits(teacherCredits.getMasterDegreeCredits().doubleValue());
        setTfcAdviseCredits(teacherCredits.getTfcAdviseCredits().doubleValue());
        setOtherCredits(teacherCredits.getOtherCredits().doubleValue());
        setInstitutionWorkingHours(teacherCredits.getInstitutionWorkingHours().doubleValue());
        setPastServiceCredits(teacherCredits.getPastServiceCredits().doubleValue());
        setThesesCredits(teacherCredits.getThesesCredits().doubleValue());
        setBalanceOfCredits(teacherCredits.getBalanceOfCredits().doubleValue());
        setMandatoryLessonHours(teacherCredits.getMandatoryLessonHours().intValue());
        setManagementCredits(teacherCredits.getManagementCredits().doubleValue());
        setServiceExemptionCredits(teacherCredits.getServiceExemptionCredits().doubleValue());
        setTotalCredits(teacherCredits.getTotalCredits().doubleValue());

        for (OtherService otherService : executionSemester.getOtherServicesCorrections()) {
            if (otherService.getTeacherService().getTeacher().equals(teacherCredits.getTeacher())
                    && !otherService.getCorrectedExecutionSemester()
                            .equals(otherService.getTeacherService().getExecutionPeriod())) {
                correctionInYears.add(otherService.getTeacherService().getExecutionPeriod().getExecutionYear());
            }
        }
    }

    public double getFinalLineCredits() {
        return round(totalCredits - getMandatoryLessonHours());
    }

    public double getTotalLineCredits() {
        return round(totalCredits - getMandatoryLessonHours() + getBalanceOfCredits());
    }

    public void setTotalCredits(double totalCredits) {
        this.totalCredits = totalCredits;
    }

    public double getTotalCredits() {
        return round(totalCredits);
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }

    public double getManagementCredits() {
        return managementCredits;
    }

    public void setManagementCredits(double managementCredits) {
        this.managementCredits = managementCredits;
    }

    public double getOtherCredits() {
        return otherCredits;
    }

    public void setOtherCredits(double otherCredits) {
        this.otherCredits = otherCredits;
    }

    public double getServiceExemptionCredits() {
        return serviceExemptionCredits;
    }

    public void setServiceExemptionCredits(double serviceExemptionsCredits) {
        this.serviceExemptionCredits = serviceExemptionsCredits;
    }

    public double getSupportLessonHours() {
        return supportLessonHours;
    }

    public void setSupportLessonHours(double supportLessonHours) {
        this.supportLessonHours = supportLessonHours;
    }

    public double getTeachingDegreeCredits() {
        return teachingDegreeCredits;
    }

    public void setTeachingDegreeCredits(double teachingDegreeCredits) {
        this.teachingDegreeCredits = teachingDegreeCredits;
    }

    public double getTfcAdviseCredits() {
        return tfcAdviseCredits;
    }

    public void setTfcAdviseCredits(double tfcAdviseCredits) {
        this.tfcAdviseCredits = tfcAdviseCredits;
    }

    public double getInstitutionWorkingHours() {
        return institutionWorkingHours;
    }

    public void setInstitutionWorkingHours(double institutionWorkingHours) {
        this.institutionWorkingHours = institutionWorkingHours;
    }

    public double getPastServiceCredits() {
        return pastServiceCredits;
    }

    public void setPastServiceCredits(double pastServiceCredits) {
        this.pastServiceCredits = pastServiceCredits;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public double getMandatoryLessonHours() {
        return mandatoryLessonHours;
    }

    public void setMandatoryLessonHours(double mandatoryLessonHours) {
        this.mandatoryLessonHours = mandatoryLessonHours;
    }

    public double getMasterDegreeCredits() {
        return masterDegreeCredits;
    }

    public void setMasterDegreeCredits(double masterDegreeCredits) {
        this.masterDegreeCredits = masterDegreeCredits;
    }

    public double getBalanceOfCredits() {
        return balanceOfCredits;
    }

    public void setBalanceOfCredits(double balanceOfCredits) {
        this.balanceOfCredits = balanceOfCredits;
    }

    public double getThesesCredits() {
        return thesesCredits;
    }

    public void setThesesCredits(double thesesCredits) {
        this.thesesCredits = thesesCredits;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public boolean isTeacherCreditsClosed() {
        TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(executionSemester, teacher);
        return teacherCredits != null && teacherCredits.getTeacherCreditsState().isCloseState();
    }

    public TeacherCreditsDocument getTeacherCreditsDocument() {
        TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(executionSemester, teacher);
        return teacherCredits != null ? teacherCredits.getLastTeacherCreditsDocument() : null;
    }

    public String getCorrections() {
        StringBuilder result = new StringBuilder();
        for (ExecutionYear executionTear : correctionInYears) {
            result.append("(** ").append(executionTear.getName()).append(") ");
        }
        return result.toString();
    }

    public Set<ExecutionYear> getCorrectionInYears() {
        return correctionInYears;
    }

    public void setCorrectionInYears(Set<ExecutionYear> correctionInYears) {
        this.correctionInYears = correctionInYears;
    }
}

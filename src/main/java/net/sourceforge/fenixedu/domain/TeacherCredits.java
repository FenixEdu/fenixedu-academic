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

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class TeacherCredits extends TeacherCredits_Base {

    public TeacherCredits(Teacher teacher, TeacherCreditsState teacherCreditsState) throws ParseException {
        super();
        setTeacher(teacher);
        setTeacherCreditsState(teacherCreditsState);
        setRootDomainObject(Bennu.getInstance());
        saveTeacherCredits();
    }

    public static TeacherCredits readTeacherCredits(ExecutionSemester executionSemester, Teacher teacher) {
        Set<TeacherCredits> teacherCredits = teacher.getTeacherCreditsSet();
        for (TeacherCredits teacherCredit : teacherCredits) {
            if (teacherCredit.getTeacherCreditsState().getExecutionSemester().equals(executionSemester)) {
                return teacherCredit;
            }
        }
        return null;
    }

    @Atomic
    public static void closeAllTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
        Collection<Teacher> teachers = Bennu.getInstance().getTeachersSet();
        TeacherCreditsState teacherCreditsState = TeacherCreditsState.getTeacherCreditsState(executionSemester);
        if (teacherCreditsState == null) {
            teacherCreditsState = new TeacherCreditsState(executionSemester);
        }
        for (Teacher teacher : teachers) {
            closeTeacherCredits(teacher, teacherCreditsState);
        }
        teacherCreditsState.setCloseState();
    }

    @Atomic
    public static void closeTeacherCredits(Teacher teacher, TeacherCreditsState teacherCreditsState) throws ParseException {
        TeacherCredits teacherCredits = teacher.getTeacherCredits(teacherCreditsState.getExecutionSemester());
        if (teacherCredits == null) {
            new TeacherCredits(teacher, teacherCreditsState);
        } else if (teacherCredits.getTeacherCreditsState().isOpenState()) {
            teacherCredits.saveTeacherCredits();
        }
    }

    @Atomic
    public static void openAllTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
        TeacherCreditsState teacherCreditsState = TeacherCreditsState.getTeacherCreditsState(executionSemester);
        teacherCreditsState.setOpenState();
    }

    @Atomic
    public void editTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
        saveTeacherCredits();
    }

    private void saveTeacherCredits() throws ParseException {
        Teacher teacher = getTeacher();
        ExecutionSemester executionSemester = getTeacherCreditsState().getExecutionSemester();
        setProfessionalCategory(teacher.getCategoryByPeriod(executionSemester));
        double managementCredits = teacher.getManagementFunctionsCredits(executionSemester);
        double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionSemester);
        double thesesCredits = teacher.getThesesCredits(executionSemester);
        double mandatoryLessonHours = teacher.getMandatoryLessonHours(executionSemester);
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        setTeacherService(teacherService);
        setThesesCredits(new BigDecimal(thesesCredits));
        setBalanceOfCredits(new BigDecimal(teacher.getBalanceOfCreditsUntil(executionSemester.getPreviousExecutionPeriod())));
        setMandatoryLessonHours(new BigDecimal(mandatoryLessonHours));
        setManagementCredits(new BigDecimal(managementCredits));
        setServiceExemptionCredits(new BigDecimal(serviceExemptionsCredits));

        double totalCredits = 0;
        if (!getTeacher().isMonitor(executionSemester)) {
            totalCredits =
                    getTeachingDegreeCredits().doubleValue() + getMasterDegreeCredits().doubleValue()
                            + getTfcAdviseCredits().doubleValue() + thesesCredits + getOtherCredits().doubleValue()
                            + managementCredits + serviceExemptionsCredits;
        }
        setTotalCredits(new BigDecimal(totalCredits));

        addTeacherCreditsDocument(new TeacherCreditsDocument(teacher, executionSemester, teacherService));
        setBasicOperations();
    }

    private void setBasicOperations() {
        setPerson(AccessControl.getPerson());
        setLastModifiedDate(new DateTime());
    }

    private void setTeacherService(TeacherService teacherService) throws ParseException {
        if (teacherService != null) {
            setTeachingDegreeCredits(new BigDecimal(teacherService.getTeachingDegreeCredits()));
            setSupportLessonHours(new BigDecimal(teacherService.getSupportLessonHours()));
            setMasterDegreeCredits(new BigDecimal(teacherService.getMasterDegreeServiceCredits()));
            setTfcAdviseCredits(new BigDecimal(teacherService.getTeacherAdviseServiceCredits()));
            setOtherCredits(new BigDecimal(teacherService.getOtherServiceCredits()));
            setInstitutionWorkingHours(new BigDecimal(teacherService.getInstitutionWorkingHours()));
            setPastServiceCredits(new BigDecimal(teacherService.getPastServiceCredits()));
        } else {
            setTeachingDegreeCredits(new BigDecimal(0));
            setSupportLessonHours(new BigDecimal(0));
            setMasterDegreeCredits(new BigDecimal(0));
            setTfcAdviseCredits(new BigDecimal(0));
            setOtherCredits(new BigDecimal(0));
            setInstitutionWorkingHours(new BigDecimal(0));
            setPastServiceCredits(new BigDecimal(0));
        }
    }

    public TeacherCreditsDocument getLastTeacherCreditsDocument() {
        TeacherCreditsDocument lastTeacherCreditsDocument = null;
        for (TeacherCreditsDocument teacherCreditsDocument : getTeacherCreditsDocument()) {
            if (lastTeacherCreditsDocument == null
                    || lastTeacherCreditsDocument.getUploadTime().isBefore(teacherCreditsDocument.getUploadTime())) {
                lastTeacherCreditsDocument = teacherCreditsDocument;
            }
        }
        return lastTeacherCreditsDocument;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherCreditsDocument> getTeacherCreditsDocument() {
        return getTeacherCreditsDocumentSet();
    }

    @Deprecated
    public boolean hasAnyTeacherCreditsDocument() {
        return !getTeacherCreditsDocumentSet().isEmpty();
    }

    @Deprecated
    public boolean hasOtherCredits() {
        return getOtherCredits() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
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
    public boolean hasManagementCredits() {
        return getManagementCredits() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategory() {
        return getProfessionalCategory() != null;
    }

    @Deprecated
    public boolean hasTotalCredits() {
        return getTotalCredits() != null;
    }

    @Deprecated
    public boolean hasSupportLessonHours() {
        return getSupportLessonHours() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeCredits() {
        return getMasterDegreeCredits() != null;
    }

    @Deprecated
    public boolean hasTfcAdviseCredits() {
        return getTfcAdviseCredits() != null;
    }

    @Deprecated
    public boolean hasPastServiceCredits() {
        return getPastServiceCredits() != null;
    }

    @Deprecated
    public boolean hasTeachingDegreeCredits() {
        return getTeachingDegreeCredits() != null;
    }

    @Deprecated
    public boolean hasThesesCredits() {
        return getThesesCredits() != null;
    }

    @Deprecated
    public boolean hasMandatoryLessonHours() {
        return getMandatoryLessonHours() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasTeacherCreditsState() {
        return getTeacherCreditsState() != null;
    }

    @Deprecated
    public boolean hasBalanceOfCredits() {
        return getBalanceOfCredits() != null;
    }

    @Deprecated
    public boolean hasInstitutionWorkingHours() {
        return getInstitutionWorkingHours() != null;
    }

}

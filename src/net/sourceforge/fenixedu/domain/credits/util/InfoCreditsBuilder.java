/*
 * Created on Sep 14, 2004
 */
package net.sourceforge.fenixedu.domain.credits.util;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.DatePeriodBaseCreditLine;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public abstract class InfoCreditsBuilder {

    public static InfoCredits build(final Teacher teacher, final ExecutionPeriod executionPeriod) {
        InfoCredits infoCredits = new InfoCredits();
        calculateDegreeAndMasterDegree(teacher, executionPeriod, infoCredits);
        calculateDegreeFinalProjectStudents(teacher, executionPeriod, infoCredits);
        calculeInstitutionWorkingTime(teacher, executionPeriod, infoCredits);
        calculateOtherCreditLine(teacher, executionPeriod, infoCredits);

        List list = teacher.getManagementPositions();

        boolean exists = CollectionUtils.exists(list, new Predicate() {

            public boolean evaluate(Object input) {
                DatePeriodBaseCreditLine creditLine = (DatePeriodBaseCreditLine) input;
                return creditLine.belongsToExecutionPeriod(executionPeriod);
            }
        });
        infoCredits.setContainsManagementPositions(new Boolean(exists));

        list = teacher.getServiceExemptionSituations();
        exists = CollectionUtils.exists(list, new Predicate() {

            public boolean evaluate(Object input) {
                TeacherServiceExemption serviceExemption = (TeacherServiceExemption) input;
                return serviceExemption.belongsToPeriod(executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay());
            }
        });
        infoCredits.setContainsServiceExemptionsSituations(new Boolean(exists));
        return infoCredits;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param infoCredits
     */
    private static void calculateOtherCreditLine(Teacher teacher, ExecutionPeriod executionPeriod,
            InfoCredits infoCredits) {
        double credits = 0;
        List list = teacher.getOtherTypeCreditLines();
        for (int i = 0; i < list.size(); i++) {
            OtherTypeCreditLine creditLine = (OtherTypeCreditLine) list.get(i);
            if (creditLine.getExecutionPeriod().equals(executionPeriod)) {
                credits += creditLine.getCredits().doubleValue();
            }
        }
        infoCredits.setOtherTypeCredits(round(credits));
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param infoCredits
     */
    private static void calculeInstitutionWorkingTime(Teacher teacher,
            ExecutionPeriod executionPeriod, InfoCredits infoCredits) {
        List list = teacher.getInstitutionWorkTimePeriods();
        double credits = 0;
        for (int i = 0; i < list.size(); i++) {
            TeacherInstitutionWorkTime workTime = (TeacherInstitutionWorkTime) list.get(i);
            if (workTime.getExecutionPeriod().equals(executionPeriod)) {
                credits += workTime.hours();
            }
        }
        infoCredits.setInstitutionWorkTime(round(credits));
    }

    /**
     * @param teacher
     * @param executionPeriod2
     */
    private static void calculateDegreeFinalProjectStudents(Teacher teacher,
            ExecutionPeriod executionPeriod, InfoCredits infoCredits) {
        List students = teacher.getDegreeFinalProjectStudents();
        double credits = 0;
        for (int i = 0; i < students.size(); i++) {
            TeacherDegreeFinalProjectStudent student = (TeacherDegreeFinalProjectStudent) students
                    .get(i);
            if (student.getExecutionPeriod().equals(executionPeriod)) {
                credits += student.getPercentage().doubleValue() / 100;
            }
        }
        infoCredits.setDegreeFinalProjectStudents(round(credits));
    }

    /**
     * @param teacher
     * @param executionPeriod2
     * @param infoCredits
     */
    private static void calculateDegreeAndMasterDegree(Teacher teacher,
            ExecutionPeriod executionPeriod, InfoCredits infoCredits) {
        List professorships = teacher.getProfessorships();
        double masterDegreeCredits = 0;
        double degreeCredits = 0;
        double supportLessonsCredits = 0;
        for (int i = 0; i < professorships.size(); i++) {
            Professorship professorship = (Professorship) professorships.get(i);
            ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                if (!executionCourse.isMasterDegreeDFAOrDEAOnly()) {
                    List shiftProfessorships = professorship.getAssociatedShiftProfessorship();
                    for (int j = 0; j < shiftProfessorships.size(); j++) {
                        ShiftProfessorship shiftProfessorship = (ShiftProfessorship) shiftProfessorships
                                .get(j);
                        degreeCredits += shiftProfessorship.getShift().hours()
                                * (shiftProfessorship.getPercentage().doubleValue() / 100);
                    }
                } else {
                    if (professorship.getHours() != null) {
                        masterDegreeCredits += professorship.getHours().doubleValue();
                    }

                }
                List supportLessons = professorship.getSupportLessons();
                for (int j = 0; j < supportLessons.size(); j++) {
                    SupportLesson supportLesson = (SupportLesson) supportLessons.get(j);
                    supportLessonsCredits += supportLesson.hours();
                }
            }
        }
        infoCredits.setSupportLessons(round(supportLessonsCredits));
        infoCredits.setLessons(round(degreeCredits));
        infoCredits.setMasterDegreeCredits(round(masterDegreeCredits));
    }

    private static Double round(double n) {
        long rounded = Math.round(n * 100);
        return new Double(rounded / 100.0);
    }
}
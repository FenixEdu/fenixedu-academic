/*
 * Created on Sep 14, 2004
 */
package Dominio.credits.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.credits.InfoCredits;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IShiftProfessorship;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.credits.IDatePeriodBasedCreditLine;
import Dominio.credits.IOtherTypeCreditLine;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;

public abstract class InfoCreditsBuilder {

    public static InfoCredits build(final ITeacher teacher, final IExecutionPeriod executionPeriod) {
        InfoCredits infoCredits = new InfoCredits();
        calculateDegreeAndMasterDegree(teacher, executionPeriod, infoCredits);
        calculateDegreeFinalProjectStudents(teacher, executionPeriod, infoCredits);
        calculeInstitutionWorkingTime(teacher, executionPeriod, infoCredits);
        calculateOtherCreditLine(teacher, executionPeriod, infoCredits);

        List list = teacher.getManagementPositions();

        boolean exists = CollectionUtils.exists(list, new Predicate() {

            public boolean evaluate(Object input) {
                IDatePeriodBasedCreditLine creditLine = (IDatePeriodBasedCreditLine) input;
                return creditLine.belongsTo(executionPeriod);
            }
        });
        infoCredits.setContainsManagementPositions(new Boolean(exists));

        list = teacher.getServiceExemptionSituations();
        exists = CollectionUtils.exists(list, new Predicate() {

            public boolean evaluate(Object input) {
                IDatePeriodBasedCreditLine creditLine = (IDatePeriodBasedCreditLine) input;
                return creditLine.belongsTo(executionPeriod);
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
    private static void calculateOtherCreditLine(ITeacher teacher, IExecutionPeriod executionPeriod,
            InfoCredits infoCredits) {
        double credits = 0;
        List list = teacher.getOtherTypeCreditLines();
        for (int i = 0; i < list.size(); i++) {
            IOtherTypeCreditLine creditLine = (IOtherTypeCreditLine) list.get(i);
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
    private static void calculeInstitutionWorkingTime(ITeacher teacher,
            IExecutionPeriod executionPeriod, InfoCredits infoCredits) {
        List list = teacher.getInstitutionWorkTimePeriods();
        double credits = 0;
        for (int i = 0; i < list.size(); i++) {
            ITeacherInstitutionWorkTime workTime = (ITeacherInstitutionWorkTime) list.get(i);
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
    private static void calculateDegreeFinalProjectStudents(ITeacher teacher,
            IExecutionPeriod executionPeriod, InfoCredits infoCredits) {
        List students = teacher.getDegreeFinalProjectStudents();
        double credits = 0;
        for (int i = 0; i < students.size(); i++) {
            ITeacherDegreeFinalProjectStudent student = (ITeacherDegreeFinalProjectStudent) students
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
    private static void calculateDegreeAndMasterDegree(ITeacher teacher,
            IExecutionPeriod executionPeriod, InfoCredits infoCredits) {
        List professorships = teacher.getProfessorships();
        double masterDegreeCredits = 0;
        double degreeCredits = 0;
        double supportLessonsCredits = 0;
        for (int i = 0; i < professorships.size(); i++) {
            IProfessorship professorship = (IProfessorship) professorships.get(i);
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                if (!executionCourse.isMasterDegreeOnly()) {
                    List shiftProfessorships = professorship.getAssociatedShiftProfessorship();
                    for (int j = 0; j < shiftProfessorships.size(); j++) {
                        IShiftProfessorship shiftProfessorship = (IShiftProfessorship) shiftProfessorships
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
                    ISupportLesson supportLesson = (ISupportLesson) supportLessons.get(j);
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
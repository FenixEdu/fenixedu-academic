/*
 * Created on 2003/10/20
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana & Ricardo
 * @author Pedro Santos & Rita Carvalho
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriodAndExams;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadFilteredExamsMap implements IService {

    public class ExamsPeriodUndefined extends FenixServiceException {
        private static final long serialVersionUID = 1L;
    }

    public InfoExamsMap run(InfoExecutionDegree infoExecutionDegree, List curricularYears,
            InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException, ExcepcaoPersistencia {

        // Object to be returned
        InfoExamsMap infoExamsMap = new InfoExamsMap();

        // Set Execution Degree
        infoExamsMap.setInfoExecutionDegree(infoExecutionDegree);
        infoExamsMap.setInfoExecutionPeriod(infoExecutionPeriod);

        // Set List of Curricular Years
        infoExamsMap.setCurricularYears(curricularYears);

        // Translate to execute following queries
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();

        IExecutionDegree executionDegree = (IExecutionDegree) sp.getIPersistentExecutionDegree()
                .readByOID(ExecutionDegree.class, infoExecutionDegree.getIdInternal());

        IPeriod period = null;

        if (infoExecutionPeriod.getSemester().equals(new Integer(1))) {
            period = executionDegree.getPeriodExamsFirstSemester();
        } else {
            period = executionDegree.getPeriodExamsSecondSemester();
        }

        if (period == null) {
            throw new ExamsPeriodUndefined();
        }

        Calendar startSeason1 = period.getStartDate();
        Calendar endSeason2 = period.getEndDateOfComposite();
        // The calendar must start at a monday
        if (startSeason1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            int shiftDays = Calendar.MONDAY - startSeason1.get(Calendar.DAY_OF_WEEK);
            startSeason1.add(Calendar.DATE, shiftDays);
        }

        // Set Exam Season info
        infoExamsMap.setStartSeason1(startSeason1);
        infoExamsMap.setEndSeason1(null);
        infoExamsMap.setStartSeason2(null);
        infoExamsMap.setEndSeason2(endSeason2);

        // List of execution courses
        List infoExecutionCourses = new ArrayList();

        // Obtain execution courses and associated information
        // of the given execution degree for each curricular year specified
        for (int i = 0; i < curricularYears.size(); i++) {
            // Obtain list os execution courses
            List executionCourses = sp.getIPersistentExecutionCourse()
                    .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                            (Integer) curricularYears.get(i), infoExecutionPeriod.getSemester(),
                            executionDegree.getDegreeCurricularPlan().getName(),
                            executionDegree.getDegreeCurricularPlan().getDegree().getSigla(),
                            infoExecutionPeriod.getIdInternal());

            // For each execution course obtain curricular courses and
            // exams
            for (int j = 0; j < executionCourses.size(); j++) {
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriodAndExams
                        .newInfoFromDomain((IExecutionCourse) executionCourses.get(j));

                infoExecutionCourse.setCurricularYear((Integer) curricularYears.get(i));

                List associatedInfoCurricularCourses = new ArrayList();
                List associatedCurricularCourses = ((IExecutionCourse) executionCourses.get(j))
                        .getAssociatedCurricularCourses();

                // Curricular courses
                for (int k = 0; k < associatedCurricularCourses.size(); k++) {
                    InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                            .newInfoFromDomain((ICurricularCourse) associatedCurricularCourses.get(k));
                    associatedInfoCurricularCourses.add(infoCurricularCourse);
                }
                infoExecutionCourse.setAssociatedInfoCurricularCourses(associatedInfoCurricularCourses);

                List associatedInfoExams = new ArrayList();
                List associatedExams = ((IExecutionCourse) executionCourses.get(j)).getAssociatedExams();
                // Exams
                for (int k = 0; k < associatedExams.size(); k++) {
                    if (!(associatedExams.get(k) instanceof IExam)) {
                        continue;
                    }

                    InfoExam infoExam = InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear
                            .newInfoFromDomain((IExam) associatedExams.get(k));
                    int numberOfStudentsForExam = 0;
                    List curricularCourseIDs = new ArrayList();
                    for (int l = 0; l < infoExam.getAssociatedCurricularCourseScope().size(); l++) {
                        InfoCurricularCourseScope scope = (InfoCurricularCourseScope) infoExam
                                .getAssociatedCurricularCourseScope().get(l);
                        if (scope != null) {
                            InfoCurricularCourse infoCurricularCourse = scope.getInfoCurricularCourse();
                            if (!curricularCourseIDs.contains(infoCurricularCourse.getIdInternal())) {
                                curricularCourseIDs.add(infoCurricularCourse.getIdInternal());
                                int numberEnroledStudentsInCurricularCourse = persistentEnrolment
                                        .countEnrolmentsByCurricularCourseAndExecutionPeriod(
                                                infoCurricularCourse.getIdInternal(),
                                                infoExecutionPeriod.getIdInternal());

                                numberOfStudentsForExam += numberEnroledStudentsInCurricularCourse;
                            }
                        }
                    }

                    infoExam.setEnrolledStudents(new Integer(numberOfStudentsForExam));

                    List associatedCurricularCourseScope = new ArrayList();
                    associatedCurricularCourseScope = infoExam.getAssociatedCurricularCourseScope();

                    for (int h = 0; h < associatedCurricularCourseScope.size(); h++) {
                        InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) associatedCurricularCourseScope
                                .get(h);
                        boolean isCurricularYearEqual = false;

                        if (infoCurricularCourseScope != null) {
                            InfoCurricularYear infoCurricularYear = infoCurricularCourseScope
                                    .getInfoCurricularSemester().getInfoCurricularYear();

                            isCurricularYearEqual = infoCurricularYear.getYear().equals(
                                    curricularYears.get(i));

                            // obter o curricular plan a partir do curricular
                            // course scope
                            InfoDegreeCurricularPlan degreeCurricularPlanFromScope = infoCurricularCourseScope
                                    .getInfoCurricularCourse().getInfoDegreeCurricularPlan();

                            // obter o curricular plan a partir do info degree
                            InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree
                                    .getInfoDegreeCurricularPlan();

                            boolean isCurricularPlanEqual = degreeCurricularPlanFromScope
                                    .equals(infoDegreeCurricularPlan);

                            if (isCurricularYearEqual && isCurricularPlanEqual
                                    && !associatedInfoExams.contains(infoExam)) {
                                associatedInfoExams.add(infoExam);
                                break;
                            }
                        }
                    }
                }
                infoExecutionCourse.setAssociatedInfoExams(associatedInfoExams);

                infoExecutionCourses.add(infoExecutionCourse);
            }
        }
        infoExamsMap.setExecutionCourses(infoExecutionCourses);

        return infoExamsMap;
    }

}

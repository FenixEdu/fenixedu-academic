/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoCurso;
import net.sourceforge.fenixedu.util.enrollment.CurricularCourseEnrollmentType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class WriteEnrollmentsList implements IService {
    public WriteEnrollmentsList() {
    }

    public void run(InfoStudent infoStudent, TipoCurso degreeType, Integer executionPeriodId,
            List curricularCoursesList, Map optionalEnrollments, IUserView userView)
            throws FenixServiceException {
        try {
            if (infoStudent == null || infoStudent.getNumber() == null || degreeType == null
                    || executionPeriodId == null ) {
                throw new FenixServiceException("");
            }

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();
            IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(), degreeType);
            if (studentCurricularPlan == null) {
                throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
            }

            if (curricularCoursesList != null && curricularCoursesList.size() > 0) {
                ListIterator iterator = curricularCoursesList.listIterator();
                while (iterator.hasNext()) {
                    String enrollmentInfo = (String) iterator.next();

                    Integer curricularCourseID = new Integer(enrollmentInfo.split("-")[0]);
                    //currently ignoring this value
                    //Integer enrollmentType = new
                    // Integer(enrollmentInfo.split("-")[0]);

                   /* IExecutionPeriod executionPeriod = findExecutionPeriod(sp, executionYear,
                            curricularCourseID, degreeType);
                    Integer executionPeriodId = null;
                    if (executionPeriod != null) {
                        executionPeriodId = executionPeriod.getIdInternal();
                    }*/

                    WriteEnrollment writeEnrollmentService = new WriteEnrollment();
                    writeEnrollmentService.run(null, studentCurricularPlan.getIdInternal(),
                            curricularCourseID, executionPeriodId,
                            CurricularCourseEnrollmentType.VALIDATED, new Integer(
                                    (String) optionalEnrollments.get(curricularCourseID.toString())),
                            userView);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

    }

    public static IExecutionPeriod findExecutionPeriod(ISuportePersistente sp,
            IExecutionYear executionYear, Integer curricularCourseID, TipoCurso degreeType)
            throws ExcepcaoPersistencia, FenixServiceException {
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = null;

        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseID);

        if (curricularCourse == null) {
            throw new FenixServiceException("");
        }

        List curricularCourseScopes = curricularCourse.getScopes();

        Integer semester = findBestSemester(persistentExecutionPeriod, curricularCourseScopes);
        if (semester != null) {
            executionPeriod = persistentExecutionPeriod.readBySemesterAndExecutionYear(semester,
                    executionYear);
        }

        return executionPeriod;

    }

    private static Integer findBestSemester(IPersistentExecutionPeriod persistentExecutionPeriod,
            List curricularCourseScopes) throws ExcepcaoPersistencia {
        Integer semester = null;
        if (curricularCourseScopes != null && curricularCourseScopes.size() > 0) {
            ListIterator iterator = curricularCourseScopes.listIterator();
            if (iterator.hasNext()) {
                //inicialize semester with the first scope
                semester = ((ICurricularCourseScope) iterator.next()).getCurricularSemester()
                        .getSemester();

                while (iterator.hasNext()) {
                    ICurricularCourseScope scope = (ICurricularCourseScope) iterator.next();
                    //if all scope have the same semester the semester correct,
                    //it does not have doubts
                    if (scope.getCurricularSemester() != null
                            && scope.getCurricularSemester().getSemester() != null
                            && !scope.getCurricularSemester().getSemester().equals(semester)) {
                        //if all scope don't have the same semester the
                        // semester correct,
                        //the semester choosen is the actual semester
                        IExecutionPeriod executionPeriod = persistentExecutionPeriod
                                .readActualExecutionPeriod();
                        if (executionPeriod != null) {
                            semester = executionPeriod.getSemester();
                            break;
                        }
                    }
                }
            }
        }
        return semester;
    }
}
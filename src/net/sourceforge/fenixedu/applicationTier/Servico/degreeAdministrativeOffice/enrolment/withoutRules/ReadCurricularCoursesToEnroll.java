/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoCurricularCourse2Enroll;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoCurricularCourse2EnrollWithInfoCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import net.sourceforge.fenixedu.util.enrollment.CurricularCourseEnrollmentType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadCurricularCoursesToEnroll implements IService {
    private static final int MAX_CURRICULAR_YEARS = 5;

    private static final int MAX_CURRICULAR_SEMESTERS = 2;

    public ReadCurricularCoursesToEnroll() {
    }

    public Object run(InfoStudent infoStudent, TipoCurso degreeType,
            Integer executionPeriodID, Integer executionDegreeID, List curricularYearsList,
            List curricularSemestersList) throws FenixServiceException {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(ExecutionPeriod.class, executionPeriodID);

            if (infoStudent == null || infoStudent.getNumber() == null) {
                throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
            }
            final IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(), degreeType);

            if (studentCurricularPlan == null) {
                throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
            }

            //Execution Degree
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeID);
            if (executionDegree == null) {
                throw new FenixServiceException("error.degree.noData");
            }

            //Degree Curricular Plan
            IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getCurricularPlan();

            if (degreeCurricularPlan == null || degreeCurricularPlan.getCurricularCourses() == null) {
                throw new FenixServiceException("error.degree.noData");
            }

            // filters a list of curricular courses by all of its scopes that
            // matters in relation to the selected semester and the selected
            // year.
            List curricularCoursesFromDegreeCurricularPlan = null;
            final List curricularYearsListFinal = verifyYears(curricularYearsList);
            final List curricularSemestersListFinal = verifySemesters(curricularSemestersList);

            curricularCoursesFromDegreeCurricularPlan = degreeCurricularPlan.getCurricularCourses();
            curricularCoursesFromDegreeCurricularPlan = (List) CollectionUtils.select(
                    curricularCoursesFromDegreeCurricularPlan, new Predicate() {
                        public boolean evaluate(Object arg0) {
                            return !studentCurricularPlan
                                    .isCurricularCourseApproved((ICurricularCourse) arg0)

                                    && !studentCurricularPlan
                                            .isCurricularCourseEnrolled((ICurricularCourse) arg0);
                        }
                    });
            if (!((curricularYearsList == null || curricularYearsList.size() <= 0) && (curricularSemestersList == null || curricularSemestersList
                    .size() <= 0))) {

                curricularCoursesFromDegreeCurricularPlan = (List) CollectionUtils.select(
                        curricularCoursesFromDegreeCurricularPlan, new Predicate() {
                            public boolean evaluate(Object arg0) {
                                boolean result = false;

                                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                                List scopes = curricularCourse.getScopes();
                                Iterator iter = scopes.iterator();
                                while (iter.hasNext() && !result) {
                                    ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                                    if (curricularSemestersListFinal.contains(scope
                                            .getCurricularSemester().getSemester())
                                            && curricularYearsListFinal.contains(scope
                                                    .getCurricularSemester().getCurricularYear()
                                                    .getYear())) {
                                        result = true;
                                    }
                                }
                                return result;
                            }
                        });
            }

            curricularCoursesFromDegreeCurricularPlan = (List) CollectionUtils.collect(
                    curricularCoursesFromDegreeCurricularPlan, new Transformer() {

                        public Object transform(Object arg0) {
                            CurricularCourse2Enroll curricularCourse2Enroll = null;

                            curricularCourse2Enroll = new CurricularCourse2Enroll();
                            curricularCourse2Enroll.setCurricularCourse((ICurricularCourse) arg0);

                            curricularCourse2Enroll
                                    .setEnrollmentType(CurricularCourseEnrollmentType.DEFINITIVE);
                            return curricularCourse2Enroll;
                        }
                    });

            infoStudentEnrolmentContext = buildResult(studentCurricularPlan,
                    curricularCoursesFromDegreeCurricularPlan, executionPeriod);
            if (infoStudentEnrolmentContext == null) {
                throw new FenixServiceException("");
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoStudentEnrolmentContext;
    }

    private List verifyYears(List curricularYearsList) {
        if (curricularYearsList != null && curricularYearsList.size() > 0) {
            return curricularYearsList;
        }

        return getListOfChosenCurricularYears();
    }

    private List getListOfChosenCurricularYears() {
        List result = new ArrayList();

        for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++) {
            result.add(new Integer(i));
        }
        return result;
    }

    private List verifySemesters(List curricularSemestersList) {
        if (curricularSemestersList != null && curricularSemestersList.size() > 0) {
            return curricularSemestersList;
        }

        return getListOfChosenCurricularSemesters();
    }

    private List getListOfChosenCurricularSemesters() {
        List result = new ArrayList();

        for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++) {
            result.add(new Integer(i));
        }
        return result;
    }

    private InfoStudentEnrollmentContext buildResult(IStudentCurricularPlan studentCurricularPlan,
            List curricularCoursesToChoose, IExecutionPeriod executionPeriod) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlanWithInfoStudent
                .newInfoFromDomain(studentCurricularPlan);
        
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod);

        List infoCurricularCoursesToChoose = new ArrayList();
        if (curricularCoursesToChoose != null && curricularCoursesToChoose.size() > 0) {
            infoCurricularCoursesToChoose = (List) CollectionUtils.collect(curricularCoursesToChoose,
                    new Transformer() {
                        public Object transform(Object input) {
                            CurricularCourse2Enroll curricularCourse = (CurricularCourse2Enroll) input;
                            return InfoCurricularCourse2EnrollWithInfoCurricularCourse
                                    .newInfoFromDomain(curricularCourse);
                        }
                    });
            Collections.sort(infoCurricularCoursesToChoose, new Comparator() {
                public int compare(Object o1, Object o2) {
                    InfoCurricularCourse2Enroll obj1 = (InfoCurricularCourse2Enroll) o1;
                    InfoCurricularCourse2Enroll obj2 = (InfoCurricularCourse2Enroll) o2;
                    return obj1.getInfoCurricularCourse().getName().compareTo(
                            obj2.getInfoCurricularCourse().getName());
                }
            });
        }

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
        infoStudentEnrolmentContext
                .setFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(infoCurricularCoursesToChoose);
        infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentNumber
     * @return IStudent
     * @throws ExcepcaoPersistencia
     */
    protected IStudent getStudent(Integer studentNumber) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent studentDAO = persistentSuport.getIPersistentStudent();

        return studentDAO.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
    }

    /**
     * @param student
     * @return IStudentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    protected IStudentCurricularPlan getStudentCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSuport
                .getIStudentCurricularPlanPersistente();

        return studentCurricularPlanDAO.readActiveStudentCurricularPlan(student.getNumber(), student
                .getDegreeType());
    }
}
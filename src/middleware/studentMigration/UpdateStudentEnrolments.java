package middleware.studentMigration;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourseOutsideStudentDegree;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Frequenta;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudentEnrolments
{
    private static IExecutionPeriod executionPeriod = null;

    public static void main(String args[]) throws Exception
    {
        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWAluno persistentMWAluno = mws.getIPersistentMWAluno();
        IPersistentMWEnrolment persistentEnrolment = mws.getIPersistentMWEnrolment();
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        sp.iniciarTransaccao();

        executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();

        Integer numberOfStudents = persistentMWAluno.countAll();
        sp.confirmarTransaccao();

        int numberOfElementsInSpan = 100;
        int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
        numberOfSpans =
            numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;

        for (int span = 0; span < numberOfSpans; span++)
        {
            sp.iniciarTransaccao();
            sp.clearCache();
            System.out.println("[INFO] Reading MWStudents...");
            List result =
                persistentMWAluno.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));

            sp.confirmarTransaccao();

            System.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");

            Iterator iterator = result.iterator();
            while (iterator.hasNext())
            {
                MWAluno oldStudent = (MWAluno) iterator.next();
                try
                {
                    sp.iniciarTransaccao();
                    // Read all the MWEnrolments.
                    oldStudent.setEnrolments(
                        persistentEnrolment.readByStudentNumber(oldStudent.getNumber()));
                    UpdateStudentEnrolments.updateStudentEnrolment(oldStudent, sp);
                    sp.confirmarTransaccao();
                } catch (Exception e)
                {
                }
            }
        }

        ReportEnrolment.report(new PrintWriter(System.out, true));
    }

    /**
     * @param oldStudent
     * @param sp
     * @throws Exception
     */
    private static void updateStudentEnrolment(MWAluno oldStudent, ISuportePersistente sp)
        throws Exception
    {
        try
        {
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            // Read Fenix Student.
            IStudent student =
                persistentStudent.readByNumero(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

            if (student == null)
            {
                System.out.println(
                    "[ERROR 01] Cannot find Fenix student with number ["
                        + oldStudent.getNumber()
                        + "]!");
                return;
            }

            IStudentCurricularPlan studentCurricularPlan =
                sp.getIStudentCurricularPlanPersistente().readActiveByStudentNumberAndDegreeType(
                    student.getNumber(),
                    TipoCurso.LICENCIATURA_OBJ);

            if (studentCurricularPlan == null)
            {
                System.out.println(
                    "[ERROR 02] Cannot find Fenix StudentCurricularPlan for student number ["
                        + oldStudent.getNumber()
                        + "]!");
                return;
            }

            List studentEnrolments =
                sp.getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);

            // Find the Fenix Enrolments that must be deleted because they no longer exist.
            List enrolments2Annul =
                getEnrolments2Annul(oldStudent, studentEnrolments, oldStudent.getEnrolments(), sp);

            // Find the new Enrolments that must be writen to Fenix.
            List enrolments2Write =
                getEnrolments2Write(
                    studentEnrolments,
                    oldStudent.getEnrolments(),
                    studentCurricularPlan,
                    sp);

            // Delete the Enrolments.
            annulEnrolments(enrolments2Annul, sp);

            // Create the new ones.
            writeEnrolments(enrolments2Write, studentCurricularPlan, oldStudent, sp);

        } catch (Exception e)
        {
            System.out.println(
                "[ERROR 10] Exception migrating student [" + oldStudent.getNumber() + "] enrolments!");
            System.out.println("[ERROR 10] Number: [" + oldStudent.getNumber() + "]");
            System.out.println("[ERROR 10] Degree: [" + oldStudent.getDegreecode() + "]");
            System.out.println("[ERROR 10] Branch: [" + oldStudent.getBranchcode() + "]");
            e.printStackTrace(System.out);
            throw new Exception(e);
        }

    }

    /**
     * @param enrolments2Write
     * @param studentCurricularPlan
     * @param oldStudent
     * @param sp
     * @throws Exception
     */
    private static void writeEnrolments(
        List enrolments2Write,
        IStudentCurricularPlan studentCurricularPlan,
        MWAluno oldStudent,
        ISuportePersistente sp)
        throws Exception
    {
        Iterator iterator = enrolments2Write.iterator();
        while (iterator.hasNext())
        {
            final MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

            // Get the Fenix DegreeCurricularPlan of the Student.
            IDegreeCurricularPlan degreeCurricularPlan =
                getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan, sp);

            if (degreeCurricularPlan == null)
            {
                System.out.println("[ERROR 03.1] Degree Curricular Plan Not Found!");
                continue;
            }

            // Get the Fenix Branch (this can be the student's branch or the curricular course's branch).
            IBranch branch =
                getBranch(
                    mwEnrolment.getDegreecode(),
                    mwEnrolment.getBranchcode(),
                    degreeCurricularPlan,
                    sp);

            if (branch == null)
            {
                System.out.println("[ERROR 04.1] Branch Not Found!");
                continue;
            }

            ICurricularCourse curricularCourse =
                getCurricularCourse(mwEnrolment, degreeCurricularPlan, sp, true);

            if (curricularCourse == null)
            {
                continue;
            }

            // If we get to this point of the code we did find a CurricularCourse.
            // Now we will try to get the CurricularCourseScope by the CurricularCourse
            // previously found and the semester an year from MWEnrolment.
            ICurricularCourseScope curricularCourseScope =
                getCurricularCourseScopeToEnrollIn(
                    studentCurricularPlan,
                    mwEnrolment,
                    curricularCourse,
                    branch,
                    sp);

            if (curricularCourseScope == null)
            {
                ReportEnrolment.addCurricularCourseScopeNotFound(
                    mwEnrolment.getCoursecode(),
                    mwEnrolment.getDegreecode().toString(),
                    mwEnrolment.getNumber().toString(),
                    mwEnrolment.getCurricularcourseyear().toString(),
                    mwEnrolment.getCurricularcoursesemester().toString(),
                    mwEnrolment.getBranchcode().toString());
                continue;
            }

            if (!hasExecutionInGivenPeriod(curricularCourse, executionPeriod, mwEnrolment, sp))
            {
                ReportEnrolment.addExecutionCourseNotFound(
                    mwEnrolment.getCoursecode(),
                    mwEnrolment.getDegreecode().toString(),
                    mwEnrolment.getNumber().toString());
                continue;
            }

            IEnrolment enrolment = createEnrolment(studentCurricularPlan, sp, curricularCourseScope);

            // Update the corresponding Fenix Attend if it exists.
            IFrequenta attend = updateAttend(curricularCourse, enrolment, mwEnrolment, sp);
            if (attend == null)
            {
                // NOTE [DAVID]: This kind of report is only pesented the first time the migration process is executed.
                // This happens because although this is a situation of error report, this kind of error doesn't
                // forbid the enrolment to be created in the Fenix DB. Thus the second time the process is executed, the
                // enrolment for this particular CurricularCourse will have already been created in the DB so this
                // CurricularCourse is no longer considered for this execution of the process.
                // NOTE [DAVID]: This is for information only.
                ReportEnrolment.addAttendNotFound(
                    mwEnrolment.getCoursecode(),
                    mwEnrolment.getDegreecode().toString(),
                    mwEnrolment.getNumber().toString());
                createAttend(curricularCourse, enrolment, mwEnrolment, sp);
            }
        }
    }

    /**
     * @param curricularCourse
     * @param enrolment
     * @param mwEnrolment
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private static IFrequenta updateAttend(
        ICurricularCourse curricularCourse,
        IEnrolment enrolment,
        MWEnrolment mwEnrolment,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IDisciplinaExecucao executionCourse =
            sp.getIDisciplinaExecucaoPersistente().readbyCurricularCourseAndExecutionPeriod(
                curricularCourse,
                executionPeriod);
        IFrequenta attend = null;
        if (executionCourse == null)
        {
            // NOTE [DAVID]: This should not happen at this point.
            ReportEnrolment.addExecutionCourseNotFound(
                mwEnrolment.getCoursecode(),
                mwEnrolment.getDegreecode().toString(),
                mwEnrolment.getNumber().toString());
        } else
        {
            IStudent student =
                sp.getIPersistentStudent().readByNumero(
                    mwEnrolment.getNumber(),
                    TipoCurso.LICENCIATURA_OBJ);
            attend =
                sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student, executionCourse);

            if (attend != null)
            {
                sp.getIFrequentaPersistente().simpleLockWrite(attend);
                attend.setEnrolment(enrolment);
            }

        }
        return attend;
    }

    /**
     * @param studentCurricularPlan
     * @param sp
     * @param curricularCourseScope
     * @return
     * @throws ExcepcaoPersistencia
     */
    private static IEnrolment createEnrolment(
        IStudentCurricularPlan studentCurricularPlan,
        ISuportePersistente sp,
        ICurricularCourseScope curricularCourseScope)
        throws ExcepcaoPersistencia
    {
        // Create the Enrolment.
        IEnrolment enrolment = new Enrolment();
        sp.getIPersistentEnrolment().simpleLockWrite(enrolment);
        enrolment.setCurricularCourseScope(curricularCourseScope);
        enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
        enrolment.setEnrolmentState(EnrolmentState.ENROLED);
        enrolment.setExecutionPeriod(executionPeriod);
        enrolment.setStudentCurricularPlan(studentCurricularPlan);

        // Create The Enrolment Evaluation.
        IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
        sp.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);

        enrolmentEvaluation.setCheckSum(null);
        enrolmentEvaluation.setEmployee(null);
        enrolmentEvaluation.setEnrolment(enrolment);
        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
        enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
        enrolmentEvaluation.setExamDate(null);
        enrolmentEvaluation.setGrade(null);
        enrolmentEvaluation.setGradeAvailableDate(null);
        enrolmentEvaluation.setObservation(null);
        enrolmentEvaluation.setPersonResponsibleForGrade(null);
        enrolmentEvaluation.setWhen(null);

        ReportEnrolment.addEnrolmentMigrated();

        return enrolment;
    }

    /**
     * @param mwEnrolment
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private static ICurricularCourse getCurricularCourseFromAnotherDegree(
        final MWEnrolment mwEnrolment,
        ISuportePersistente sp,
        IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia, PersistentMiddlewareSupportException
    {
        ICurricularCourse curricularCourse = null;

        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWCurricularCourseOutsideStudentDegree persistentMWCurricularCourseOutsideStudentDegree =
            mws.getIPersistentMWCurricularCourseOutsideStudentDegree();

        // First of all we look in the MWCurricularCourseOutsideStudentDegree table to see if there is already a
        // correspondence between this CurricularCourse and this Degree.
        MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegree =
            persistentMWCurricularCourseOutsideStudentDegree.readByCourseCodeAndDegreeCode(
                mwEnrolment.getCoursecode(),
                mwEnrolment.getDegreecode());
        if (mwCurricularCourseOutsideStudentDegree != null)
        {
            curricularCourse = mwCurricularCourseOutsideStudentDegree.getCurricularCourse();

        // If there is no correspondence yet, let us look if this CurricularCourse has only one
        // ExecuitonCourse in the given period.
        } else if (
            curricularCourseHasOnlyOneExecutionInGivenPeriod(
                executionPeriod,
                mwEnrolment,
                sp,
                degreeCurricularPlan))
        {

            // If there is only one ExecutionCourse for all the existing CurricularCourses then we can choose
            // any CurricularCourse but this choice is registred in table MWCurricularCourseOutsideStudentDegree
            // to keep coherence of choice and to make the next similar choice quicker.
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            List curricularCourses =
                persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
                    StringUtils.trim(mwEnrolment.getCoursecode()),
                    degreeCurricularPlan.getDegree().getTipoCurso(),
                    degreeCurricularPlan.getState());
            curricularCourse = (ICurricularCourse) curricularCourses.get(0);

            MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite =
                new MWCurricularCourseOutsideStudentDegree();
            persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(
                mwCurricularCourseOutsideStudentDegreeToWrite);
            mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
            mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
            mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

        } else
        {

            IFrequentaPersistente attendDAO = sp.getIFrequentaPersistente();
            List attendList =
                attendDAO.readByStudentNumberInCurrentExecutionPeriod(mwEnrolment.getNumber());
            List attendsWithCurricularCourseCode =
                (List) CollectionUtils.select(attendList, new Predicate()
            {
                public boolean evaluate(Object input)
                {
                    IFrequenta attend = (IFrequenta) input;

                    String courseCode = mwEnrolment.getCoursecode();

                    List associatedCurricularCourses =
                        attend.getDisciplinaExecucao().getAssociatedCurricularCourses();
                    Iterator iterator = associatedCurricularCourses.iterator();
                    while (iterator.hasNext())
                    {
                        ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
                        if (curricularCourse.getCode().equals(courseCode))
                        {
                            return true;
                        }
                    }
                    return false;
                }
            });

            if (attendsWithCurricularCourseCode.size() > 0)
            {
                List associatedCurricularCourses =
                    ((IFrequenta) attendsWithCurricularCourseCode.get(0))
                        .getDisciplinaExecucao()
                        .getAssociatedCurricularCourses();

                curricularCourse = (ICurricularCourse) associatedCurricularCourses.get(0);

                MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite =
                    new MWCurricularCourseOutsideStudentDegree();
                persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(
                    mwCurricularCourseOutsideStudentDegreeToWrite);
                mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
                mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
                mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

            } else
            {
                ReportEnrolment.addFoundCurricularCourseInOtherDegrees(
                    mwEnrolment.getCoursecode(),
                    mwEnrolment.getDegreecode().toString(),
                    mwEnrolment.getNumber().toString());
            }
        }

        return curricularCourse;
    }

    /**
     * @param studentCurricularPlan
     * @param mwEnrolment
     * @param curricularCourse
     * @param branch
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    public static ICurricularCourseScope getCurricularCourseScopeToEnrollIn(
        IStudentCurricularPlan studentCurricularPlan,
        MWEnrolment mwEnrolment,
        ICurricularCourse curricularCourse,
        IBranch branch,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentCurricularCourseScope curricularCourseScopeDAO =
            sp.getIPersistentCurricularCourseScope();

        List curricularCourseScopes = null;
        ICurricularCourseScope curricularCourseScope = null;

        if (!curricularCourse
            .getDegreeCurricularPlan()
            .equals(studentCurricularPlan.getDegreeCurricularPlan()))
        {
            curricularCourseScopes = curricularCourseScopeDAO.readByCurricularCourse(curricularCourse);

            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("curricularSemester.idInternal"));

            if ((curricularCourseScopes != null) && (!curricularCourseScopes.isEmpty()))
            {
                Collections.sort(curricularCourseScopes, comparatorChain);
                return (ICurricularCourseScope) curricularCourseScopes.get(0);
            } else
            {
                return null;
            }
        }

        curricularCourseScopes =
            curricularCourseScopeDAO.readByCurricularCourseAndYearAndSemester(
                curricularCourse,
                mwEnrolment.getCurricularcourseyear(),
                mwEnrolment.getCurricularcoursesemester());

        if ((curricularCourseScopes == null) || (curricularCourseScopes.isEmpty()))
        {
            // Try to read by the CurricularCourse previously found and the year only.
            curricularCourseScopes =
                curricularCourseScopeDAO.readByCurricularCourseAndYear(
                    curricularCourse,
                    mwEnrolment.getCurricularcourseyear());

            if ((curricularCourseScopes == null) || (curricularCourseScopes.isEmpty()))
            {
                // The CurricularCourse is from the same Degree that the StudentCurricularPlan. If we cannot find it
                // by year and semester than we'll try only by year and if still it can't be found we'll try only by semester.
                curricularCourseScopes =
                    curricularCourseScopeDAO.readByCurricularCourseAndSemester(
                        curricularCourse,
                        mwEnrolment.getCurricularcoursesemester());

                if ((curricularCourseScopes != null) && (!curricularCourseScopes.isEmpty()))
                {
                    curricularCourseScope =
                        getActualScope(
                            curricularCourseScopes,
                            branch,
                            studentCurricularPlan,
                            curricularCourse);
                }
            } else
            {
                curricularCourseScope =
                    getActualScope(
                        curricularCourseScopes,
                        branch,
                        studentCurricularPlan,
                        curricularCourse);
            }

        } else
        {
            curricularCourseScope =
                getActualScope(curricularCourseScopes, branch, studentCurricularPlan, curricularCourse);
        }

        return curricularCourseScope;
    }

    /**
     * @param curricularCourseScopes
     * @param branch
     * @param studentCurricularPlan
     * @param curricularCourse
     * @return
     */
    private static ICurricularCourseScope getActualScope(
        List curricularCourseScopes,
        IBranch branch,
        IStudentCurricularPlan studentCurricularPlan,
        ICurricularCourse curricularCourse)
    {

        ICurricularCourseScope curricularCourseScope = null;

        if (curricularCourseScopes.size() == 1)
        {
            curricularCourseScope = (ICurricularCourseScope) curricularCourseScopes.get(0);
        } else
        {

            curricularCourseScope = findScopeForBranch(curricularCourseScopes, branch);

            if (curricularCourseScope == null)
            {
                curricularCourseScope =
                    findScopeForBranch(curricularCourseScopes, studentCurricularPlan.getBranch());
            }

            // null means Branch without name (tronco comum).
            if (curricularCourseScope == null)
            {
                curricularCourseScope = findScopeForBranch(curricularCourseScopes, null);
            }

            // If we can't find a scope and the degree of the course is diferent from the student's, them we ignore the branch.
            if ((curricularCourseScope == null)
                && (!studentCurricularPlan
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .equals(curricularCourse.getDegreeCurricularPlan().getDegree())))
            {
                curricularCourseScope = (ICurricularCourseScope) curricularCourseScopes.get(0);
            }
        }
        return curricularCourseScope;
    }

    /**
     * @param curricularCourses
     * @return
     */
    private static boolean hasDiferentDegrees(List curricularCourses)
    {
        int numberOfDegrees = CollectionUtils.getCardinalityMap(curricularCourses).size();
        return (numberOfDegrees > 1);
    }

    /**
     * @param curricularCourseScopes
     * @param branch
     * @return
     */
    private static ICurricularCourseScope findScopeForBranch(
        List curricularCourseScopes,
        IBranch branch)
    {
        Iterator iterator = curricularCourseScopes.iterator();
        while (iterator.hasNext())
        {
            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();

            if (branch == null)
            {
                if (((curricularCourseScope.getBranch().getCode().equals(""))
                    && (curricularCourseScope.getBranch().getName().equals("")))
                    /* ||
                				(curricularCourseScope.getBranch().getName().startsWith("CURSO DE"))*/
                    )
                {
                    return curricularCourseScope;
                }
            } else
            {
                if (curricularCourseScope.getBranch().equals(branch))
                {
                    return curricularCourseScope;
                }
            }
        }
        return null;
    }

    /**
     * @param studentEnrolments
     * @param oldEnrolments
     * @param studentCurricularPlan
     * @param sp
     * @return
     * @throws Exception
     */
    private static List getEnrolments2Write(
        List studentEnrolments,
        List oldEnrolments,
        IStudentCurricularPlan studentCurricularPlan,
        ISuportePersistente sp)
        throws Exception
    {
        List result = new ArrayList();

        Iterator oldEnrolmentIterator = oldEnrolments.iterator();

        while (oldEnrolmentIterator.hasNext())
        {
            MWEnrolment mwEnrolment = (MWEnrolment) oldEnrolmentIterator.next();

            // Get the Degree Of the Curricular Course.
            IDegreeCurricularPlan degreeCurricularPlan =
                getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan, sp);

            if (degreeCurricularPlan == null)
            {
                System.out.println(
                    "[ERROR 03.2] Degree Curricular Plan Not Found (getEnrolments2Write)!");
                throw new Exception();
            }

            // Get The Branch for This Curricular Course.
            IBranch branch =
                getBranch(
                    mwEnrolment.getDegreecode(),
                    mwEnrolment.getBranchcode(),
                    degreeCurricularPlan,
                    sp);

            if (branch == null)
            {
                System.out.println("[ERROR 04.2] Branch Not Found (getEnrolments2Write)!");
                throw new Exception();
            }

            // Check if The Enrolment exists.
            if (!enrolmentExistsOnFenix(mwEnrolment,
                degreeCurricularPlan,
                branch,
                studentEnrolments,
                sp))
            {
                result.add(mwEnrolment);
            }

        }
        return result;
    }

    /**
     * @param mwEnrolment
     * @param degreeCurricularPlan
     * @param branch
     * @param studentEnrolments
     * @param sp
     * @return
     */
    private static boolean enrolmentExistsOnFenix(
        MWEnrolment mwEnrolment,
        IDegreeCurricularPlan degreeCurricularPlan,
        IBranch branch,
        List studentEnrolments,
        ISuportePersistente sp)
    {

        Iterator iterator = studentEnrolments.iterator();
        while (iterator.hasNext())
        {
            IEnrolment enrolment = (IEnrolment) iterator.next();
            if ((enrolment
                .getCurricularCourseScope()
                .getCurricularCourse()
                .getCode()
                .equalsIgnoreCase(StringUtils.trim(mwEnrolment.getCoursecode()))
                && (enrolment.getExecutionPeriod().equals(executionPeriod))))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @param enrolments2Annul
     * @param sp
     * @throws ExcepcaoPersistencia
     */
    private static void annulEnrolments(List enrolments2Annul, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        Iterator iterator = enrolments2Annul.iterator();
        while (iterator.hasNext())
        {
            IEnrolment enrolment = (IEnrolment) iterator.next();

            // Find the Attend.
            IDisciplinaExecucao executionCourse =
                sp.getIDisciplinaExecucaoPersistente().readbyCurricularCourseAndExecutionPeriod(
                    enrolment.getCurricularCourseScope().getCurricularCourse(),
                    executionPeriod);
            if (executionCourse == null)
            {
                continue;
            }
            IFrequenta attend =
                sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
                    enrolment.getStudentCurricularPlan().getStudent(),
                    executionCourse);

            if (attend != null)
            {
                sp.getIFrequentaPersistente().simpleLockWrite(attend);
                attend.setEnrolment(null);
            }

            // Delete EnrolmentEvalutaion.
            Iterator evaluations = enrolment.getEvaluations().iterator();
            while (evaluations.hasNext())
            {
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) evaluations.next();
                sp.getIPersistentEnrolmentEvaluation().delete(enrolmentEvaluation);
            }

            // Delete Enrolment.
            sp.getIPersistentEnrolment().delete(enrolment);
        }
    }

    /**
     * @param oldStudent
     * @param studentEnrolments
     * @param oldEnrolments
     * @param sp
     * @return
     * @throws Exception
     */
    private static List getEnrolments2Annul(
        MWAluno oldStudent,
        List studentEnrolments,
        List oldEnrolments,
        ISuportePersistente sp)
        throws Exception
    {
        List result = new ArrayList();

        Iterator fenixEnrolments = studentEnrolments.iterator();

        while (fenixEnrolments.hasNext())
        {
            IEnrolment enrolment = (IEnrolment) fenixEnrolments.next();

            // Check if The Enrolment exists on the old System.
            if (!enrolmentExistsOnAlmeidaServer(enrolment, oldEnrolments, sp))
            {
                result.add(enrolment);
            }
        }

        return result;
    }

    /**
     * @param enrolment
     * @param oldEnrolments
     * @param sp
     * @return
     */
    private static boolean enrolmentExistsOnAlmeidaServer(
        IEnrolment enrolment,
        List oldEnrolments,
        ISuportePersistente sp)
    {
        Iterator iterator = oldEnrolments.iterator();
        while (iterator.hasNext())
        {
            MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

            // To read an MWEnrolment we need the student number, the Course Code, the Semester and the enrolment year.
            if ((mwEnrolment
                .getNumber()
                .equals(enrolment.getStudentCurricularPlan().getStudent().getNumber()))
                && (StringUtils
                    .trim(mwEnrolment.getCoursecode())
                    .equals(enrolment.getCurricularCourseScope().getCurricularCourse().getCode())))
            {
                return true;
            }

        }
        return false;
    }

    /**
     * @param degreeCode
     * @param studentCurricularPlan
     * @param sp
     * @return
     * @throws PersistentMiddlewareSupportException
     * @throws ExcepcaoPersistencia
     */
    private static IDegreeCurricularPlan getDegreeCurricularPlan(
        Integer degreeCode,
        IStudentCurricularPlan studentCurricularPlan,
        ISuportePersistente sp)
        throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
    {
        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWDegreeTranslation persistentMWDegreeTranslation =
            mws.getIPersistentMWDegreeTranslation();

        MWDegreeTranslation mwDegreeTranslation =
            persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

        ICursoExecucao executionDegree =
            sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(
                mwDegreeTranslation.getDegree().getNome(),
                executionPeriod.getExecutionYear(),
                TipoCurso.LICENCIATURA_OBJ);

        if (executionDegree == null)
        {
            System.out.println(
                "[ERROR 06] the degree has no execution in ["
                    + executionPeriod.getExecutionYear().getYear()
                    + "]!");
            return null;
        } else
        {
            if (!studentCurricularPlan
                .getDegreeCurricularPlan()
                .equals(executionDegree.getCurricularPlan()))
            {
                System.out.println(
                    "[INFO] the student ["
                        + studentCurricularPlan.getStudent().getNumber()
                        + "] has changed his degree!");
                return studentCurricularPlan.getDegreeCurricularPlan();
            } else
            {
                return executionDegree.getCurricularPlan();
            }
        }
    }

    /**
     * @param degreeCode
     * @param branchCode
     * @param degreeCurricularPlan
     * @param sp
     * @return
     * @throws PersistentMiddlewareSupportException
     * @throws ExcepcaoPersistencia
     */
    private static IBranch getBranch(
        Integer degreeCode,
        Integer branchCode,
        IDegreeCurricularPlan degreeCurricularPlan,
        ISuportePersistente sp)
        throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
    {
        IBranch branch = null;

        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();

        // Get the MWBranch.
        sp.clearCache();
        MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(degreeCode, branchCode);

        // Get the Fenix Branch.
        if (mwbranch == null)
        {
            System.out.println(
                "[ERROR 08] the middleware Branch ["
                    + branchCode
                    + "] from degree ["
                    + degreeCode
                    + "] cannot be found!");
        }

        String realBranchCode =
            new String(
                mwbranch.getDegreecode().toString()
                    + mwbranch.getBranchcode().toString()
                    + mwbranch.getOrientationcode().toString());
        branch =
            sp.getIPersistentBranch().readByDegreeCurricularPlanAndCode(
                degreeCurricularPlan,
                realBranchCode);

        if (branch == null)
        {
            branch =
                sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(
                    degreeCurricularPlan,
                    "");
        }

        if (branch == null)
        {
            System.out.println(
                "[ERROR 09] the Fenix Branch ["
                    + mwbranch.getDescription()
                    + "] from degree ["
                    + degreeCurricularPlan.getName()
                    + "] cannot be found!");
        }

        return branch;
    }

    /**
     * @param curricularCourse
     * @param enrolment
     * @param mwEnrolment
     * @param sp
     * @throws ExcepcaoPersistencia
     */
    private static void createAttend(
        ICurricularCourse curricularCourse,
        IEnrolment enrolment,
        MWEnrolment mwEnrolment,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IDisciplinaExecucao executionCourse =
            sp.getIDisciplinaExecucaoPersistente().readbyCurricularCourseAndExecutionPeriod(
                curricularCourse,
                executionPeriod);

        if (executionCourse == null)
        {
            // NOTE [DAVID]: This error report can be added here even if it was added before in the updateAttend() method
            // because this addition wont repeat same occurrences.
            // NOTE [DAVID]: This should not happen at this point.
            ReportEnrolment.addExecutionCourseNotFound(
                mwEnrolment.getCoursecode(),
                mwEnrolment.getDegreecode().toString(),
                mwEnrolment.getNumber().toString());
        } else
        {
            IStudent student =
                sp.getIPersistentStudent().readByNumero(
                    mwEnrolment.getNumber(),
                    TipoCurso.LICENCIATURA_OBJ);
            IFrequenta attend = new Frequenta();
            sp.getIFrequentaPersistente().simpleLockWrite(attend);
            attend.setAluno(student);
            attend.setDisciplinaExecucao(executionCourse);
            attend.setEnrolment(enrolment);
            // NOTE [DAVID]: This is for information only.
            ReportEnrolment.addCreatedAttend(
                mwEnrolment.getCoursecode(),
                mwEnrolment.getDegreecode().toString(),
                mwEnrolment.getNumber().toString());
        }
    }

    /**
     * @param curricularCourse
     * @param executionPeriod
     * @param mwEnrolment
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private static boolean hasExecutionInGivenPeriod(
        ICurricularCourse curricularCourse,
        IExecutionPeriod executionPeriod,
        MWEnrolment mwEnrolment,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IDisciplinaExecucao executionCourse =
            sp.getIDisciplinaExecucaoPersistente().readbyCurricularCourseAndExecutionPeriod(
                curricularCourse,
                executionPeriod);
        if (executionCourse == null)
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * @param executionPeriod
     * @param mwEnrolment
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private static boolean curricularCourseHasOnlyOneExecutionInGivenPeriod(
        IExecutionPeriod executionPeriod,
        MWEnrolment mwEnrolment,
        ISuportePersistente sp,
        IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        List curricularCourses =
            persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
                StringUtils.trim(mwEnrolment.getCoursecode()),
                degreeCurricularPlan.getDegree().getTipoCurso(),
                degreeCurricularPlan.getState());
        List executionCourses = new ArrayList();

        Iterator iterator1 = curricularCourses.iterator();
        while (iterator1.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
            List associatedExecutionCourses = curricularCourse.getAssociatedExecutionCourses();
            Iterator iterator2 = associatedExecutionCourses.iterator();
            while (iterator2.hasNext())
            {
                IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) iterator2.next();
                if (executionCourse.getExecutionPeriod().equals(executionPeriod))
                {
                    if (!executionCourses.contains(executionCourse))
                    {
                        executionCourses.add(executionCourse);
                    }
                }
            }
        }

        if (executionCourses.size() == 1)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public static ICurricularCourse getCurricularCourse(
        MWEnrolment mwEnrolment,
        IDegreeCurricularPlan degreeCurricularPlan,
        ISuportePersistente sp,
        boolean solveSomeProblems)
        throws Exception
    {
        String courseCode = null;
        if (solveSomeProblems)
        {
            courseCode = getRealCurricularCourseCodeForCodesAZx(mwEnrolment);
        } else
        {
            courseCode = mwEnrolment.getCoursecode();
        }

        // Get the list of Fenix CurricularCourses with that code for the selected DegreeCurricularPlan.
        List curricularCourses =
            sp.getIPersistentCurricularCourse().readbyCourseCodeAndDegreeCurricularPlan(
                StringUtils.trim(courseCode),
                degreeCurricularPlan);

        ICurricularCourse curricularCourse = null;

        // Ideally we find only one CurricularCourse but we never know, we may find more or even less.
        if (curricularCourses.size() != 1)
        {
            if (curricularCourses.size() > 1)
            {
                System.out.println(
                    "[ERROR 05] Several Fenix CurricularCourses with code ["
                        + courseCode
                        + "] were found for Degree ["
                        + mwEnrolment.getDegreecode()
                        + "]!");
                return null;
            } else // size == 0
                {
                // We did not find any CurricularCourse with that code in that DegreeCurricularPlan.
                // Now we try to read by the CurricularCourse code only.
                IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
                curricularCourses =
                    curricularCourseDAO.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
                        StringUtils.trim(courseCode),
                        degreeCurricularPlan.getDegree().getTipoCurso(),
                        degreeCurricularPlan.getState());

                if (curricularCourses.size() == 1)
                {
                    curricularCourse = (ICurricularCourse) curricularCourses.get(0);
                } else if (curricularCourses.size() > 1)
                {
                    if (hasDiferentDegrees(curricularCourses))
                    {
                        curricularCourse =
                            getCurricularCourseFromAnotherDegree(mwEnrolment, sp, degreeCurricularPlan);
                        if (curricularCourse == null)
                        {
                            // NOTE [DAVID]: This is for information only.
                            ReportEnrolment.addReplicatedCurricularCourses(
                                courseCode,
                                curricularCourses);
                            return null;
                        }
                    } else
                    {
                        curricularCourse = (ICurricularCourse) curricularCourses.get(0);
                    }
                } else
                {
                    ReportEnrolment.addCurricularCourseNotFound(
                        courseCode,
                        mwEnrolment.getDegreecode().toString(),
                        mwEnrolment.getNumber().toString());
                    return null;
                }
            }
        } else // curricularCourses.size() == 1
            {
            curricularCourse = (ICurricularCourse) curricularCourses.get(0);
        }
        return curricularCourse;
    }

// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------
// ------------------------------- METHODS TO SOLVE SPECIFIC PROBLEMS ----------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------------

    private static String getRealCurricularCourseCodeForCodesAZx(MWEnrolment mwEnrolment)
    {
        if (mwEnrolment.getCoursecode().equals("AZ2")
            && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
        {
            return "QN";
        } else if (
            mwEnrolment.getCoursecode().equals("AZ3")
                && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
        {
            return "PY";
        } else if (
            mwEnrolment.getCoursecode().equals("AZ4")
                && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
        {
            return "P5";
        } else if (
            mwEnrolment.getCoursecode().equals("AZ5")
                && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
        {
            return "UN";
        } else if (
            mwEnrolment.getCoursecode().equals("AZ6")
                && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
        {
            return "U8";
        } else
        {
            return mwEnrolment.getCoursecode();
        }
    }
}
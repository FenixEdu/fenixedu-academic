package middleware.curricularCourseMigration;

import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourse;
import middleware.middlewareDomain.MWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.commons.lang.StringUtils;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class CreateAndUpdateScopes
{

    private static IExecutionPeriod executionPeriod = null;
    private static int curricularCoursesWritten = 0;
    private static int curricularCourseScopesWritten = 0;

    public static void main(String args[]) throws Exception
    {

        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWCurricularCourseScope persistentCurricularCourseScopes =
            mws.getIPersistentMWCurricularCourseScope();

        System.out.println("Reading Scopes ....");

        SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
        sp.iniciarTransaccao();
        executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();
        List result = persistentCurricularCourseScopes.readAll();
        sp.confirmarTransaccao();

        System.out.println("Creating and Updating " + result.size() + " Curricular Course Scopes ...");

        Iterator iterator = result.iterator();
        while (iterator.hasNext())
        {
            MWCurricularCourseScope scope = (MWCurricularCourseScope) iterator.next();

            try
            {
                sp.iniciarTransaccao();
                sp.clearCache();
                CreateAndUpdateScopes.createAndUpdate(scope, sp);
                sp.confirmarTransaccao();
            } catch (Exception e)
            {
                sp.confirmarTransaccao();
                System.out.println("Error Migrating Scope " + scope);
                e.printStackTrace(System.out);
            }
        }

        System.out.println("Success !!");
        System.out.println("Curricular Courses Written " + curricularCoursesWritten);
        System.out.println("Curricular Course Scopes Written " + curricularCourseScopesWritten);

    }

    /**
	 * 
	 * This method creates new Students
	 *  
	 */

    public static void createAndUpdate(
        MWCurricularCourseScope mwCurricularCourseScope,
        SuportePersistenteOJB sp)
        throws Exception
    {

        try
        {
            // Read The Degree Curricular Plan

            IDegreeCurricularPlan degreeCurricularPlan =
                getDegreeCurricularPlan(mwCurricularCourseScope, sp);

            if (degreeCurricularPlan == null)
            {
                System.out.println(
                    "Degree Curricular Plan Not Found ! Degree Number "
                        + mwCurricularCourseScope.getDegreecode());
                return;
            }

            // Read The Branch

            IBranch branch = getBranch(mwCurricularCourseScope, degreeCurricularPlan, sp);

            if (branch == null)
            {
                System.out.println(
                    "Branch Not Found ! Degree Number "
                        + mwCurricularCourseScope.getDegreecode()
                        + " Branch Number "
                        + mwCurricularCourseScope.getBranchcode());
                return;
            }

            // Read The Curricular Course

            List curricularCourses =
                sp.getIPersistentCurricularCourse().readbyCourseCodeAndDegreeCurricularPlan(
                    StringUtils.trim(mwCurricularCourseScope.getCoursecode()),
                    degreeCurricularPlan);

            ICurricularCourse curricularCourse = null;

            if (curricularCourses.size() != 1)
            {
                System.out.println(
                    "Error Reading Curricular Course !! Course Code : "
                        + mwCurricularCourseScope.getCoursecode()
                        + " DCP "
                        + degreeCurricularPlan.getIdInternal());

                // Read MWCurricularCourse

                IPersistentMiddlewareSupport pms = PersistentMiddlewareSupportOJB.getInstance();
                IPersistentMWCurricularCourse persistentMWCurricularCourse =
                    pms.getIPersistentMWCurricularCourse();

                MWCurricularCourse mwCurricularCourse =
                    persistentMWCurricularCourse.readByCode(mwCurricularCourseScope.getCoursecode());
                if (mwCurricularCourse == null)
                {
                    System.out.println(
                        "Error Reading Middleware Curricular Course "
                            + mwCurricularCourseScope.getCoursecode());
                    return;
                }

                curricularCourse = new CurricularCourse();
                sp.getIPersistentCurricularCourse().simpleLockWrite(curricularCourse);
                curricularCourse.setBasic(Boolean.FALSE);
                curricularCourse.setCode(StringUtils.trim(mwCurricularCourseScope.getCoursecode()));
                curricularCourse.setCredits(mwCurricularCourseScope.getCredits());
                curricularCourse.setCurricularCourseExecutionScope(
                    CurricularCourseExecutionScope.SEMESTRIAL_OBJ);
                curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
                curricularCourse.setDepartmentCourse(null);
                curricularCourse.setMandatory(Boolean.FALSE);
                curricularCourse.setName(mwCurricularCourse.getCoursename());
                curricularCourse.setType(CurricularCourseType.NORMAL_COURSE_OBJ);

                System.out.println("Curricular Course Written !");
                curricularCoursesWritten++;

            } else
            {
                curricularCourse = (ICurricularCourse) curricularCourses.get(0);
            }

            // Read The Curricular Semester

            ICurricularYear curricularYear =
                sp.getIPersistentCurricularYear().readCurricularYearByYear(
                    mwCurricularCourseScope.getCurricularyear());
            ICurricularSemester curricularSemester =
                sp.getIPersistentCurricularSemester().readCurricularSemesterBySemesterAndCurricularYear(
                    mwCurricularCourseScope.getCurricularsemester(),
                    curricularYear);

            // Check if the Scope Exists

            ICurricularCourseScope curricularCourseScope =
                sp
                    .getIPersistentCurricularCourseScope()
                    .readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
                        curricularCourse,
                        curricularSemester,
                        branch,
                        null);
            if (curricularCourseScope != null)
            {
                return;
            }

            // Write The Curricular Course Scope
            curricularCourseScope = new CurricularCourseScope();
            sp.getIPersistentCurricularCourseScope().simpleLockWrite(curricularCourseScope);

            curricularCourseScope.setBranch(branch);
            curricularCourseScope.setCredits(
                new Double(mwCurricularCourseScope.getCredits().doubleValue()));
            curricularCourseScope.setCurricularCourse(curricularCourse);
            curricularCourseScope.setCurricularSemester(curricularSemester);
            curricularCourseScope.setLabHours(
                new Double(mwCurricularCourseScope.getLabhours().doubleValue()));
            curricularCourseScope.setTheoPratHours(
                new Double(mwCurricularCourseScope.getTheoprathours().doubleValue()));
            curricularCourseScope.setTheoreticalHours(
                new Double(mwCurricularCourseScope.getTheoreticalhours().doubleValue()));
            curricularCourseScope.setPraticalHours(
                new Double(mwCurricularCourseScope.getPraticahours().doubleValue()));
            curricularCourseScope.setMaxIncrementNac(new Integer(2));
            curricularCourseScope.setMinIncrementNac(new Integer(1));
            curricularCourseScope.setWeigth(
                new Integer(mwCurricularCourseScope.getCredits().intValue()));

            //			TODO: add sets for begin and end dates and ectsCredits
            curricularCourseScopesWritten++;
        } catch (Exception e)
        {
            throw new Exception(e);
        }
    }

    /**
	 * @param oldStudent
	 * @param sp
	 * @return @throws
	 *         PersistentMiddlewareSupportException
	 * @throws ExcepcaoPersistencia
	 */

    private static IDegreeCurricularPlan getDegreeCurricularPlan(
        MWCurricularCourseScope mwCurricularCourseScope,
        ISuportePersistente sp)
        throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
    {
        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();

        // Get the Old Degree

        MWBranch mwBranch =
            persistentBranch.readByDegreeCodeAndBranchCode(
                mwCurricularCourseScope.getDegreecode(),
                new Integer(0));

        // Get the Actual Degree Curricular Plan for this Degree

        String degreeName = StringUtils.substringAfter(mwBranch.getDescription(), "DE ");
        //		String degreeName = StringUtils.prechomp(mwBranch.getDescription(), "DE ");

        if (degreeName.indexOf("TAGUS") != -1)
        {
            degreeName = "Engenharia Informática e de Computadores - Taguspark";
        }

        ICursoExecucao executionDegree =
            sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(
                degreeName,
                executionPeriod.getExecutionYear(),
                TipoCurso.LICENCIATURA_OBJ);

        return executionDegree.getCurricularPlan();
    }

    /**
	 * @param mwCurricularCourseScope
	 * @param degreeCurricularPlan
	 * @param sp
	 * @return @throws
	 *         PersistentMiddlewareSupportException
	 * @throws ExcepcaoPersistencia
	 */
    private static IBranch getBranch(
        MWCurricularCourseScope mwCurricularCourseScope,
        IDegreeCurricularPlan degreeCurricularPlan,
        ISuportePersistente sp)
        throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
    {

        IBranch branch = null;

        IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();

        // Get the old BRanch

        sp.clearCache();
        MWBranch mwbranch =
            persistentBranch.readByDegreeCodeAndBranchCode(
                mwCurricularCourseScope.getDegreecode(),
                mwCurricularCourseScope.getBranchcode());

        // Get the new one

        branch =
            sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(
                degreeCurricularPlan,
                mwbranch.getDescription());

        if (branch == null)
        {
            branch =
                sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(
                    degreeCurricularPlan,
                    "");
        }

        if (branch == null)
        {
            System.out.println("Ramo Antigo " + mwbranch);
        }

        return branch;
    }

}

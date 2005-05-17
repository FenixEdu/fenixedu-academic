/*
 * StudentCurricularPlanOJB.java Created on 21 of December of 2002, 17:01
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Nuno Nunes & Joana Mota
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.util.Specialization;

import org.apache.ojb.broker.query.Criteria;

public class StudentCurricularPlanOJB extends PersistentObjectOJB implements
        IPersistentStudentCurricularPlan {

    /** Creates a new instance of StudentCurricularPlanOJB */
    public StudentCurricularPlanOJB() {
    }

    public IStudentCurricularPlan readActiveStudentCurricularPlan(String username, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.person.username", username);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);
    }

    // TODO Remove DegreeType from method interface...
    public IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber,
            DegreeType degreeType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);

    }

    public List readAllActiveStudentCurricularPlan(Integer studentNumber) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readAllActiveStudentCurricularPlansWithEnrollmentsInExecutionPeriod(
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("enrolments.executionPeriod.idInternal", executionPeriod.getIdInternal());
        crit.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        return queryList(StudentCurricularPlan.class, crit, true);
    }

    public List readAllActiveStudentCurricularPlansByDegreeWithEnrollmentsInExecutionPeriod(
            IExecutionPeriod executionPeriod, IDegree degree) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("enrolments.executionPeriod.idInternal", executionPeriod.getIdInternal());
        crit.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        crit.addEqualTo("degreeCurricularPlan.degree.idInternal", degree.getIdInternal());
        return queryList(StudentCurricularPlan.class, crit, true);
    }

    public List readAllByDegreeCurricularPlanAndState(IDegreeCurricularPlan degreeCurricularPlan,
            StudentCurricularPlanState state) throws ExcepcaoPersistencia {

        try {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
            criteria.addEqualTo("currentState", state);
            return queryList(StudentCurricularPlan.class, criteria);
        } catch (ExcepcaoPersistencia e) {
            throw e;
        }
    }

    // TODO : This method is not yet availabe through the
    // StudentCurricularPlanPersistente interface.
    //        I wrote it to be used in the lockWrite method, but maby it should be
    // made available to
    //        the aplication layer as well.
    // TODO : Write a test case for this method.
    public IStudentCurricularPlan readStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlanToRead) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentCurricularPlanToRead.getStudent().getNumber());
        crit.addEqualTo("student.degreeType", studentCurricularPlanToRead.getStudent().getDegreeType());
        crit.addEqualTo("degreeCurricularPlan.name", studentCurricularPlanToRead
                .getDegreeCurricularPlan().getName());
        crit.addEqualTo("degreeCurricularPlan.degree.sigla", studentCurricularPlanToRead
                .getDegreeCurricularPlan().getDegree().getSigla());
        crit.addEqualTo("currentState", studentCurricularPlanToRead.getCurrentState());
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);

    }

    public void delete(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia {
        super.delete(curricularPlan);
    }

    public List readAllFromStudent(int studentNumber) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.number", new Integer(studentNumber));
        return queryList(StudentCurricularPlan.class, criteria);
    }

    public IStudentCurricularPlan readActiveStudentAndSpecializationCurricularPlan(
            Integer studentNumber, DegreeType degreeType, Specialization specialization)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("specialization", specialization.getSpecialization());
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);

    }

    public List readAllByStudentNumberAndSpecialization(Integer studentNumber, DegreeType degreeType,
            Specialization specialization) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("specialization", specialization.getSpecialization());
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readAllByStudentNumberAndSpecializationAndState(Integer studentNumber,
            DegreeType degreeType, Specialization specialization, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("currentState", state);
        crit.addEqualTo("specialization", specialization.getSpecialization());

        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlan.getName());
        crit
                .addEqualTo("degreeCurricularPlan.degree.sigla", degreeCurricularPlan.getDegree()
                        .getSigla());
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branchKey", branch.getIdInternal());
        return queryList(StudentCurricularPlan.class, criteria);
    }

    //    public List readByCurricularCourseScope(ICurricularCourseScope
    // curricularCourseScope)
    //        throws ExcepcaoPersistencia
    //    {
    //        Criteria criteria = new Criteria();
    //        criteria.addEqualTo("curricularCourseScopeKey",
    // curricularCourseScope.getIdInternal());
    //        List enrolments = queryList(Enrolment.class, criteria);
    //        List studentCurricularPlans = new ArrayList();
    //        Integer studentCurricularPlanId;
    //        IStudentCurricularPlan helpStudentCurricularPlan = new
    // StudentCurricularPlan();
    //        Iterator iter = enrolments.iterator();
    //        while (iter.hasNext())
    //        {
    //            studentCurricularPlanId =
    //                ((IEnrolment) iter.next()).getStudentCurricularPlan().getIdInternal();
    //            helpStudentCurricularPlan.setIdInternal(studentCurricularPlanId);
    //            IStudentCurricularPlan studentCurricularPlan =
    //                (IStudentCurricularPlan) this.readByOId(helpStudentCurricularPlan,
    // false);
    //            studentCurricularPlans.add(studentCurricularPlan);
    //        }
    //        return studentCurricularPlans;
    //    }

    public List readByUsername(String username) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.person.username", username);
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readByStudentNumberAndDegreeType(Integer number, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", number);
        crit.addEqualTo("student.degreeType", degreeType);
        return queryList(StudentCurricularPlan.class, crit);

    }

    //modified by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 24/Set/2003
    /**
     * Service rewritten by Naat & Sana to correct bug that cause the returning
     * of an empty StudentCurricular when a student does not have an active
     * version. The new version now uses Persistence Broker criteria API
     */
    public IStudentCurricularPlan readActiveByStudentNumberAndDegreeType(Integer number,
            DegreeType degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List studentPlanState = new ArrayList();
        studentPlanState.add(StudentCurricularPlanState.ACTIVE);
        studentPlanState.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED);

        criteria.addIn("currentState", studentPlanState);
        //   criteria.addEqualTo("currentState",
        // StudentCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("student.number", number);
        criteria.addEqualTo("student.degreeType", degreeType);

        IStudentCurricularPlan storedStudentCurricularPlan = (IStudentCurricularPlan) queryObject(
                StudentCurricularPlan.class, criteria);
        return storedStudentCurricularPlan;

    }

    public List readAllByStudentAntState(IStudent student, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentKey", student.getIdInternal());
        criteria.addEqualTo("currentState", state);
        return queryList(StudentCurricularPlan.class, criteria);
    }

    public IStudentCurricularPlan readByStudentDegreeCurricularPlanAndState(IStudent student,
            IDegreeCurricularPlan degreeCurricularPlan, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("studentKey", student.getIdInternal());
        criteria.addEqualTo("currentState", state);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, criteria);
    }

    public List readAllByStudentAndDegreeCurricularPlan(IStudent student,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("studentKey", student.getIdInternal());

        return queryList(StudentCurricularPlan.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IStudentCurricularPlanPersistente#readAllBySeveralDegreeCurricularPlansAndSpecialization(java.util.List,
     *      Util.Specialization)
     */
    public List readAllByDegreeCurricularPlanAndSpecialization(
            IDegreeCurricularPlan degreeCurricularPlan, Specialization specialization)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());

        if (specialization != null && specialization.getSpecialization() != null) {
            criteria.addEqualTo("specialization", specialization.getSpecialization());
        } else //all specialization required, but not records with
        // specialization null
        {
            criteria.addNotNull("specialization");
        }

        return queryList(StudentCurricularPlan.class, criteria);
    }

}
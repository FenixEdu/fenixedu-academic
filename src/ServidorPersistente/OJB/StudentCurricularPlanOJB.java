/*
 * StudentCurricularPlanOJB.java
 * 
 * Created on 21 of December of 2002, 17:01
 */

package ServidorPersistente.OJB;

/**
 * @author Nuno Nunes & Joana Mota
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IBranch;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

public class StudentCurricularPlanOJB
    extends ObjectFenixOJB
    implements IStudentCurricularPlanPersistente
{

    /** Creates a new instance of StudentCurricularPlanOJB */
    public StudentCurricularPlanOJB()
    {
    }

    // TODO Remove TipoCurso from method interface...
    public IStudentCurricularPlan readActiveStudentCurricularPlan(
        Integer studentNumber,
        TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("currentState", new Integer(StudentCurricularPlanState.ACTIVE));
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);

    }

    public List readAllByDegreeCurricularPlanAndState(
        IDegreeCurricularPlan degreeCurricularPlan,
        StudentCurricularPlanState state)
        throws ExcepcaoPersistencia
    {

        try
        {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
            criteria.addEqualTo("currentState", state);
            return queryList(StudentCurricularPlan.class, criteria);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw e;
        }
    }

    // TODO : This method is not yet availabe through the
    // StudentCurricularPlanPersistente interface.
    //        I wrote it to be used in the lockWrite method, but maby it should be
    // made available to
    //        the aplication layer as well.
    // TODO : Write a test case for this method.
    public IStudentCurricularPlan readStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlanToRead)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentCurricularPlanToRead.getStudent().getNumber());
        crit.addEqualTo("student.degreeType", studentCurricularPlanToRead.getStudent().getDegreeType());
        crit.addEqualTo(
            "degreeCurricularPlan.name",
            studentCurricularPlanToRead.getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "degreeCurricularPlan.degree.sigla",
            studentCurricularPlanToRead.getDegreeCurricularPlan().getDegree().getSigla());
        crit.addEqualTo("currentState", studentCurricularPlanToRead.getCurrentState());
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);

    }

    public void lockWrite(IStudentCurricularPlan studentCurricularPlanToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IStudentCurricularPlan studentCurricularPlanFromDB = null;

        // If there is nothing to write, simply return.
        if (studentCurricularPlanToWrite == null)
            return;

        // Read studentCurricularPlan from database.
        studentCurricularPlanFromDB = this.readStudentCurricularPlan(studentCurricularPlanToWrite);

        // If studentCurricularPlan is not in database, then write it.
        if (studentCurricularPlanFromDB == null)
            super.lockWrite(studentCurricularPlanToWrite);
        // else If the studentCurricularPlan is mapped to the database, then
        // write any existing changes.
        else if (
            (studentCurricularPlanToWrite instanceof StudentCurricularPlan)
                && ((StudentCurricularPlan) studentCurricularPlanFromDB).getIdInternal().equals(
                    ((StudentCurricularPlan) studentCurricularPlanToWrite).getIdInternal()))
        {
            super.lockWrite(studentCurricularPlanToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia
    {
        super.delete(curricularPlan);
    }

    public List readAllFromStudent(int studentNumber) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.number", new Integer(studentNumber));
        return queryList(StudentCurricularPlan.class, criteria);
    }

    public IStudentCurricularPlan readActiveStudentAndSpecializationCurricularPlan(
        Integer studentNumber,
        TipoCurso degreeType,
        Specialization specialization)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("specialization", specialization.getSpecialization());
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);

    }

    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlan.getName());
        crit.addEqualTo(
            "degreeCurricularPlan.degree.sigla",
            degreeCurricularPlan.getDegree().getSigla());
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branchKey", branch.getIdInternal());
        return queryList(StudentCurricularPlan.class, criteria);
    }

//    public List readByCurricularCourseScope(ICurricularCourseScope curricularCourseScope)
//        throws ExcepcaoPersistencia
//    {
//        Criteria criteria = new Criteria();
//        criteria.addEqualTo("curricularCourseScopeKey", curricularCourseScope.getIdInternal());
//        List enrolments = queryList(Enrolment.class, criteria);
//        List studentCurricularPlans = new ArrayList();
//        Integer studentCurricularPlanId;
//        IStudentCurricularPlan helpStudentCurricularPlan = new StudentCurricularPlan();
//        Iterator iter = enrolments.iterator();
//        while (iter.hasNext())
//        {
//            studentCurricularPlanId =
//                ((IEnrolment) iter.next()).getStudentCurricularPlan().getIdInternal();
//            helpStudentCurricularPlan.setIdInternal(studentCurricularPlanId);
//            IStudentCurricularPlan studentCurricularPlan =
//                (IStudentCurricularPlan) this.readByOId(helpStudentCurricularPlan, false);
//            studentCurricularPlans.add(studentCurricularPlan);
//        }
//        return studentCurricularPlans;
//    }

    public List readByUsername(String username) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.person.username", username);
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readByStudentNumberAndDegreeType(Integer number, TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", number);
        crit.addEqualTo("student.degreeType", degreeType);
        return queryList(StudentCurricularPlan.class, crit);

    }
    //modified by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 24/Set/2003
    /**
	 * 
	 * Service rewritten by Naat & Sana to correct bug that cause the returning
	 * of an empty StudentCurricular when a student does not have an active
	 * version. The new version now uses Persistence Broker criteria API
	 */
    public IStudentCurricularPlan readActiveByStudentNumberAndDegreeType(
        Integer number,
        TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
		List studentPlanState = new ArrayList();
		studentPlanState.add(new Integer(1));
		studentPlanState.add(new Integer(4));
		
		criteria.addIn("currentState",studentPlanState);
     //   criteria.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("student.number", number);
        criteria.addEqualTo("student.degreeType", degreeType);

        IStudentCurricularPlan storedStudentCurricularPlan =
            (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, criteria);
        return storedStudentCurricularPlan;

    }

    public List readAllByStudentAntState(IStudent student, StudentCurricularPlanState state)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentKey", student.getIdInternal());
        criteria.addEqualTo("currentState", state);
        return queryList(StudentCurricularPlan.class, criteria);
    }

    public IStudentCurricularPlan readByStudentDegreeCurricularPlanAndState(
        IStudent student,
        IDegreeCurricularPlan degreeCurricularPlan,
        StudentCurricularPlanState state)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("studentKey", student.getIdInternal());
        criteria.addEqualTo("currentState", state);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, criteria);
    }

}
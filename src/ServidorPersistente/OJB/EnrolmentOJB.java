package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * 
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentOJB extends ObjectFenixOJB implements IPersistentEnrolment
{
	public List readAll() throws ExcepcaoPersistencia
	{
		 return queryList(Enrolment.class, new Criteria());
	}

  

   

       
	public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(enrolment);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

   
   

    public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(IStudentCurricularPlan studentCurricularPlan, EnrolmentState enrolmentState) throws ExcepcaoPersistencia
    {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
    	criteria.addEqualTo("enrolmentState", enrolmentState);
    	return queryList(Enrolment.class, criteria);
    }

    public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
    {
     
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        List result = queryList(Enrolment.class, criteria);
        return result;
    }

  
    public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {

   

    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
    	criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
    	return queryList(Enrolment.class, criteria);
    }

    public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, String year) throws ExcepcaoPersistencia
    {
    

        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        crit.addEqualTo("executionPeriod.executionYear.year", year);
        return queryList(Enrolment.class, crit);
    }

    public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, String year) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addEqualTo("executionPeriod.executionYear.year", year);
        criteria.addEqualTo("studentCurricularPlan.student.number", studentCurricularPlan.getStudent().getNumber());
        return (IEnrolment) queryObject(Enrolment.class, criteria);
    }

    public List readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(StudentCurricularPlanState state, EnrolmentState state2, IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.currentState", state);
        criteria.addEqualTo("enrolmentState", state2);
        criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        return queryList(Enrolment.class, criteria);
    }

    public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
    	criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
    	criteria.addNotEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
    	criteria.addEqualTo("enrolmentState", EnrolmentState.APROVED);
    	return queryList(Enrolment.class, criteria);
    }

    public IEnrolment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
    	criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
    	criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
    	return (IEnrolment) queryObject(Enrolment.class, criteria);
    }

    public IEnrolment readByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
    {
  
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
    	criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
    	return (IEnrolment) queryObject(Enrolment.class, criteria);
    }

  
    public List readAllByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
    {
     
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
    	criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
    	return queryList(Enrolment.class, criteria);
    }

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
    {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
    	return queryList(Enrolment.class, crit);
    }

	public List readAllByStudentCurricularPlanAndEnrolmentStateAndExecutionPeriod(IStudentCurricularPlan studentCurricularPlan, EnrolmentState enrolmentState, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("enrolmentState", enrolmentState);
		criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
		return queryList(Enrolment.class, criteria);
	}

}
/*
 * TeacherOJB.java
 */
package ServidorPersistente.OJB;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;
import Dominio.Employee;
import Dominio.EmployeeHistoric;
import Dominio.IDepartment;
import Dominio.IDisciplinaExecucao;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
/**
 *
 * @author  EP 15
 * @author Ivo Brandão
 */
public class TeacherOJB extends ObjectFenixOJB implements IPersistentTeacher
{
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia
    {
        try
        {
            ITeacher teacher = null;
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where person.username = $1";
            query.create(oqlQuery);
            query.bind(user);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
            {
                teacher = (ITeacher) result.get(0);
            }
            return teacher;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public ITeacher readTeacherByUsernamePB(String user) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", user);

        return (ITeacher) queryObject(Teacher.class, criteria);
    }
    public ITeacher readTeacherByNumber(Integer teacherNumber) throws ExcepcaoPersistencia
    {
        try
        {
            ITeacher teacher = null;
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where teacherNumber = $1";
            query.create(oqlQuery);
            query.bind(teacherNumber);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
            {
                teacher = (ITeacher) result.get(0);
            }
            return teacher;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public void lockWrite(ITeacher teacher) throws ExcepcaoPersistencia
    {
        super.lockWrite(teacher);
    }
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia
    {
        super.delete(teacher);
    }
    public void deleteAll() throws ExcepcaoPersistencia
    {
        String oqlQuery = "select all from " + Teacher.class.getName();
        super.deleteAll(oqlQuery);
    }
    public List readAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + Teacher.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentTeacher#readTeacherByExecutionCourse()
     */
    public List readTeacherByExecutionCourseProfessorship(IDisciplinaExecucao executionCourse)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where professorShipsExecutionCourses.executionPeriod.executionYear.year = $1";
            oqlQuery += " and professorShipsExecutionCourses.sigla = $2";
            oqlQuery += " and professorShipsExecutionCourses.executionPeriod.name = $3";
            query.create(oqlQuery);
            query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
            query.bind(executionCourse.getSigla());
            query.bind(executionCourse.getExecutionPeriod().getName());
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (Exception ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentTeacher#readTeacherByExecutionCourseResponsibility(Dominio.IDisciplinaExecucao)
     */
    public List readTeacherByExecutionCourseResponsibility(IDisciplinaExecucao executionCourse)
        throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where responsibleForExecutionCourses.executionPeriod.executionYear.year = $1";
            oqlQuery += " and responsibleForExecutionCourses.sigla = $2";
            oqlQuery += " and responsibleForExecutionCourses.executionPeriod.name = $3";
            query.create(oqlQuery);
            query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
            query.bind(executionCourse.getSigla());
            query.bind(executionCourse.getExecutionPeriod().getName());
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (Exception ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentTeacher#readByDepartment(Dominio.IDepartment)
     */
    public List readByDepartment(IDepartment department) throws ExcepcaoPersistencia
    {
        // TODO remove this method by refactoring teacher. Teacher has an employee instead of a person.
        List employees = getEmployees(department);
        Collection teacherNumberList = CollectionUtils.collect(employees, new Transformer()
        {
            public Object transform(Object input)
            {
                Employee employee = (Employee) input;
                return employee.getEmployeeNumber();
            }
        });
        Criteria criteria = new Criteria();
        System.out.println("teacherNumberList size=" + teacherNumberList.size());
        criteria.addIn("teacherNumber", teacherNumberList);
        return queryList(Teacher.class, criteria);
    }
    private List getEmployees(IDepartment department) throws ExcepcaoPersistencia
    {
        List employees = null;

        String likeCode = department.getCode() + "%";
        Criteria workingCostCenter = new Criteria();
        workingCostCenter.addLike("workingPlaceCostCenter.sigla", likeCode);
        Criteria mailingCostCenter = new Criteria();
        mailingCostCenter.addLike("mailingCostCenter.sigla", likeCode);

        //Criteria salaryCostCenter = new Criteria();
        //salaryCostCenter.addLike("salaryCostCenter.sigla", likeCode);

        Criteria criteriaDate = new Criteria();
        criteriaDate.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteriaDate.addIsNull("endDate");

        Criteria criteriaDate2 = new Criteria();
        criteriaDate2.addLessOrEqualThan("beginDate", Calendar.getInstance().getTime());
        criteriaDate2.addNotNull("endDate");
        criteriaDate2.addGreaterOrEqualThan("endDate", Calendar.getInstance().getTime());

        Criteria criteriaFinal = new Criteria();
        //criteriaFinal.addOrCriteria(salaryCostCenter);
        criteriaFinal.addOrCriteria(workingCostCenter);
        criteriaFinal.addOrCriteria(mailingCostCenter);
        criteriaFinal.addAndCriteria(criteriaDate);
        criteriaFinal.addOrCriteria(criteriaDate2);

        List employeesHistoric = queryList(EmployeeHistoric.class, criteriaFinal);
        if (employees != null)
        {
            ListIterator listIterator = employeesHistoric.listIterator();
            while (listIterator.hasNext())
            {
                EmployeeHistoric employeeHistoric = (EmployeeHistoric) listIterator.next();
                Criteria criteria = new Criteria();
                criteria.addEqualTo("idInternal", employeeHistoric.getKeyEmployee());
                employees.add(queryObject(Employee.class, criteria));
            }
        }
        return employees;
    }
    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentTeacher#readByNumber(java.lang.Integer)
     */
    public ITeacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacherNumber", teacherNumber);
        return (ITeacher) queryObject(Teacher.class, criteria);
    }
}

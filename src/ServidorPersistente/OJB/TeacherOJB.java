/*
 * TeacherOJB.java
 */
package ServidorPersistente.OJB;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;

import Dominio.EmployeeHistoric;
import Dominio.IDepartment;
import Dominio.IEmployeeHistoric;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
/**
 * @author EP 15
 * @author Ivo Brandão
 * 
 */
public class TeacherOJB extends ObjectFenixOJB implements IPersistentTeacher
{
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", user);
        return (ITeacher) queryObject(Teacher.class, criteria);
    }
  
   
    
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia
    {
        super.delete(teacher);
    }
    public void deleteAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        List result = queryList(Teacher.class, criteria);
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
        {
            delete((ITeacher) iterator.next());
        }
    }
    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(Teacher.class, criteria);
    }
    

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentTeacher#readByDepartment(Dominio.IDepartment)
	 */
    public List readByDepartment(IDepartment department) throws ExcepcaoPersistencia
    { // TODO remove this method by refactoring teacher. Teacher has an employee instead of a person.
        List employeesNumber = getEmployees(department);
        Criteria criteria = new Criteria();

        criteria.addIn("teacherNumber", employeesNumber);
        return queryList(Teacher.class, criteria);
    }
    private List getEmployees(IDepartment department) throws ExcepcaoPersistencia
    {
        String likeCode = department.getCode() + "%";
        Criteria workingCostCenter = new Criteria();
        workingCostCenter.addLike("workingPlaceCostCenter.sigla", likeCode);
        Date now = Calendar.getInstance().getTime();
        Criteria criteriaDate = new Criteria();
        criteriaDate.addLessOrEqualThan("beginDate", now);
        criteriaDate.addIsNull("endDate");
        Criteria criteriaDate2 = new Criteria();
        criteriaDate2.addLessOrEqualThan("beginDate", now);
        criteriaDate2.addNotNull("endDate");
        criteriaDate2.addGreaterOrEqualThan("endDate", now);
        Criteria finalCriteria = new Criteria();
        finalCriteria.addOrCriteria(workingCostCenter);
        Criteria activeEmployeeCriteria = new Criteria();
        activeEmployeeCriteria.addEqualTo("employee.active", Boolean.TRUE);
        finalCriteria.addAndCriteria(criteriaDate);
        finalCriteria.addOrCriteria(criteriaDate2);
        finalCriteria.addAndCriteria(activeEmployeeCriteria);
        List employeesHistoric = queryList(EmployeeHistoric.class, finalCriteria);
        List employeesNumbers = (List) CollectionUtils.collect(employeesHistoric, new Transformer()
        {
            public Object transform(Object input)
            {
                IEmployeeHistoric employeeHistoric = (IEmployeeHistoric) input;
                return employeeHistoric.getEmployee().getEmployeeNumber();
            }
        });
        return employeesNumbers;
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentTeacher#readByNumber(java.lang.Integer)
	 */
    public ITeacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacherNumber", teacherNumber);
        return (ITeacher) queryObject(Teacher.class, criteria);
    }
}
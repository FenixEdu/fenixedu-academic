/*
 * TeacherOJB.java
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.EmployeeHistoric;
import net.sourceforge.fenixedu.domain.IEmployeeHistoric;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;

/**
 * @author EP 15
 * @author Ivo Brandão
 *  
 */
public class TeacherOJB extends ObjectFenixOJB implements IPersistentTeacher {
    
    public ITeacher readTeacherByUsername(String userName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.username", userName);
        return (ITeacher) queryObject(Teacher.class, criteria);
    }
    
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Teacher.class, criteria);
    }
    
    public List readByDepartment(String departmentCode) throws ExcepcaoPersistencia {
        List employees = getEmployees(departmentCode);
        Collection teacherNumberList = CollectionUtils.collect(employees, new Transformer() {
            public Object transform(Object input) {
                Employee employee = (Employee) input;
                return employee.getEmployeeNumber();
            }
        });
        Criteria criteria = new Criteria();
        criteria.addIn("teacherNumber", teacherNumberList);
        return queryList(Teacher.class, criteria);
    }

    private List getEmployees(String departmentCode) throws ExcepcaoPersistencia {
        String likeCode = departmentCode + "%";
        
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
        finalCriteria.addAndCriteria(criteriaDate);
        finalCriteria.addOrCriteria(criteriaDate2);
        
        Criteria activeEmployeeCriteria = new Criteria();
        activeEmployeeCriteria.addEqualTo("employee.active", Boolean.TRUE);
        
        finalCriteria.addAndCriteria(activeEmployeeCriteria);
                
        List employeesHistoric = queryList(EmployeeHistoric.class, finalCriteria);
        
        Collection employeesIdInternals = CollectionUtils.collect(employeesHistoric, new Transformer() {
            public Object transform(Object input) {
                IEmployeeHistoric employeeHistoric = (IEmployeeHistoric) input;
                return employeeHistoric.getEmployee().getIdInternal();
            }
        });
        
        Criteria criteria = new Criteria();
        criteria.addIn("idInternal", employeesIdInternals);
        
        return queryList(Employee.class, criteria);
    }

    public ITeacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacherNumber", teacherNumber);
        return (ITeacher) queryObject(Teacher.class, criteria);
    }
    
    public Collection<ITeacher> readByNumbers(Collection<Integer> teacherNumbers) throws ExcepcaoPersistencia {
        if(teacherNumbers.isEmpty()){
            return new ArrayList<ITeacher>();
        }
        
        Criteria criteria = new Criteria();
        criteria.addIn("teacherNumber",teacherNumbers);
        return queryList(Teacher.class, criteria);
    }
    
}
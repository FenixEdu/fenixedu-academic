package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEmployeeHistoric;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.OJB.EmployeeOJB;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class DepartmentVO extends VersionedObjectsBase implements IPersistentDepartment {

	
	   public List readAll() {
		   return (List)readAll(Department.class);
	   }

	 
		public IDepartment readByTeacher(Integer teacherId) throws ExcepcaoPersistencia {

			ITeacher teacher = (ITeacher)readByOID(Teacher.class, teacherId);
	        IEmployeeHistoric employeeHistoric = getEmployee(teacher);

	        ICostCenter workingCC = employeeHistoric.getWorkingPlaceCostCenter();

	        List<IDepartment> departmentList = new ArrayList();
	        IDepartment department = null;

	        if (workingCC != null) {
	            String code = workingCC.getCode();

	            if (code != null && !(code.equals(""))) {
					Collection<IDepartment> departments = readAll(Department.class);
					
					for (IDepartment departmentIt : departments){
						String subCodeIt = departmentIt.getCode().substring(0, 2);
						String subCode = code.substring(0,2);
						
						if (subCodeIt.equals(subCode))
							departmentList.add(departmentIt);
					}
	            }

	            if (departmentList.size() != 1) {
					
					Collection<IDepartment> departments = readAll(Department.class);
					
					for (IDepartment departmentIt : departments){
						if (departmentIt.getCode().equals(code)){
							department = departmentIt;
							break;
						}		
					}
	            } else {
	                department = departmentList.get(0);
	            }
	        }
	        return department;
	    }

	 
		
		public IDepartment readByEmployee(Integer employeeId) throws ExcepcaoPersistencia {

			IEmployee employee = (IEmployee)readByOID(Employee.class, employeeId);
	        List employeeHistoricList = employee.getHistoricList();

	        ICostCenter workingCC = null;
	        Date date = null;

	        for (Iterator iter = employeeHistoricList.iterator(); iter.hasNext();) {
	            IEmployeeHistoric element = (IEmployeeHistoric) iter.next();
	            if (element.getWorkingPlaceCostCenter() != null
	                    && (date == null || element.getBeginDate().after(date))) {
	                workingCC = element.getWorkingPlaceCostCenter();
	                date = element.getBeginDate();
	            }
	        }

	        List departmentList = new ArrayList();

	        String code = workingCC.getCode();

	        if (code != null && !(code.equals(""))) {
				
				Collection<IDepartment> departments = readAll(Department.class);
				
				for (IDepartment departmentIt : departments){
					String subCodeIt = departmentIt.getCode().substring(0, 2);
					String subCode = code.substring(0,2);
					
					if (subCodeIt.equals(subCode))
						departmentList.add(departmentIt);
				}
	        }
	        IDepartment department = null;
	        if (departmentList.size() != 1) {
				
				Collection<IDepartment> departments = readAll(Department.class);
				
				for (IDepartment departmentIt : departments){
					if (departmentIt.getCode().equals(code)){
						department = departmentIt;
						break;
					}		
				}

	        } else {
	            department = (IDepartment) departmentList.get(0);
	        }
	        return department;
	    }

	    /**
	     * @param teacher
	     * @return
	     */
	    private IEmployeeHistoric getEmployee(ITeacher teacher) throws ExcepcaoPersistencia {
	        IPersistentEmployee employeeDAO = new EmployeeOJB();

	        IEmployee employee = employeeDAO.readByNumber(teacher.getTeacherNumber());
	        employee.setHistoricList(employeeDAO.readHistoricByKeyEmployee(employee.getIdInternal()
	                .intValue()));

	        employee.fillEmployeeHistoric();

	        return employee.getEmployeeHistoric();
	    }
}

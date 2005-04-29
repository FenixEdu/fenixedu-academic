/*
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.EmployeeHistoric;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEmployeeHistoric;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;

import org.apache.ojb.broker.query.Criteria;

public class DepartmentOJB extends PersistentObjectOJB implements IPersistentDepartment {

    public DepartmentOJB() {
    }

    public IDepartment lerDepartamentoPorNome(String nome) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        return (IDepartment) queryObject(Department.class, crit);

    }

    public IDepartment lerDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("sigla", sigla);
        return (IDepartment) queryObject(Department.class, crit);

    }

    public List readAllDepartments() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(Department.class, crit);
    }

    public void apagarDepartamento(IDepartment disciplina) throws ExcepcaoPersistencia {
        super.delete(disciplina);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDepartment#readByTeacher(Dominio.ITeacher)
     */
    public IDepartment readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {

        // TODO: Remove this call after refactoring teacher...
        // teacher.getEmployee();
        EmployeeHistoric employeeHistoric = getEmployee(teacher);

        ICostCenter workingCC = employeeHistoric.getWorkingPlaceCostCenter();

        List departmentList = null;
        IDepartment department = null;

        if (workingCC != null) {
            String code = workingCC.getCode();

            if (code != null && !(code.equals(""))) {
                Criteria workingCCCriteria = new Criteria();
                workingCCCriteria.addLike("code", code.substring(0, 2) + "%");
                departmentList = queryList(Department.class, workingCCCriteria);
            }

            if (departmentList.size() != 1) {
                Criteria criteria = new Criteria();
                criteria.addEqualTo("code", code);
                department = (IDepartment) queryObject(Department.class, criteria);
            } else {
                department = (IDepartment) departmentList.get(0);
            }
        }
        return department;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDepartment#readByTeacher(Dominio.ITeacher)
     */
    public IDepartment readByEmployee(IEmployee employee) throws ExcepcaoPersistencia {

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

        List departmentList = null;

        String code = workingCC.getCode();

        if (code != null && !(code.equals(""))) {
            Criteria workingCCCriteria = new Criteria();
            workingCCCriteria.addLike("code", code.substring(0, 2) + "%");
            departmentList = queryList(Department.class, workingCCCriteria);
        }
        IDepartment department;
        if (departmentList.size() != 1) {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("code", code);
            department = (IDepartment) queryObject(Department.class, criteria);
        } else {
            department = (IDepartment) departmentList.get(0);
        }
        return department;
    }

    /**
     * @param teacher
     * @return
     */
    private EmployeeHistoric getEmployee(ITeacher teacher) throws ExcepcaoPersistencia {
        IPersistentEmployee employeeDAO = new EmployeeOJB();

        IEmployee employee = employeeDAO.readByNumber(teacher.getTeacherNumber());
        employee.setHistoricList(employeeDAO.readHistoricByKeyEmployee(employee.getIdInternal()
                .intValue()));

        employee.fillEmployeeHistoric();

        return employee.getEmployeeHistoric();
    }
}
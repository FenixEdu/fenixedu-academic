/*
 *  
 */

package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Department;
import Dominio.EmployeeHistoric;
import Dominio.ICostCenter;
import Dominio.IDepartment;
import Dominio.IEmployee;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DepartmentOJB extends ObjectFenixOJB implements IPersistentDepartment
{

    public DepartmentOJB()
    {
    }

   

    public void escreverDepartamento(IDepartment departmentToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IDepartment departmentFromDB = null;

        // If there is nothing to write, simply return.
        if (departmentToWrite == null)
            return;

        // Read department from database.
        departmentFromDB = this.lerDepartamentoPorNome(departmentToWrite.getName());

        // If department is not in database, then write it.
        if (departmentFromDB == null)
            super.lockWrite(departmentToWrite);
        // else If the department is mapped to the database, then write any
		// existing changes.
        else if (departmentFromDB.getIdInternal().equals((departmentToWrite.getIdInternal())))
        {
            super.lockWrite(departmentToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public IDepartment lerDepartamentoPorNome(String nome) throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        return (IDepartment) queryObject(Department.class, crit);

    }

    public IDepartment lerDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("sigla", sigla);
        return (IDepartment) queryObject(Department.class, crit);
       
    }

   

    public List readAllDepartments() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        return queryList(Department.class, crit);
    }

    public void apagarDepartamento(IDepartment disciplina) throws ExcepcaoPersistencia
    {
        super.delete(disciplina);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentDepartment#readByTeacher(Dominio.ITeacher)
	 */
    public IDepartment readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {

        // TODO: Remove this call after refactoring teacher...
		// teacher.getEmployee();
        EmployeeHistoric employeeHistoric = getEmployee(teacher);

        ICostCenter workingCC = employeeHistoric.getWorkingPlaceCostCenter();

        List departmentList = null;

        String code = workingCC.getSigla();

        if (code != null && !(code.equals("")))
        {
            Criteria workingCCCriteria = new Criteria();
            workingCCCriteria.addLike("code", code.substring(0, 2) + "%");
            departmentList = queryList(Department.class, workingCCCriteria);
        }
        IDepartment department;
        if (departmentList.size() != 1)
        {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("code", code);
            department = (IDepartment) queryObject(Department.class, criteria);
        }
        else
        {
            department = (IDepartment) departmentList.get(0);
        }
        return department;
    }

    /**
	 * @param teacher
	 * @return
	 */
    private EmployeeHistoric getEmployee(ITeacher teacher) throws ExcepcaoPersistencia
    {
        IPersistentEmployee employeeDAO = SuportePersistenteOJB.getInstance().getIPersistentEmployee();

        IEmployee employee = employeeDAO.readByNumber(teacher.getTeacherNumber());
        employee.setHistoricList(
            employeeDAO.readHistoricByKeyEmployee(employee.getIdInternal().intValue()));

        employee.fillEmployeeHistoric();

        return employee.getEmployeeHistoric();
    }
}
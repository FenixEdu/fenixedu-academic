/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

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

public class DepartmentOJB extends ObjectFenixOJB implements IPersistentDepartment {
    
    public DepartmentOJB() {
    }
    
    public void apagarTodosOsDepartamentos() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Department.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public void escreverDepartamento(IDepartment departmentToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IDepartment departmentFromDB = null;

		// If there is nothing to write, simply return.
		if (departmentToWrite == null)
			return;

		// Read department from database.
		departmentFromDB = this.lerDepartamentoPorNome(departmentToWrite.getName());

		// If department is not in database, then write it.
		if (departmentFromDB == null)
			super.lockWrite(departmentToWrite);
		// else If the department is mapped to the database, then write any existing changes.
		else if (departmentFromDB.getIdInternal()
				.equals(
					(departmentToWrite.getIdInternal()))) {
			super.lockWrite(departmentToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
    }
    
    public IDepartment lerDepartamentoPorNome(String nome) throws ExcepcaoPersistencia {
        try {
            IDepartment de = null;
            String oqlQuery = "select all from " + Department.class.getName();
            oqlQuery += " where nome = $1";
            query.create(oqlQuery);
            query.bind(nome);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                de = (IDepartment) result.get(0);
            return de;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IDepartment lerDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia {
        try {
            IDepartment de = null;
            String oqlQuery = "select all from " + Department.class.getName();
            oqlQuery += " where sigla = $1";
            query.create(oqlQuery);
            query.bind(sigla);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                de = (IDepartment) result.get(0);
            return de;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarDepartamentoPorNome(String nome) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Department.class.getName();
            oqlQuery += " where nome = $1";
            query.create(oqlQuery);
            query.bind(nome);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void apagarDepartamentoPorSigla(String sigla) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Department.class.getName();
            oqlQuery += " where sigla = $1";
            query.create(oqlQuery);
            query.bind(sigla);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public ArrayList lerTodosOsDepartamentos() throws ExcepcaoPersistencia {
        try {
            ArrayList listade = new ArrayList();
            String oqlQuery = "select all from " + Department.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listade.add(iterator.next());
            }
            return listade;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void apagarDepartamento(IDepartment disciplina) throws ExcepcaoPersistencia {
        super.delete(disciplina);
    }


	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentDepartment#readByTeacher(Dominio.ITeacher)
	 */
	public IDepartment readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
		
		// TODO: Remove this call after refactoring teacher... teacher.getEmployee();
		EmployeeHistoric employeeHistoric = getEmployee(teacher);

		ICostCenter workingCC = employeeHistoric.getWorkingPlaceCostCenter();
		ICostCenter mailingCC = employeeHistoric.getMailingCostCenter();
		
		
		Criteria criteria = new Criteria();
		if (workingCC != null) {
			Criteria workingCCCriteria = new Criteria();	
			workingCCCriteria.addLike("code", workingCC.getSigla().substring(0, 2) + "%");
			criteria.addOrCriteria(workingCCCriteria);
		}
		
		if (mailingCC != null) {
			Criteria mailingCCCriteria = new Criteria();
			mailingCCCriteria.addLike("code", mailingCC.getSigla().substring(0, 2) + "%");
			criteria.addOrCriteria(mailingCCCriteria);
		}
			
		
		
		if (criteria.getElements().hasMoreElements()) {
			return (IDepartment) queryObject(Department.class, criteria);
		}else {
			return null;
		}
		
		
	}

	/**
	 * @param teacher
	 * @return
	 */
	private EmployeeHistoric getEmployee(ITeacher teacher) throws ExcepcaoPersistencia {
		IPersistentEmployee employeeDAO = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
		
		IEmployee employee = employeeDAO.readByNumber(teacher.getTeacherNumber());
		employee.setHistoricList(employeeDAO.readHistoricByKeyEmployee(employee.getIdInternal().intValue()));
		
		employee.fillEmployeeHistoric();

		return	employee.getEmployeeHistoric();				
	}
}

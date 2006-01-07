package net.sourceforge.fenixedu.applicationTier.Filtro.departmentAdmOffice;

import java.util.HashMap;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author mrsp and jdnf
 *
 */
public class EmployeeBelongsToTeacherDepartment extends Filtro{

	public void execute(ServiceRequest request, ServiceResponse response)
			throws FilterException, Exception {
	    
	    ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
	    IPersistentDepartment persistentDepartment = persistentSuport.getIDepartamentoPersistente();
	    IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        IPersistentEmployee persistentEmployee = persistentSuport.getIPersistentEmployee();
        IPessoaPersistente pessoaPersistente = persistentSuport.getIPessoaPersistente();
	         
	    IUserView userView = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        
        Department teacherDepartment = getTeacherDepartment(persistentDepartment, persistentTeacher, argumentos);
        Department employeeDepartment = getEmployeeDepartment(persistentDepartment, persistentEmployee, pessoaPersistente, userView);
           
        if(!employeeDepartment.getName().equals(teacherDepartment.getName()))
            throw new NotAuthorizedFilterException();    	                     
	}

    /**
     * @param persistentDepartment
     * @param persistentEmployee
     * @param pessoaPersistente
     * @param userView
     * @return
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     * @throws NotAuthorizedFilterException
     */
    protected Department getEmployeeDepartment(IPersistentDepartment persistentDepartment, IPersistentEmployee persistentEmployee, IPessoaPersistente pessoaPersistente, IUserView userView) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        Person pessoa = getPessoa(pessoaPersistente, userView);
        Employee employee = persistentEmployee.readByPerson(pessoa);
        if(employee == null)
            throw new NotAuthorizedFilterException("Não existe funcionario");
        Department employeeDepartment = getDepartment(persistentDepartment, employee);
        return employeeDepartment;
    }

    /**
     * @param persistentDepartment
     * @param persistentTeacher
     * @param argumentos
     * @return
     * @throws FenixServiceException
     * @throws ExcepcaoPersistencia
     * @throws NotAuthorizedFilterException
     */
    protected Department getTeacherDepartment(IPersistentDepartment persistentDepartment, IPersistentTeacher persistentTeacher, Object[] argumentos) throws NotAuthorizedFilterException, ExcepcaoPersistencia {
        Teacher teacher = getTeacher(persistentTeacher, argumentos);
        Department teacherDepartment = getDepartment(persistentDepartment, teacher);
        return teacherDepartment;
    }

    /**
     * @param pessoaPersistente
     * @param userView
     * @return
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     * @throws NotAuthorizedFilterException
     */
    protected Person getPessoa(IPessoaPersistente pessoaPersistente, IUserView userView) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        String utilizador = userView.getUtilizador();        
        Person pessoa = pessoaPersistente.lerPessoaPorUsername(utilizador); 
        if(pessoa == null)
            throw new NotAuthorizedFilterException("error.noPerson");
        return pessoa;
    }

    /**
     * @param persistentDepartment
     * @param teacher
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     * @throws NotAuthorizedFilterException
     */
    protected Department getDepartment(IPersistentDepartment persistentDepartment, Teacher teacher) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        Department department = teacher.getCurrentWorkingDepartment();
        if(department == null)
            throw new NotAuthorizedFilterException("error.noDepartment");
        return department;
    }
    
    /**
     * @param persistentDepartment
     * @param teacher
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     * @throws NotAuthorizedFilterException
     */
    protected Department getDepartment(IPersistentDepartment persistentDepartment, Employee employee) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        Department department = employee.getCurrentDepartmentWorkingPlace();
        if(department == null)
            throw new NotAuthorizedFilterException("error.noDepartment");
        return department;
    }

    /**
     * @param persistentTeacher
     * @param argumentos
     * @return
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     * @throws NotAuthorizedFilterException
     */
    protected Teacher getTeacher(IPersistentTeacher persistentTeacher, Object[] argumentos) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        String teacherNumber = getTeacherNumber(argumentos);
        Teacher teacher = persistentTeacher.readByNumber(new Integer(teacherNumber));
        
        if(teacher == null) {
            throw new NotAuthorizedFilterException("error.teacher.not.found");
        }
        return teacher;
    }

    /**
     * @param argumentos
     */
    protected String getTeacherNumber(Object[] argumentos) {
        String teacherNumber = null;
        
        if(argumentos.length == 1 && argumentos[0] instanceof HashMap){
            HashMap hashMap = (HashMap) argumentos[0];
            teacherNumber = (String) hashMap.get("teacherNumber");            
        }
        else
            teacherNumber = argumentos[0].toString();
                                    
        return teacherNumber;
    }

}

package ServidorAplicacao.Filtro.departmentAdmOffice;

import java.util.HashMap;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.ICostCenter;
import Dominio.IDepartment;
import Dominio.IEmployee;
import Dominio.IFuncionario;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFuncionarioPersistente;

/**
 * @author mrsp and jdnf
 *
 */
public class EmployeeBelongsToTeacherDepartment extends Filtro{

	public void execute(ServiceRequest request, ServiceResponse response)
			throws FilterException, Exception, FenixServiceException {
	    
	    ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        
	    IPersistentDepartment persistentDepartment = persistentSuport.getIDepartamentoPersistente();
	    IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        IPersistentEmployee persistentEmployee = persistentSuport.getIPersistentEmployee();
        IPessoaPersistente pessoaPersistente = persistentSuport.getIPessoaPersistente();
	         
	    IUserView userView = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        
        IDepartment teacherDepartment = getTeacherDepartment(persistentDepartment, persistentTeacher, argumentos);
        IDepartment employeeDepartment = getEmployeeDepartment(persistentDepartment, persistentEmployee, pessoaPersistente, userView);
           
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
    protected IDepartment getEmployeeDepartment(IPersistentDepartment persistentDepartment, IPersistentEmployee persistentEmployee, IPessoaPersistente pessoaPersistente, IUserView userView) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        IPessoa pessoa = getPessoa(pessoaPersistente, userView);
        IEmployee employee = persistentEmployee.readByPerson(pessoa);
        if(employee == null)
            throw new NotAuthorizedFilterException("Não existe funcionario");
        IDepartment employeeDepartment = getDepartment(persistentDepartment, employee);
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
    protected IDepartment getTeacherDepartment(IPersistentDepartment persistentDepartment, IPersistentTeacher persistentTeacher, Object[] argumentos) throws NotAuthorizedFilterException, ExcepcaoPersistencia {
        ITeacher teacher = getTeacher(persistentTeacher, argumentos);
        IDepartment teacherDepartment = getDepartment(persistentDepartment, teacher);
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
    protected IPessoa getPessoa(IPessoaPersistente pessoaPersistente, IUserView userView) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        String utilizador = userView.getUtilizador();        
        IPessoa pessoa = pessoaPersistente.lerPessoaPorUsername(utilizador); 
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
    protected IDepartment getDepartment(IPersistentDepartment persistentDepartment, ITeacher teacher) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        IDepartment department = persistentDepartment.readByTeacher(teacher);
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
    protected IDepartment getDepartment(IPersistentDepartment persistentDepartment, IEmployee employee) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        IDepartment department = persistentDepartment.readByEmployee(employee);
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
    protected ITeacher getTeacher(IPersistentTeacher persistentTeacher, Object[] argumentos) throws ExcepcaoPersistencia, NotAuthorizedFilterException {
        String teacherNumber = getTeacherNumber(argumentos);
        ITeacher teacher = persistentTeacher.readByNumber(new Integer(teacherNumber));
        
        if(teacher == null) {
            throw new NotAuthorizedFilterException("error.teacher.not.found");
        }
        return teacher;
    }

    /**
     * @param argumentos
     */
    protected String getTeacherNumber(Object[] argumentos) {
        HashMap hashMap = (HashMap) argumentos[0];
        String teacherNumber = (String) hashMap.get("teacherNumber");
        return teacherNumber;
    }

}

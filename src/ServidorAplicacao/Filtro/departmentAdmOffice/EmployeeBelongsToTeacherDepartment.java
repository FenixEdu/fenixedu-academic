package ServidorAplicacao.Filtro.departmentAdmOffice;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import ServidorAplicacao.Filtro.AccessControlFilter;

/**
 * @author mrsp and jdnf and gedl
 *
 */
public class EmployeeBelongsToTeacherDepartment extends AccessControlFilter {

	public void execute(ServiceRequest arg0, ServiceResponse arg1)
			throws FilterException, Exception {
		
		System.err.println("Ca vou eu");
		System.out.println(arg0.getArguments());
		System.err.println("Ja me fui");
	}

}

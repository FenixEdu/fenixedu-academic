package net.sourceforge.fenixedu.predicates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class MarkSheetPredicates {
    
    public static final Set<String> rectificationEmployees = new HashSet<String>();
    public static final Set<String> dissertationEmployees = new HashSet<String>();
    
    static {
	rectificationEmployees.add("1272");
	rectificationEmployees.add("1268");
	rectificationEmployees.add("2675");
	rectificationEmployees.add("3978");
	rectificationEmployees.add("3068");
	rectificationEmployees.add("2973");
	rectificationEmployees.add("3821");
	
	dissertationEmployees.add("3978");
	dissertationEmployees.add("3821");
	
    }
    
    public static final AccessControlPredicate<MarkSheet> confirmPredicate = new AccessControlPredicate<MarkSheet>() {

	public boolean evaluate(final MarkSheet markSheet) {
	    return hasAcademinAdminOfficeRole() &&
	    	(!markSheet.isRectification() || (markSheet.isRectification() && checkRectification())) &&
	    		(!markSheet.isDissertation() || (markSheet.isDissertation() && checkDissertation()));
	}
	
    };
    
    public static final AccessControlPredicate<MarkSheet> editPredicate = new AccessControlPredicate<MarkSheet>() {

	public boolean evaluate(final MarkSheet markSheet) {
	    return hasScientificCouncilRole() || 
	    	(hasAcademinAdminOfficeRole() &&
		    	(!markSheet.isRectification() || (markSheet.isRectification() && checkRectification())) &&
		    		(!markSheet.isDissertation() || (markSheet.isDissertation() && checkDissertation())));
	}
	
    };
    
    private static boolean hasAcademinAdminOfficeRole() {
	return RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE.evaluate(null);
    }

    private static boolean hasScientificCouncilRole() {
	return RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE.evaluate(null);
    }

    
    public static boolean checkRectification() {
	Employee employee = AccessControl.getPerson().getEmployee();
	if (employee != null) {
	    return rectificationEmployees.contains(employee.getEmployeeNumber().toString());
	} else {
	    return false;
	}
    }

    public static boolean checkDissertation() {
	Employee employee = AccessControl.getPerson().getEmployee();
	if (employee != null) {
	    return dissertationEmployees.contains(employee.getEmployeeNumber().toString());
	} else {
	    return false;
	}
    }


}

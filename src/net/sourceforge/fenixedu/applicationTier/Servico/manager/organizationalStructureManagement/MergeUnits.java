package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class MergeUnits extends Service {

    public void run(Unit fromUnit, Unit destinationUnit, Boolean sendMail) {
	if(fromUnit != null && destinationUnit != null) {
	    
	    Unit.mergeExternalUnits(fromUnit, destinationUnit);
	    
	    if(sendMail != null && sendMail.booleanValue()) {
                Employee employee = Employee.readByNumber(4578);
                Person person = AccessControl.getPerson();                
                String body = "Foi efectuado um merge de unidades externas por " + person.getName() + "[" + person.getUsername() + "]" + " : Unidade Origem -> " + fromUnit.getName() + "[" + fromUnit.getIdInternal() + "]  Unidade Destino -> " + destinationUnit.getName() + "[" + destinationUnit.getIdInternal() + "]";
                new Email("Fénix", "suporte@ist.utl.pt", null, Collections.singletonList(employee.getPerson().getEmail()), null, null, "MergeUnits", body);
	    }
	}
    }
}

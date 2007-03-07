package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.Email;

public class MergeUnits extends Service {

    public void run(Unit fromUnit, Unit destinationUnit) {
	if(fromUnit != null && destinationUnit != null) {
	    
	    Unit.mergeExternalUnits(fromUnit, destinationUnit);
	    
	    Employee employee = Employee.readByNumber(4578);	    
	    String body = "Foi efectuado um merge de unidades externas: Unidade Origem -> " + fromUnit.getName() + "[" + fromUnit.getIdInternal() + "]  Unidade Destino -> " + destinationUnit.getName() + "[" + destinationUnit.getIdInternal() + "]";
	    new Email("Fénix", "suporte@ist.utl.pt", null, Collections.singletonList(employee.getPerson().getEmail()), null, null, "MergeUnits", body);
	}
    }
}

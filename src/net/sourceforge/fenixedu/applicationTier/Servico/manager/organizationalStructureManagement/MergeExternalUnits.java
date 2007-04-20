package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;

public class MergeExternalUnits extends Service {

    public void run(Unit fromUnit, Unit destinationUnit, Boolean sendMail) {
	if(fromUnit != null && destinationUnit != null) {
	    
	    String fromUnitName = fromUnit.getName();
	    Integer fromUnitID = fromUnit.getIdInternal();
	    
	    Unit.mergeExternalUnits(fromUnit, destinationUnit);
	    
	    if(sendMail != null && sendMail.booleanValue()) {                		
		
		String emails = PropertiesManager.getProperty("merge.units.emails");
		if(!StringUtils.isEmpty(emails)) {		    
		    
		    List<String> resultEmails = new ArrayList<String>();
		    String[] splitedEmails = emails.split(",");			    
		    for (String email : splitedEmails) {
			resultEmails.add(email.trim());
		    }
		    
		    Person person = AccessControl.getPerson();                
		    String body = "Foi efectuado um merge de unidades externas por " + person.getName() + "[" + person.getUsername() + "]" 
				+ " : Unidade Origem -> " + fromUnitName + "[" + fromUnitID + "]  Unidade Destino -> " 
				+ destinationUnit.getName() + "[" + destinationUnit.getIdInternal() + "]";
		    
		    new Email("Fénix", "suporte@ist.utl.pt", null, resultEmails, null, null, "MergeUnits", body);		   		    		    
		}					               
	    }
	}
    }
}

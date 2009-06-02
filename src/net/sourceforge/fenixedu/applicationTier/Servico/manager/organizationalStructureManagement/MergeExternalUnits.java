package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class MergeExternalUnits extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Unit fromUnit, Unit destinationUnit, Boolean sendMail) {

	if (fromUnit != null && destinationUnit != null) {

	    String fromUnitName = fromUnit.getName();
	    Integer fromUnitID = fromUnit.getIdInternal();

	    Unit.mergeExternalUnits(fromUnit, destinationUnit);

	    if (sendMail != null && sendMail.booleanValue()) {

		String emails = PropertiesManager.getProperty("merge.units.emails");
		if (!StringUtils.isEmpty(emails)) {

		    Set<String> resultEmails = new HashSet<String>();
		    String[] splitedEmails = emails.split(",");
		    for (String email : splitedEmails) {
			resultEmails.add(email.trim());
		    }

		    // Foi efectuado um merge de unidades externas por {0}[{1}]
		    // : Unidade Origem -> {2} [{3}] Unidade Destino -> {4}[{5}]
		    final Person person = AccessControl.getPerson();
		    final String subject = RenderUtils.getResourceString("GLOBAL_RESOURCES", "mergeExternalUnits.email.subject");
		    final String body = RenderUtils.getResourceString("GLOBAL_RESOURCES", "mergeExternalUnits.email.body",
			    new Object[] { person.getName(), person.getUsername(), fromUnitName, fromUnitID,
				    destinationUnit.getName(), destinationUnit.getIdInternal() });
		    // String body =
		    // "Foi efectuado um merge de unidades externas por " +
		    // person.getName() + "["
		    // + person.getUsername() + "]" + " : Unidade Origem -> " +
		    // fromUnitName + "[" + fromUnitID
		    // + "]  Unidade Destino -> " + destinationUnit.getName() +
		    // "[" + destinationUnit.getIdInternal() + "]";

		    SystemSender systemSender = rootDomainObject.getSystemSender();
		    new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, subject, body,
			    resultEmails);
		    // new Email("Fénix", "suporte@ist.utl.pt", null,
		    // resultEmails, null, null, "MergeUnits", body);
		}
	    }
	}
    }
}
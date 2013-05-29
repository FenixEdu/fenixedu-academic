package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class MergeExternalUnits {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Unit fromUnit, Unit destinationUnit, Boolean sendMail) {

        if (fromUnit != null && destinationUnit != null) {

            String fromUnitName = fromUnit.getName();
            String fromUnitID = fromUnit.getExternalId();

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
                    final String subject =
                            BundleUtil.getStringFromResourceBundle("resources.GlobalResources",
                                    "mergeExternalUnits.email.subject");
                    final String body =
                            BundleUtil.getStringFromResourceBundle("resources.GlobalResources", "mergeExternalUnits.email.body",
                                    new String[] { person.getName(), person.getUsername(), fromUnitName, fromUnitID,
                                            destinationUnit.getName(), destinationUnit.getExternalId().toString() });

                    SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
                    new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, subject, body,
                            resultEmails);
                }
            }
        }
    }
}
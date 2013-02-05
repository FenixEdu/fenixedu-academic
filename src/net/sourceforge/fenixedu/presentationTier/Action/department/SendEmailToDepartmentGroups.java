package net.sourceforge.fenixedu.presentationTier.Action.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.UnitMailSenderAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SendEmailToDepartmentGroups extends UnitMailSenderAction {

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Unit unit = getUnit(request);

        final Sender unitSender = getSomeSender(unit);

        if (userOfficialSender(unit, unitSender)) {
            return EmailsDA.sendEmail(request, unitSender);
        } else {
            final Person person = AccessControl.getPerson();
            final PersonSender sender = person.getSender();

            return unitSender == null ? EmailsDA.sendEmail(request, sender) : EmailsDA.sendEmail(request, sender, unitSender
                    .getRecipientsSet().toArray(new Recipient[0]));
        }
    }

    private boolean userOfficialSender(final Unit unit, final Sender unitSender) {
        if (unit instanceof DepartmentUnit) {
            final DepartmentUnit departmentUnit = (DepartmentUnit) unit;
            final Department department = departmentUnit.getDepartment();
            return department.isCurrentUserCurrentDepartmentPresident() && unitSender != null;
        }
        return false;
    }

    private Sender getSomeSender(final Unit unit) {
        for (final Sender sender : unit.getUnitBasedSenderSet()) {
            return sender;
        }
        return null;
    }

}

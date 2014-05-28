/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberMessagingApp;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.UnitMailSenderAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberMessagingApp.class, path = "send-email-to-department-groups",
        titleKey = "label.sendEmailToGroups")
@Mapping(module = "departmentMember", path = "/sendEmailToDepartmentGroups")
@Forwards(@Forward(name = "chooseUnit", path = "/departmentMember/chooseUnit.jsp"))
public class SendEmailToDepartmentGroups extends UnitMailSenderAction {

    @EntryPoint
    public ActionForward chooseUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Unit unit = getUnit(request);
        if (unit != null) {
            return prepare(mapping, actionForm, request, response);
        }
        Unit departmentUnit = AccessControl.getPerson().getTeacher().getCurrentWorkingDepartment().getDepartmentUnit();
        Set<Unit> units = new TreeSet<>(Party.COMPARATOR_BY_NAME);
        units.add(departmentUnit);

        for (Unit subUnit : departmentUnit.getAllSubUnits()) {
            if (subUnit.isScientificAreaUnit()) {
                ScientificAreaUnit scientificAreaUnit = (ScientificAreaUnit) subUnit;
                if (scientificAreaUnit.isCurrentUserMemberOfScientificArea()) {
                    units.add(scientificAreaUnit);
                }
            }
        }

        if (units.size() == 1) {
            request.setAttribute("unitId", departmentUnit.getExternalId());
            return prepare(mapping, actionForm, request, response);
        }

        request.setAttribute("units", units);
        return mapping.findForward("chooseUnit");
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
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

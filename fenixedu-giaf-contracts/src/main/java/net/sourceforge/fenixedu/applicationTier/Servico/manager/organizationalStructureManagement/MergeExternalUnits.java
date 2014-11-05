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
package org.fenixedu.academic.service.services.manager.organizationalStructureManagement;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Accountability;
import org.fenixedu.academic.domain.organizationalStructure.ExternalContract;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.FenixEduAcademicConfiguration;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public class MergeExternalUnits {

    @Atomic
    public static void run(Unit fromUnit, Unit destinationUnit, Boolean sendMail) {
        check(RolePredicates.MANAGER_PREDICATE);

        if (fromUnit != null && destinationUnit != null) {

            String fromUnitName = fromUnit.getName();
            String fromUnitID = fromUnit.getExternalId();

            mergeExternalUnits(fromUnit, destinationUnit);

            if (sendMail != null && sendMail.booleanValue()) {

                String emails = FenixConfigurationManager.getConfiguration().getMergeUnitsEmails();
                if (!StringUtils.isEmpty(emails)) {

                    Set<String> resultEmails = new HashSet<String>();
                    String[] splitedEmails = emails.split(",");
                    for (String email : splitedEmails) {
                        resultEmails.add(email.trim());
                    }

                    // Foi efectuado um merge de unidades externas por {0}[{1}]
                    // : Unidade Origem -> {2} [{3}] Unidade Destino -> {4}[{5}]
                    final Person person = AccessControl.getPerson();
                    final String subject = BundleUtil.getString(Bundle.GLOBAL, "mergeExternalUnits.email.subject");
                    final String body =
                            BundleUtil.getString(Bundle.GLOBAL, "mergeExternalUnits.email.body", new String[] { person.getName(),
                                    person.getUsername(), fromUnitName, fromUnitID, destinationUnit.getName(),
                                    destinationUnit.getExternalId().toString() });

                    SystemSender systemSender = Bennu.getInstance().getSystemSender();
                    new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, subject, body,
                            resultEmails);
                }
            }
        }
    }

    public static void mergeExternalUnits(Unit fromUnit, Unit destinationUnit) {

        if (fromUnit == null || destinationUnit == null || fromUnit.equals(destinationUnit)) {
            throw new DomainException("error.merge.external.units.equals.units");
        }

        if (!fromUnit.isNoOfficialExternal() || destinationUnit.isInternal()) {
            throw new DomainException("error.merge.external.units.invalid.units");
        }

        Collection<? extends Accountability> externalContracts =
                fromUnit.getChildAccountabilitiesByAccountabilityClass(ExternalContract.class);
        destinationUnit.getChildsSet().addAll(externalContracts);
        destinationUnit.getAssociatedNonAffiliatedTeachersSet().addAll(fromUnit.getAssociatedNonAffiliatedTeachersSet());
        destinationUnit.getPrecedentDegreeInformationsSet().addAll(fromUnit.getPrecedentDegreeInformationsSet());

        fromUnit.delete();
    }
}
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
package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreateUnavailablePeriod {

    @Atomic
    public static void run(Person person, DateTime begin, DateTime end, String justification) {

        CreateUnavailable(person, begin, end, justification);
        sendEmail(person, begin, end, justification,
                person.getVigilantGroupsForExecutionYear(ExecutionYear.readCurrentExecutionYear()));
    }

    private static void CreateUnavailable(Person person, DateTime begin, DateTime end, String justification) {
        new UnavailablePeriod(begin, end, justification, person);
    }

    private static void sendEmail(Person person, DateTime begin, DateTime end, String justification, List<VigilantGroup> groups) {
        for (VigilantGroup group : groups) {
            String bccs = group.getContactEmail();

            String beginDate =
                    begin.getDayOfMonth() + "/" + begin.getMonthOfYear() + "/" + begin.getYear() + " - "
                            + String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour())
                            + "h";
            String endDate =
                    end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear() + " - "
                            + String.format("%02d", end.getHourOfDay()) + ":" + String.format("%02d", end.getMinuteOfHour())
                            + "h";;
            String message =
                    BundleUtil.getString(Bundle.VIGILANCY, "email.convoke.unavailablePeriod",
                            new String[] { person.getName(), beginDate, endDate, justification });

            String subject =
                    BundleUtil.getString(Bundle.VIGILANCY,
                            "email.convoke.unavailablePeriod.subject", new String[] { group.getName() });

            Sender sender = Bennu.getInstance().getSystemSender();
            new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(), Collections.EMPTY_LIST, subject,
                    message, bccs);
        }
    }
}
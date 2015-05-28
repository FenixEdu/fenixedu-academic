/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.util.icalendar;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Url;
import net.fortuna.ical4j.model.property.Version;

import org.fenixedu.academic.domain.organizationalStructure.Unit;

public class CalendarFactory {
    public static String PROD_ID_APPLICATION = "Sistema Fenix";
    public static TimeZone TIMEZONE = TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone("Europe/Lisbon");

    private static String digest(String obj) {
        byte[] defaultBytes = obj.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (byte element : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & element));
            }
            String foo = messageDigest.toString();
            obj = hexString + "";
        } catch (NoSuchAlgorithmException nsae) {

        }
        return obj;

    }

    private static VEvent convertEventBean(EventBean event) {
        Date begin;
        Date end;
        if (event.isAllDay()) {
            begin = new Date(event.getBegin().toCalendar(new Locale("pt")).getTime());
            end = new Date(event.getEnd().toCalendar(new Locale("pt")).getTime());
        } else {
            begin = new DateTime(event.getBegin().toCalendar(new Locale("pt")).getTime());
            end = new DateTime(event.getEnd().toCalendar(new Locale("pt")).getTime());
        }
        VEvent vEvent = new VEvent(begin, end, event.getTitle());

        vEvent.getStartDate().setTimeZone(TIMEZONE);
        vEvent.getEndDate().setTimeZone(TIMEZONE);

        if (event.getLocation() != null) {
            vEvent.getProperties().add(new Location(event.getLocation()));
        }

        if (event.getUrl() != null) {
            try {
                vEvent.getProperties().add(new Url(new URI(event.getUrl())));
            } catch (URISyntaxException e) {
            }
        }

        if (event.getNote() != null) {
            vEvent.getProperties().add(new Description(event.getNote()));
        }

        Uid uid = new Uid(digest(event.getTitle() + "-" + begin.toGMTString() + "-" + end.toGMTString()));
        vEvent.getProperties().add(uid);

        return vEvent;
    }

    public static Calendar createCalendar(List<EventBean> events) {

        final String prodIdCompany = Unit.getInstitutionName().getContent();
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//" + prodIdCompany + "//" + PROD_ID_APPLICATION + "//PT"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        VTimeZone tz = TIMEZONE.getVTimeZone();
        calendar.getComponents().add(tz);

        for (EventBean eventBean : events) {
            calendar.getComponents().add(convertEventBean(eventBean));
        }
        return calendar;

    }

}

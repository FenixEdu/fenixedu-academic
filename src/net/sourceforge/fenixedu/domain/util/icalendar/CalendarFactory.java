package net.sourceforge.fenixedu.domain.util.icalendar;

import java.net.URI;
import java.net.URISyntaxException;
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

public class CalendarFactory {
	public static String PROD_ID_COMPANY = "Instituto Superior Técnico";
	public static String PROD_ID_APPLICATION = "Sistema Fenix";
	public static TimeZone TIMEZONE = TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone("Europe/Lisbon");

	private static String digest(String obj) {
		byte[] defaultBytes = obj.getBytes();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
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

		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//" + PROD_ID_COMPANY + "//" + PROD_ID_APPLICATION + "//PT"));
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

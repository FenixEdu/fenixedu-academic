/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

/**
 * @author Susana Fernandes
 */
public class StudentTestLog extends StudentTestLog_Base {

	public StudentTestLog(DistributedTest distributedTest, Registration student, String event) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setDistributedTest(distributedTest);
		setStudent(student);
		DateTime now = new DateTime();
		setDateDateTime(now);
		setEvent(event);
		setChecksum(getChecksum(distributedTest, student, now));
	}

	public static String getChecksum(DistributedTest distributedTest, Registration student, DateTime dateTime) {
		StringBuilder checksumCode = new StringBuilder();
		checksumCode.append(student.getNumber());
		checksumCode.append(distributedTest.getIdInternal());
		final DateTime thisDateTime =
				new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(),
						dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute(), 0);
		checksumCode.append(thisDateTime.getMillis());
		checksumCode.append(RootDomainObject.getInstance().getTestChecksums().iterator().next().getChecksumCode());
		return (DigestUtils.shaHex(checksumCode.toString()));
	}

	public void delete() {
		removeStudent();
		removeDistributedTest();
		removeRootDomainObject();
		deleteDomainObject();
	}

	public String getDateFormatted() {
		return getDateDateTime().toString("dd/MM/yyyy HH:mm:ss");
	}

	public List<String> getEventList() {
		String[] eventTokens = getEvent().split(";");
		List<String> eventList = new ArrayList<String>();
		for (String eventToken : eventTokens) {
			eventList.add(eventToken);
		}
		return eventList;
	}

	@Deprecated
	public java.util.Date getDate() {
		org.joda.time.DateTime dt = getDateDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setDate(java.util.Date date) {
		if (date == null) {
			setDateDateTime(null);
		} else {
			setDateDateTime(new org.joda.time.DateTime(date.getTime()));
		}
	}

}

/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

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
	setDateDateTime(new DateTime());
	setEvent(event);
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

    public List getEventList() {
	String[] eventTokens = getEvent().split(";");
	List<String> eventList = new ArrayList<String>();
	for (int i = 0; i < eventTokens.length; i++) {
	    eventList.add(eventTokens[i]);
	}
	return eventList;
    }
}

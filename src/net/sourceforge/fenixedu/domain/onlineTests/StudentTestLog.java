/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Susana Fernandes
 */
public class StudentTestLog extends StudentTestLog_Base {

    public StudentTestLog() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removeStudent();
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

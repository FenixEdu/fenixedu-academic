package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class TeacherServiceLog extends TeacherServiceLog_Base implements Comparable<TeacherServiceLog> {

    public TeacherServiceLog(final TeacherService teacherService, final String description) {
	super();
	setModificationDate(new DateTime());
	setTeacherService(teacherService);
	final Person person = AccessControl.getPerson();
	final User user = person == null ? null : person.getUser();
	setUser(user);
	setDescription(description);
    }

    @Override
    public int compareTo(final TeacherServiceLog o) {
	if (o == null) {
	    return -1;
	}
	int compareTo = getModificationDate().compareTo(o.getModificationDate());
	return compareTo == 0 ? getExternalId().compareTo(o.getExternalId()) : compareTo;
    }

}

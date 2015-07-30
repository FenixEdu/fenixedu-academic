package org.fenixedu.academic.domain.log;

import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class StudentRegistrationTransferLog extends StudentRegistrationTransferLog_Base {
    public StudentRegistrationTransferLog(Student source, Student target) {
        super();
        setSource(source);
        setTarget(target);
        setWhen(DateTime.now());
        setWho(Authenticate.getUser().getUsername());
    }

    @Override
    public Student getSource() {
        //FIXME: remove when the framework enables read-only slots
        return super.getSource();
    }

    @Override
    public Student getTarget() {
        //FIXME: remove when the framework enables read-only slots
        return super.getTarget();
    }

    @Override
    public DateTime getWhen() {
        //FIXME: remove when the framework enables read-only slots
        return super.getWhen();
    }

    @Override
    public String getWho() {
        //FIXME: remove when the framework enables read-only slots
        return super.getWho();
    }
}
